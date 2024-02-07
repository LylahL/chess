package chess;

import javax.swing.text.Position;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

import static chess.ChessPiece.PieceType.*;

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
          case KNIGHT -> knightMoves(board, myPosition);
          case ROOK -> rookMoves(board, myPosition);
          case PAWN -> pawnMoves(board, myPosition);
        };
    }
    private void addToPromotionCollection(Collection<ChessMove> moves, ChessPosition startPosition, ChessPosition endPosition, PieceType type){
        moves.add(new ChessMove(startPosition, endPosition, type));
    }
    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new HashSet<>();
        Collection<ChessMove> promotionMoves = new HashSet<>();
        Collection<ChessMove> discardedMoves = new HashSet<>();
        int col = myPosition.getColumn();
        int row = myPosition.getRow();
        ChessPosition endPosition = new ChessPosition(row, col);
        int i = 1, j = -1;
        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.BLACK){
            // Up
            MoveHelper(moves, row, col, board, myPosition, endPosition, j, 0);
            // if moves includes reach end position, grab the position and promote it's all posibilities
            for (ChessMove move: moves){
                if (move.getEndPosition().getRow() == 1) {
                    //promotion
                    // collection add all the promotion moves deletes all the promotion null
                    addToCollection(discardedMoves, myPosition, move.getEndPosition());
                    addToPromotionCollection(promotionMoves, myPosition, move.getEndPosition(), BISHOP);
                    addToPromotionCollection(promotionMoves, myPosition, move.getEndPosition(), QUEEN);
                    addToPromotionCollection(promotionMoves, myPosition, move.getEndPosition(), KNIGHT);
                    addToPromotionCollection(promotionMoves, myPosition, move.getEndPosition(), ROOK);
                }
            }
            for (ChessMove move: discardedMoves){
                moves.remove(move);
            }
            moves.addAll(promotionMoves);

        }
        else{
            // Down
            MoveHelper(moves, row, col, board, myPosition, endPosition, i, 0);// change moving directions by modifying i and j.
            for (ChessMove move: moves){
                if (move.getEndPosition().getRow() == 8) {
                    //promotion
                    // collection add all the promotion moves deletes all the promotion null
                    addToCollection(discardedMoves, myPosition, move.getEndPosition());
                    addToPromotionCollection(promotionMoves, myPosition, move.getEndPosition(), BISHOP);
                    addToPromotionCollection(promotionMoves, myPosition, move.getEndPosition(), QUEEN);
                    addToPromotionCollection(promotionMoves, myPosition, move.getEndPosition(), KNIGHT);
                    addToPromotionCollection(promotionMoves, myPosition, move.getEndPosition(), ROOK);
                }
            }
            for (ChessMove move: discardedMoves){
                moves.remove(move);
            }
            moves.addAll(promotionMoves);
        }
        return moves;
    }

    private Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new HashSet<>();
        int col = myPosition.getColumn();
        int row = myPosition.getRow();
        ChessPosition endPosition = new ChessPosition(row, col);
        int i = 2, j = 1;
        // UpLeft
        MoveHelper(moves, row, col, board, myPosition, endPosition, -i, j); // change moving directions by modifying i and j.
        MoveHelper(moves, row, col, board, myPosition, endPosition, -i, -j);
        MoveHelper(moves, row, col, board, myPosition, endPosition, i, j);
        MoveHelper(moves, row, col, board, myPosition, endPosition, i, -j);
        MoveHelper(moves, row, col, board, myPosition, endPosition, j, i);
        MoveHelper(moves, row, col, board, myPosition, endPosition, j, -i);
        MoveHelper(moves, row, col, board, myPosition, endPosition, -j, i);
        MoveHelper(moves, row, col, board, myPosition, endPosition, -j, -i);
        return moves;
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
    
    private boolean checkInitialStepPawn(ChessPosition myPosition, ChessPiece pawn){
        return (myPosition.getRow() == 2 && pawn.getTeamColor() == ChessGame.TeamColor.WHITE) ||
                (myPosition.getRow() == 7 && pawn.getTeamColor() == ChessGame.TeamColor.BLACK);
    }



    private void MoveHelper(Collection<ChessMove> moves, int row, int col, ChessBoard board, ChessPosition myPosition, ChessPosition endPosition, int i, int j) {
        ChessPosition nextStepPosition = new ChessPosition(row + i, col + j);
        if(type == PAWN && checkInitialStepPawn(myPosition, this) && frontNotBlock(board,myPosition) && front2NotBlock(board, myPosition)){
            // you can do two step
            addToCollection(moves,myPosition,nextStepPosition);
            nextStepPosition = new ChessPosition(row + 2*i, col + 2*j);
            addToCollection(moves, myPosition, nextStepPosition);
        }
        while (checkBounds(nextStepPosition)) {
            ChessPiece pieceUnder=board.getPiece(nextStepPosition);
            // gonna hit own team, stop before next step
            if (pieceUnder != null && pieceUnder.getTeamColor() == board.getPiece(myPosition).getTeamColor()) {
                // check if it's first move, if it is, don't add it
                if (myPosition.equals(endPosition)) {
                    //myPosition.getRow()+i == endPosition.getRow() && myPosition.getColumn()+j == endPosition.getColumn()
                    if(type == PAWN){
                        // modifies endPosition it may return an array of end Positinos
                        Collection<ChessPosition> possibleCaptures = checkCapture(board, myPosition, myPosition, endPosition);
                        for(ChessPosition capturePosition : possibleCaptures){
                            if (capturePosition != null) {
                                addToCollection(moves, myPosition, capturePosition);
                            }
                        }
                        break;
                    }
                    break;
                }
                addToCollection(moves, myPosition, endPosition);
                break;
            }
            // hit opposite team
            else if (pieceUnder != null && pieceUnder.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                if(type == PAWN){
                    Collection<ChessPosition> possibleCaptures = checkCapture(board, myPosition, myPosition, endPosition);
                    for(ChessPosition capturePosition : possibleCaptures){
                        if (capturePosition != null) {
                            addToCollection(moves, myPosition, capturePosition);
                        }
                    }
                    break;
                }
                if ((type == KING ||type == KNIGHT)&& !myPosition.equals(endPosition)){
                    break;
                }
                endPosition=nextStepPosition;
                addToCollection(moves, myPosition, endPosition);
                break;
            }
            // all clear
            else if (pieceUnder == null && myPosition != endPosition) {
                if ((type == KING ||type == KNIGHT)&& !myPosition.equals(endPosition) ||(type == PAWN)){
                    // if pawn just detect capture
                    if(type == PAWN) {
                        addToCollection(moves, myPosition, nextStepPosition);
                        Collection<ChessPosition> possibleCaptures=checkCapture(board, myPosition, myPosition, endPosition);
                        for (ChessPosition capturePosition : possibleCaptures) {
                            if (capturePosition != null) {
                                addToCollection(moves, myPosition, capturePosition);
                            }
                        }
                        break;
                    }
                    // Pieces that can only make one move
                    break;
                }
                endPosition=nextStepPosition;
                addToCollection(moves, myPosition, endPosition);
            }
            // just check capture

            nextStepPosition=new ChessPosition(nextStepPosition.getRow() + i, nextStepPosition.getColumn() + j);
        }
    }

    private boolean front2NotBlock(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        if(board.getPiece(myPosition) == null){
            return false;
        }
        if(board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE){
            ChessPosition frontPosition = new ChessPosition(row+2, col);
            return board.getPiece(frontPosition) == null;
        }else{
            ChessPosition frontPosition = new ChessPosition(row-2, col);
            return board.getPiece(frontPosition) == null;

        }
    }

    private boolean frontNotBlock(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        if(board.getPiece(myPosition) == null){
            return false;
        }
        if(board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE){
            ChessPosition frontPosition = new ChessPosition(row+1, col);
            return board.getPiece(frontPosition) == null;
        }else{
            ChessPosition frontPosition = new ChessPosition(row-1, col);
            return board.getPiece(frontPosition) == null;

        }
    }

    // assign endPosition to the possible Position
    private ChessPosition checkCaptureHelper(ChessBoard board, ChessPosition position, ChessPosition endPosition){
        if(board.getPiece(position) != null){
            if(board.getPiece(position).getTeamColor() != null && board.getPiece(endPosition).getTeamColor() != board.getPiece(position).getTeamColor()){
                return position;
            }
        }
        return null;
    }

    private Collection<ChessPosition> checkCapture(ChessBoard board, ChessPosition nextPosition, ChessPosition myPosition, ChessPosition endPosition) {
        Collection<ChessPosition> returnCollection = new HashSet<>();
        int row = nextPosition.getRow();
        int col = nextPosition.getColumn();
        // check if the positions around has opposite side pieces
        // up left
        ChessPosition upLeft = new ChessPosition(row-1, col-1);
        ChessPosition upRight = new ChessPosition(row-1, col+1);
        ChessPosition downLeft = new ChessPosition(row+1, col-1);
        ChessPosition downRight = new ChessPosition(row+1, col+1);
        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE){
            // return a possible Capture possition so I can add it
            returnCollection.add(checkCaptureHelper(board, downLeft, endPosition));
            returnCollection.add(checkCaptureHelper(board, downRight, endPosition));
        }
        else{
            returnCollection.add(checkCaptureHelper(board, upLeft, endPosition));
            returnCollection.add(checkCaptureHelper(board, upRight, endPosition));
        }
        return returnCollection;






        /* up right

        if(board.getPiece(upRight).getTeamColor() != null && board.getPiece(myPosition).getTeamColor() != board.getPiece(upRight).getTeamColor()){
            endPosition = upRight;
        }
        // down left

        if(board.getPiece(downLeft).getTeamColor() != null && board.getPiece(myPosition).getTeamColor() != board.getPiece(downLeft).getTeamColor()){
            endPosition = downLeft;
        }
        // down right

        if(board.getPiece(downRight).getTeamColor() != null && board.getPiece(myPosition).getTeamColor() != board.getPiece(downRight).getTeamColor()){
            endPosition = downRight;
        }*/
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
    if (pieceColor == ChessGame.TeamColor.BLACK) {
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
