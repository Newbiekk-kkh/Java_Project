package com.example.project.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "API 에러 응답")
public class ErrorResponse {

    @Schema(implementation = ErrorDetails.class)
    private ErrorDetails error;
}
