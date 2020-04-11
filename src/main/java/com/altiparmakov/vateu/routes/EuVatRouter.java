package com.altiparmakov.vateu.routes;

import com.altiparmakov.vateu.handler.EuVatHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * Router for defining REST API routes for {@link EuVatHandler}, instead using annotation in the controller.
 */
@Configuration
public class EuVatRouter {

    /**
     * Defines list of routes for the EU Vat API.
     *
     * @param handler handler used for EU VAT APIs
     * @return {@link ServerRequest} object with results
     */
    @Bean
    public RouterFunction<ServerResponse> route(EuVatHandler handler) {
        return RouterFunctions
                .route(RequestPredicates.GET("/eu-vat")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::getRates);
    }
}
