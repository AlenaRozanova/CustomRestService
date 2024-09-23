package com.example.servlet;

import com.example.requset.CountryInsertRequest;
import com.example.requset.CountryModifyRequest;
import com.example.response.CountryResponse;
import com.example.service.CountryService;
import com.example.exception.CountryNotFoundException;
import com.example.exception.PathVariableException;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CountryServletTest {
    private final String SERVLET_PATH = "/countries";
    private final String REQUEST_URI_WITH_PATH_VARIABLE = "CustomRestService_war/countries/1";
    private final String REQUEST_URI_WITHOUT_PATH_VARIABLE = "CustomRestService_war/countries";

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @InjectMocks
    private CountryServlet servlet;
    @Mock
    private CountryService service;
    private StringWriter jsonResponse;

    @Test
    void testGetRequest() throws IOException, JSONException {
        mockRequestPath(true);
        mockResponseWriter();
        String expectedJson = """
                {
                    "id": 1,
                    "name": "im first",
                    "banks_name": [
                        "bank_1",
                        "bank_2"
                   ]
                }
                """;
        CountryResponse countryResponse = new CountryResponse(1,
                "im first",
                List.of("bank_1", "bank_2"));
        when(service.getCountryById(1)).thenReturn(countryResponse);
        servlet.doGet(request, response);

        Mockito.verify(response).setContentType("application/json");
        Mockito.verify(response).setCharacterEncoding("UTF-8");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
        JSONAssert.assertEquals(expectedJson, jsonResponse.toString(), JSONCompareMode.LENIENT);
    }

    @Test
    void testNotExistCountryGetRequest() throws Exception {
        mockRequestPath(true);

        given(service.getCountryById(1)).willThrow((new CountryNotFoundException(1)));

        assertThrows(CountryNotFoundException.class, () -> servlet.doGet(request, response));
    }

    @Test
    void testBlankPathVariableCountryGetRequest() throws Exception {
        mockRequestPath(false);
        assertThrows(PathVariableException.class, () -> servlet.doGet(request, response));
    }

    @Test
    void testPutRequest() throws Exception {
        mockRequestPath(true);
        jsonResponse = new StringWriter();
        final String inputJson = """
                {
                    "id": 1,
                    "name": "im first"
                }
                """;
        mockRequestReader(inputJson);
        doNothing().when(service).updateCountry(any(CountryModifyRequest.class), eq(1));
        servlet.doPut(request, response);

        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testNotExistCountryPutRequest() throws Exception {
        mockRequestPath(true);
        final String inputJson = """
                {
                    "id": 1,
                    "name": "im first"
                }
                """;
        mockRequestReader(inputJson);
        doThrow(new CountryNotFoundException(1)).when(service).updateCountry(any(CountryModifyRequest.class), eq(1));

        assertThrows(CountryNotFoundException.class, () -> servlet.doPut(request, response));
    }

    @Test
    void testBlankPathVariableCountryPutRequest() {
        mockRequestPath(false);
        assertThrows(PathVariableException.class, () -> servlet.doPut(request, response));
    }

    @Test
    void testPostRequest() throws Exception {
        jsonResponse = new StringWriter();
        final String inputJson = """
                {
                    "name": "im first"
                }
                """;
        mockRequestReader(inputJson);
        doNothing().when(service).addCountry(any(CountryInsertRequest.class));
        servlet.doPost(request, response);

        Mockito.verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    void testDeleteRequest() throws Exception {
        mockRequestPath(true);
        doNothing().when(service).deleteCountryById(1);

        servlet.doDelete(request, response);

        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDeleteNotExistCountryRequest() throws Exception {
        mockRequestPath(true);
        doThrow(new CountryNotFoundException(1)).when(service).deleteCountryById(1);

        assertThrows(CountryNotFoundException.class, () -> servlet.doDelete(request, response));
    }

    @Test
    void testBlankPathVariableCountryDeleteRequest() {
        mockRequestPath(false);
        assertThrows(PathVariableException.class, () -> servlet.doDelete(request, response));
    }

    private void mockRequestReader(final String requestJsonStr) throws IOException {
        final BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new ByteArrayInputStream(requestJsonStr.getBytes(StandardCharsets.UTF_8))));
        when(request.getReader()).thenReturn(reader);
    }

    private void mockResponseWriter() throws IOException {
        jsonResponse = new StringWriter();
        PrintWriter pw = new PrintWriter(jsonResponse);
        when(response.getWriter()).thenReturn(pw);
    }

    private void mockRequestPath(boolean isPathVariableExist) {
        when(request.getServletPath()).thenReturn(SERVLET_PATH);
        if (isPathVariableExist) when(request.getRequestURI()).thenReturn(REQUEST_URI_WITH_PATH_VARIABLE);
        else when(request.getRequestURI()).thenReturn(REQUEST_URI_WITHOUT_PATH_VARIABLE);
    }
}