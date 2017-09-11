package io.danpalmer101.csc.provider.local;

import io.danpalmer101.csc.filter.ConcurrentSessionFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

/**
 * Unit tests for HttpSessionStateProvider
 */
public class HttpSessionStateProviderTest {

    private static final String SESSION_KEY = "SESSION_KEY";
    private static final Object USER_KEY = "USER_KEY";

    private static final String USER_IDENTIFIER_KEY = "USER";

    private HttpSessionStateProvider sessionStateProvider;

    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private HttpSession session;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        this.sessionStateProvider = new HttpSessionStateProvider();
        this.sessionStateProvider.setUserIdentifierKey(USER_IDENTIFIER_KEY);
    }

    @Test
    public void test_getSessionKey_sessionExists() {
        when(this.request.getSession(false)).thenReturn(this.session);
        when(this.session.getId()).thenReturn(SESSION_KEY);

        String sessionKey = this.sessionStateProvider.getSessionKey(this.request, this.response);

        assertEquals(SESSION_KEY, sessionKey, "Incorrect sessionKey");
    }

    @Test
    public void test_getSessionKey_sessionDoesNotExist() {
        when(this.request.getSession(false)).thenReturn(null);

        String sessionKey = this.sessionStateProvider.getSessionKey(this.request, this.response);

        assertNull(sessionKey, "sessionKey should be null");
    }

    @Test
    public void test_getUserKey_sessionExists() {
        when(this.request.getSession(false)).thenReturn(this.session);
        when(this.session.getAttribute(USER_IDENTIFIER_KEY)).thenReturn(USER_KEY);

        Object userKey = this.sessionStateProvider.getUserKey(this.request, this.response);

        assertEquals(USER_KEY, userKey, "Incorrect userKey");
    }

    @Test
    public void test_getUserKey_userNotKnown() {
        when(this.request.getSession(false)).thenReturn(this.session);
        when(this.session.getAttribute(USER_IDENTIFIER_KEY)).thenReturn(null);

        Object userKey = this.sessionStateProvider.getUserKey(this.request, this.response);

        assertNull(userKey, "userKey should be null");
    }

    @Test
    public void test_getUserKey_sessionDoesNotExist() {
        when(this.request.getSession(false)).thenReturn(null);

        Object userKey = this.sessionStateProvider.getUserKey(this.request, this.response);

        assertNull(userKey, "userKey should be null");
    }

}
