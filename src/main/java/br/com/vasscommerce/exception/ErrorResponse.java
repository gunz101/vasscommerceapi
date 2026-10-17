package br.com.vasscommerce.exception;

import java.util.List;

public record ErrorResponse(String error, List<FieldMessage> fields) {

    public record FieldMessage(String name, String message) {}
}
