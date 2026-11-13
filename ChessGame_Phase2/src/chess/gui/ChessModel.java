package chess.gui;

import chess.pieces.*;
import chess.position.Position;
import java.util.ArrayList;
import java.util.List;

/** Phase 2 board state. Uses the Phase 1 piece classes, but intentionally allows any destination. */
public final class ChessModel {
    private final Piece[][] squares = new Piece[8][8];
    private final List<Move> history = new ArrayList<>();
    private Color turn = Color.WHITE;
    private boolean finished;

    /** A completed move with enough data to restore a captured piece on undo. */
    public record Move(Piece piece, Piece captured, int fromRow, int fromCol, int toRow, int toCol) {
        /** @return human-readable algebraic squares for history display */
        public String description() {
            return pieceName(piece) + " " + square(fromRow, fromCol) + " → " + square(toRow, toCol)
                + (captured == null ? "" : "  × " + pieceName(captured));
        }
    }

    /** Set up all 32 pieces and reset the turn and history. */
    public void newGame() {
        for (int r = 0; r < 8; r++) for (int c = 0; c < 8; c++) squares[r][c] = null;
        history.clear(); turn = Color.WHITE; finished = false;
        for (int c = 0; c < 8; c++) {
            squares[1][c] = new Pawn(Color.BLACK, new Position(1, c));
            squares[6][c] = new Pawn(Color.WHITE, new Position(6, c));
        }
        for (int c = 0; c < 8; c++) {
            squares[0][c] = backPiece(Color.BLACK, 0, c);
            squares[7][c] = backPiece(Color.WHITE, 7, c);
        }
    }

    /** Builds a model with the standard opening arrangement. */
    public ChessModel() { newGame(); }

    private static Piece backPiece(Color color, int r, int c) {
        Position p = new Position(r, c);
        return switch (c) {
            case 0, 7 -> new Rook(color, p);
            case 1, 6 -> new Knight(color, p);
            case 2, 5 -> new Bishop(color, p);
            case 3 -> new Queen(color, p);
            default -> new King(color, p);
        };
    }

    /** @return piece occupying a board square, or null */
    public Piece pieceAt(int row, int col) { return squares[row][col]; }
    /** @return side to move */
    public Color turn() { return turn; }
    /** @return whether a king has been captured */
    public boolean isFinished() { return finished; }
    /** @return copy of completed moves */
    public List<Move> history() { return List.copyOf(history); }

    /**
     * Move a piece to any other square, except one occupied by a friendly piece.
     * Turn order is enforced; Phase 2 does not validate normal chess movement patterns.
     * @return error message, or null for a successful move
     */
    public String move(int fromRow, int fromCol, int toRow, int toCol) {
        if (finished) return "The game is over. Choose New Game to continue.";
        if (!valid(fromRow, fromCol) || !valid(toRow, toCol)) return "Invalid square.";
        if (fromRow == toRow && fromCol == toCol) return "Choose a different destination.";
        Piece piece = squares[fromRow][fromCol], target = squares[toRow][toCol];
        if (piece == null) return "Choose a piece to move.";
        if (piece.getColor() != turn) return "It's " + name(turn) + "'s turn.";
        if (target != null && target.getColor() == turn) return "Cannot capture your own piece.";
        history.add(new Move(piece, target, fromRow, fromCol, toRow, toCol));
        squares[fromRow][fromCol] = null;
        squares[toRow][toCol] = piece;
        piece.setPosition(new Position(toRow, toCol));
        if (target instanceof King) finished = true;
        else turn = turn.opposite();
        return null;
    }

    /** Undo the last move, restoring its captured piece and the previous turn. */
    public boolean undo() {
        if (history.isEmpty()) return false;
        Move m = history.remove(history.size() - 1);
        squares[m.fromRow][m.fromCol] = m.piece;
        squares[m.toRow][m.toCol] = m.captured;
        m.piece.setPosition(new Position(m.fromRow, m.fromCol));
        if (m.captured != null) m.captured.setPosition(new Position(m.toRow, m.toCol));
        turn = m.piece.getColor(); finished = false;
        return true;
    }

