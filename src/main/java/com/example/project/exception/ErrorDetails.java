package com.example.project.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "에러 상세 정보")
public class ErrorDetails {

    @Schema(description = "에러 코드", example = "각 상황에 맞는 에러코드")
    private String code;

    @Schema(description = "에러 메시지", example = "각 상황에 맞는 에러메시지")
    private String message;
}
