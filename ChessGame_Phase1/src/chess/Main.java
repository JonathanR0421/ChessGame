package chess;

import chess.game.Game;

/** Starts the console chess game. */
public class Main {
    /** Program entry point. @param args command-line arguments (not used) */
    public static void main(String[] args) {
        // Create the game and start asking players for moves.
        new Game().start();
    }
}
