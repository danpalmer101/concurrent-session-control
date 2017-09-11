package io.danpalmer101.csc.filter;

import io.danpalmer101.csc.filter.io.danpalmer101.csc.provider.SessionRepositoryProvider;
import io.danpalmer101.csc.filter.io.danpalmer101.csc.provider.SessionStateProvider;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter for restricting a user's concurrent sessions
 *
 * &lt;S&lt; The type of session key
 * &lt;U&lt; The type of user key
 */
public class ConcurrentSessionFilter<S, U>  implements Filter {

    /** Filter Functional Providers **/

    private SessionRepositoryProvider<S, U> sessionRepository;
    private SessionStateProvider<S, U> sessionState;

    public void setSessionRepository(SessionRepositoryProvider<S, U> sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public void setSessionState(SessionStateProvider<S, U> sessionState) {
        this.sessionState = sessionState;
    }

    /** Filter Behaviour **/

    /**
     * {@inheritDoc}
     */
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;

        S sessionKey = this.sessionState.getSessionKey(request, response);
        U userKey = this.sessionState.getUserKey(request, response);

        if (this.sessionRepository.isSessionExpired(sessionKey)) {
            // If this session has been expired by another session, then handle it
            handleExpiredSession(request, response, filterChain);
        } else {
            // This session is not expired, expire concurrent sessions
            boolean currentExpired = this.sessionRepository.expireAllOtherSessions(sessionKey, userKey);

            if (currentExpired) {
                // This session was just expired, so handle it
                handleExpiredSession(request, response, filterChain);
            } else {
                // This session is still not expired, record the latest activity on this session
                this.sessionRepository.recordSessionActivity(request, sessionKey, userKey);

                // Handle the session not being expire (e.g. just continue to application logic)
                handleActiveSession(request, response, filterChain);
            }
        }
    }

    /**
     * Handle the scenario where this session is expired.
     * For example, re-direct the user to the login page
     * @param request the HttpServletRequest
     * @param response the HttpServletRequest
     * @param filterChain the FilterChain
     * @throws IOException handled by container
     * @throws ServletException handled by container
     */
    protected void handleExpiredSession(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        // TODO: re-direct
    }

    /**
     * Handle the scenario where this session is not expired.
     * For example, continue down filter chain
     * @param request the HttpServletRequest
     * @param response the HttpServletRequest
     * @param filterChain the FilterChain
     * @throws IOException handled by container
     * @throws ServletException handled by container
     */
    protected void handleActiveSession(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        filterChain.doFilter(request, response);
    }

    /** Filter Lifecycle **/

    /**
     * No default implementation
     * @param filterConfig the FilterConfig
     * @throws ServletException handled by the container
     */
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    /**
     * No default implementation
     */
    public void destroy() {

    }

}
