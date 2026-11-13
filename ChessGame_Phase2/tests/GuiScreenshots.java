import chess.gui.ChessWindow;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/** Takes genuine screenshots of GUI interactions using a virtual display. */
public class GuiScreenshots {
    private static ChessWindow window;
    private static JButton[][] squares = new JButton[8][8];
    private static void click(int fr, int fc, int tr, int tc) throws Exception {
        SwingUtilities.invokeAndWait(() -> { squares[fr][fc].doClick(); squares[tr][tc].doClick(); });
    }
    private static void snapshot(String name) throws Exception {
        Thread.sleep(450);
        Rectangle r = window.getBounds();
        Point p = window.getLocationOnScreen();
        BufferedImage bi = new Robot().createScreenCapture(new Rectangle(p.x, p.y, r.width, r.height));
        ImageIO.write(bi, "png", new File(name));
        System.out.println("SCREENSHOT " + name);
    }
    public static void main(String[] args) throws Exception {
        System.setProperty("java.awt.headless", "false");
        SwingUtilities.invokeAndWait(() -> { window = new ChessWindow(); window.setLocation(30, 30); window.setVisible(true);
            JPanel grid = null;
            for (Component comp : window.getContentPane().getComponents())
                if (comp instanceof JPanel && ((JPanel)comp).getLayout() instanceof GridLayout) grid = (JPanel)comp;
            for (int r = 0; r < 8; r++) for (int c = 0; c < 8; c++) squares[r][c] = (JButton)grid.getComponent(r*8+c);
        });
        snapshot(args[0]+"/01_start.png");
        SwingUtilities.invokeAndWait(() -> window.getJMenuBar().getMenu(0).doClick());
        Thread.sleep(200); snapshot(args[0]+"/05_game_menu.png");
        Robot rr = new Robot(); rr.keyPress(java.awt.event.KeyEvent.VK_ESCAPE); rr.keyRelease(java.awt.event.KeyEvent.VK_ESCAPE);
        click(6,4,4,4); // E2 E4 white pawn
        click(1,3,4,4); // D7 E4 black pawn captures
        click(7,3,3,3); // D1 D5 white queen
        click(1,0,3,0); // A7 A5 black pawn
        snapshot(args[0]+"/02_moves_captures.png");
        // Show undo on a previously captured black pawn.
        SwingUtilities.invokeAndWait(() -> {
           for (Component c : window.getContentPane().getComponents()) {
             if (c instanceof JPanel && ((JPanel)c).getLayout() instanceof BorderLayout) {
               for (Component b : ((JPanel)c).getComponents()) if (b instanceof JButton && ((JButton)b).getText().startsWith("Undo")) ((JButton)b).doClick();
             }
           }
        });
        snapshot(args[0]+"/06_undo.png");
        click(1,0,3,0); // Reapply black pawn A7-A5 for winner steps? (Undo returns Black turn)
        // For settings capture, use menu and dialog as a real GUI screenshot.
        SwingUtilities.invokeLater(() -> window.getJMenuBar().getMenu(1).getItem(0).doClick());
        Thread.sleep(500); snapshot(args[0]+"/03_settings.png");
        // Close settings via ESC using Robot
        Robot robot = new Robot(); robot.keyPress(java.awt.event.KeyEvent.VK_ESCAPE); robot.keyRelease(java.awt.event.KeyEvent.VK_ESCAPE);
        Thread.sleep(350);
        // White to move. From D5 queen capture black king at E8.
        // The screenshot is taken while the modal winner popup is visible, before program ends.
        SwingUtilities.invokeLater(() -> { squares[3][3].doClick(); squares[0][4].doClick(); });
        Thread.sleep(850);
        snapshot(args[0]+"/04_winner.png");
        System.exit(0);
    }
}
