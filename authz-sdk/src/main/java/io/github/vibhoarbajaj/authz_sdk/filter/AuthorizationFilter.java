package io.github.vibhoarbajaj.authz_sdk.filter;


import io.github.vibhoarbajaj.authz_sdk.manager.AuthorizationManager;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AuthorizationFilter implements Filter {

    private final AuthorizationManager manager;

    public AuthorizationFilter(AuthorizationManager manager) {
        this.manager = manager;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        boolean authorized = manager.authorize(req);
        if (!authorized) {
            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            res.getWriter().write("Access Denied");
            return;
        }

        chain.doFilter(request, response);
    }
}