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

    public AuthData getAuthByUsername(String username) {
        // get auth base on username
        for (AuthData auth : authData.keySet()) {
            //starts a loop that iterates over the keys (which are AuthData objects) in the authData map.
            if (authData.get(auth).equals(username)) {
                return auth;
            }
        }
        return null;
    }
    public String getUserByAuthToken(AuthData authToken) {
        for (AuthData key : authData.keySet()) {
            if (key.equals(authToken)) {
                return authData.get(key);
            }
        }
        return null;
    }

    public AuthData createAuthToken(String username) {
        AuthData newAuthData = new AuthData();
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
