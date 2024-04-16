package ui;

import chess.*;
import exception.ResponseException;
import model.AuthData;
import server.ServerMessageHandler;
import server.ServerFacade;
import server.WebSocketFacade;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static java.lang.Integer.parseInt;
import static ui.EscapeSequences.*;
// TODO: add more cmds like make move

public class GameplayUI {

  private State state;
  private ServerMessageHandler serverMessageHandler;
  private WebSocketFacade webSocketFacade=new WebSocketFacade("http://localhost:8282");
  private AuthData auth;
  private int gameID;
  private String username;
  private final ServerFacade serverFacade=new ServerFacade("http://localhost:8282");
  public static ChessGame chessGame;
  private static final String textColor=SET_TEXT_COLOR_WHITE;
  private static final String indexColor=SET_TEXT_COLOR_BLACK;
  private static final String whitePieceColor=SET_TEXT_COLOR_BLUE;
  private static final String blackPieceColor=SET_TEXT_COLOR_YELLOW;
  private static final String boardSpaceWhiteColor=SET_BG_COLOR_LIGHT_GREY;
  private static final String boardSpaceBlackColor=SET_BG_COLOR_DARK_GREY;
  private static final String BackgroundColor=SET_BG_COLOR_DARK_GREEN;
  private static final String DefaultBackgroundColor=SET_BG_COLOR_BLACK;
  private static final String space=EMPTY;

  private boolean isrunning=true;

  GameplayUI(String auth, String username, int gameID, State state) throws ResponseException {
    AuthData authData=new AuthData(auth, username);
    this.auth=authData;
    this.username=username;
    this.gameID=gameID;
    this.state=state;
  }

  public void run() throws ResponseException, IOException, URISyntaxException {
    System.out.println("This is the GamePlay page, type help to check available commands");
    while (isrunning) {
      System.out.printf("[%s] >>>", state.toString());
      BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
      ArrayList<String> cmds=new ArrayList<>(List.of(reader.readLine().split(" ")));
      parseCommads(cmds);
    }
  }

  private void parseCommads(ArrayList<String> cmds) throws ResponseException, IOException, URISyntaxException {
    String cmd=cmds.getFirst().toLowerCase();
    String[] params=cmds.subList(1, cmds.size()).toArray(new String[0]);
    VaidInputCheck(cmd, params);
    switch (cmd) {
      case "quit" -> quit();
      case "highlight" -> highLight(chessGame, params);
      case "help" -> help();
      case "drawboard" -> redraw();
      case "makemove" -> makeMove(params);
      case "leave" -> leave();
      case "resign" -> resign();
    }
  }

  private void redraw() {
    return;
  }

  private void VaidInputCheck(String cmd, String[] params) {
    ArrayList<String> validInputList=new ArrayList<>(Arrays.asList("quit", "highlight", "help", "drawboard", "makemove", "leave", "resign"));
    // not a valid input
    if (!validInputList.contains(cmd)) {
      System.out.println("Please input a valid command");
      help();
    } else if ((Objects.equals(cmd, "highlight") && params.length != 1) || (Objects.equals(cmd, "makemove") && params.length != 2)) {
      System.out.println("Please input correct amount of params");
      help();
    }
  }

  private void resign() {
    webSocketFacade.resign(auth, gameID);
  }

  private void leave() {
    webSocketFacade.leave(auth, gameID);
  }

  private void makeMove(String[] params) {
    if (params.length == 2) {
      String start=params[0];
      String end=params[1];
      if (!isValidMoveInput(start) || !isValidMoveInput(end)) {
        System.out.println("Enter a valid position input");
        return;
      }
      ChessPosition startPosition = convertPosition(start);
      ChessPosition endPosition = convertPosition(end);
      ChessMove chessMove = new ChessMove(startPosition, endPosition, null);
      webSocketFacade.makeMove(auth, gameID, chessMove);
    }
  }

  private ChessPosition convertPosition(String position) {
    int column=0;
    int row;
    String[] coordinates=position.split("");
    row=parseInt(coordinates[1]);
    switch (coordinates[0].toLowerCase()) {
      case "a" -> column=1;
      case "b" -> column=2;
      case "c" -> column=3;
      case "d" -> column=4;
      case "e" -> column=5;
      case "f" -> column=6;
      case "g" -> column=7;
      case "h" -> column=8;
    }
    return new ChessPosition(column, row);
  }

