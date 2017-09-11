package io.danpalmer101.csc.filter.io.danpalmer101.csc.provider.local;

/**
 * Basic metadata for a session
 */
public class StoredSession {

    private String id;
    private boolean expired;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

}
