package server;

import spark.Request;
import spark.Response;
import spark.Spark;

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
        //register
        Spark.post("/user", this::register);




        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
    }

    public Object register(Request req, Response res) {
        return null;
    }
}
