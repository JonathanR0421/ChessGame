package chess.position;

/** Represents one square on the chessboard. Row 0 is rank 8; column 0 is file A. */
public class Position {
    /** Zero-based row number. */
    private final int row;
    /** Zero-based column number. */
    private final int column;

    /** Creates a position. @param row row 0-7 @param column column 0-7 */
    public Position(int row, int column) {
        // Reject squares outside the 8x8 board.
        if (row < 0 || row > 7 || column < 0 || column > 7) {
            throw new IllegalArgumentException("Position must be inside the 8x8 board.");
        }
        this.row = row;
        this.column = column;
    }

    /** Converts notation such as E2 into a position. @param coordinate chess square @return position */
    public static Position fromChessNotation(String coordinate) {
        if (coordinate == null) throw new IllegalArgumentException("Coordinate cannot be empty.");
        String text = coordinate.trim().toUpperCase();
        if (text.length() != 2) throw new IllegalArgumentException("Use a coordinate such as E2.");
        char file = text.charAt(0), rank = text.charAt(1);
        if (file < 'A' || file > 'H' || rank < '1' || rank > '8') {
            throw new IllegalArgumentException("Coordinate must use files A-H and ranks 1-8.");
        }
        // Java arrays start at zero, while chess ranks start at one.
        return new Position(8 - Character.getNumericValue(rank), file - 'A');
    }

    /** @return row index */
    public int getRow() { return row; }
    /** @return column index */
    public int getColumn() { return column; }

    /** @return chess notation such as E2 */
    @Override public String toString() { return "" + (char) ('A' + column) + (8 - row); }

    /** @return true when both positions identify the same square */
    @Override public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Position)) return false;
        Position p = (Position) other;
        return row == p.row && column == p.column;
    }

    /** @return hash code based on row and column */
    @Override public int hashCode() { return row * 8 + column; }
}
