# Phase 2 — Teammate Handoff

Phase 1 was not changed. Phase 2 is in `ChessGame_Phase2`, with the `ChessWindow` Swing GUI, `ChessModel` data/turn/history/serialization logic, and all three extra features. The GUI reuses the original Phase 1 piece classes.

## Responsibilities for teammate (integration and independent verification)

1. Pull or extract the latest repository and launch `ChessGame_Phase2/run.bat` on Windows with Java 21. Verify no errors.
2. Independently test click-to-move **and real drag-and-drop**. Drag a white pawn to an empty square; drag a black pawn onto it to capture. Report any issues rather than silently changing shared files.
3. Verify the complete interaction path: opening pieces -> multiple moves -> enemy capture -> history -> Undo -> saved game -> load game -> New Game -> Settings. Check all state changes.
4. Check king-capture winner dialog and screenshot it **before pressing OK**, since game closes afterward. Ensure it identifies the capturing player's color.
5. Review readability (piece contrast, board sizing), error cases, and Windows GUI appearance. Fix any discovered issues in a separate Git branch/commit and merge after review.
6. Check the PDF against the instructor requirements, replace screenshots with your own Windows screenshots if requested, confirm both teammates' names, and ensure the GitHub URL points to the updated code.
7. Push or merge the Phase 2 folder to the shared repository and confirm that both team members can access it.

## Suggested Git workflow

From the root of the cloned repository:
```bash
git checkout -b feature/ishark8-phase2-gui
# Copy ChessGame_Phase2 folder here if it is not present yet
git add ChessGame_Phase2
git commit -m "Add Phase 2 Swing chess GUI, game controls, history and settings"
git push -u origin feature/ishark8-phase2-gui
```
Then open a pull request into the team-agreed integration branch (`dev` if the team still uses it). Merge only after both team members run the app. Do not use `git push --force` and do not overwrite existing Phase 1 files.

## Code ownership / contribution note

For the contribution record, `ChessModel.java` and `ChessWindow.java` are included as the Phase 2 GUI implementation package. Both teammates should **agree on an accurate division of work** and list actual authors/contributions in the PDF or PR; do not claim tests/changes that were not personally reviewed. Feel free to split review tasks, bug fixes, README, screenshots, and Git integration.

## Important distinction from Phase 1

Phase 1 validates real chess movement patterns, but the Phase 2 PDF says no piece movement validation is required. The Phase 2 interface does not call Phase 1 `Board.movePiece(...)` and intentionally allows any destination not containing a friendly piece.
