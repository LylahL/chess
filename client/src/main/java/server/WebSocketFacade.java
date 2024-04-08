package server;


import com.google.gson.Gson;
import exception.ResponseException;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint{

  Session session;
  NotificationHandler notificationHandler;
  @Override
  public void onOpen(Session session, EndpointConfig endpointConfig) {

  }
  // make connection to the /connect endpoint in my server
  public WebSocketFacade(String url, NotificationHandler notificationHandler)throws ResponseException {
    try {
      url = url.replace("http", "ws");
      URI socketURI = new URI(url + "/connect");
      this.notificationHandler = notificationHandler;

      WebSocketContainer container = ContainerProvider.getWebSocketContainer();
      this.session = container.connectToServer(this, socketURI);
      //set message handler
      this.session.addMessageHandler(new MessageHandler.Whole<String>() {
        @Override
        public void onMessage(String message) {
          Notification notification = new Gson().fromJson(message, Notification.class);
          notificationHandler.notify(notification);
        }
      });
    } catch (DeploymentException | IOException | URISyntaxException ex) {
      throw new ResponseException(500, ex.getMessage());
    }
  }
  }
}