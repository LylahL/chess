package chess;

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

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()][position.getColumn()];
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
      ChessPosition positionWhiteKing = new ChessPosition(0, 4);
      addPiece(positionWhiteKing, whiteKing);

      ChessPiece whiteQueen = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
      ChessPosition positionWhiteQueen = new ChessPosition(0, 3);
      addPiece(positionWhiteQueen, whiteQueen);

      ChessPiece whiteBishop = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
      ChessPosition positionWhiteBishop1 = new ChessPosition(0, 2);
      addPiece(positionWhiteBishop1, whiteBishop);
      ChessPosition positionWhiteBishop2 = new ChessPosition(0, 5);
      addPiece(positionWhiteBishop2, whiteBishop);

      ChessPiece whiteKnight = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
      ChessPosition positionWhiteKnight1 = new ChessPosition(0, 1);
      addPiece(positionWhiteKnight1, whiteKnight);
      ChessPosition positionWhiteKnight2 = new ChessPosition(0, 6);
      addPiece(positionWhiteKnight2, whiteKnight);

      ChessPiece whiteRook = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
      ChessPosition positionWhiteRook1 = new ChessPosition(0, 0);
      addPiece(positionWhiteRook1, whiteRook);
      ChessPosition positionWhiteRook2 = new ChessPosition(0, 7);
      addPiece(positionWhiteRook2, whiteRook);

      for (int i = 0; i < 8; i++ ) {
      // String name = String.format("whitePawn%d", i + 1);
      // ChessPiece name = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece whitePawn = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPosition position = new ChessPosition(1, i);
        addPiece(position, whitePawn);
      }


      ChessPiece blackKing = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
      ChessPosition positionBlackKing = new ChessPosition(7, 4  );
      addPiece(positionBlackKing, blackKing);

      ChessPiece blackQueen = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
      ChessPosition positionBlackQueen = new ChessPosition(7, 3);
      addPiece(positionBlackQueen, blackQueen);

      ChessPiece blackBishop = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
      ChessPosition positionBlackBishop1 = new ChessPosition(7, 2);
      addPiece(positionBlackBishop1, blackBishop);
      ChessPosition positionBlackBishop2 = new ChessPosition(7, 5);
      addPiece(positionBlackBishop2, blackBishop);

      ChessPiece blackKnight = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
      ChessPosition positionBlackKnight1 = new ChessPosition(7, 1);
      addPiece(positionBlackKnight1, blackKnight);
      ChessPosition positionBlackKnight2 = new ChessPosition(7, 6);
      addPiece(positionBlackKnight2, blackKnight);

      ChessPiece blackRook = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);

      for (int i = 0; i < 8; i++ ) {
        // String name = String.format("whitePawn%d", i + 1);
        // ChessPiece name = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece blackPawn = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPosition position = new ChessPosition(6, i);
        addPiece(position, blackPawn);
      }
    }


}
