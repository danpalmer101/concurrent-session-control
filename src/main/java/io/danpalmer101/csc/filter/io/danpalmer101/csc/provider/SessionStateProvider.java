package io.danpalmer101.csc.filter.io.danpalmer101.csc.provider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interface for obtaining a session state from the current request context
 *
 * &lt;S&lt; The type of session key
 * &lt;U&lt; The type of user key
 */
public interface SessionStateProvider<S, U> {

    /**
     * Get the unique identifier for the current session
     * @param request the HttpServletRequest
     * @param response the HttpServletResponse
     * @return the sessionKey
     */
    S getSessionKey(HttpServletRequest request, HttpServletResponse response);

    /**
     * Get the unique identifier for the current user
     * @param request the HttpServletRequest
     * @param response the HttpServletResponse
     * @return the userKey
     */
    U getUserKey(HttpServletRequest request, HttpServletResponse response);

}
