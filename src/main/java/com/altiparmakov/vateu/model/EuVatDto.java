package com.altiparmakov.vateu.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO object used to return specific values for the EU Vat request.
 */
@Getter @Setter @ToString
public class EuVatDto {
    private String name;
    private String code;
    @JsonProperty("standard_vat")
    private double standardVat;
}
