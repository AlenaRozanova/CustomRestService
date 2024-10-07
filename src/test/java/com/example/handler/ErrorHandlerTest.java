package com.example.handler;

import com.example.exception.EntityNotFoundException;
import com.example.exception.NotProvidedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ErrorHandlerTest {
    private final String SERVLET_PATH = "/countries";
    private final String REQUEST_URI_WITH_PATH_VARIABLE = "CustomRestService_war/countries/1";
    private final String REQUEST_URI_WITHOUT_PATH_VARIABLE = "CustomRestService_war/countries";

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @InjectMocks
    private ErrorHandler servlet;

    private StringWriter jsonResponse;

    @Test
    void testGetRequestAndRuntimeException() throws IOException {
        mockResponseWriter();
        when(request.getAttribute("javax.servlet.error.exception")).thenReturn(new RuntimeException("test"));
        when(request.getAttribute("javax.servlet.error.servlet_name")).thenReturn("test2");
        when(request.getAttribute("javax.servlet.error.request_uri")).thenReturn("test3");

        servlet.doGet(request, response);

        Mockito.verify(response).setContentType("application/json");
        Mockito.verify(response).setCharacterEncoding("UTF-8");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void testDeleteRequestAndNotProvidedException() throws IOException {
        mockResponseWriter();
        when(request.getAttribute("javax.servlet.error.exception")).thenReturn(new NotProvidedException("test"));
        when(request.getAttribute("javax.servlet.error.servlet_name")).thenReturn("test2");
        when(request.getAttribute("javax.servlet.error.request_uri")).thenReturn("test3");

        servlet.doDelete(request, response);

        Mockito.verify(response).setContentType("application/json");
        Mockito.verify(response).setCharacterEncoding("UTF-8");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
    }

    @Test
    void testPutRequestAndEntityNotFoundException() throws IOException {
        mockResponseWriter();
        when(request.getAttribute("javax.servlet.error.exception")).thenReturn(new EntityNotFoundException("test"));
        when(request.getAttribute("javax.servlet.error.servlet_name")).thenReturn("test2");
        when(request.getAttribute("javax.servlet.error.request_uri")).thenReturn("test3");

        servlet.doPut(request, response);

        Mockito.verify(response).setContentType("application/json");
        Mockito.verify(response).setCharacterEncoding("UTF-8");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void testPostRequestAndThrowable() throws IOException {
        mockResponseWriter();
        when(request.getAttribute("javax.servlet.error.exception")).thenReturn(new Throwable("test"));
        when(request.getAttribute("javax.servlet.error.servlet_name")).thenReturn("test2");
        when(request.getAttribute("javax.servlet.error.request_uri")).thenReturn("test3");

        servlet.doPost(request, response);

        Mockito.verify(response).setContentType("application/json");
        Mockito.verify(response).setCharacterEncoding("UTF-8");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    private void mockResponseWriter() throws IOException {
        jsonResponse = new StringWriter();
        PrintWriter pw = new PrintWriter(jsonResponse);
        when(response.getWriter()).thenReturn(pw);
    }
}
