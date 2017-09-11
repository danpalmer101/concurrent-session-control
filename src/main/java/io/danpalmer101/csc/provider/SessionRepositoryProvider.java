package io.danpalmer101.csc.provider;

import javax.servlet.http.HttpServletRequest;

/**
 * Interface for providing a session repository responsible for persisting session data
 *
 * &lt;S&lt; The type of session key
 * &lt;U&lt; The type of user key
 */
public interface SessionRepositoryProvider<S, U> {

    /**
     * Check whether the session identified by the sessionKey is expired
     *
     * Notes:
     *  * sessionKey could be null if no current session is found
     *
     * @param sessionKey a unique identifier for the session
     * @return true if the session is expired
     */
    boolean isSessionExpired(S sessionKey);

    /**
     * Expire sessions for a user.
     *
     * Notes:
     *  * sessionKey could be null if no current session is found
     *  * userKey could be null if the current user is not known
     *
     * Examples:
     *  * All other sessions
     *  * This session
     *  * All but the most recently used N sessions
     *
     * @param sessionKey the unique identifier for the current session
     * @param userKey the unique identifier for the current user
     * @return whether the current session is expired
     */
    boolean expireAllOtherSessions(final S sessionKey, U userKey);

    /*
    getAllActiveSessionKeys(userKey)
                .stream()
                .filter(s -> !s.equals(sessionKey))
                .forEach(s -> expireOtherSession(s));
     */

    /**
     * Record activity for this session
     *
     * Notes:
     *  * sessionKey could be null if no current session is found
     *  * userKey could be null if the current user is not known
     *
     * @param request the HttpServletRequest
     * @param sessionKey the unique identifier for the current session
     * @param userKey the unique identifier for the current user
     */
    void recordSessionActivity(HttpServletRequest request, S sessionKey, U userKey);

}
