package com.ascendion.agentflowmind.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Generated;
import lombok.ToString;

@ToString
@Generated
public class ApiResponse<T>{
    @JsonProperty("statusCode")
    private Integer statusCode;
    @JsonProperty("message")
    private String message;
    @JsonProperty("data")
    private T data;
    @JsonProperty("workflowName")
    private String workflowName;

    public ApiResponse(int statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public ApiResponse(int statusCode, String message, String workflowName, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.workflowName = workflowName;
        this.data = data;
    }
}
