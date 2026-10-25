package chess.board;

import java.util.ArrayList;
import java.util.List;
import chess.pieces.*;
import chess.position.Position;

/** Stores the 8x8 chessboard and provides board operations. */
public class Board {
    /** Each square contains a Piece, or null when the square is empty. */
    private final Piece[][] squares = new Piece[8][8];
    /** List of pieces captured during the game. */
    private final List<Piece> capturedPieces = new ArrayList<>();

    /** Creates a board and places the pieces in their starting squares. */
    public Board() { setupStartingPosition(); }

    /** Sets up the standard starting position. */
    public void setupStartingPosition() {
        // Clear existing squares so this method can safely be called again.
        for (int r = 0; r < 8; r++) for (int c = 0; c < 8; c++) squares[r][c] = null;
        capturedPieces.clear();

        // Black back row and pawns.
        squares[0][0] = new Rook(Color.BLACK, new Position(0,0));
        squares[0][1] = new Knight(Color.BLACK, new Position(0,1));
        squares[0][2] = new Bishop(Color.BLACK, new Position(0,2));
        squares[0][3] = new Queen(Color.BLACK, new Position(0,3));
        squares[0][4] = new King(Color.BLACK, new Position(0,4));
        squares[0][5] = new Bishop(Color.BLACK, new Position(0,5));
        squares[0][6] = new Knight(Color.BLACK, new Position(0,6));
        squares[0][7] = new Rook(Color.BLACK, new Position(0,7));
        for (int c = 0; c < 8; c++) squares[1][c] = new Pawn(Color.BLACK, new Position(1,c));

        // White pawns and back row.
        for (int c = 0; c < 8; c++) squares[6][c] = new Pawn(Color.WHITE, new Position(6,c));
        squares[7][0] = new Rook(Color.WHITE, new Position(7,0));
        squares[7][1] = new Knight(Color.WHITE, new Position(7,1));
        squares[7][2] = new Bishop(Color.WHITE, new Position(7,2));
        squares[7][3] = new Queen(Color.WHITE, new Position(7,3));
        squares[7][4] = new King(Color.WHITE, new Position(7,4));
        squares[7][5] = new Bishop(Color.WHITE, new Position(7,5));
        squares[7][6] = new Knight(Color.WHITE, new Position(7,6));
        squares[7][7] = new Rook(Color.WHITE, new Position(7,7));
    }

    /** Returns the piece on a square, or null if the square is empty. */
    public Piece getPiece(Position position) { return squares[position.getRow()][position.getColumn()]; }

    /** Returns a copy of the captured-piece list. */
    public List<Piece> getCapturedPieces() { return new ArrayList<>(capturedPieces); }

    /**
     * Validates and performs a move.
     * @return null if successful, otherwise a human-readable error message
     */
    public String movePiece(Position from, Position to, Color currentTurn) {
        Piece moving = getPiece(from);
        Piece target = getPiece(to);
        if (moving == null) return "There is no piece on " + from + ".";
        if (moving.getColor() != currentTurn) return "It is " + currentTurn + "'s turn; choose your own piece.";
        if (target != null && target.getColor() == currentTurn) return "You cannot capture your own piece.";
        if (!moving.possibleMoves(this).contains(to)) return "That piece cannot move from " + from + " to " + to + ".";

        // Record captures before replacing the destination square.
        if (target != null) capturedPieces.add(target);
        squares[to.getRow()][to.getColumn()] = moving;
        squares[from.getRow()][from.getColumn()] = null;
        moving.setPosition(to);
        return null;
    }

    /** Prints the board with file letters and rank numbers. */
    public void display() {
        System.out.println("    A   B   C   D   E   F   G   H");
        System.out.println("  +---+---+---+---+---+---+---+---+");
        // Row zero is rank 8, so print the array from top to bottom.
        for (int row = 0; row < 8; row++) {
            System.out.print((8 - row) + " |");
            for (int col = 0; col < 8; col++) {
                Piece piece = squares[row][col];
                String symbol = piece == null ? "##" : piece.getSymbol();
                System.out.print(" " + symbol + "|");
            }
            System.out.println(" " + (8 - row));
            System.out.println("  +---+---+---+---+---+---+---+---+");
        }
        System.out.println("    A   B   C   D   E   F   G   H");
    }
}
