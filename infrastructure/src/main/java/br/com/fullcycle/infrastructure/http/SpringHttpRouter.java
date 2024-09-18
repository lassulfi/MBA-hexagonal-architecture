package br.com.fullcycle.infrastructure.http;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

public class SpringHttpRouter implements HttpRouter {

    private static final Logger LOG = LoggerFactory.getLogger(SpringHttpRouter.class);

    private final RouterFunctions.Builder router;

    public SpringHttpRouter() {
        this.router = RouterFunctions.route();
    }

    @Override
    public <T> HttpRouter POST(final String pattern, final HttpHandler<T> handler) {
        this.router.POST(pattern, wrapHandler(pattern, handler));

        return this;
    }

    @Override
    public <T> HttpRouter GET(final String pattern, final HttpHandler<T> handler) {
        this.router.GET(pattern, wrapHandler(pattern, handler));

        return this;
    }

    private <T> HandlerFunction<ServerResponse> wrapHandler(String pattern, HttpHandler<T> handler) {
        return request -> {
            try {
                final var response = handler.handle(new SpringHttpRequest(request));
                return ServerResponse.status(response.statusCode())
                        .headers(headers -> response.headers().forEach(headers::add))
                        .body(response.body());
            } catch (final Throwable t) {
                LOG.error("Unexpected error was observed at %s".formatted(pattern), t);
                return ServerResponse.status(500).body("Unexpected error was observed");
            }
        };
    }

    public RouterFunctions.Builder router() {
        return router;
    }

    public record SpringHttpRequest(ServerRequest request) implements HttpRequest {

        @Override
        public <T> T body(final Class<T> tClass) {
            try {
                return request.body(tClass);
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }

        @Override
        public String pathParam(final String name) {
            return request.pathVariable(name);
        }

        @Override
        public Optional<String> queryParam(String name) {
            return request.param(name);
        }
    }
}
