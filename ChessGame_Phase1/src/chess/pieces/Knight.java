package chess.pieces;

import java.util.List;
import chess.board.Board;
import chess.position.Position;

/** Represents a knight, which moves in an L shape. */
public class Knight extends Piece {
    /** Creates a knight. */
    public Knight(Color color, Position position) { super(color, position); }
    /** Returns the knight's possible L-shaped destinations. */
    @Override public List<Position> possibleMoves(Board board) {
        List<Position> moves = newMoveList();
        int row = getPosition().getRow(), column = getPosition().getColumn();
        // Knights can jump over pieces, so each destination is checked independently.
        int[][] offsets = {{-2,-1}, {-2,1}, {-1,-2}, {-1,2}, {1,-2}, {1,2}, {2,-1}, {2,1}};
        for (int[] offset : offsets) addMoveIfAvailable(board, moves, row + offset[0], column + offset[1]);
        return moves;
    }
    /** Returns wN or bN. */
    @Override public String getSymbol() { return getColor() == Color.WHITE ? "wN" : "bN"; }
}
