package chess.pieces;

import java.util.List;
import chess.board.Board;
import chess.position.Position;

/** Represents a bishop, which moves diagonally. */
public class Bishop extends Piece {
    /** Creates a bishop. */
    public Bishop(Color color, Position position) { super(color, position); }
    /** Returns unobstructed diagonal destinations. */
    @Override public List<Position> possibleMoves(Board board) {
        List<Position> moves = newMoveList();
        addSlidingMoves(board, moves, -1, -1);
        addSlidingMoves(board, moves, -1, 1);
        addSlidingMoves(board, moves, 1, -1);
        addSlidingMoves(board, moves, 1, 1);
        return moves;
    }
    /** Returns wB or bB. */
    @Override public String getSymbol() { return getColor() == Color.WHITE ? "wB" : "bB"; }
}
