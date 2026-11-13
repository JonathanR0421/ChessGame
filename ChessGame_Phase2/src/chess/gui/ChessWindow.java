package chess.gui;

import chess.pieces.Color;
import chess.pieces.Piece;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import javax.swing.*;

/** Java Swing Phase 2 interface: board, mouse controls, menu, settings, and undoable history. */
public final class ChessWindow extends JFrame {
    private final ChessModel game = new ChessModel();
    private final JPanel grid = new JPanel(new GridLayout(8, 8));
    private final JLabel status = new JLabel("White to move");
    private final JTextArea moves = new JTextArea(15, 24);
    private final JTextArea captures = new JTextArea(7, 24);
    private final JButton undo = new JButton("Undo last move");
    private final JButton[][] buttons = new JButton[8][8];
    private final ColorScheme[] themes = {
        new ColorScheme("Classic", new java.awt.Color(240, 217, 181), new java.awt.Color(181, 136, 99)),
        new ColorScheme("Modern gray", new java.awt.Color(224, 228, 232), new java.awt.Color(104, 118, 132)),
        new ColorScheme("Green", new java.awt.Color(238, 238, 210), new java.awt.Color(118, 150, 86))
    };
    private int theme = 0, squareSize = 70, selectedRow = -1, selectedCol = -1;
    private boolean symbols = true;
    private int dragRow = -1, dragCol = -1;
    private Point dragStart;

    private record ColorScheme(String name, java.awt.Color light, java.awt.Color dark) { }

