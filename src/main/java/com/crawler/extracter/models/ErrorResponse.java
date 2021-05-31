package com.crawler.extracter.models;

import lombok.Data;

@Data
public class ErrorResponse {
    private int errorCode;
    private String message;
}
