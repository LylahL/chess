package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */

// why do I need to overwrite the functions.
public class ChessBoard {
  // why can't it be ChessPosition[8][8];
    private ChessPiece[][] squares = new ChessPiece[8][8];
    public ChessBoard() {
      resetBoard();
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    /**cb
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     * put pieces in the right position
     * call chessPiece and create all the pieces
     * I feel so dumb is there a better way to do this?
     */
    public void resetBoard() {
      // change to normal numbers
      ChessPiece whiteKing =new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
      ChessPosition positionWhiteKing = new ChessPosition(1, 5);
      addPiece(positionWhiteKing, whiteKing);

      ChessPiece whiteQueen = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
      ChessPosition positionWhiteQueen = new ChessPosition(1, 4);
      addPiece(positionWhiteQueen, whiteQueen);

      ChessPiece whiteBishop = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
      ChessPosition positionWhiteBishop1 = new ChessPosition(1, 3);
      addPiece(positionWhiteBishop1, whiteBishop);
      ChessPosition positionWhiteBishop2 = new ChessPosition(1, 6);
      addPiece(positionWhiteBishop2, whiteBishop);

      ChessPiece whiteKnight = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
      ChessPosition positionWhiteKnight1 = new ChessPosition(1, 2);
      addPiece(positionWhiteKnight1, whiteKnight);
      ChessPosition positionWhiteKnight2 = new ChessPosition(1, 7);
      addPiece(positionWhiteKnight2, whiteKnight);

      ChessPiece whiteRook = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
      ChessPosition positionWhiteRook1 = new ChessPosition(1, 1);
      addPiece(positionWhiteRook1, whiteRook);
      ChessPosition positionWhiteRook2 = new ChessPosition(1, 8);
      addPiece(positionWhiteRook2, whiteRook);

      for (int i = 1; i <= 8; i++ ) {
      // String name = String.format("whitePawn%d", i + 1);
      // ChessPiece name = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece whitePawn = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPosition position = new ChessPosition(2, i);
        addPiece(position, whitePawn);
      }


      ChessPiece blackKing = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
      ChessPosition positionBlackKing = new ChessPosition(8, 5  );
      addPiece(positionBlackKing, blackKing);

      ChessPiece blackQueen = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
      ChessPosition positionBlackQueen = new ChessPosition(8, 4);
      addPiece(positionBlackQueen, blackQueen);

      ChessPiece blackBishop = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
      ChessPosition positionBlackBishop1 = new ChessPosition(8, 3);
      addPiece(positionBlackBishop1, blackBishop);
      ChessPosition positionBlackBishop2 = new ChessPosition(8, 6);
      addPiece(positionBlackBishop2, blackBishop);

      ChessPiece blackKnight = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
      ChessPosition positionBlackKnight1 = new ChessPosition(8, 2);
      addPiece(positionBlackKnight1, blackKnight);
      ChessPosition positionBlackKnight2 = new ChessPosition(8, 7);
      addPiece(positionBlackKnight2, blackKnight);

      ChessPiece blackRook = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
      ChessPosition positionBlackRook1 = new ChessPosition(8, 1);
      addPiece(positionBlackRook1, blackRook);
      ChessPosition positionBlackRook2 = new ChessPosition(8, 8);
      addPiece(positionBlackRook2, blackRook);

      for (int i = 1; i <= 8; i++ ) {
        // String name = String.format("whitePawn%d", i + 1);
        // ChessPiece name = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece blackPawn = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPosition position = new ChessPosition(7, i);
        addPiece(position, blackPawn);
      }
    }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ChessBoard that=(ChessBoard) o;
    return Arrays.deepEquals(squares, that.squares);
  }

  @Override
  public int hashCode() {
    return Arrays.deepHashCode(squares);
  }

  @Override
    public String toString() {
//                |r|n|b|q|k|b|n|r|
//                |p|p|p|p|p|p|p|p|
//                | | | | | | | | |
//                | | | | | | | | |
//                | | | | | | | | |
//                | | | | | | | | |
//                |P|P|P|P|P|P|P|P|
//                |R|N|B|Q|K|B|N|R|
      StringBuilder board = new StringBuilder();
      for(int i = 0; i < 8; i++) {
        for(int j = 0; j < 8; j++ ){
          if(squares[i][j] == null){
            board.append("| ");
          }else {
            board.append("|")
                 .append(squares[i][j].toString());
          }
        }
        board.append("|")
             .append("\n");
      }
      return board.toString();
    }


}