    /** @return compact text encoding for every square and the player to move */
    public String saveText() {
        StringBuilder out = new StringBuilder("CHESS_GUI_PHASE2_V1\n" + turn + "\n");
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = squares[r][c];
                out.append(p == null ? ".." : p.getSymbol()).append(c == 7 ? '\n' : ' ');
            }
        }
        // Save full move history for Undo / Captured Pieces / history restoration.
        out.append("HISTORY ").append(history.size()).append('\n');
        for (Move m : history) {
            out.append(m.piece.getSymbol()).append(' ')
               .append(m.captured == null ? ".." : m.captured.getSymbol()).append(' ')
               .append(m.fromRow).append(' ').append(m.fromCol).append(' ')
               .append(m.toRow).append(' ').append(m.toCol).append('\n');
        }
        return out.toString();
    }

    /** Restore board, turn, and complete move history from a saved game. */
    public void loadText(String text) {
        String[] lines = text.strip().split("\\R");
        if (lines.length < 11 || !lines[0].equals("CHESS_GUI_PHASE2_V1"))
            throw new IllegalArgumentException("Not a supported Phase 2 save file.");
        Color loadedTurn;
        try { loadedTurn = Color.valueOf(lines[1]); }
        catch (RuntimeException e) { throw new IllegalArgumentException("Invalid player turn."); }
        Piece[][] loaded = new Piece[8][8];
        for (int r = 0; r < 8; r++) {
            String[] tokens = lines[r + 2].trim().split("\\s+");
            if (tokens.length != 8) throw new IllegalArgumentException("Expected eight squares per row.");
            for (int c = 0; c < 8; c++) loaded[r][c] = makePiece(tokens[c], r, c);
        }
        String[] head = lines[10].split(" ");
        if (head.length != 2 || !head[0].equals("HISTORY")) throw new IllegalArgumentException("Missing move history.");
        int count;
        try { count = Integer.parseInt(head[1]); } catch (NumberFormatException e) { throw new IllegalArgumentException("Bad move count."); }
        if (count < 0 || count > 100000 || lines.length != 11 + count) throw new IllegalArgumentException("Incomplete move history.");
        List<Move> loadedHistory = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String[] t = lines[11 + i].split("\\s+");
            if (t.length != 6) throw new IllegalArgumentException("Invalid saved move.");
            try {
                int fr = Integer.parseInt(t[2]), fc = Integer.parseInt(t[3]);
                int tr = Integer.parseInt(t[4]), tc = Integer.parseInt(t[5]);
                if (!valid(fr, fc) || !valid(tr, tc)) throw new IllegalArgumentException("Invalid move coordinates.");
                loadedHistory.add(new Move(makePiece(t[0], tr, tc), makePiece(t[1], tr, tc), fr, fc, tr, tc));
            } catch (NumberFormatException e) { throw new IllegalArgumentException("Invalid saved move coordinates."); }
        }
        // Only replace live game once the save text has been parsed fully.
        for (int r = 0; r < 8; r++) System.arraycopy(loaded[r], 0, squares[r], 0, 8);
        history.clear(); history.addAll(loadedHistory);
        turn = loadedTurn; finished = !hasKing(Color.WHITE) || !hasKing(Color.BLACK);
    }

    private boolean hasKing(Color color) {
        for (Piece[] row : squares) for (Piece p : row)
            if (p instanceof King && p.getColor() == color) return true;
        return false;
    }

    private static Piece makePiece(String symbol, int r, int c) {
        if (symbol.equals("..")) return null;
        if (!symbol.matches("[wb][pRNBQK]")) throw new IllegalArgumentException("Invalid piece symbol: " + symbol);
        Color color = symbol.charAt(0) == 'w' ? Color.WHITE : Color.BLACK;
        Position pos = new Position(r, c);
        return switch (symbol.charAt(1)) {
            case 'p' -> new Pawn(color, pos);
            case 'R' -> new Rook(color, pos);
            case 'N' -> new Knight(color, pos);
            case 'B' -> new Bishop(color, pos);
            case 'Q' -> new Queen(color, pos);
            default -> new King(color, pos);
        };
    }
    private static boolean valid(int r, int c) { return r >= 0 && r < 8 && c >= 0 && c < 8; }
    /** @return e.g., E2 */
    public static String square(int r, int c) { return "" + (char) ('A' + c) + (8 - r); }
    /** @return English name of the color */
    public static String name(Color c) { return c == Color.WHITE ? "White" : "Black"; }
    /** @return descriptive name of a Phase 1 piece */
    public static String pieceName(Piece p) {
        if (p == null) return "";
        String type = switch (p.getSymbol().charAt(1)) {
            case 'p' -> "Pawn"; case 'R' -> "Rook"; case 'N' -> "Knight";
            case 'B' -> "Bishop"; case 'Q' -> "Queen"; default -> "King";
        };
        return name(p.getColor()) + " " + type;
    }
}
