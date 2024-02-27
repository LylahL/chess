package model;

import java.util.Objects;
import java.util.UUID;

public record AuthData(String authToken) {
    public AuthData() {
        this(UUID.randomUUID().toString());
    }

    public AuthData(String authToken) {
        this.authToken = authToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthData authData = (AuthData) o;
        return Objects.equals(authToken, authData.authToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authToken);
    }

    @Override
    public String toString() {
        return "{ authToken =" + authToken + '}';
    }
}

