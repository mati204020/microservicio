package com.gateway.api_gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Value("${jwt.secret}")
    private String secreto;

    public AuthenticationFilter() {
        super(Config.class);
    }

    public static class Config {

    }

    /**
     * Si el path empieza con "prefijoRuta" y el método está en "metodos", el usuario
     * necesita AL MENOS UNO de "rolesPermitidos" (o cualquier rol autenticado si es null).
     * Se evalúa en orden: la primera regla que matchea manda.
     */
    private record ReglaPermiso(String prefijoRuta, Set<String> metodos, Set<String> rolesPermitidos) {
        boolean aplica(String path, String metodo) {
            return path.startsWith(prefijoRuta) && metodos.contains(metodo);
        }

        boolean permite(List<?> rolesUsuario) {
            if (rolesPermitidos == null) return true;
            if (rolesUsuario == null) return false;
            return rolesUsuario.stream().anyMatch(r -> rolesPermitidos.contains(String.valueOf(r)));
        }
    }

    private static final Set<String> ESCRITURA = Set.of("POST", "PUT", "DELETE");
    private static final Set<String> TODOS_LOS_METODOS = Set.of("GET", "POST", "PUT", "DELETE");

    /**
     * Matriz de permisos:
     *  - ADMINISTRADOR: acceso total (siempre incluido en las reglas que restringen).
     *  - FOTOGRAFO: gestiona su propia agenda (reservas, disponibilidad, eventos, galería).
     *  - CLIENTE: solo lectura del catálogo, y lectura/escritura de SUS PROPIOS recursos.
     *    El filtrado "solo lo mío" se valida dentro de cada microservicio (X-User-Id).
     */
    private static final List<ReglaPermiso> REGLAS = List.of(
        new ReglaPermiso("/auth/registrar-staff", TODOS_LOS_METODOS, Set.of("ADMINISTRADOR")),

        new ReglaPermiso("/fotografos", ESCRITURA, Set.of("ADMINISTRADOR")),
        new ReglaPermiso("/tipos-sesion", ESCRITURA, Set.of("ADMINISTRADOR")),

        new ReglaPermiso("/disponibilidades", ESCRITURA, Set.of("ADMINISTRADOR", "FOTOGRAFO")),

        new ReglaPermiso("/api/v1/reservas", Set.of("PUT", "DELETE"), Set.of("ADMINISTRADOR", "FOTOGRAFO", "CLIENTE")),

        new ReglaPermiso("/api/v1/pagos", TODOS_LOS_METODOS, Set.of("ADMINISTRADOR", "CLIENTE")),

        new ReglaPermiso("/eventos", ESCRITURA, Set.of("ADMINISTRADOR", "FOTOGRAFO")),

        new ReglaPermiso("/notificaciones", ESCRITURA, Set.of("ADMINISTRADOR", "FOTOGRAFO")),

        new ReglaPermiso("/resenas", Set.of("DELETE"), Set.of("ADMINISTRADOR", "CLIENTE")),

        new ReglaPermiso("/contacto", Set.of("GET", "PUT", "DELETE"), Set.of("ADMINISTRADOR")),

        new ReglaPermiso("/api/v1/galerias", ESCRITURA, Set.of("ADMINISTRADOR", "FOTOGRAFO")),

        new ReglaPermiso("/usuario", TODOS_LOS_METODOS, Set.of("ADMINISTRADOR"))
    );

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return onError(exchange, "Token de autenticación faltante o con formato inválido.", HttpStatus.UNAUTHORIZED);
            }

            String token = authHeader.substring(7);

            try {
                Claims claims = Jwts.parser()
                        .verifyWith(Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8)))
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();

                String username = claims.getSubject();
                List<?> rolesRaw = claims.get("roles", List.class);
                Long userId = claims.get("userId", Long.class);

                String path = exchange.getRequest().getURI().getPath();
                HttpMethod method = exchange.getRequest().getMethod();
                String methodStr = (method != null) ? method.name() : "";

                for (ReglaPermiso regla : REGLAS) {
                    if (regla.aplica(path, methodStr)) {
                        if (!regla.permite(rolesRaw)) {
                            return onError(exchange, "No tienes permiso para realizar esta acción.", HttpStatus.FORBIDDEN);
                        }
                        break;
                    }
                }

                String rolesStr = "";
                if (rolesRaw != null) {
                    rolesStr = String.join(",", rolesRaw.stream().map(Object::toString).toList());
                }

                ServerHttpRequest.Builder requestBuilder = exchange.getRequest().mutate()
                        .header("X-User-Username", username)
                        .header("X-User-Roles", rolesStr);

                if (userId != null) {
                    requestBuilder.header("X-User-Id", String.valueOf(userId));
                }

                ServerHttpRequest requestMutada = requestBuilder.build();

                return chain.filter(exchange.mutate().request(requestMutada).build());

            } catch (Exception e) {
                return onError(exchange, "El token JWT ha expirado o su firma es inválida.", HttpStatus.UNAUTHORIZED);
            }
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");

        String jsonResponse = String.format(
            "{\"timestamp\": \"%s\", \"status\": %d, \"error\": \"%s\", \"mensaje\": \"%s\"}",
            LocalDateTime.now(), httpStatus.value(), httpStatus.getReasonPhrase(), err
        );

        DataBuffer buffer = response.bufferFactory().wrap(jsonResponse.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }
}