#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
mkdir -p ChessGame_Phase2/out
javac -encoding UTF-8 -d ChessGame_Phase2/out $(find ChessGame_Phase1/src ChessGame_Phase2/src -name '*.java')
java -cp ChessGame_Phase2/out chess.gui.ChessWindow
