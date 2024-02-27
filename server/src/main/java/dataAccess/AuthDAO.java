package dataAccess;
import java.util.*;

/*
For the most part, the methods on your DAO classes will be CRUD operations that:

Create objects in the data store
Read objects from the data store
Update objects already in the data store
Delete objects from the data store
 */

import model.AuthData;

import java.util.HashMap;

public class AuthDAO implements DataAccessInterface{
    private static final HashMap<AuthData, String> authData = new HashMap<>();


    public AuthData getData(String username) {
        for (AuthData auth : authData.keySet()) {
            //starts a loop that iterates over the keys (which are AuthData objects) in the authData map.
            if (authData.get(auth).equals(username)) {
                return auth;
            }
        }
        return null;
    }

    public Object creatData(String username) {
        AuthData newAuthData = new AuthData();
        authData.put(newAuthData, username);
        return newAuthData;
    }

    @Override
    public Object setData() {
        return null;
    }


    public Object deleteData(AuthData auth) {
        String removedUsername = authData.remove(auth); // Remove the AuthData object from the map
        return removedUsername; // Return the username associated with the deleted AuthData object
    }

    @Override
    public Object checkExist(AuthData auth) {
        return authData.containsKey(auth);
    }
}
