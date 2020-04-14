package com.altiparmakov.vateu.handler;

import com.altiparmakov.vateu.model.EuVatDto;
import com.altiparmakov.vateu.routes.EuVatRouter;
import com.altiparmakov.vateu.service.EuVatService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {EuVatRouter.class, EuVatHandler.class, EuVatService.class})
@WebFluxTest
public class EuVatHandlerTest {

    @Autowired
    private WebTestClient webTestClient;

    /**
     * Tests that request to eu-vat path will return three countries for given query parameter vat=high.
     */
    @Test
    void testRouteGetThreeCountriesWithHighestRates() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/eu-vat")
                        .queryParam("vat", "high")
                        .build())
                .accept(MediaType.APPLICATION_JSON).exchange()
                .expectStatus().isOk()
                .expectBodyList(EuVatDto.class).hasSize(3);
    }
}
