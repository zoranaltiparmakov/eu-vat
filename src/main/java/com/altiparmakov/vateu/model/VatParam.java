package com.altiparmakov.vateu.model;

/**
 * Enumerator for query parameters used for VAT.
 * <p>
 * Allowed values are:
 * <ul>
 *     <li>LOW</li>
 *     <li>HIGH</li>
 * </ul>
 */
public enum VatParam {
    LOW("low"), HIGH("high");

    String value;

    VatParam(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}