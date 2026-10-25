# Console Chess — Phase 1

A simple two-player, console-based chess project written in Java.

## Requirements

- A Java Development Kit (JDK) installed.
- A terminal or Java IDE such as Visual Studio Code.

## Compile and run on Windows PowerShell

Run from the project root:

```powershell
$files = Get-ChildItem -Recurse src -Filter *.java | ForEach-Object { $_.FullName }
javac -d out $files
java -cp out chess.Main
```

## Compile and run on macOS or Linux

```bash
javac -d out $(find src -name "*.java")
java -cp out chess.Main
```

## How to play

- White moves first.
- Enter moves as two coordinates separated by a space, for example `E2 E4`.
- Coordinates use files A-H and ranks 1-8.
- Type `quit` to exit.

## Implemented

- Abstract `Piece` class and six piece subclasses.
- `Position` class and `Color` enum.
- Standard board setup and console display.
- Basic movement patterns, blocking, and captures.
- Coordinate parsing and format validation.
- Turn switching after successful moves.
- Prevention of moving the opponent's pieces and capturing friendly pieces.
- Captured-piece tracking and Javadoc comments.

## Limitations

- Check, checkmate, and stalemate detection are not implemented.
- Castling, en passant, and pawn promotion are not implemented.
- The game does not prevent a move that leaves a king in check.
- This is a Phase 1 foundation, not a complete tournament-rules chess engine.

## Generate Javadoc

```bash
javadoc -d docs/api -sourcepath src -subpackages chess
```

Generated HTML documentation is placed in `docs/api`.

