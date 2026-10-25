package chess.pieces;

import java.util.List;
import chess.board.Board;
import chess.position.Position;

/** Represents a pawn. */
public class Pawn extends Piece {
    /** Creates a pawn. */
    public Pawn(Color color, Position position) { super(color, position); }

    /** Returns forward moves and diagonal captures. */
    @Override public List<Position> possibleMoves(Board board) {
        List<Position> moves = newMoveList();
        int row = getPosition().getRow(), column = getPosition().getColumn();
        int direction = getColor() == Color.WHITE ? -1 : 1;
        int nextRow = row + direction;

        // Pawns move forward only if the square is empty.
        if (nextRow >= 0 && nextRow < 8 && board.getPiece(new Position(nextRow, column)) == null) {
            moves.add(new Position(nextRow, column));
            int startRow = getColor() == Color.WHITE ? 6 : 1;
            int twoRows = row + 2 * direction;
            if (row == startRow && board.getPiece(new Position(twoRows, column)) == null) {
                moves.add(new Position(twoRows, column));
            }
        }

        // Pawns capture diagonally when an opponent occupies that square.
        for (int change : new int[]{-1, 1}) {
            int targetColumn = column + change;
            if (nextRow >= 0 && nextRow < 8 && targetColumn >= 0 && targetColumn < 8) {
                Position target = new Position(nextRow, targetColumn);
                Piece occupant = board.getPiece(target);
                if (occupant != null && occupant.getColor() != getColor()) moves.add(target);
            }
        }
        // En passant and promotion are not included in this phase.
        return moves;
    }

    /** Returns wp for white or bp for black. */
    @Override public String getSymbol() { return getColor() == Color.WHITE ? "wp" : "bp"; }
}
