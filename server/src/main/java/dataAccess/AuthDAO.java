package dataAccess;


/*
For the most part, the methods on your DAO classes will be CRUD operations that:

Create objects in the data store
Read objects from the data store
Update objects already in the data store
Delete objects from the data store
 */

import model.AuthData;

import java.util.HashMap;

public class AuthDAO implements AuthDAOInterface{
    private static final HashMap<AuthData, String> authData = new HashMap<>();

    public void clear(){
        authData.clear();
    }

    @Override
    public AuthData getAuthDataByAuthString(String authToken) {
        for (AuthData auth : authData.keySet()) {
            if (auth.getAuthToken().equals(authToken)){
                return auth;
            }
        }
        return null;
    }


    public AuthData createAuthToken(String username) {
        AuthData newAuthData = new AuthData(username);
        authData.put(newAuthData, username);
        return newAuthData;
    }

    public void deleteAuthToken(AuthData auth) {
        String removedUsername = authData.remove(auth);
        // Remove the AuthData object from the map
    }


    public boolean checkExist(AuthData auth) {
        return authData.containsKey(auth);
    }
}
