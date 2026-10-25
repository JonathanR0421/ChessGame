package chess.game;

import java.util.Scanner;
import chess.board.Board;
import chess.pieces.Color;
import chess.position.Position;

/** Controls the console input loop and player turns. */
public class Game {
    /** The board used for this game. */
    private final Board board;
    /** White player. */
    private final Player whitePlayer;
    /** Black player. */
    private final Player blackPlayer;
    /** Player whose turn it currently is. */
    private Player currentPlayer;

    /** Creates a new game and selects White to move first. */
    public Game() {
        board = new Board();
        whitePlayer = new Player(Color.WHITE);
        blackPlayer = new Player(Color.BLACK);
        currentPlayer = whitePlayer;
    }

    /** Starts the game until the user enters quit. */
    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Console Chess!");
        System.out.println("Enter moves like E2 E4. Type quit to exit.");
        System.out.println("Phase 1 limitations: check/checkmate and special moves are not implemented.");

        boolean playing = true;
        while (playing) {
            board.display();
            System.out.println(currentPlayer.getName() + "'s turn.");
            System.out.print("Move (FROM TO): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("quit")) {
                playing = false;
                continue;
            }

            // A move must contain exactly two coordinates separated by whitespace.
            String[] parts = input.split("\\s+");
            if (parts.length != 2) {
                System.out.println("Invalid format. Example: E2 E4");
                continue;
            }

            try {
                Position from = Position.fromChessNotation(parts[0]);
                Position to = Position.fromChessNotation(parts[1]);
                String error = board.movePiece(from, to, currentPlayer.getColor());
                if (error != null) {
                    System.out.println("Invalid move: " + error);
                    continue; // An invalid move does not change the turn.
                }
                switchTurn();
            } catch (IllegalArgumentException exception) {
                // Bad coordinates are reported instead of crashing the program.
                System.out.println("Invalid coordinate: " + exception.getMessage());
            }
        }
        board.display();
        System.out.println("Game ended. Thanks for playing!");
        scanner.close();
    }

    /** Switches to the other player after a successful move. */
    private void switchTurn() {
        currentPlayer = currentPlayer == whitePlayer ? blackPlayer : whitePlayer;
    }
}