  private boolean isValidMoveInput(String position) {
    if (position.length() != 2) {
      System.out.println("Invalid input, please only input two coordinates to represent one position");
      return false;
    }

    HashSet<Character> validInputSet=new HashSet<>();
    validInputSet.addAll(Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', '1', '2', '3', '4', '5', '6', '7', '8'));

    char col=Character.toLowerCase(position.charAt(0)); // Convert to lowercase for case-insensitive comparison
    char row=position.charAt(1);

    return validInputSet.contains(col) && validInputSet.contains(row);
  }

  private void highLight(ChessGame chessGame, String[] params) {

  }

  private void help() {
    System.out.print("""
            - quit - quit game
            - drawBoard - redraws the board
            - leave - removes the user from the game
            - resign - the user forfeits the game and the game is over. Does not cause the user to leave the game
            - makeMove <START POSITION> <END POSITION> - make a move
            - highlight -<POSITION> highlights legal moves
            """);
  }

  // calldrawBoard()
  public static void drawBoard(ChessGame game) {
    // call websocketFacade
    ChessBoard board=game.getBoard();
    drawBoardOnce(board, ChessGame.TeamColor.WHITE);
    System.out.println();
    drawBoardOnce(board, ChessGame.TeamColor.BLACK);
  }

  public static void drawBoardOnce(ChessBoard board, ChessGame.TeamColor teamColor) {
    PrintStream out=new PrintStream(System.out, true, StandardCharsets.UTF_8);
    System.out.println();
    printIndex(out, teamColor);
    if (teamColor == ChessGame.TeamColor.WHITE) {
      for (int i=1; i <= 8; i++) {
        out.print(BackgroundColor);
        out.print(indexColor);
        out.print(space);
        out.print(i);
        out.print(space);
        for (int j=1; j <= 8; j++) {
          ChessPiece currentPiece=board.getPiece(new ChessPosition(i, j));
          if (i % 2 == 1) {
            if (j % 2 == 1) {
              out.print(boardSpaceWhiteColor);
              printpiece(out, currentPiece, teamColor);
            } else {
              out.print(boardSpaceBlackColor);
              printpiece(out, currentPiece, teamColor);
            }
          } else {
            if (j % 2 == 1) {
              out.print(boardSpaceBlackColor);
              printpiece(out, currentPiece, teamColor);
            } else {
              out.print(boardSpaceWhiteColor);
              printpiece(out, currentPiece, teamColor);
            }
          }
        }
        out.print(" ");
        out.println();
//        out.print(BackgroundColor);
      }
//      out.print(BackgroundColor);
//      out.print(space);

    } else {
      for (int i=8; i >= 1; i--) {
        out.print(BackgroundColor);
        out.print(indexColor);
        out.print(space);
        out.print(i);
        out.print(space);
        for (int j=8; j >= 1; j--) {
          ChessPiece currentPiece=board.getPiece(new ChessPosition(i, j));
          if (i % 2 == 1) {
            if (j % 2 == 1) {
              out.print(boardSpaceWhiteColor);
              printpiece(out, currentPiece, teamColor);
            } else {
              out.print(boardSpaceBlackColor);
              printpiece(out, currentPiece, teamColor);
            }
          } else {
            if (j % 2 == 1) {
              out.print(boardSpaceBlackColor);
              printpiece(out, currentPiece, teamColor);
            } else {
              out.print(boardSpaceWhiteColor);
              printpiece(out, currentPiece, teamColor);
            }
          }
        }
        out.println();
//        out.print(BackgroundColor);
      }
//      out.print(BackgroundColor);
      out.print(space);
    }

  }

  private static void printpiece(PrintStream out, ChessPiece currentPiece, ChessGame.TeamColor teamColor) {
    if (currentPiece != null) {
      if (currentPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
        out.print(whitePieceColor);
      } else {
        out.print(blackPieceColor);
      }
      switch (currentPiece.getPieceType()) {
        case KING -> out.print(BLACK_KING);
        case QUEEN -> out.print(BLACK_QUEEN);
        case BISHOP -> out.print(BLACK_BISHOP);
        case ROOK -> out.print(BLACK_ROOK);
        case KNIGHT -> out.print(BLACK_KNIGHT);
        case PAWN -> out.print(BLACK_PAWN);
        default -> out.print(space);
      }
    } else {
      out.print(space);
    }
  }

  private static void printIndex(PrintStream out, ChessGame.TeamColor teamColor) {
    out.print(BackgroundColor);
    out.print(textColor);
    String[] index;
    if (teamColor == ChessGame.TeamColor.WHITE) {
      index=new String[]{"a", "b", "c", "d", "e", "f", "g", "h"};
    } else {
      index=new String[]{"h", "g", "f", "e", "d", "c", "b", "a"};
    }
    out.print(space);
    out.print(space);
    out.print("\u2002");
    for (String letter : index) {
      out.print("\u2003");
      out.print(letter);
      out.print("\u2002");
    }
    out.print(space);
    out.print(textColor);
    out.print(BackgroundColor);
    out.println();
  }

  private void quit() {
    this.isrunning=false;
  }



}
