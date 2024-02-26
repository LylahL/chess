package handler;

import service.ClearApplication;
import spark.Spark;

import static spark.Spark.halt;

/*
The server handler classes serve as a translator between HTTP and Java.
Your handlers will convert an HTTP request into Java usable objects & data.
The handler then calls the appropriate service.
When the service responds it converts the response object back to JSON and sends the HTTP response.
 */
public class ClearHandler {
  public void clearDataBase(ClearApplication clearApplication){
    Spark.delete("/db", (request, response) -> {
      // response handling
      boolean success = clearApplication.clearDataBase().getSuccess();
      // if failed
      if(!success){
        halt(500, "ERROR: failed to clear database");
      }
      // else succeed
      else{
        response.status(200);
      }
      return response.body();
    });

  }
}
