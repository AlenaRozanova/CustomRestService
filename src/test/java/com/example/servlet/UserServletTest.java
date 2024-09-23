package com.example.servlet;

import com.example.exception.PathVariableException;
import com.example.exception.UserNotFoundException;
import com.example.requset.UserInsertRequest;
import com.example.requset.UserModifyRequest;
import com.example.response.UserResponse;
import com.example.service.UserService;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServletTest {
    private final String SERVLET_PATH = "/users";
    private final String REQUEST_URI_WITH_PATH_VARIABLE = "CustomRestService_war/users/1";
    private final String REQUEST_URI_WITHOUT_PATH_VARIABLE = "CustomRestService_war/users";

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @InjectMocks
    private UserServlet servlet;
    @Mock
    private UserService userService;
    private StringWriter jsonResponse;

    @Test
    void testGetRequest() throws Exception {
        mockRequestPath(true);
        mockResponseWriter();
        String expectedJson = """
                {
                   "id": 1,
                   "name": "user",
                   "email": "user@mail.ru",
                   "sex": "RUS",
                   "banks_name": [
                      "bank_1",
                      "bank_2"
                   ]
                }
                """;
        final UserResponse userResponse = new UserResponse(1, "user",
                "user@mail.ru",
                "RUS",
                List.of("bank_1", "bank_2"));
        when(userService.getUserById(1)).thenReturn(userResponse);
        servlet.doGet(request, response);


        Mockito.verify(response).setContentType("application/json");
        Mockito.verify(response).setCharacterEncoding("UTF-8");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
        JSONAssert.assertEquals(expectedJson, jsonResponse.toString(), JSONCompareMode.LENIENT);
    }

    @Test
    void testNotExistUserGetRequest() throws Exception {
        mockRequestPath(true);

        given(userService.getUserById(1)).willThrow((new UserNotFoundException(1)));

        assertThrows(UserNotFoundException.class, () -> servlet.doGet(request, response));
    }

    @Test
    void testBlankPathVariableUserGetRequest() throws Exception {
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
                   "name": "user",
                   "email": "user@mail.ru",
                   "sex": "RUS"
                }
                """;
        mockRequestReader(inputJson);
        doNothing().when(userService).updateUser(any(UserModifyRequest.class), eq(1));
        servlet.doPut(request, response);

        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testNotExistUserPutRequest() throws Exception {
        mockRequestPath(true);
        final String inputJson = """
                {
                   "id": 1,
                   "name": "user",
                   "email": "user@mail.ru",
                   "sex": "RUS"
                }
                """;
        mockRequestReader(inputJson);
        doThrow(new UserNotFoundException(1)).when(userService).updateUser(any(UserModifyRequest.class), eq(1));

        assertThrows(UserNotFoundException.class, () -> servlet.doPut(request, response));
    }

    @Test
    void testBlankPathVariableUserPutRequest() {
        mockRequestPath(false);
        assertThrows(PathVariableException.class, () -> servlet.doPut(request, response));
    }

    @Test
    void testPostRequest() throws Exception {
        jsonResponse = new StringWriter();
        final String inputJson = """
                {
                   "name": "user",
                   "email": "user@mail.ru",
                   "sex": "RUS"
                }
                """;
        mockRequestReader(inputJson);
        doNothing().when(userService).addUser(any(UserInsertRequest.class));
        servlet.doPost(request, response);

        Mockito.verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }


    @Test
    void testDeleteRequest() throws Exception {
        mockRequestPath(true);
        doNothing().when(userService).deleteUserById(1);

        servlet.doDelete(request, response);

        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDeleteNotExistUserRequest() throws Exception {
        mockRequestPath(true);
        doThrow(new UserNotFoundException(1)).when(userService).deleteUserById(eq(1));

        assertThrows(UserNotFoundException.class, () -> servlet.doDelete(request, response));
    }

    @Test
    void testBlankPathVariableUserDeleteRequest() {
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