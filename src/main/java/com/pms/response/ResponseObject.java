package com.pms.response;

public record ResponseObject(
        int status,
        String message,
        Object data
) {

}
