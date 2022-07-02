package com.onoprienko.server.entity;

public enum StatusCode {
    OK(200, "OK"),
    BAD_REQUEST(400, " Bad Request"),
    NOT_FOUND(404, " Not Found"),
    METHOD_NOT_ALLOWED(405, " Method not allowed"),
    INTERNAL_SERVER_ERROR(500, " Internal Server Error");

    private final int code;
    private final String status;

    StatusCode(int code, String statusText) {
        this.code = code;
        this.status = statusText;
    }

    @Override
    public String toString() {
        return code + " " + status + "\r\n\r\n";
    }
}
