package server.websocket;

import server.Server;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
  public String authToken;
  public Session session;

  public Connection(String authToken, Session session){
    this.authToken =  authToken;
    this.session = session;
  }

  public void send(String message) throws IOException {
    session.getRemote().sendString(message);
    // sends a string message (message) over a WebSocket connection to the remote endpoint.
    // by remote, it means either the client or the server
    // in this case from server to the client
  }
}
