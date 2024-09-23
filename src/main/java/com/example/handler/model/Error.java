package com.example.handler.model;

/**
 * POJO containing error info
 */
public class Error {
    private Integer statusCode;

    private String servletName;

    private String requestedURI;
    private String message;

    public Error(Integer statusCode, String servletName, String requestedURI, String message) {
        this.statusCode = statusCode;
        this.servletName = servletName;
        this.requestedURI = requestedURI;
        this.message = message;
    }

    public String getRequestedURI() {
        return requestedURI;
    }

    public void setRequestedURI(String requestedURI) {
        this.requestedURI = requestedURI;
    }

    public String getServletName() {
        return servletName;
    }

    public void setServletName(String servletName) {
        this.servletName = servletName;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
