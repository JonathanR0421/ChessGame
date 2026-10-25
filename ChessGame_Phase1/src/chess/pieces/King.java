package chess.pieces;

import java.util.List;
import chess.board.Board;
import chess.position.Position;

/** Represents a king, which moves one square in any direction. */
public class King extends Piece {
    /** Creates a king. */
    public King(Color color, Position position) { super(color, position); }
    /** Returns adjacent destinations that are empty or occupied by an opponent. */
    @Override public List<Position> possibleMoves(Board board) {
        List<Position> moves = newMoveList();
        int row = getPosition().getRow(), column = getPosition().getColumn();
        for (int rowStep = -1; rowStep <= 1; rowStep++) {
            for (int columnStep = -1; columnStep <= 1; columnStep++) {
                if (rowStep != 0 || columnStep != 0) {
                    addMoveIfAvailable(board, moves, row + rowStep, column + columnStep);
                }
            }
        }
        // Check safety and castling are not implemented in this phase.
        return moves;
    }
    /** Returns wK or bK. */
    @Override public String getSymbol() { return getColor() == Color.WHITE ? "wK" : "bK"; }
}
