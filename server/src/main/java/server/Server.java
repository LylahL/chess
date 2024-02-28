package server;

import spark.Spark;
import service.ClearApplication;

/*The Server receives network HTTP requests and sends them to the correct handler for processing.
  The server should also handle all unhandled exceptions that your application generates and return the appropriate HTTP status code.
*/

public class Server {

    public static void main(String[] args){
        Server server = new Server();
        server.run(8080);
    }
    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        ClearApplication clearApplication = new ClearApplication();
        Spark.delete("/db", (request, response) -> {
            // response handling
            boolean success = clearApplication.clearDataBase().getSuccess();
            // if failed
            if(!success){
                // halt(500, "ERROR: failed to clear database");
                response.status(500)
            }
            // else succeed
            else{
                response.status(200);
            }
            return response.body();
        });




        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
    }
}
