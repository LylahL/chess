package chess;

import jdk.jshell.spi.ExecutionControl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    TeamColor teamTurn;
    ChessBoard board = new ChessBoard();

    public ChessGame() {
        board.resetBoard();
        teamTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) throws InvalidMoveException {
        ChessPiece current_piece = board.getPiece(startPosition);
        Collection<ChessMove> allMoves = current_piece.pieceMoves(board, startPosition);
        Collection<ChessMove> validMoves = new HashSet<>();
      for (ChessMove move : allMoves) {
        this.makeMove(move);
        if (!isInCheck(current_piece.getTeamColor())) {
          validMoves.add(move);
        }
      }
      return  validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece piece = board.getPiece(startPosition);
        if(piece == null){
            throw new InvalidMoveException("There is no piece to move at starting position");
        }
        Collection<ChessMove> validMoves = validMoves(startPosition);
        if (validMoves.contains(move)){
            board.addPiece(endPosition, piece);
            board.addPiece(startPosition, null);
            if(piece.getTeamColor() == TeamColor.WHITE){
                setTeamTurn(TeamColor.BLACK);
            }else{
                setTeamTurn(TeamColor.WHITE);
            }
        }else{
            throw new InvalidMoveException("Invalid move");
        }

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        // checks if team's KING is in other team's pieceMoves
        Collection<ChessMove> allMoves = new HashSet<>();
        ChessPosition kingPosition=null;
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                ChessPosition currentPosition = new ChessPosition(i,j);
                ChessPiece currentPiece = board.getPiece(currentPosition);
                if(currentPiece == null){
                    continue;
                }
                else {
                    // if is team's KING get position
                    if(currentPiece.getPieceType() == ChessPiece.PieceType.KING && currentPiece.getTeamColor() == teamColor){
                      kingPosition = currentPosition;
                    }
                    // if is opposite team's piece, add the possible move to collection
                    else if(currentPiece.getTeamColor() != teamColor){
                        allMoves.addAll(currentPiece.pieceMoves(board, currentPosition));
                    }
                    // if team's other pieces
                    else {
                        continue;
                    }
                }
            }
        }
        // check if kingPosition in collection
        for(ChessMove move: allMoves){
            if (move.getEndPosition() == kingPosition){
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) throws InvalidMoveException {
        // if the team's KING makes a move and is still InCheck then isInCheckMate
        // find KING
        ChessPosition kingPosition=null;
        ChessPiece kingPiece=null;
        for (int i=0; i < 8; i++) {
            for (int j=0; j < 8; j++) {
                ChessPosition currentPosition=new ChessPosition(i, j);
                ChessPiece currentPiece=board.getPiece(currentPosition);
                if(currentPiece == null){
                    continue;
                }
                else if (currentPiece.getPieceType() == ChessPiece.PieceType.KING && currentPiece.getTeamColor() == teamColor) {
                    kingPosition=currentPosition;
                    kingPiece=currentPiece;
                } else {
                    continue;
                }
            }
        }
        assert kingPiece != null;
        Collection<ChessMove> kingMoves=kingPiece.pieceMoves(board, kingPosition);

        if (!isInCheck(teamColor)) {
            return false;
        }
        for(ChessMove move : kingMoves){
            this.makeMove(move);
        }
      return !isInCheck(teamColor);

    }


    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame=(ChessGame) o;
        return teamTurn == chessGame.teamTurn && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, board);
    }
}
