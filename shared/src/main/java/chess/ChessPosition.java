package chess;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    private final int row;
    private final int col;

    static Map<String, Integer> charToColMap = new HashMap<String, Integer>();

    String colLabels = "abcdefgh";

    static {
        charToColMap.put("a", 1);
        charToColMap.put("b", 2);
        charToColMap.put("c", 3);
        charToColMap.put("d", 4);
        charToColMap.put("e", 5);
        charToColMap.put("f", 6);
        charToColMap.put("g", 7);
        charToColMap.put("h", 8);
    }

    public ChessPosition(int row, int col) {
        this.col = col;
        this.row = row;
    }

    @Override
    public String toString() {
        return String.valueOf(colLabels.charAt(col)) + row;
    }

    public static ChessPosition fromString(String input) {
        if (input == null) {
            throw new IllegalArgumentException("input cannot be null");
        }
        String loweredInput = input.toLowerCase();
        if (!Character.isAlphabetic(loweredInput.charAt(0))) {
            throw new IllegalArgumentException("the column must be a letter");
        }
        Integer col = charToColMap.get(Character.toString(loweredInput.charAt(0)));
        if (col == null) {
            throw new IllegalArgumentException("the column must be in the range a through h");
        }

        char rowChar = loweredInput.charAt(1);
        if (!Character.isDigit(rowChar)) {
            throw new IllegalArgumentException("the row must be a number");
        }
        int row = Character.getNumericValue(rowChar);
        if (row < 1 || row > 8) {
            throw new IllegalArgumentException("the row must be between 1-8");
        }

        return new ChessPosition(row, col);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) { return false; }
        ChessPosition that = (ChessPosition) o;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return this.row;
    }

    public ChessPosition offsetRowBy(int offset) {
        return new ChessPosition(this.row + offset, this.col);
    }


    public ChessPosition offsetColBy(int offset) {
        return new ChessPosition(this.row, this.col + offset);
    }

    public ChessPosition offsetPosBy(int row, int col) {
        return new ChessPosition(this.row + row, this.col + col);
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return this.col;
    }
}
