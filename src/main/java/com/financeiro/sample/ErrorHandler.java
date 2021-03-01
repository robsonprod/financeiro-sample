package com.financeiro.sample;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.hibernate.QueryException;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;
import java.io.Serializable;
import java.nio.file.AccessDeniedException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Slf4j
@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @SuppressWarnings("ConstantConditions")
    @Override
    @ResponseBody
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        val errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .filter(erro -> {
                    erro.getField();
                    return true;
                })
                .collect(toMap(fieldError ->
                                fieldError.getField()
                                        .replaceAll("([^_A-Z])([A-Z])", "$1_$2")
                                        .toLowerCase().replace(".", "_"),
                        FieldError::getDefaultMessage, (a1, a2) -> a1)
                );

        log.warn("errors de validacao: " + errors);

        MessageExceptionResponse body = body(status);
        body.setErrors(errors);

        return new ResponseEntity<>(body, headers, status);
    }

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonPropertyOrder({"timestamp", "status", "error", "message"})
    public static class MessageExceptionResponse implements Serializable, AutoCloseable {

        @JsonProperty(value = "message")
        private String message;

        @JsonProperty(value = "timestamp")
        private String timestamp;

        @JsonProperty(value = "status")
        private Integer status;

        @JsonProperty(value = "error")
        private String error;

        @JsonProperty(value = "errors")
        private Map<String, String> errors;

        public MessageExceptionResponse() {
        }

        public MessageExceptionResponse(String message) {
            this.message = message;
        }

        @Override
        public void close() {
        }
    }

    @ExceptionHandler(value = {
            StackOverflowError.class,
            IllegalStateException.class,
            IllegalArgumentException.class,
            SQLGrammarException.class,
            QueryException.class,
            PersistenceException.class,
            AccessDeniedException.class,
            NullPointerException.class,
            ParseException.class,
            ConstraintViolationException.class,
            InvalidDataAccessApiUsageException.class
    })
    public ResponseEntity<Object> handleAccessDeniedException(Exception ex, WebRequest request) {

        var status = HttpStatus.BAD_REQUEST;
        val exceptionResponse = new MessageExceptionResponse("Serviço indisponível");

        if (ex instanceof AccessDeniedException) {
            status = HttpStatus.UNAUTHORIZED;
            exceptionResponse.setMessage(ex.getMessage());

            log.warn(ex.getMessage(), request.getRemoteUser());
        } else if (ex instanceof IllegalStateException) {
            IllegalStateException stateException = (IllegalStateException) ex;
            illegalException(stateException, exceptionResponse);
        } else if (ex instanceof IllegalArgumentException) {
            IllegalArgumentException argumentException = (IllegalArgumentException) ex;
            illegalException(argumentException, exceptionResponse);
        } else if (ex instanceof ConstraintViolationException) {
            exceptionResponse.setMessage("not valid due to validation error: " + ex.getMessage());
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            exceptionResponse.setMessage("Internal server error");
        }

        log.error(ex.getMessage(), ex);

        exceptionResponse.setStatus(status.value());
        exceptionResponse.setError(status.name());
        exceptionResponse.setTimestamp(dateFormatter());

        return ResponseEntity.status(status).body(exceptionResponse);
    }

    private void illegalException(Exception ex, MessageExceptionResponse exceptionResponse) {
        if (ex.getCause() instanceof QueryException) {
            QueryException queryException = (QueryException) ex.getCause();
            exceptionResponse.setMessage("Tivemos um problema de comunicação em nossos serviços.");
            log.error(queryException.getMessage(), queryException.getQueryString());
        }
    }

    public static MessageExceptionResponse body(HttpStatus status) {
        MessageExceptionResponse exceptionResponse = new MessageExceptionResponse();
        exceptionResponse.setTimestamp(dateFormatter());
        exceptionResponse.setStatus(status.value());
        exceptionResponse.setError(status.name());
        return exceptionResponse;
    }

    public static String dateFormatter() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy H:m:s");
        return formatter.format(LocalDateTime.now());
    }
}
