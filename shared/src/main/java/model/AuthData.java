package model;

import java.util.Objects;
import java.util.UUID;

public class AuthData {
    public String getAuthToken() {
        return authToken;
    }

    String authToken;

    public String getUsername() {
        return username;
    }

    String username;

    public AuthData(String username) {
        // create an authData object by username, authToken is not specified
        this(UUID.randomUUID().toString(), username);

    }

    public AuthData(String authToken, String username) {
        // authToken is specified
        this.authToken = authToken;
        this.username = username;
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

