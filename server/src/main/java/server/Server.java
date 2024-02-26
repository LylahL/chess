package server;

import handler.ClearHandler;
import spark.Spark;

import java.nio.file.Paths;

/*The Server receives network HTTP requests and sends them to the correct handler for processing.
  The server should also handle all unhandled exceptions that your application generates and return the appropriate HTTP status code.
*/

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        var webDir = Paths.get(Server.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "web");
        Spark.externalStaticFileLocation(webDir.toString());

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db",(request, response) -> new ClearHandler().clearDataBase(request, response));

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
    }
}
