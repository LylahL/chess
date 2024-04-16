package server.websocket;

import com.google.gson.Gson;
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
      connectionArrayList.add(connection);
      connections.put(gameID, connectionArrayList);
    } else {
      ArrayList<Connection> connectionArrayList = connections.get(gameID);
      connectionArrayList.add(connection);
      connections.put(gameID, connectionArrayList);
    }
  }

  public void remove(int gameID){
    connections.remove(gameID);
  }

  public void sendMessage(String auth, Object object) throws IOException {
    // sendMessage to one specific player in one game
    for(var connectionList : connections.values()){
      for(var c: connectionList){
        if (c.session.isOpen() && c.authToken.equals(auth)){
          String message = new Gson().toJson(object);
          c.send(message);
      }
      }
    }
  }

  public void sendMessageToOthers(String auth, Object object, int gameID) throws IOException {
    // sendMessage to everyone besides the root client
    for(var c : connections.get(gameID)){
      // find that game
      if (c.session.isOpen() && !c.authToken.equals(auth)){
        String message = new Gson().toJson(object);
        c.send(message);
      }
    }
  }

  public void broadcast(String auth, Notification notification, int gameID) throws IOException {
    // send message to everyone within one game
    for(var c : connections.get(gameID)){
        if(c.session.isOpen()){
          String message = new Gson().toJson(notification);
          c.send(message);
      }
    }
  }

}
