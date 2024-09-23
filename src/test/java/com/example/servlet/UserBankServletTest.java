package com.example.servlet;

import com.example.exception.BankNotFoundException;
import com.example.exception.UserNotFoundException;
import com.example.requset.UserBankRequest;
import com.example.service.UserBankService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserBankServletTest {
    private final String INPUT_JSON = """
            {
               "user_id": 1,
               "banks_id": [
                  1,
                  2
               ]
            }
            """;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @InjectMocks
    private UserBankServlet servlet;
    @Mock
    private UserBankService service;


    @Test
    void testPostRequest() throws Exception {
        mockRequestReader(INPUT_JSON);
        doNothing().when(service).addBanksToUser(any(UserBankRequest.class));
        servlet.doPost(request, response);

        Mockito.verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    void testNotExistUserPostRequest() throws IOException {
        mockRequestReader(INPUT_JSON);
        doThrow(new UserNotFoundException(1)).when(service).addBanksToUser(any(UserBankRequest.class));

        assertThrows(UserNotFoundException.class, () -> servlet.doPost(request, response));
    }

    @Test
    void testNotExistBankPostRequest() throws IOException {
        mockRequestReader(INPUT_JSON);
        doThrow(new BankNotFoundException(1)).when(service).addBanksToUser(any(UserBankRequest.class));

        assertThrows(BankNotFoundException.class, () -> servlet.doPost(request, response));
    }

    @Test
    void testDeleteRequest() throws Exception {
        mockRequestReader(INPUT_JSON);
        doNothing().when(service).deleteBankFromUser(any(UserBankRequest.class));
        servlet.doDelete(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testNotExistUserDeleteRequest() throws IOException {
        mockRequestReader(INPUT_JSON);
        doThrow(new UserNotFoundException(1)).when(service).deleteBankFromUser(any(UserBankRequest.class));

        assertThrows(UserNotFoundException.class, () -> servlet.doDelete(request, response));
    }

    @Test
    void testNotExistBankDeleteRequest() throws IOException {
        mockRequestReader(INPUT_JSON);
        doThrow(new BankNotFoundException(1)).when(service).deleteBankFromUser(any(UserBankRequest.class));

        assertThrows(BankNotFoundException.class, () -> servlet.doDelete(request, response));
    }

    private void mockRequestReader(final String requestJsonStr) throws IOException {
        final BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new ByteArrayInputStream(requestJsonStr.getBytes(StandardCharsets.UTF_8))));
        when(request.getReader()).thenReturn(reader);
    }
}