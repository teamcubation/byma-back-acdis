package com.byma.back_acdits.exceptionHandler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorMessageResponse {
    private final String exception;
    private final int status;
    private final String message;
    private final String path;
    private final String method;
    private List<String> details;
    @Builder.Default
    private final LocalDateTime timestamp = LocalDateTime.now();
}
