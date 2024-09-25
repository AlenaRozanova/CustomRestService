package com.example.servlet;

import com.example.exception.BankNotFoundException;
import com.example.exception.PathVariableException;
import com.example.requset.BankInsertRequest;
import com.example.requset.BankModifyRequest;
import com.example.response.BankResponse;
import com.example.service.BankService;
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
class BankServletTest {
    private final String SERVLET_PATH = "/banks";
    private final String REQUEST_URI_WITH_PATH_VARIABLE = "CustomRestService_war/banks/1";
    private final String REQUEST_URI_WITHOUT_PATH_VARIABLE = "CustomRestService_war/banks";

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @InjectMocks
    private BankServlet servlet;
    @Mock
    private BankService service;
    private StringWriter jsonResponse;

    @Test
    void testGetRequest() throws IOException, JSONException {
        mockRequestPath(true);
        mockResponseWriter();
        String expectedJson = """
                {
                   "id": 1,
                   "name": "bank_name",
                   "country_name": "country",
                   "users_name": [
                      "user_1",
                      "user_2"
                   ]
                }
                """;
        final BankResponse bankResponse = new BankResponse(1,
                "bank_name",
                "country",
                List.of("user_1", "user_2"));
        when(service.getBankById(1)).thenReturn(bankResponse);
        servlet.doGet(request, response);

        Mockito.verify(response).setContentType("application/json");
        Mockito.verify(response).setCharacterEncoding("UTF-8");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
        JSONAssert.assertEquals(expectedJson, jsonResponse.toString(), JSONCompareMode.LENIENT);
    }

    @Test
    void testNotExistBankGetRequest() throws Exception {
        mockRequestPath(true);

        given(service.getBankById(1)).willThrow((new BankNotFoundException(1)));

        assertThrows(BankNotFoundException.class, () -> servlet.doGet(request, response));
    }

    @Test
    void testBlankPathVariableBankGetRequest() throws Exception {
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
                    "name": "bank_name",
                    "country_id": 1
                }
                """;
        mockRequestReader(inputJson);
        doNothing().when(service).updateBank(any(BankModifyRequest.class), eq(1));
        servlet.doPut(request, response);

        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testNotExistBankPutRequest() throws Exception {
        mockRequestPath(true);
        final String inputJson = """
                {
                    "id": 1,
                    "name": "bank_name",
                    "country_id": 1
                }
                """;
        mockRequestReader(inputJson);
        doThrow(new BankNotFoundException(1)).when(service).updateBank(any(BankModifyRequest.class), eq(1));

        assertThrows(BankNotFoundException.class, () -> servlet.doPut(request, response));
    }

    @Test
    void testBlankPathVariableBankPutRequest() {
        mockRequestPath(false);
        assertThrows(PathVariableException.class, () -> servlet.doPut(request, response));
    }

    @Test
    void testPostRequest() throws Exception {
        jsonResponse = new StringWriter();
        final String inputJson = """
                {
                    "name": "bank_name",
                    "country_id": 1
                }
                """;
        mockRequestReader(inputJson);
        doNothing().when(service).addBank(any(BankInsertRequest.class));
        servlet.doPost(request, response);

        Mockito.verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    void testDeleteRequest() throws Exception {
        mockRequestPath(true);
        doNothing().when(service).deleteBankById(1);

        servlet.doDelete(request, response);

        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDeleteNotExistBankRequest() throws Exception {
        mockRequestPath(true);
        doThrow(new BankNotFoundException(1)).when(service).deleteBankById(eq(1));

        assertThrows(BankNotFoundException.class, () -> servlet.doDelete(request, response));
    }

    @Test
    void testBlankPathVariableBankDeleteRequest() {
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