# Phase 1 Testing

Tester: ishark8

## Tests

1. Check that the chessboard displays correctly.
2. Test E2 E4.
3. Test E7 E5.
4. Test invalid input such as HELLO.
5. Test invalid coordinates such as E2 E9.
6. Confirm invalid moves do not change the turn.
7. Confirm quit exits the program.

## Results

1. **PASS** - The chessboard displayed correctly with all 32 pieces in their starting positions.

2. **PASS** - E2 E4 successfully moved the white pawn from E2 to E4. The turn changed to Black.

3. **PASS** - E7 E5 successfully moved the black pawn from E7 to E5. The turn changed to White.

4. **PASS** - Entering HELLO displayed "Invalid format. Example: E2 E4". The game continued without crashing, and the turn remained White.

5. **PASS** - Entering E2 E9 displayed "Invalid coordinate: Coordinate must use files A-H and ranks 1-8." The board remained unchanged, and White kept the turn.

6. **PASS** - Attempting E7 E6 during White's turn displayed "Invalid move: It is WHITE's turn; choose your own piece." The board remained unchanged, and White kept the turn.

7. **PASS** - Entering quit displayed "Game ended. Thanks for playing!" and successfully exited the program, returning to PowerShell.

## Summary

- Total tests: 7
- Passed: 7
- Failed: 0

All seven manual tests passed successfully. The chess program displayed the board correctly, accepted valid moves, rejected invalid inputs, maintained the correct turn, and exited properly.

Phase 1 manual testing completed by ishark8 on October 25, 2026.