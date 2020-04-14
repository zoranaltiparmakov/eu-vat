package com.altiparmakov.vateu.service;

import com.altiparmakov.vateu.handler.EuVatHandler;
import com.altiparmakov.vateu.model.ApiModel;
import com.altiparmakov.vateu.model.EuVat;
import com.altiparmakov.vateu.model.EuVatDto;
import com.altiparmakov.vateu.model.VatParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service that keeps logic for {@link EuVatHandler}.
 */
@Slf4j
@Service
public class EuVatService {

    /**
     * Number of elements in the resulting collection.
     */
    private static final int NUM_ELEMENTS = 3;

    /**
     * Returns rates based on vat query parameter, and sorts them appropriately.
     *
     * @param apiModel {@link ApiModel} object
     * @param vatParam query parameter
     * @return collection of rates
     */
    public Collection<EuVatDto> getRates(ApiModel apiModel, String vatParam) {
        // Extracts only rates from ApiModel and converts them to stream of {@link EuVatDto} objects.
        Stream<EuVatDto> vatRatesStream = apiModel.getRates()
                .parallelStream()
                .map(this::convertToDto);

        if (vatParam.equalsIgnoreCase(VatParam.LOW.getValue())) {
            // Ascending sort.
            vatRatesStream = vatRatesStream
                    .sorted(Comparator.comparingDouble(EuVatDto::getStandardVat))
                    .limit(NUM_ELEMENTS);
        } else if (vatParam.equalsIgnoreCase(VatParam.HIGH.getValue())) {
            // Descending sort.
            vatRatesStream = vatRatesStream
                    .sorted(Comparator.comparingDouble(EuVatDto::getStandardVat).reversed())
                    .limit(NUM_ELEMENTS);
        }

        return vatRatesStream.collect(Collectors.toList());
    }

    /**
     * Overloading method for {@link #getRates(ApiModel, String)} when all rates need to be returned.
     *
     * @param apiModel {@link ApiModel object}
     * @return collection of vat rates
     */
    public Collection<EuVatDto> getRates(ApiModel apiModel) {
        return getRates(apiModel, "");
    }

    /**
     * Converts EuVat object into {@link EuVatDto} object with only data needed for representation.
     *
     * @param euVat original object
     * @return DTO object
     */
    private EuVatDto convertToDto(EuVat euVat) {
        EuVatDto vatDto = new EuVatDto();
        vatDto.setCode(euVat.getCode());
        vatDto.setName(euVat.getName());

        // Get latest effective date from periods.
        Optional<EuVat.Period> period = euVat.getPeriods()
                .stream()
                .max(Comparator.comparing(EuVat.Period::getEffectiveFrom));
        // Set standard vat or set NaN if no standard vat for that period exists.
        period.ifPresentOrElse(
                value -> vatDto.setStandardVat(value.getRates().getStandard()),
                () -> vatDto.setStandardVat(Double.NaN));

        return vatDto;
    }
}
