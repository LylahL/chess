package server;

import spark.Spark;

/*The Server receives network HTTP requests and sends them to the correct handler for processing.
  The server should also handle all unhandled exceptions that your application generates and return the appropriate HTTP status code.
*/

public class Server {
    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.


        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
    }
}
