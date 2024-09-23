package com.example.util;

import com.example.exception.BadRequestException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet utils class
 */
public final class ServletUtils {
    private ServletUtils() {
    }

    /**
     * Writes a JSON response to the given HTTP response.
     *
     * @param response       The HTTP response to write the JSON response to.
     * @param body           The object to be converted to JSON and written to the response.
     * @param responseStatus The HTTP status code to be set in the response.
     * @param <T>            The type of the object to be converted to JSON.
     * @throws RuntimeException if an error occurs during the conversion
     */
    public static <T> void writeJsonResponse(final HttpServletResponse response, final T body, final int responseStatus) {
        try {
            String jsonString = JsonUtils.toJsonString(body);
            response.setStatus(responseStatus);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().println(jsonString);
            response.getWriter().flush();
        } catch (IOException ioException) {
            throw new RuntimeException(ioException.getMessage());
        }
    }

    /**
     * Reads the request body as an object of the specified class.
     *
     * @param request     The HTTP request containing the request body.
     * @param objectClass The class of the object to be created from the request body.
     * @param <T>         The type of the object to be created from the request body.
     * @return The object created from the request body.
     * @throws BadRequestException If the request body is not in the expected format.
     */
    public static <T> T readRequestBody(final HttpServletRequest request, final Class<T> objectClass) {
        try {
            return JsonUtils.fromJsonString(request.getReader(), objectClass);
        } catch (IOException ioException) {
            throw new BadRequestException(ioException.getMessage());
        }
    }

    /**
     * Retrieves the path variable from the given HTTP request.
     *
     * @param request The HTTP request containing the path variable.
     * @return The path variable extracted from the request.
     */
    public static String getPathVariable(final HttpServletRequest request) {
        String uri = request.getRequestURI();
        String contextPath = request.getServletPath();

        int lastPartStartIndex = uri.lastIndexOf(contextPath) + contextPath.length();
        int parametersStartIndex = uri.indexOf('?');
        return uri.substring(Math.min(lastPartStartIndex + 1, uri.length()),
                parametersStartIndex == -1 ? uri.length() : parametersStartIndex);
    }
}
