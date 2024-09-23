package com.example.servlet;

import com.example.exception.NotProvidedException;
import com.example.requset.UserBankRequest;
import com.example.service.UserBankService;
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
 * UserBankServlet is a servlet that handles HTTP requests related to adding and removing banks from a user.
 * It uses the UserBankService to perform these operations.
 */
@Singleton
@WebServlet(value = "/usersbanks", name = "UserBankServlet")
public class UserBankServlet extends HttpServlet {
    private static final Logger log = LogManager.getLogger(UserBankServlet.class);

    private final UserBankService userBankService;

    @Inject
    public UserBankServlet(final UserBankService userBankService) {
        this.userBankService = userBankService;
    }

    /**
     * Handles HTTP POST requests by adding banks to a user.
     *
     * @param request  the HTTP request containing the UserBankRequest object.
     * @param response the HTTP response to be sent back to the client.
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) {
        UserBankRequest userBankRequest = ServletUtils.readRequestBody(request, UserBankRequest.class);
        userBankService.addBanksToUser(userBankRequest);
        log.info("to user with id {} added banks - {}", userBankRequest.getUserId(), userBankRequest.getBanksId());
        response.setStatus(HttpServletResponse.SC_CREATED);
    }

    /**
     * Handles HTTP GET requests. This method is not implemented as it is not provided.
     *
     * @param request  the HTTP request.
     * @param response the HTTP response to be sent back to the client.
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) {
        throw new NotProvidedException("GET NOT PROVIDED! USE POST or DELETE");
    }

    /**
     * Handles HTTP PUT requests. This method is not implemented as it is not provided.
     *
     * @param request  the HTTP request.
     * @param response the HTTP response to be sent back to the client.
     */
    @Override
    public void doPut(final HttpServletRequest request, final HttpServletResponse response) {
        throw new NotProvidedException("PUT NOT PROVIDED! USE POST or DELETE");
    }

    /**
     * Handles HTTP DELETE requests by removing banks from a user.
     *
     * @param request  the HTTP request containing the UserBankRequest object.
     * @param response the HTTP response to be sent back to the client.
     */
    @Override
    public void doDelete(final HttpServletRequest request, final HttpServletResponse response) {
        UserBankRequest userBankRequest = ServletUtils.readRequestBody(request, UserBankRequest.class);
        userBankService.deleteBankFromUser(userBankRequest);
        log.info("from user with id {} deleted banks - {}", userBankRequest.getUserId(), userBankRequest.getBanksId());
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
