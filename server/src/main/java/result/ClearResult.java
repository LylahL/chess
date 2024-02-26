package result;

public class ClearResult {


  private final String message;
  private final boolean success;

  public ClearResult(String message, boolean success) {
    this.message = message;
    this.success = success;
  }

  public String getMessage() {return message;}
  public boolean getSuccess() {return success;}


}
