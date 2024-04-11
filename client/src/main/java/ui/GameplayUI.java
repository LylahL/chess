package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import exception.ResponseException;
import model.AuthData;
import server.ServerFacade;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static ui.EscapeSequences.*;
// TODO: add more cmds like make move

public class GameplayUI {
  private AuthData auth;
  private String username;
  private final ServerFacade serverFacade = new ServerFacade("http://localhost:8282");
  public static ChessGame chessGame;
  private static final String textColor = SET_TEXT_COLOR_WHITE;
  private static final String indexColor = SET_TEXT_COLOR_BLACK;
  private static final String whitePieceColor = SET_TEXT_COLOR_BLUE;
  private static final String blackPieceColor = SET_TEXT_COLOR_YELLOW;
  private static final String boardSpaceWhiteColor = SET_BG_COLOR_LIGHT_GREY;
  private static final String boardSpaceBlackColor = SET_BG_COLOR_DARK_GREY;
  private static final String BackgroundColor = SET_BG_COLOR_DARK_GREEN;
  private static final String DefaultBackgroundColor = SET_BG_COLOR_BLACK;
  private static final String space = EMPTY;



  private boolean isrunning = true;

  GameplayUI (String auth, String username){
    AuthData authData = new AuthData(auth, username);
    this.auth =  authData;
    this.username = username;
  }

  public void run() throws ResponseException, IOException, URISyntaxException {
    while(isrunning){
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      ArrayList<String> cmds = new ArrayList<>(List.of(reader.readLine().split(" ")));
      parseCommads(cmds);
    }
  }

  private void parseCommads(ArrayList<String> cmds) throws ResponseException, IOException, URISyntaxException {
    String cmd = cmds.getFirst().toLowerCase();
    String[] params =cmds.subList(1, cmds.size()).toArray(new String[0]);
    switch (cmd) {
     case "quit" -> quit();
     case "highlight" -> highLight(chessGame, params);
     case "help" -> help();
     case "drawBoard" -> drawBoard(chessGame.getBoard());
     case "makeMove" -> makeMove(params);
     case "leave" -> leave();
     case "resign" -> resign();
    }
  }

  private void resign() {
  }

  private void leave() {
  }

  private void makeMove(String[] params) {

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
                - highlight - hight lights legal moves
                """);
  }

  private void drawBoard(ChessBoard board) {
    drawBoardOnce(board, ChessGame.TeamColor.WHITE);
    System.out.println();
    drawBoardOnce(board, ChessGame.TeamColor.BLACK);
  }

  private void drawBoardOnce(ChessBoard board, ChessGame.TeamColor teamColor) {
    PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    printIndex(out, teamColor);
    if(teamColor == ChessGame.TeamColor.WHITE){
      for(int i=1; i <= 8; i++){
        out.print(BackgroundColor);
        out.print(indexColor);
        out.print(space);
        out.print(i);
        out.print(space);
        for(int j=1; j <= 8; j++){
          ChessPiece currentPiece = board.getPiece(new ChessPosition(i, j));
          if (i % 2 == 1){
            if(j % 2 == 1){
              out.print(boardSpaceWhiteColor);
              printpiece(out, currentPiece, teamColor);
            }else {
              out.print(boardSpaceBlackColor);
              printpiece(out, currentPiece, teamColor);
            }
          }else {
            if(j % 2 == 1){
              out.print(boardSpaceBlackColor);
              printpiece(out, currentPiece, teamColor);
            }else {
              out.print(boardSpaceWhiteColor);
              printpiece(out, currentPiece, teamColor);
            }
          }
        }
        out.print(" ");
        out.println();
//        out.print(BackgroundColor);
      }
      out.print(BackgroundColor);
//      out.print(space);

    }else{
      for(int i=8; i >=1; i--){
        out.print(BackgroundColor);
        out.print(indexColor);
        out.print(space);
        out.print(i);
        out.print(space);
        for(int j=8; j >= 1; j--){
          ChessPiece currentPiece = board.getPiece(new ChessPosition(i, j));
          if (i % 2 == 1){
            if(j % 2 == 1){
              out.print(boardSpaceWhiteColor);
              printpiece(out, currentPiece, teamColor);
            }else {
              out.print(boardSpaceBlackColor);
              printpiece(out, currentPiece, teamColor);
            }
          }else {
            if(j % 2 == 1){
              out.print(boardSpaceBlackColor);
              printpiece(out, currentPiece, teamColor);
            }else {
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

  private void printpiece(PrintStream out, ChessPiece currentPiece, ChessGame.TeamColor teamColor) {
    if(currentPiece != null){
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
        case KNIGHT ->out.print(BLACK_KNIGHT);
        case PAWN -> out.print(BLACK_PAWN);
        default -> out.print(space);
    }
    }else {
      out.print(space);
    }
  }

  private void printIndex(PrintStream out, ChessGame.TeamColor teamColor) {
    out.print(BackgroundColor);
    out.print(textColor);
    String [] index;
    if(teamColor == ChessGame.TeamColor.WHITE){
      index = new String[]{"a", "b", "c", "d", "e", "f", "g", "h"};
    } else {
      index =  new String [] {"h", "g", "f", "e", "d", "c", "b", "a"};
    }
    out.print(space);
    out.print(space);
    out.print("\u2002");
    for (String letter: index){
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
    this.isrunning = false;
  }


}