    /** Build a complete playable Phase 2 window with the original Phase 1 piece classes. */
    public ChessWindow() {
        super("Chess Game – Phase 2 (Java Swing)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));
        setJMenuBar(createMenuBar());
        status.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        status.setFont(status.getFont().deriveFont(Font.BOLD, 16));
        add(status, BorderLayout.NORTH);
        grid.setBorder(BorderFactory.createLineBorder(java.awt.Color.DARK_GRAY, 2));
        for (int r = 0; r < 8; r++) for (int c = 0; c < 8; c++) {
            final int row = r, col = c;
            JButton b = new JButton();
            b.setMargin(new Insets(0, 0, 0, 0));
            b.setFocusPainted(false);
            b.setOpaque(true);
            b.setBorderPainted(false);
            b.setFont(new Font("Dialog", Font.PLAIN, 44));
            b.addActionListener(e -> squareClicked(row, col));
            b.addMouseListener(new MouseAdapter() {
                @Override public void mousePressed(MouseEvent e) {
                    dragRow = row; dragCol = col; dragStart = e.getPoint();
                }
                @Override public void mouseReleased(MouseEvent e) {
                    if (dragStart == null) return;
                    Point onGrid = SwingUtilities.convertPoint(b, e.getPoint(), grid);
                    Component dest = grid.getComponentAt(onGrid);
                    if (dest instanceof JButton target && target != b) {
                        for (int rr = 0; rr < 8; rr++) for (int cc = 0; cc < 8; cc++)
                            if (buttons[rr][cc] == target) {
                                performMove(dragRow, dragCol, rr, cc);
                            }
                    }
                    dragStart = null;
                }
            });
            buttons[r][c] = b;
            grid.add(b);
        }
        add(grid, BorderLayout.CENTER);
        moves.setEditable(false); captures.setEditable(false);
        moves.setLineWrap(true); moves.setWrapStyleWord(true);
        captures.setLineWrap(true); captures.setWrapStyleWord(true);
        moves.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        captures.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JPanel sidebar = new JPanel(new BorderLayout(4, 8));
        sidebar.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 10));
        JPanel lists = new JPanel(new BorderLayout(4, 8));
        lists.add(titled("Move history", moves), BorderLayout.CENTER);
        lists.add(titled("Captured pieces", captures), BorderLayout.SOUTH);
        sidebar.add(lists, BorderLayout.CENTER);
        undo.addActionListener(e -> { if (game.undo()) { clearSelection(); redraw(); } });
        sidebar.add(undo, BorderLayout.SOUTH);
        add(sidebar, BorderLayout.EAST);
        redraw(); pack(); setLocationRelativeTo(null);
    }

    private JPanel titled(String title, JTextArea field) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.add(new JScrollPane(field));
        return panel;
    }

    /** Select a piece with first click, then move to the square clicked next. */
    private void squareClicked(int row, int col) {
        if (selectedRow < 0) {
            Piece piece = game.pieceAt(row, col);
            if (piece == null || piece.getColor() != game.turn()) {
                status.setText("Select a " + ChessModel.name(game.turn()) + " piece.");
                return;
            }
            selectedRow = row; selectedCol = col;
            redraw();
        } else if (selectedRow == row && selectedCol == col) {
            clearSelection(); redraw();
        } else {
            performMove(selectedRow, selectedCol, row, col);
        }
    }

    /** Apply a move to the model and display the winner dialog if a king was taken. */
    private void performMove(int fr, int fc, int tr, int tc) {
        if (fr < 0 || game.isFinished()) return;
        Color movingColor = game.turn();
        String error = game.move(fr, fc, tr, tc);
        clearSelection();
        redraw();
        if (error != null) { status.setText(error); return; }
        if (game.isFinished()) {
            status.setText(ChessModel.name(movingColor) + " wins!");
            JOptionPane.showMessageDialog(this, ChessModel.name(movingColor) + " wins! The opponent's King was captured.",
                "Game Over", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            // Ending the application is required by the assignment.
            System.exit(0);
        }
    }

    private void clearSelection() { selectedRow = -1; selectedCol = -1; }

    /** Refresh pieces, colors, move list, captured list, and player status. */
    private void redraw() {
        StringBuilder history = new StringBuilder(), captured = new StringBuilder();
        int index = 1;
        for (ChessModel.Move m : game.history()) {
            history.append(index++).append(". ").append(m.description()).append('\n');
            if (m.captured() != null)
                captured.append(ChessModel.name(m.piece().getColor())).append(" took ")
                    .append(ChessModel.pieceName(m.captured())).append('\n');
        }
        moves.setText(history.toString()); captures.setText(captured.toString());
        moves.setCaretPosition(0); captures.setCaretPosition(0);
        undo.setEnabled(!game.history().isEmpty());
        status.setText(game.isFinished() ? "Game over" : ChessModel.name(game.turn()) + " to move  •  Select a piece, then its destination (or drag)");
        for (int r = 0; r < 8; r++) for (int c = 0; c < 8; c++) {
            JButton b = buttons[r][c];
            b.setPreferredSize(new Dimension(squareSize, squareSize));
            java.awt.Color bg = ((r + c) & 1) == 0 ? themes[theme].light : themes[theme].dark;
            if (r == selectedRow && c == selectedCol) bg = new java.awt.Color(255, 213, 70);
            b.setBackground(bg);
            Piece p = game.pieceAt(r, c);
            b.setText(p == null ? "" : displayPiece(p));
            b.setFont(new Font(symbols ? "DejaVu Sans" : "Dialog", symbols ? Font.PLAIN : Font.BOLD,
                symbols ? (int) (squareSize * .64) : (int) (squareSize * .32)));
            b.setForeground(new java.awt.Color(25, 25, 28));
            b.setToolTipText(p == null ? ChessModel.square(r, c) : ChessModel.square(r, c) + " — " + ChessModel.pieceName(p));
        }
        grid.revalidate(); grid.repaint();
    }

    private String displayPiece(Piece p) {
        String type = p.getSymbol();
        if (!symbols) return type;
        int offset = switch (type.charAt(1)) {
            case 'K' -> 0; case 'Q' -> 1; case 'R' -> 2;
            case 'B' -> 3; case 'N' -> 4; default -> 5;
        };
        // Black Unicode symbols are U+265A..265F; White are U+2654..2659.
        return new String(Character.toChars((p.getColor() == Color.WHITE ? 0x2654 : 0x265A) + offset));
    }

    /** Create New/Save/Load actions and the settings window. */
    private JMenuBar createMenuBar() {
        JMenuBar bar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");
        JMenuItem fresh = new JMenuItem("New Game");
        fresh.addActionListener(e -> { game.newGame(); clearSelection(); redraw(); });
        JMenuItem save = new JMenuItem("Save Game...");
        save.addActionListener(e -> saveGame());
        JMenuItem load = new JMenuItem("Load Game...");
        load.addActionListener(e -> loadGame());
        gameMenu.add(fresh); gameMenu.add(save); gameMenu.add(load);
        JMenu options = new JMenu("Options");
        JMenuItem settings = new JMenuItem("Settings...");
        settings.addActionListener(e -> showSettings());
        options.add(settings); bar.add(gameMenu); bar.add(options);
        return bar;
    }

    private void saveGame() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("chess-save.txt"));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        try {
            Files.writeString(chooser.getSelectedFile().toPath(), game.saveText(), StandardCharsets.UTF_8);
            JOptionPane.showMessageDialog(this, "Game saved successfully.");
        } catch (IOException e) { showError("Could not save: " + e.getMessage()); }
    }

    private void loadGame() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
        try {
            game.loadText(Files.readString(chooser.getSelectedFile().toPath(), StandardCharsets.UTF_8));
            clearSelection(); redraw();
        } catch (IOException | IllegalArgumentException e) { showError("Could not load: " + e.getMessage()); }
    }

    private void showError(String message) { JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE); }

    /** Allow on-the-fly theme, chess-piece appearance, and board-size updates. */
    private void showSettings() {
        JComboBox<String> color = new JComboBox<>(new String[] {"Classic", "Modern gray", "Green"});
        color.setSelectedIndex(theme);
        JComboBox<String> style = new JComboBox<>(new String[] {"Unicode chess symbols", "Letter symbols (wP / bK)"});
        style.setSelectedIndex(symbols ? 0 : 1);
        JComboBox<String> size = new JComboBox<>(new String[] {"Small", "Medium", "Large"});
        size.setSelectedIndex(squareSize == 54 ? 0 : squareSize == 70 ? 1 : 2);
        JPanel panel = new JPanel(new GridLayout(3, 2, 8, 8));
        panel.add(new JLabel("Board style:")); panel.add(color);
        panel.add(new JLabel("Piece style:")); panel.add(style);
        panel.add(new JLabel("Board size:")); panel.add(size);
        if (JOptionPane.showConfirmDialog(this, panel, "Settings — Apply changes", JOptionPane.OK_CANCEL_OPTION)
            == JOptionPane.OK_OPTION) {
            theme = color.getSelectedIndex(); symbols = style.getSelectedIndex() == 0;
            squareSize = switch (size.getSelectedIndex()) { case 0 -> 54; case 2 -> 84; default -> 70; };
            redraw(); pack(); setLocationRelativeTo(null);
        }
    }

    /** Launch the GUI safely on the Swing event-dispatch thread. */
    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new ChessWindow().setVisible(true)); }
}
