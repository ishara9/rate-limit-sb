package com.ishravlabs.ratelimitsb.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
@Builder
@AllArgsConstructor
public class CustomError {
    private String timestamp;
    private String message;
    private int status;
    private String path;
    private String error;
}
