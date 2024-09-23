package com.example.servlet;

import com.example.exception.PathVariableException;
import com.example.requset.CountryInsertRequest;
import com.example.requset.CountryModifyRequest;
import com.example.response.CountryResponse;
import com.example.service.CountryService;
import com.example.util.ServletUtils;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
@WebServlet(value = "/countries", name = "CountryServlet")
public class CountryServlet extends HttpServlet {
    private static final Logger log = LogManager.getLogger(CountryServlet.class);
    private final CountryService countryService;

    @Inject
    public CountryServlet(CountryService countryService) {
        this.countryService = countryService;
    }

    /**
     * Handles HTTP POST requests to add a new country.
     *
     * @param request  the {@link HttpServletRequest} object that contains the request the client made of the servlet
     * @param response the {@link HttpServletResponse} object that contains the response the servlet returns to the client
     * @throws IOException if an error occurs while reading or writing data to/from the client
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        CountryInsertRequest countryInsertRequest = ServletUtils.readRequestBody(request, CountryInsertRequest.class);
        countryService.addCountry(countryInsertRequest);
        log.info("country added: %s", countryInsertRequest);
        response.setStatus(HttpServletResponse.SC_CREATED);
    }

    /**
     * get country by specified id (path variable) if exist
     *
     * @param request  an {@link HttpServletRequest} object that
     *                 contains the request the client has made
     *                 of the servlet
     * @param response an {@link HttpServletResponse} object that
     *                 contains the response the servlet sends
     *                 to the client
     * @throws NumberFormatException if path variable id can't be parsed to Integer
     * @throws PathVariableException if there is no path variable in request
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) {
        String pathVariable = ServletUtils.getPathVariable(request);
        if (!pathVariable.isEmpty()) {
            int countryId = Integer.parseInt(pathVariable);
            CountryResponse country = countryService.getCountryById(countryId);
            ServletUtils.writeJsonResponse(response, country, HttpServletResponse.SC_OK);
        } else throw new PathVariableException("Need to specify country id to get");
    }


    /**
     * Updates an country by id if country exist
     *
     * @param request  the {@link HttpServletRequest} object that
     *                 contains the request the client made of
     *                 the servlet
     * @param response the {@link HttpServletResponse} object that
     *                 contains the response the servlet returns
     *                 to the client
     * @throws NumberFormatException if path variable id can't be parsed to Integer
     * @throws PathVariableException if no country id is specified in the path variable
     */
    @Override
    public void doPut(final HttpServletRequest request, final HttpServletResponse response) {
        String pathVariable = ServletUtils.getPathVariable(request);
        if (!pathVariable.isEmpty()) {
            int countryId = Integer.parseInt(pathVariable);
            CountryModifyRequest countryModifyRequest = ServletUtils.readRequestBody(request, CountryModifyRequest.class);
            countryService.updateCountry(countryModifyRequest, countryId);
            response.setStatus(HttpServletResponse.SC_OK);
        } else throw new PathVariableException("Need to specify country id for update");
    }

    /**
     * Deletes an country by id.
     *
     * @param request  the {@link HttpServletRequest} object that
     *                 contains the request the client made of
     *                 the servlet
     * @param response the {@link HttpServletResponse} object that
     *                 contains the response the servlet returns
     *                 to the client
     * @throws NumberFormatException if path variable id can't be parsed to Integer
     * @throws PathVariableException if no country id is specified in the path variable
     */
    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) {
        String pathVariable = ServletUtils.getPathVariable(request);
        if (!pathVariable.isEmpty()) {
            int countryId = Integer.parseInt(pathVariable);
            countryService.deleteCountryById(countryId);
            response.setStatus(HttpServletResponse.SC_OK);
        } else throw new PathVariableException("Need to specify country id for delete");
    }
}
