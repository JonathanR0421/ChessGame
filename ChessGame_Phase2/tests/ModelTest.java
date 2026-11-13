import chess.gui.ChessModel;
import chess.pieces.Color;

/** Runnable no-dependency smoke and regression checks for Phase 2 model. */
public final class ModelTest {
    private static void ok(boolean test, String label) {
        if (!test) throw new AssertionError(label);
        System.out.println("PASS " + label);
    }
    public static void main(String[] args) {
        ChessModel m = new ChessModel();
        int count = 0;
        for (int r = 0; r < 8; r++) for (int c = 0; c < 8; c++) if (m.pieceAt(r, c) != null) count++;
        ok(count == 32 && m.turn() == Color.WHITE, "standard starting board");
        ok(m.move(6, 4, 4, 4) == null && m.turn() == Color.BLACK, "white moves E2-E4");
        ok(m.move(6, 3, 4, 3) != null, "wrong turn is rejected");
        ok(m.move(1, 3, 4, 4) == null && m.history().get(1).captured() != null, "black captures white pawn on E4");
        ok(m.undo() && m.pieceAt(4, 4) != null && m.pieceAt(1, 3) != null && m.turn() == Color.BLACK, "undo restores capture and player");
        ok(m.move(1, 3, 4, 3) == null, "new black move");
        String save = m.saveText();
        ChessModel loaded = new ChessModel();
        loaded.loadText(save);
        ok(loaded.saveText().equals(save), "save/load exact round trip");
        ok(loaded.undo() && loaded.pieceAt(1, 3) != null && loaded.turn() == Color.BLACK, "undo still works after loading");
        loaded.newGame();
        ok(loaded.history().isEmpty() && loaded.turn() == Color.WHITE, "new game resets all data");
        ok(loaded.move(6, 4, 0, 4) == null && loaded.isFinished(), "king capture declares finish");
        boolean rejected = false;
        try { loaded.loadText("broken"); } catch (IllegalArgumentException ex) { rejected = true; }
        ok(rejected && loaded.isFinished(), "bad save rejected without changing board");
        System.out.println("ALL TESTS PASSED");
    }
}
