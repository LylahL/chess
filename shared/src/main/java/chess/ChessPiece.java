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
          case KING -> bishopMoves(board, myPosition);
          case QUEEN -> bishopMoves(board, myPosition);
          case BISHOP -> bishopMoves(board, myPosition);
          case KNIGHT -> bishopMoves(board, myPosition);
          case ROOK -> bishopMoves(board, myPosition);
          case PAWN -> bishopMoves(board, myPosition);
        };
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
        bishopMoveHelper(moves, row, col, board, myPosition, endPosition, i, i); // change moving directions by modifying i and j.
        // DownLeft
        bishopMoveHelper(moves, row, col, board, myPosition, endPosition, i, j);
        // UpRight
        bishopMoveHelper(moves, row, col, board, myPosition, endPosition, j, i);
        // UpLeft
        bishopMoveHelper(moves, row, col, board, myPosition, endPosition, j, j);


//        bishopDownRight(moves, row, col, board, myPosition, endPosition);
//        bishopDownLeft(moves, row, col, board, myPosition, endPosition);

        return moves;
    }

    private void bishopMoveHelper(Collection<ChessMove> moves, int row, int col, ChessBoard board, ChessPosition myPosition, ChessPosition endPosition, int i, int j) {
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
                if (myPosition == endPosition) {
                    break;
                }
                endPosition=nextStepPosition;
                addToCollection(moves, myPosition, endPosition);
                break;
            }
            // all clear
            else if (pieceUnder == null && myPosition != endPosition) {
                endPosition=nextStepPosition;
                addToCollection(moves, myPosition, endPosition);
            }
            nextStepPosition=new ChessPosition(nextStepPosition.getRow() + i, nextStepPosition.getColumn() + j);
        }
    }

    private void addToCollection(Collection<ChessMove> moves, ChessPosition startPosition, ChessPosition endPosition){
        moves.add(new ChessMove(startPosition, endPosition, null));
    }

    private HashSet<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        throw new RuntimeException("Not Implemented");
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
