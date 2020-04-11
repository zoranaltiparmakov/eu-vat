package com.altiparmakov.vateu.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

/**
 * JSON Vat API model.
 */
@Getter @Setter @NoArgsConstructor
public class ApiModel {
    private String details;
    private String version;
    private Collection<EuVat> rates;
}
