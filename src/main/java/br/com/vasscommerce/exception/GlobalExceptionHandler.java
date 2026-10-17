package br.com.vasscommerce.exception;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import br.com.vasscommerce.exception.ErrorResponse.FieldMessage;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // falha no @Valid (ex.: nome vazio, valor negativo)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        List<FieldMessage> fields = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> new FieldMessage(e.getField(), e.getDefaultMessage()))
                .toList();
        return ResponseEntity.badRequest().body(new ErrorResponse("Invalid input", fields));
    }

    // JSON malformado ou tipo errado no corpo (ex.: "valor": "abc")
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMalformedJson(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse("Malformed JSON", List.of()));
    }

    // parâmetro de rota/consulta com tipo errado (ex.: /produto/abc)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        FieldMessage field = new FieldMessage(ex.getName(), "valor inválido: " + ex.getValue());
        return ResponseEntity.badRequest().body(new ErrorResponse("Invalid parameter", List.of(field)));
    }
}
