package io.danpalmer101.csc.filter.io.danpalmer101.csc.provider.local;

import io.danpalmer101.csc.filter.io.danpalmer101.csc.provider.SessionRepositoryProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Default SessionRepositoryProvider implementation that restricts user to a single session
 * and stores all session information locally in memory.
 */
public class LocalSessionRepositoryProvider implements SessionRepositoryProvider<String, Object> {

    private Map<String, StoredSession> sessionMap = new HashMap<>();
    private Map<Object, Set<String>> userSessionMap = new HashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSessionExpired(String sessionKey) {
        if (sessionKey == null) {
            // No session, so not expired
            return false;
        }

        StoredSession session = this.sessionMap.get(sessionKey);

        return session != null && session.isExpired();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void recordSessionActivity(HttpServletRequest request, String sessionKey, Object userKey) {
        if (sessionKey == null) {
            // No session, so do not record
            return;
        }

        StoredSession session = new StoredSession();
        session.setId(sessionKey);
        session.setExpired(false);

        // Add to the session map, may override an existing value
        this.sessionMap.put(sessionKey, session);

        // Add to the user's session map if the user is known
        if (userKey != null) {
            Set<String> userSessionKeys = this.userSessionMap.getOrDefault(userKey, new HashSet<>());
            userSessionKeys.add(sessionKey);
            this.userSessionMap.put(userKey, userSessionKeys);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean expireAllOtherSessions(String sessionKey, Object userKey) {
        if (userKey == null) {
            // Current user is not known, so do nothing
            return false;
        }

        // Get all sessions for this user
        Set<String> userSessionKeys = this.userSessionMap.get(userKey);

        if (userSessionKeys != null) {
            userSessionKeys.stream()
                    .filter(s -> sessionKey == null || !s.equals(sessionKey)) // Filter out this session
                    .forEach(this::expireSession);                            // Expire all other sessions
        }

        // Did not expire the current session
        return false;
    }

    private void expireSession(String sessionKey) {
        if (sessionKey == null) {
            // No session, do nothing
            return;
        }

        StoredSession session = this.sessionMap.get(sessionKey);

        if (session != null) {
            session.setExpired(true);
        }
    }

}
