package chess.game;

import chess.pieces.Color;

/** Represents one player in the game. */
public class Player {
    /** The color this player controls. */
    private final Color color;

    /** Creates a player. @param color player's color */
    public Player(Color color) { this.color = color; }
    /** @return player's color */
    public Color getColor() { return color; }
    /** @return display name for the player */
    public String getName() { return color == Color.WHITE ? "White" : "Black"; }
}
