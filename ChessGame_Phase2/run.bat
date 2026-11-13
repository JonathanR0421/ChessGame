@echo off
setlocal
cd /d "%~dp0.."
if not exist "ChessGame_Phase2\out" mkdir "ChessGame_Phase2\out"
echo Compiling Phase 1 and Phase 2 Java files...
javac -encoding UTF-8 -d "ChessGame_Phase2\out" "ChessGame_Phase1\src\chess\board\Board.java" "ChessGame_Phase1\src\chess\Main.java" "ChessGame_Phase1\src\chess\position\Position.java" "ChessGame_Phase1\src\chess\game\Game.java" "ChessGame_Phase1\src\chess\game\Player.java" "ChessGame_Phase1\src\chess\pieces\Bishop.java" "ChessGame_Phase1\src\chess\pieces\Knight.java" "ChessGame_Phase1\src\chess\pieces\Rook.java" "ChessGame_Phase1\src\chess\pieces\Piece.java" "ChessGame_Phase1\src\chess\pieces\Queen.java" "ChessGame_Phase1\src\chess\pieces\Color.java" "ChessGame_Phase1\src\chess\pieces\Pawn.java" "ChessGame_Phase1\src\chess\pieces\King.java" "ChessGame_Phase2\src\chess\gui\ChessModel.java" "ChessGame_Phase2\src\chess\gui\ChessWindow.java"
if errorlevel 1 (
  echo Compilation failed. Please send the error message.
  pause
  exit /b 1
)
echo Starting Chess Game Phase 2...
java -cp "ChessGame_Phase2\out" chess.gui.ChessWindow
pause
