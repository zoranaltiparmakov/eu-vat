package com.altiparmakov.vateu.handler;

import com.altiparmakov.vateu.model.ApiModel;
import com.altiparmakov.vateu.model.EuVatDto;
import com.altiparmakov.vateu.service.EuVatService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Optional;

/**
 * Handler for Eu Vat REST API calls.
 */
@Component
public class EuVatHandler {

    /**
     * Endpoint for receiving EU VAT rates.
     */
    @Value("${api.json_vat}")
    private String VAT_API;

    /**
     * Http client for performing HTTP calls.
     */
    private RestTemplate restTemplate;

    /**
     * Eu Vat service.
     */
    private EuVatService euVatService;

    /**
     * Constructor to initialize rest template and service DI.
     *
     * @param euVatService EU VAT service
     */
    public EuVatHandler(EuVatService euVatService) {
        this.euVatService = euVatService;
        this.restTemplate = new RestTemplate();
    }

    /**
     * Retrieves countries with highest/lowest standard vat when using query parameters.
     * <code>vat</code> query parameter options:
     * <ul>
     *     <li>high</li>
     *     <li>low</li>
     * </ul>
     *
     * @param request {@link ServerRequest} object
     * @return JSON response as list of {@link EuVatDto} objects
     */
    public Mono<ServerResponse> getRates(ServerRequest request) {
        Optional<String> vatParam = request.queryParam("vat");

        final ApiModel apiResponse;
        try {
            apiResponse = restTemplate.getForObject(VAT_API, ApiModel.class);
        } catch (RestClientException restClientException) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST);
        }

        if (apiResponse == null) {
            throw new HttpServerErrorException(HttpStatus.BAD_GATEWAY);
        }

        Collection<EuVatDto> rates;
        if (vatParam.isPresent()) {
            rates = euVatService.getRates(apiResponse, vatParam.get());
        } else {
            rates = euVatService.getRates(apiResponse);
        }

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(rates));
    }
}
