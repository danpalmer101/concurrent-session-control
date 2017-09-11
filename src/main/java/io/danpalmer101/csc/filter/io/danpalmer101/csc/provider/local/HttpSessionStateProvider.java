package io.danpalmer101.csc.filter.io.danpalmer101.csc.provider.local;

import io.danpalmer101.csc.filter.io.danpalmer101.csc.provider.SessionStateProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Default SessionRepositoryProvider implementation for a session that is represented
 * by a standard HttpSession managed by the container
 */
public class HttpSessionStateProvider implements SessionStateProvider<String, Object> {

    private static final String DEFAULT_USER_IDENTIFIER_KEY = "CSC_USER_KEY";

    private String userIdentifierKey = DEFAULT_USER_IDENTIFIER_KEY;

    public void setUserIdentifierKey(String userIdentifierKey) {
        this.userIdentifierKey = userIdentifierKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSessionKey(HttpServletRequest request, HttpServletResponse response) {
        // Get the session
        HttpSession session = request.getSession(false);

        // Get the ID
        if (session != null) {
            return session.getId();
        }

        // No session
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getUserKey(HttpServletRequest request, HttpServletResponse response) {
        // Get the session
        HttpSession session = request.getSession(false);

        // Get the user identifier
        if (session != null) {
            return session.getAttribute(this.userIdentifierKey);
        }

        // No session
        return null;
    }

}
