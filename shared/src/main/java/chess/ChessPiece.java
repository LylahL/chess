package chess;

import javax.swing.text.Position;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

import static chess.ChessPiece.PieceType.KING;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final PieceType type;
    private final ChessGame.TeamColor pieceColor;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
      // generate all possible moves for that type of piece from that position.
        return switch (type) {
          case KING -> kingMoves(board, myPosition);
          case QUEEN -> queenMoves(board, myPosition);
          case BISHOP -> bishopMoves(board, myPosition);
          case KNIGHT -> bishopMoves(board, myPosition);
          case ROOK -> rookMoves(board, myPosition);
          case PAWN -> bishopMoves(board, myPosition);
        };
    }

    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new HashSet<>();
        int col = myPosition.getColumn();
        int row = myPosition.getRow();
        ChessPosition endPosition = new ChessPosition(row, col);
        int i = 1, j = -1;
        // Down
        MoveHelper(moves, row, col, board, myPosition, endPosition, i, 0); // change moving directions by modifying i and j.
        // Left
        MoveHelper(moves, row, col, board, myPosition, endPosition, 0, j);
        // Up
        MoveHelper(moves, row, col, board, myPosition, endPosition, j, 0);
        // Right
        MoveHelper(moves, row, col, board, myPosition, endPosition, 0, i);
        // DownRight
        MoveHelper(moves, row, col, board, myPosition, endPosition, i, i);
        // DownLeft
        MoveHelper(moves, row, col, board, myPosition, endPosition, i, j);
        // UpRight
        MoveHelper(moves, row, col, board, myPosition, endPosition, j, i);
        // UpLeft
        MoveHelper(moves, row, col, board, myPosition, endPosition, j, j);
        return moves;
    }



    private Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new HashSet<>();
        int col = myPosition.getColumn();
        int row = myPosition.getRow();
        ChessPosition endPosition = new ChessPosition(row, col);
        int i = 1, j = -1;
        // Down
        MoveHelper(moves, row, col, board, myPosition, endPosition, i, 0); // change moving directions by modifying i and j.
        // Left
        MoveHelper(moves, row, col, board, myPosition, endPosition, 0, j);
        // Up
        MoveHelper(moves, row, col, board, myPosition, endPosition, j, 0);
        // Right
        MoveHelper(moves, row, col, board, myPosition, endPosition, 0, i);
        // DownRight
        MoveHelper(moves, row, col, board, myPosition, endPosition, i, i);
        // DownLeft
        MoveHelper(moves, row, col, board, myPosition, endPosition, i, j);
        // UpRight
        MoveHelper(moves, row, col, board, myPosition, endPosition, j, i);
        // UpLeft
        MoveHelper(moves, row, col, board, myPosition, endPosition, j, j);
        return moves;
    }

    private Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new HashSet<>();
        int col = myPosition.getColumn();
        int row = myPosition.getRow();
        ChessPosition endPosition = new ChessPosition(row, col);
        int i = 1, j = -1;
        // Down
        MoveHelper(moves, row, col, board, myPosition, endPosition, i, 0); // change moving directions by modifying i and j.
        // Left
        MoveHelper(moves, row, col, board, myPosition, endPosition, 0, j);
        // Up
        MoveHelper(moves, row, col, board, myPosition, endPosition, j, 0);
        // Right
        MoveHelper(moves, row, col, board, myPosition, endPosition, 0, i);
        return moves;
    }

    private boolean checkBounds(ChessPosition Position) {
        return Position.getRow() <= 8 &&
                Position.getRow() > 0 &&
                Position.getColumn() <= 8 &&
                Position.getColumn() > 0;
    }


    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new HashSet<>();
        int col = myPosition.getColumn();
        int row = myPosition.getRow();
        ChessPosition endPosition = new ChessPosition(row, col);
        int i = 1, j = -1;
        // DownRight
        MoveHelper(moves, row, col, board, myPosition, endPosition, i, i); // change moving directions by modifying i and j.
        // DownLeft
        MoveHelper(moves, row, col, board, myPosition, endPosition, i, j);
        // UpRight
        MoveHelper(moves, row, col, board, myPosition, endPosition, j, i);
        // UpLeft
        MoveHelper(moves, row, col, board, myPosition, endPosition, j, j);


//        bishopDownRight(moves, row, col, board, myPosition, endPosition);
//        bishopDownLeft(moves, row, col, board, myPosition, endPosition);

        return moves;
    }

    private void MoveHelper(Collection<ChessMove> moves, int row, int col, ChessBoard board, ChessPosition myPosition, ChessPosition endPosition, int i, int j) {
        ChessPosition nextStepPosition = new ChessPosition(row + i, col + j);
        while (checkBounds(endPosition) && checkBounds(nextStepPosition)) {
            ChessPiece pieceUnder=board.getPiece(nextStepPosition);
            // gonna hit own team, stop before next step
            if (pieceUnder != null && pieceUnder.getTeamColor() == board.getPiece(myPosition).getTeamColor()) {
                // check if it's first move, if it is, don't add it
                if (myPosition.equals(endPosition)) {
                    //myPosition.getRow()+i == endPosition.getRow() && myPosition.getColumn()+j == endPosition.getColumn()
                    break;
                }
                addToCollection(moves, myPosition, endPosition);
                break;
            }
            // hit opposite team
            else if (pieceUnder != null && pieceUnder.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                endPosition=nextStepPosition;
                addToCollection(moves, myPosition, endPosition);
                break;
            }
            // all clear
            else if (pieceUnder == null && myPosition != endPosition) {
                if (type == KING && !myPosition.equals(endPosition)){
                    // Pieces that can only make one move
                    break;
                }
                endPosition=nextStepPosition;
                addToCollection(moves, myPosition, endPosition);
            }
            nextStepPosition=new ChessPosition(nextStepPosition.getRow() + i, nextStepPosition.getColumn() + j);
        }
    }

    private void addToCollection(Collection<ChessMove> moves, ChessPosition startPosition, ChessPosition endPosition){
        moves.add(new ChessMove(startPosition, endPosition, null));
    }


@Override
    public String toString() {
        StringBuilder piece = new StringBuilder();
        switch(type){
            case KING:
                piece.append("K");
                break;
            case QUEEN:
                piece.append("Q");
                break;
            case BISHOP:
                piece.append("B");
                break;
            case KNIGHT:
                piece.append("N");
                break;
            case ROOK:
                piece.append("R");
                break;
            case PAWN:
                piece.append("P");
                break;
        }
    if (pieceColor == ChessGame.TeamColor.WHITE) {
        return piece.toString().toLowerCase();
    }
    return piece.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that=(ChessPiece) o;
        return type == that.type && pieceColor == that.pieceColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, pieceColor);
    }
}
