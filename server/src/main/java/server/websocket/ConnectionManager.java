package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.Notification;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
  public final ConcurrentHashMap<Integer, ArrayList<Connection>> connections = new ConcurrentHashMap<>();

  public void add(int gameID, String auth, Session session){
    // add one game connection to the hashMap
    Connection connection = new Connection(auth, session);
    if (connections.get(gameID) == null){
      // if key not initialized
      ArrayList<Connection> connectionArrayList = new ArrayList<>();
      connectionArrayList.add(connection)
      connections.put(gameID, connectionArrayList);
    } else {
      ArrayList<Connection> connectionArrayList = connections.get(gameID);
      connectionArrayList.add(connection);
      connections.put(gameID, connectionArrayList);
    }
  }

  public void remove(int gameID, String authToken){
    connections.remove(gameID);
  }

  public void broadcast(String auth, Notification notification, int gameID) throws IOException {
    for(var connectionList : connections.values()){
      for (var c : connectionList){
        if(c.session.isOpen()){
          if (!c.authToken.equals(auth)) {
            c.send(notification.getMessage());9-
          }
        }
      }
    }
  }
}
