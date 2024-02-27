package dataAccess;

public class AuthDAO {
  private DataAccessInterface database;

  public AuthDAO(Database database){
    this.database = database;
  }

  public void clear() throws DataAccessException{
    this.database.clearAuth();
  }

}
