package com.example.handler;

import com.example.handler.model.Error;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorTest {

    @Test
    void errorTest() {
        int statusCode = 400;
        String servletName = "servletName";
        String requestedURI = "requestedURI";
        String message = "message";
        Error error = new Error(statusCode, servletName, requestedURI, message);

        assertEquals(error.getStatusCode(), statusCode);
        assertEquals(error.getServletName(), servletName);
        assertEquals(error.getRequestedURI(), requestedURI);
        assertEquals(error.getMessage(), message);

        int statusCode2 = 404;
        String servletName2 = "servletName2";
        String requestedURI2 = "requestedURI2";
        String message2 = "message2";
        error.setStatusCode(statusCode2);
        error.setServletName(servletName2);
        error.setRequestedURI(requestedURI2);
        error.setMessage(message2);

        assertEquals(error.getStatusCode(), statusCode2);
        assertEquals(error.getServletName(), servletName2);
        assertEquals(error.getRequestedURI(), requestedURI2);
        assertEquals(error.getMessage(), message2);
    }
}
