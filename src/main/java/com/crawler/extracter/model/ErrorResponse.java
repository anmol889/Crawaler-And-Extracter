package com.crawler.extracter.model;

import lombok.Data;

@Data
public class ErrorResponse {
    private int errorCode;
    private String message;
}
