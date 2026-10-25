# Phase 1 Plan — Console Chess



## Team branches

- Repository owner: `JonathanR0421`
- Member 1: `JonathanR0421`
- Member 2: `feature/REPLACE_WITH_USERNAME`

Use the instructor-required branches: `main`, `dev`, and one `feature/<github-username>` branch per member.

## Milestone 1 — Project setup and core model

**Tasks**
- Create Java packages and repository documentation.
- Implement `Position`, `Color`, and abstract `Piece`.
- Add Javadoc comments and confirm the project compiles.

**Manual tests**
- Convert `E2` to row 6, column 4 and back to `E2`.
- Confirm invalid coordinates such as `Z9` are rejected.
- Compile the project from a clean checkout.

## Milestone 2 — Piece subclasses

**Tasks**
- Implement `Pawn`, `Rook`, `Knight`, `Bishop`, `Queen`, and `King`.
- Add movement patterns and display symbols.

**Manual tests**
- Test knight jumps.
- Test that sliding pieces stop at blocking pieces.
- Test pawn forward moves and diagonal captures.
- Confirm pieces cannot capture their own color.

## Milestone 3 — Board setup and display

**Tasks**
- Implement the 8x8 board and all 32 starting pieces.
- Display ranks, files, and piece symbols.
- Implement piece lookup, movement, and captured-piece tracking.

**Manual tests**
- Confirm each side has 16 pieces in the correct starting squares.
- Print the board and verify ranks 8-1 and files A-H.

## Milestone 4 — Console interface and validation

**Tasks**
- Accept moves using `FROM TO` notation.
- Validate coordinates, player ownership, movement patterns, and friendly captures.
- Switch turns only after successful moves.
- Finish README, Javadoc, and individual AI notes.

**Manual tests**
- Try `E2 E4`, malformed input, invalid coordinates, empty source squares, and wrong-color moves.
- Confirm invalid moves do not change the board or turn.
- Confirm `quit` exits the game.
- Compile and launch from a clean checkout.

## Current limitations

- Check, checkmate, and stalemate are not implemented.
- Castling, en passant, and pawn promotion are not implemented.
- The game does not prevent moves that leave a king in check.
