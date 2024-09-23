package com.example.servlet;

import com.example.exception.PathVariableException;
import com.example.requset.BankInsertRequest;
import com.example.requset.BankModifyRequest;
import com.example.response.BankResponse;
import com.example.service.BankService;
import com.example.util.ServletUtils;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that serve requests linked to banks
 */

@Singleton
@WebServlet(value = "/banks", name = "BankServlet")
public class BankServlet extends HttpServlet {
    private static final Logger log = LogManager.getLogger(BankServlet.class);
    private final BankService bankService;

    @Inject
    public BankServlet(final BankService bankService) {
        this.bankService = bankService;
    }

    /**
     * Handles HTTP POST requests to add a new bank.
     *
     * @param request  the {@link HttpServletRequest} object that contains the request the client made of the servlet
     * @param response the {@link HttpServletResponse} object that contains the response the servlet returns to the client
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) {
        BankInsertRequest bankInsertRequest = ServletUtils.readRequestBody(request, BankInsertRequest.class);
        bankService.addBank(bankInsertRequest);
        log.info("bank added: %s", bankInsertRequest);
        response.setStatus(HttpServletResponse.SC_CREATED);
    }

    /**
     * get bank by specified id (path variable) if exist
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
            int bankId = Integer.parseInt(pathVariable);
            BankResponse bank = bankService.getBankById(bankId);
            ServletUtils.writeJsonResponse(response, bank, HttpServletResponse.SC_OK);
        } else throw new PathVariableException("Need to specify bank id to get");
    }


    /**
     * Updates a bank by id if bank exist
     *
     * @param request  the {@link HttpServletRequest} object that
     *                 contains the request the client made of
     *                 the servlet
     * @param response the {@link HttpServletResponse} object that
     *                 contains the response the servlet returns
     *                 to the client
     * @throws NumberFormatException if path variable id can't be parsed to Integer
     * @throws PathVariableException if no bank id is specified in the path variable
     */
    @Override
    public void doPut(final HttpServletRequest request, final HttpServletResponse response) {
        String pathVariable = ServletUtils.getPathVariable(request);
        if (!pathVariable.isEmpty()) {
            int bankId = Integer.parseInt(pathVariable);
            BankModifyRequest bankModifyRequest = ServletUtils.readRequestBody(request, BankModifyRequest.class);
            bankService.updateBank(bankModifyRequest, bankId);
            response.setStatus(HttpServletResponse.SC_OK);
        } else throw new PathVariableException("Need to specify bank id for update");
    }

    /**
     * Deletes a bank by id.
     *
     * @param request  the {@link HttpServletRequest} object that
     *                 contains the request the client made of
     *                 the servlet
     * @param response the {@link HttpServletResponse} object that
     *                 contains the response the servlet returns
     *                 to the client
     * @throws NumberFormatException if path variable id can't be parsed to Integer
     * @throws PathVariableException if no bank id is specified in the path variable
     */
    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) {
        String pathVariable = ServletUtils.getPathVariable(request);
        if (!pathVariable.isEmpty()) {
            int bankId = Integer.parseInt(pathVariable);
            bankService.deleteBankById(bankId);
            response.setStatus(HttpServletResponse.SC_OK);
        } else throw new PathVariableException("Need to specify bank id for delete");
    }
}
