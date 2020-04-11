package com.altiparmakov.vateu.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;
import java.util.Collection;

/**
 * Model that keeps EU Vats.
 */
@Getter @Setter @ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class EuVat {
    private String name;
    private String code;
    @JsonProperty("country_code")
    private String countryCode;
    private Collection<Period> periods;

    @Getter @Setter @ToString
    public static class Period {
        @JsonProperty("effective_from")
        private Date effectiveFrom;
        private Rate rates;
    }

    @Getter @Setter @ToString
    public static class Rate {
        @JsonProperty("super_reduced")
        private double superReduced;
        private double reduced;
        private double standard;
    }
}
