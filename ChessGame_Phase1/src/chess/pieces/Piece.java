package chess.pieces;

import java.util.ArrayList;
import java.util.List;
import chess.board.Board;
import chess.position.Position;

/** Common parent class for all chess pieces. */
public abstract class Piece {
    /** Piece color, which does not change. */
    private final Color color;
    /** Current square occupied by this piece. */
    private Position position;

    /** Creates a piece. @param color piece color @param position starting square */
    public Piece(Color color, Position position) {
        if (color == null || position == null) throw new IllegalArgumentException("Color and position are required.");
        this.color = color;
        this.position = position;
    }

    /** @return piece color */
    public Color getColor() { return color; }
    /** @return current square */
    public Position getPosition() { return position; }
    /** Updates the piece's square after a move. @param position new square */
    public void setPosition(Position position) {
        if (position == null) throw new IllegalArgumentException("Position cannot be null.");
        this.position = position;
    }

    /**
     * Returns destinations matching this piece's movement pattern.
     * This Phase 1 version does not check whether the king would be left in check.
     * @param board current board @return possible destination squares
     */
    public abstract List<Position> possibleMoves(Board board);
    /** @return two-character display symbol such as wP or bK */
    public abstract String getSymbol();

    /** Adds an adjacent square if it is empty or contains an opponent. */
    protected void addMoveIfAvailable(Board board, List<Position> moves, int row, int column) {
        if (row < 0 || row > 7 || column < 0 || column > 7) return;
        Position target = new Position(row, column);
        Piece occupant = board.getPiece(target);
        if (occupant == null || occupant.getColor() != color) moves.add(target);
    }

    /** Adds squares in one direction until a piece or the board edge blocks the path. */
    protected void addSlidingMoves(Board board, List<Position> moves, int rowStep, int columnStep) {
        int row = position.getRow() + rowStep;
        int column = position.getColumn() + columnStep;
        while (row >= 0 && row < 8 && column >= 0 && column < 8) {
            Position target = new Position(row, column);
            Piece occupant = board.getPiece(target);
            if (occupant == null) {
                moves.add(target);
            } else {
                if (occupant.getColor() != color) moves.add(target);
                break; // Pieces block movement beyond their square.
            }
            row += rowStep;
            column += columnStep;
        }
    }

    /** @return a new list for a subclass to fill */
    protected List<Position> newMoveList() { return new ArrayList<>(); }
}
