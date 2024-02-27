package dataAccess;


import model.AuthData;

public interface DataAccessInterface<T> {
    T getData(String username);
    T creatData(String username);
    T setData();
    T deleteData(AuthData auth);
    T checkExist(AuthData auth);

}
