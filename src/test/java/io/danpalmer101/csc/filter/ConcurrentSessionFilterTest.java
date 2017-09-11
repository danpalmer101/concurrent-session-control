package io.danpalmer101.csc.filter;

import io.danpalmer101.csc.provider.SessionRepositoryProvider;
import io.danpalmer101.csc.provider.SessionStateProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;

/**
 * Unit tests for ConcurrentSessionFilter
 */
public class ConcurrentSessionFilterTest {

    private static final String SESSION_KEY = "SESSION_KEY";
    private static final String USER_KEY = "USER_KEY";

    private ConcurrentSessionFilter<String, String> filter;

    @Mock private SessionRepositoryProvider<String, String> sessionRepositoryProvider;
    @Mock private SessionStateProvider<String, String> sessionStateProvider;

    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private FilterChain filterChain;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        this.filter = new ConcurrentSessionFilter<>();
        this.filter.setSessionRepository(this.sessionRepositoryProvider);
        this.filter.setSessionState(this.sessionStateProvider);
    }

    @Test
    public void test_doFilter_expired() throws Exception {
        when(this.sessionStateProvider.getSessionKey(this.request, this.response)).thenReturn(SESSION_KEY);
        when(this.sessionStateProvider.getUserKey(this.request, this.response)).thenReturn(USER_KEY);

        when(this.sessionRepositoryProvider.isSessionExpired(SESSION_KEY)).thenReturn(true);

        this.filter.doFilter(this.request, this.response, this.filterChain);

        verifyZeroInteractions(this.filterChain);
    }

    @Test
    public void test_doFilter_currentExpired() throws Exception {
        when(this.sessionStateProvider.getSessionKey(this.request, this.response)).thenReturn(SESSION_KEY);
        when(this.sessionStateProvider.getUserKey(this.request, this.response)).thenReturn(USER_KEY);

        when(this.sessionRepositoryProvider.isSessionExpired(SESSION_KEY)).thenReturn(false);
        when(this.sessionRepositoryProvider.expireAllOtherSessions(SESSION_KEY, USER_KEY)).thenReturn(true);

        this.filter.doFilter(this.request, this.response, this.filterChain);

        verifyZeroInteractions(this.filterChain);
    }

    @Test
    public void test_doFilter_active() throws Exception {
        when(this.sessionStateProvider.getSessionKey(this.request, this.response)).thenReturn(SESSION_KEY);
        when(this.sessionStateProvider.getUserKey(this.request, this.response)).thenReturn(USER_KEY);

        when(this.sessionRepositoryProvider.isSessionExpired(SESSION_KEY)).thenReturn(false);
        when(this.sessionRepositoryProvider.expireAllOtherSessions(SESSION_KEY, USER_KEY)).thenReturn(false);

        this.filter.doFilter(this.request, this.response, this.filterChain);

        verify(this.sessionRepositoryProvider).recordSessionActivity(this.request, SESSION_KEY, USER_KEY);
        verify(this.filterChain).doFilter(this.request, this.response);
    }

}
