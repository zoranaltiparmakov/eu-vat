package com.altiparmakov.vateu.service;

import com.altiparmakov.vateu.handler.EuVatHandler;
import com.altiparmakov.vateu.util.VatParam;
import com.altiparmakov.vateu.model.ApiModel;
import com.altiparmakov.vateu.model.EuVat;
import com.altiparmakov.vateu.model.EuVatDto;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service that keeps logic for {@link EuVatHandler}.
 */
@Service
public class EuVatService {

    /**
     * Number of elements in the resulting object.
     */
    private static final int NUM_ELEMENTS = 3;

    /**
     * Returns rates based on vat query parameter.
     *
     * @param apiModel {@link ApiModel} object
     * @param vatParam query parameter
     * @return collection of rates
     */
    public Collection<EuVatDto> getRates(ApiModel apiModel, String vatParam) {
        Collection<EuVatDto> vatApiResponse = getRates(apiModel);

        if (vatParam.equalsIgnoreCase(VatParam.LOW.getValue())) {
            vatApiResponse = vatApiResponse.stream()
                    .sorted(Comparator.comparingDouble(EuVatDto::getStandardVat))
                    .limit(NUM_ELEMENTS)
                    .collect(Collectors.toList());
        } else if (vatParam.equalsIgnoreCase(VatParam.HIGH.getValue())) {
            vatApiResponse = vatApiResponse.stream()
                    .sorted(Comparator.comparingDouble(EuVatDto::getStandardVat).reversed())
                    .limit(NUM_ELEMENTS)
                    .collect(Collectors.toList());
        }

        return vatApiResponse;
    }

    /**
     * Returns all rates.
     *
     * @param apiModel api model
     * @return collection of {@link EuVatDto} objects
     */
    public Collection<EuVatDto> getRates(ApiModel apiModel) {
        return apiModel.getRates().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
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

        Collection<EuVat.Period> periods = euVat.getPeriods();
        Optional<Date> latestEffectivePeriod = getLatestEffectivePeriod(periods);

        EuVat.Period period = periods.iterator().next();
        Iterator<EuVat.Period> periodIterator = periods.iterator();

        // Loops through the periods if has multiple, and assigns the last in effect to the period variable.
        if (latestEffectivePeriod.isPresent() && periods.size() > 1) {
            while (periodIterator.hasNext()) {
                if (periodIterator.next().getEffectiveFrom().equals(latestEffectivePeriod.get())) {
                    period = periodIterator.next();
                }
            }
        }

        vatDto.setStandardVat(period.getRates().getStandard());

        return vatDto;
    }

    /**
     * Returns date of the latest effective period.
     *
     * @param periods collection of periods
     * @return {@link Optional} date
     */
    private Optional<Date> getLatestEffectivePeriod(Collection<EuVat.Period> periods) {
        return periods.stream()
                .map(EuVat.Period::getEffectiveFrom)
                .max(Date::compareTo);
    }
}
