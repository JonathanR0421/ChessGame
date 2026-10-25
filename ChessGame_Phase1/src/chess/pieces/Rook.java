package chess.pieces;

import java.util.List;
import chess.board.Board;
import chess.position.Position;

/** Represents a rook, which moves horizontally and vertically. */
public class Rook extends Piece {
    /** Creates a rook. */
    public Rook(Color color, Position position) { super(color, position); }
    /** Returns unobstructed horizontal and vertical destinations. */
    @Override public List<Position> possibleMoves(Board board) {
        List<Position> moves = newMoveList();
        addSlidingMoves(board, moves, -1, 0);
        addSlidingMoves(board, moves, 1, 0);
        addSlidingMoves(board, moves, 0, -1);
        addSlidingMoves(board, moves, 0, 1);
        return moves;
    }
    /** Returns wR or bR. */
    @Override public String getSymbol() { return getColor() == Color.WHITE ? "wR" : "bR"; }
}
