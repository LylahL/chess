package chess;

import java.util.*;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    TeamColor teamTurn;

    public int getGameOver() {
        return GameOver;
    }

    public void setGameOver(int gameOver) {
        GameOver=gameOver;
    }

    int GameOver = 0;
    ChessBoard board = new ChessBoard();


    public ChessGame() {
        board.resetBoard();
        teamTurn = null;
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
    public void  setTeamTurn(TeamColor team) {
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
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece currentPiece = board.getPiece(startPosition);
        Collection<ChessMove> allMoves = currentPiece.pieceMoves(board, startPosition);
        Collection<ChessMove> validMoves = new HashSet<>();
        // need to make copy of the board
        ChessBoard originalBoard;
        originalBoard = copyBoard(board);
        // use a for loop, create new chess pieces to that new board

        if(currentPiece != null){
            for (ChessMove move : allMoves) {
                board.addPiece(startPosition, null);
                board.addPiece(move.getEndPosition(), currentPiece);
                if (!isInCheck(currentPiece.getTeamColor())) {
                    validMoves.add(move);
                }
                board = copyBoard(originalBoard);
            }
        }
      return  validMoves;
    }

    private ChessBoard copyBoard(ChessBoard board) {
        ChessBoard newBoard = new ChessBoard();
        for (int i=1; i <= 8; i++) {
            for (int j=1; j <= 8; j++) {
                ChessPosition currentPosition = new ChessPosition(i,j);
                ChessPiece currentPiece = board.getPiece(currentPosition);
                if(currentPiece == null){
                    continue;
                }
                else {
                    newBoard.addPiece(currentPosition, currentPiece);
                }
            }
        }
        return newBoard;
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
        Collection<ChessMove> possibleMoves = new HashSet<>();
        possibleMoves = validMoves(startPosition);
        if(piece == null){
            throw new InvalidMoveException("There is no piece to move at starting position");
        }
        if (!(move.getStartPosition().getRow() > 0 && move.getStartPosition().getRow() <= 8 && move.getStartPosition().getColumn() > 0 && move.getStartPosition().getColumn() <= 8)) {
            throw new InvalidMoveException("start Position out of bounds");
        }
        if (!(move.getEndPosition().getRow() > 0 && move.getEndPosition().getRow() <= 8 && move.getEndPosition().getColumn() > 0 && move.getEndPosition().getColumn() <= 8)) {
            throw new InvalidMoveException("end position out of bounds");
        }
        if(!possibleMoves.contains(move)){
            throw new InvalidMoveException("Invalid move");
        }
        if(teamTurn != piece.getTeamColor()){
            throw new InvalidMoveException("Wrong team turn");
        }
        if(move.getPromotionPiece() != null){
            board.addPiece(endPosition, new ChessPiece(piece.getTeamColor(), move.getPromotionPiece()));
        }else {
            board.addPiece(endPosition, piece);
        }
            board.addPiece(startPosition, null);
            if(piece.getTeamColor() == TeamColor.WHITE){
                setTeamTurn(TeamColor.BLACK);
            }else{
                setTeamTurn(TeamColor.WHITE);
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
        ChessPosition currentPosition = null;
        ChessPiece currentPiece = null;
        kingPosition = checkIsInCheck(teamColor, allMoves, kingPosition, currentPiece, currentPosition);

        // check if kingPosition in collection
        for(ChessMove move: allMoves){
            if (move.getEndPosition().equals(kingPosition)){
                return true;
            }
        }
        return false;
    }

    private ChessPosition checkIsInCheck(TeamColor teamColor, Collection<ChessMove> allMoves, ChessPosition kingPosition, ChessPiece currentPiece, ChessPosition currentPosition) {
        for (int i=1; i <= 8; i++) {
            for (int j=1; j <= 8; j++) {
                currentPosition = new ChessPosition(i,j);
                currentPiece = board.getPiece(currentPosition);
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
        return kingPosition;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor){
        // if the team's KING makes a move and is still InCheck then isInCheckMate
        // find KING
        ChessPosition kingPosition=null;
        ChessPiece kingPiece=null;
        ChessPosition currentPosition = null;
        ChessPiece currentPiece = null;
        Object[] kingPiecePosition = checkIsInCheckmate(kingPiece, kingPosition, currentPiece, currentPosition, teamColor);
        kingPiece = (ChessPiece) kingPiecePosition[0];
        kingPosition = (ChessPosition) kingPiecePosition[1];
        Collection<ChessMove> kingMoves=kingPiece.pieceMoves(board, kingPosition);
        ChessBoard originalBoard;
        originalBoard = copyBoard(board);

        if (!isInCheck(teamColor)) {
            return false;
        }
        for(ChessMove move : kingMoves){
            board.addPiece(move.getStartPosition(), null);
            board.addPiece(move.getEndPosition(), kingPiece);
            if( isInCheck(teamColor)){
                return true;
            }
            board = copyBoard(originalBoard);
        }
        return false;
    }

    private Object[] checkIsInCheckmate(ChessPiece kingPiece, ChessPosition kingPosition, ChessPiece currentPiece, ChessPosition currentPosition, TeamColor teamColor) {
        Object[] kingPiecePosition = null;
        for (int i=1; i <= 8; i++) {
            for (int j=1; j <= 8; j++) {
                currentPosition=new ChessPosition(i, j);
                currentPiece=board.getPiece(currentPosition);
                if(currentPiece == null){
                    continue;
                }
                else if (currentPiece.getPieceType() == ChessPiece.PieceType.KING && currentPiece.getTeamColor() == teamColor) {
                    kingPosition=currentPosition;
                    kingPiece=currentPiece;
                    kingPiecePosition =new Object[]{kingPiece, kingPosition};
                } else {
                    continue;
                }
            }
        }
        return kingPiecePosition;
    }


    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        Collection<ChessMove>  validMoves = null;
        Collection<ChessMove>  returningMoves = null;
        ChessPosition currentPosition = null;
        ChessPiece currentPiece = null;
        if (isInCheck(teamColor)){
            return false;
        }
         returningMoves = checkIsInStalemate(validMoves, currentPosition, currentPiece, teamColor);

      return returningMoves.isEmpty();
    }

    private Collection<ChessMove> checkIsInStalemate(Collection<ChessMove> validMoves, ChessPosition currentPosition, ChessPiece currentPiece, TeamColor teamColor) {
        for (int i=1; i <= 8; i++) {
            for (int j=1; j <= 8; j++) {
                currentPosition=new ChessPosition(i, j);
                currentPiece=board.getPiece(currentPosition);
                if(currentPiece == null){
                    continue;
                }
                else if(currentPiece.getTeamColor() == teamColor){
                    validMoves = this.validMoves(currentPosition);
                }
            }
        }
        return validMoves;
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
