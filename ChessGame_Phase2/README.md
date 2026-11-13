# Chess Game — Phase 2 (Swing GUI)

**Course:** CS 3354  |  **Phase 2 contribution:** GUI and additional features

## What is implemented

- Standard 8x8 two-color chessboard, using the original Phase 1 `chess.pieces` classes
- All 32 standard opening pieces rendered with Unicode chess symbols (or letters)
- Click a piece and click a destination, **or** drag it to another square
- Capture enemy pieces; friendly captures prohibited; alternate White/Black turns
- Capture a king to display a winner dialog and close the game
- **Extra feature 1:** Game menu with New Game, Save Game, Load Game
- **Extra feature 2:** Options > Settings with board colors, piece symbols, and size
- **Extra feature 3:** Move history, captured pieces panel, and Undo
- All source code is under `ChessGame_Phase2/src` and the original Phase 1 code is left unchanged

**Phase 2 game rules:** The instructor expressly says full chess movement validation is unnecessary. Players may therefore move any piece to any other square (except onto a friendly piece). This phase retains player turns but does not implement check/checkmate, legal chess paths, castling, en passant, or promotion.

## Start on Windows (Java 21)

1. Extract the ZIP and open the `ChessGame-main` folder. Keep `ChessGame_Phase1` and `ChessGame_Phase2` next to each other.
2. Ensure Java JDK 21 is installed; check `java -version` and `javac -version`.
3. Double-click `ChessGame_Phase2/run.bat` or in Command Prompt run:
   ```bat
   cd ChessGame-main
   ChessGame_Phase2\run.bat
   ```
4. Select a white piece and click its destination, or drag it. Players take turns.

On macOS/Linux from the `ChessGame-main` folder: `bash ChessGame_Phase2/run.sh`

In VS Code: open **ChessGame-main** as your workspace and run `chess.gui.ChessWindow`, with both phase source folders on the Java source path. The scripts above are simpler if VS Code import is not set up.

## Test the required screenshots

- **Start:** run game; board displays 32 pieces in 8x8 opening position.
- **Moves and capture:** click E2 then E4; click D7 then E4 to capture the white pawn. The side panel displays Black's capture. Additional moves: White D1 to D5, Black A7 to A5.
- **Winner popup:** after the above moves, click White queen D5 then Black king E8. Because movement is intentionally unrestricted in Phase 2, White immediately captures the King; the winner dialog appears. **Capture a screenshot BEFORE clicking OK** (the game exits after OK).
- **Game controls:** Game > Save Game... writes `.txt`; Game > Load Game... restores exact board, turn, history; Game > New Game resets everything.
- **Settings:** Options > Settings... choose board theme, pieces, size and click OK to apply (the OK button is the Apply action).
- **Undo:** click Undo last move after a capture; the captured piece reappears, the turn reverts, and history updates.

## Tests

In the top-level folder:
```bash
javac -encoding UTF-8 -d ChessGame_Phase2/out $(find ChessGame_Phase1/src ChessGame_Phase2/src ChessGame_Phase2/tests -name '*.java')
java -cp ChessGame_Phase2/out ModelTest
```
On Windows, you can run the game using the batch script and perform the manual tests above; the testing command is for macOS/Linux shells.

## Notes for submission

Submit **(1)** the shared GitHub repository URL showing the Phase 2 folder and **(2)** the final PDF report. See `TEAMMATE_HANDOFF.md` and `SUBMISSION_CHECKLIST.md`. A demonstration PDF with tested screenshots is supplied separately; both team members should verify behavior on their own machine before submission.
