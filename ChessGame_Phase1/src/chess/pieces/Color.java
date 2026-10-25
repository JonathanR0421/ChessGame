package chess.pieces;

/** The two colors in chess. */
public enum Color {
    /** White player and pieces. */ WHITE,
    /** Black player and pieces. */ BLACK;

    /** @return the other color */
    public Color opposite() { return this == WHITE ? BLACK : WHITE; }
}
