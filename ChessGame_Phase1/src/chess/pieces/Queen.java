package chess.pieces;

import java.util.List;
import chess.board.Board;
import chess.position.Position;

/** Represents a queen, which combines rook and bishop movement. */
public class Queen extends Piece {
    /** Creates a queen. */
    public Queen(Color color, Position position) { super(color, position); }
    /** Returns horizontal, vertical, and diagonal destinations. */
    @Override public List<Position> possibleMoves(Board board) {
        List<Position> moves = newMoveList();
        addSlidingMoves(board, moves, -1, 0);
        addSlidingMoves(board, moves, 1, 0);
        addSlidingMoves(board, moves, 0, -1);
        addSlidingMoves(board, moves, 0, 1);
        addSlidingMoves(board, moves, -1, -1);
        addSlidingMoves(board, moves, -1, 1);
        addSlidingMoves(board, moves, 1, -1);
        addSlidingMoves(board, moves, 1, 1);
        return moves;
    }
    /** Returns wQ or bQ. */
    @Override public String getSymbol() { return getColor() == Color.WHITE ? "wQ" : "bQ"; }
}
