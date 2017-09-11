package io.danpalmer101.csc.provider.local;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Unit tests for LocalSessionRepositoryProvider
 */
public class LocalSessionRepositoryProviderTest {

    private static final Object USER_KEY_1 = "USER_KEY_1";
    private static final String SESSION_KEY_1 = "SESSION_KEY_1";
    private static final String SESSION_KEY_2 = "SESSION_KEY_2";
    private static final String SESSION_KEY_3 = "SESSION_KEY_3";
    private static final Object USER_KEY_2 = "USER_KEY_2";
    private static final String SESSION_KEY_4 = "SESSION_KEY_4";
    private static final String SESSION_KEY_5 = "SESSION_KEY_5";
    private static final String SESSION_KEY_6 = "SESSION_KEY_6";

    private LocalSessionRepositoryProvider sessionRepositoryProvider;

    @Mock private HttpServletRequest request;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        this.sessionRepositoryProvider = new LocalSessionRepositoryProvider();
    }

    @Test
    public void test_fullFlow() {
        // Record 6 sessions across 2 users
        this.sessionRepositoryProvider.recordSessionActivity(this.request, SESSION_KEY_1, USER_KEY_1);
        this.sessionRepositoryProvider.recordSessionActivity(this.request, SESSION_KEY_2, USER_KEY_1);
        this.sessionRepositoryProvider.recordSessionActivity(this.request, SESSION_KEY_3, USER_KEY_1);

        this.sessionRepositoryProvider.recordSessionActivity(this.request, SESSION_KEY_4, USER_KEY_2);
        this.sessionRepositoryProvider.recordSessionActivity(this.request, SESSION_KEY_5, USER_KEY_2);
        this.sessionRepositoryProvider.recordSessionActivity(this.request, SESSION_KEY_6, USER_KEY_2);

        // All session should be active
        assertFalse(this.sessionRepositoryProvider.isSessionExpired(SESSION_KEY_1), SESSION_KEY_1 + " should not be expired");
        assertFalse(this.sessionRepositoryProvider.isSessionExpired(SESSION_KEY_2), SESSION_KEY_2 + " should not be expired");
        assertFalse(this.sessionRepositoryProvider.isSessionExpired(SESSION_KEY_3), SESSION_KEY_3 + " should not be expired");
        assertFalse(this.sessionRepositoryProvider.isSessionExpired(SESSION_KEY_4), SESSION_KEY_4 + " should not be expired");
        assertFalse(this.sessionRepositoryProvider.isSessionExpired(SESSION_KEY_5), SESSION_KEY_5 + " should not be expired");
        assertFalse(this.sessionRepositoryProvider.isSessionExpired(SESSION_KEY_6), SESSION_KEY_6 + " should not be expired");

        // Expire all other sessions for user 1
        this.sessionRepositoryProvider.expireAllOtherSessions(SESSION_KEY_3, USER_KEY_1);

        // Session 1 and 2 should be expired, current session 3 should not be expired, user 2 sessions should not be expired
        assertTrue(this.sessionRepositoryProvider.isSessionExpired(SESSION_KEY_1), SESSION_KEY_1 + " should not be expired");
        assertTrue(this.sessionRepositoryProvider.isSessionExpired(SESSION_KEY_2), SESSION_KEY_2 + " should not be expired");
        assertFalse(this.sessionRepositoryProvider.isSessionExpired(SESSION_KEY_3), SESSION_KEY_3 + " should not be expired");
        assertFalse(this.sessionRepositoryProvider.isSessionExpired(SESSION_KEY_4), SESSION_KEY_4 + " should not be expired");
        assertFalse(this.sessionRepositoryProvider.isSessionExpired(SESSION_KEY_5), SESSION_KEY_5 + " should not be expired");
        assertFalse(this.sessionRepositoryProvider.isSessionExpired(SESSION_KEY_6), SESSION_KEY_6 + " should not be expired");
    }

}
