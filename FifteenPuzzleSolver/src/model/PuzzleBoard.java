package model;

import java.util.Arrays;
import java.util.Random;


/**
 * represents a puzzle board with tiles.
 */
public class PuzzleBoard implements Comparable<PuzzleBoard> {
	private int[][] board;
	private int numRows;
    private int numCols;
    private int blankRow;
    private int blankCol;
	
	/**
     * constructs a puzzle board with the given number of rows and columns.
     *
     * @param numRows Number of rows in the board.
     * @param numCols Number of columns in the board.
     */
	public PuzzleBoard(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        initializeBoard();
    }
	
	public PuzzleBoard(int[][] customBoard) {
		this.board = customBoard;
		this.numRows = 4;
		this.numCols = 4;
		this.blankRow = this.getEmptyTileRow();
		this.blankCol = this.getEmptyTileCol();
	}
	
    public int getBlankRow() {
		return this.blankRow;
	}

	public void setBlankRow(int blankRow) {
		this.blankRow = blankRow;
	}

	public int getBlankCol() {
		return this.blankCol;
	}

	public void setBlankCol(int blankCol) {
		this.blankCol = blankCol;
	}
	
	public int getNumRows() {
		// TODO Auto-generated method stub
		return this.numRows;
	}
	
	public int getNumCols() {
		// TODO Auto-generated method stub
		return this.numCols;
	}
	
	
	 /**
     * initializes the board by creating tiles and shuffling them.
     */
	//create the board,re-generate if it is not solvable
	private void initializeBoard() {
	    do {
	        board = new int[numRows][numCols];
	        int count = 1;
	        for (int i = 0; i < numRows; i++) {
	            for (int j = 0; j < numCols; j++) {
	                board[i][j] = count;
	                count = (count + 1) % (numRows * numCols);
	            }
	        }

	        setBlankRow(numRows - 1);
	        setBlankCol(numCols - 1);
	        shuffleBoard();
	    } while (!isSolvable()); // Re-generate if not solvable
	}
	
	//shuffle it using Fisher-Yates algorithm
	public void shuffleBoard() {
		int totalTiles = numRows * numCols;
	    int[] tileValues = new int[totalTiles];
	    int count = 1;
	    
	    // fill an array with tile values, excluding the empty tile value
	    for (int i = 0; i < totalTiles - 1; i++) {
	        tileValues[i] = count++;
	    }
	    tileValues[totalTiles - 1] = 0; // Set the last value as the empty tile
	    
	    // shuffle the array using Fisher-Yates algorithm
	    Random random = new Random();
	    for (int i = totalTiles - 1; i > 0; i--) {
	        int j = random.nextInt(i + 1);
	        
	        int temp = tileValues[i];
	        tileValues[i] = tileValues[j];
	        tileValues[j] = temp;
	    }
	    
	    // assign shuffled tile values to the board
	    int index = 0;
	    for (int i = 0; i < numRows; i++) {
	        for (int j = 0; j < numCols; j++) {
	            int tileValue = tileValues[index++];
	            board[i][j] = tileValue;
	            
	            if (tileValue == 0) {
	                setBlankRow(i);
	                setBlankCol(j);
	            }
	        }
	    }
    }
	/**
     * checks if a tile can be moved to the blank position.
     *
     * @param row Row of the tile.
     * @param col Column of the tile.
     * @return True if the tile can be moved, false otherwise.
     */
	public boolean canMoveTile(int row, int col) {
	    return (Math.abs(row - blankRow) == 1 && col == blankCol) ||
	           (Math.abs(col - blankCol) == 1 && row == blankRow);
	}
    
	//swapping tiles
	public void swapTiles(int row1, int col1, int row2, int col2) {
		int temp = board[row1][col1];
        board[row1][col1] = board[row2][col2];
        board[row2][col2] = temp;
    }
	
	//moving tiles
	public void moveTile(int row, int col) {
	    if (canMoveTile(row, col)) {
	        swapTiles(row, col, blankRow, blankCol);
	        blankRow = row;
	        blankCol = col;
	    }
	}
    
	/**
     * checks if the puzzle is solvable.
     *
     * @return True if the puzzle is solvable, false otherwise.
     */
	public boolean isSolvable() {
        int inversionCount = 0;
        int[] tiles = new int[numRows * numCols];
        int k = 0;

        // convert the 2D array to a 1D array for ease of counting inversions
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                tiles[k++] = board[i][j];
            }
        }

        for (int i = 0; i < tiles.length; i++) {
            if (tiles[i] == 0) {
                continue;
            }
            for (int j = i + 1; j < tiles.length; j++) {
                if (tiles[j] == 0) {
                    continue;
                }
                if (tiles[i] > tiles[j]) {
                    inversionCount++;
                }
            }
        }

        if (numRows % 2 == 0) {
            // For even grid sizes, add the row number of the empty tile
            inversionCount += getEmptyTileRow() + 1;
        }

        // Check if the inversion count is even
        return inversionCount % 2 == 0;
    }
	
	
	/**
     * Checks if the puzzle is solved.
     *
     * @return True if the puzzle is solved, false otherwise.
     */
	public boolean isSolved() {
	    int expectedValue = 1;
	    for (int i = 0; i < numRows; i++) {
	        for (int j = 0; j < numCols; j++) {
	            if (board[i][j] != expectedValue) {
	                // If any tile's value is not as expected, puzzle is not solved
	                return false;
	            }
	            expectedValue = (expectedValue + 1) % (numRows * numCols);
	        }
	    }
	    return true;
	}
	
	
	/**
     * gets the tile at the specified position.
     *
     * @param i Row index.
     * @param j Column index.
     * @return The tile at the specified position.
     * @throws IllegalArgumentException If the position is invalid.
     */
	public int getTileAt(int i, int j) {
		// TODO Auto-generated method stub
		if (isValidPosition(i, j)) {
	        return board[i][j];
	    } else {
	        throw new IllegalArgumentException("Invalid row or column index.");
	    }
	}
	
	public int getEmptyTileRow() {
        // implement this method to find the row of the empty tile
		for (int i = 0; i < numRows; i++) {
		     for (int j = 0; j < numCols; j++) {
		          if (board[i][j] == 0) {
		              return i;
		        }
		     }
		 }
		    return -1; 
    }

    public int getEmptyTileCol() {
		for (int i = 0; i < numRows; i++) {
	        for (int j = 0; j < numCols; j++) {
	            if (board[i][j] == 0) {
	                return j;
	            }
	        }
	    }
	    return -1; 
    }
	
	
	/**
     * Checks if a position is valid on the board.
     *
     * @param i Row index.
     * @param j Column index.
     * @return True if the position is valid, false otherwise.
     */
	private boolean isValidPosition(int i, int j) {
	    return i >= 0 && i < numRows && j >= 0 && j < numCols;
	}
	
	
	/**
	 * calculate manhattan distance
	 */
	public int calculateManhattanDistance() {
	    int distance = 0;
	    for (int i = 0; i < numRows; i++) {
	        for (int j = 0; j < numCols; j++) {
	            int value = board[i][j];
	            if (value != 0) {
	                int targetRow = (value - 1) / numCols;
	                int targetCol = (value - 1) % numCols;
	                distance += Math.abs(i - targetRow) + Math.abs(j - targetCol);

	                if (i == targetRow) {
	                    for (int k = j + 1; k < numCols; k++) {
	                        int otherValue = board[i][k];
	                        if (otherValue != 0 && (otherValue - 1) / numCols == i && value > otherValue) {
	                            distance += 2;
	                        }
	                    }
	                }
	                if (j == targetCol) {
	                    for (int k = i + 1; k < numRows; k++) {
	                        int otherValue = board[k][j];
	                        if (otherValue != 0 && (otherValue - 1) % numCols == j && value > otherValue) {
	                            distance += 2;
	                        }
	                    }
	                }
	            }
	        }
	    }
	    return distance;
	}
	

	/**
	 * @Overriden methods
	 */
	@Override
	public int compareTo(PuzzleBoard other) {
		// TODO Auto-generated method stub
		return Integer.compare(this.calculateManhattanDistance(), other.calculateManhattanDistance());
	}
	
	@Override
	public int hashCode() {
	    return Arrays.deepHashCode(board);
	}
	
	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numRows; i++) {
            sb.append(Arrays.toString(board[i])).append("\n");
        }
        return sb.toString();
    }
	
	@Override
	public boolean equals(Object o) {
	    if (this == o) return true;
	    if (o == null || getClass() != o.getClass()) return false;
	    PuzzleBoard other = (PuzzleBoard) o;
	    return Arrays.deepEquals(board, other.board); 
	}
	
	
	
	/**
	 * generating final board
	 */
	public static PuzzleBoard generateGoalBoard(int numRows, int numCols) {
		// TODO Auto-generated method stub
		PuzzleBoard goalBoard = new PuzzleBoard(numRows, numCols);
        int value = 1;
        
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                goalBoard.setTileValue(row, col, value);
                value = (value + 1) % (numRows * numCols);
            }
        }
        return goalBoard;
	}
	
	/**
	 * copy the board and set tile value in the board
	 */
	public void setTileValue(int row, int col, int value) {
        board[row][col] = value;
    }
	
	public PuzzleBoard copy() {
	    int numRows = this.getNumRows();
	    int numCols = this.getNumCols();
	    PuzzleBoard copy = new PuzzleBoard(numRows, numCols);

	    for (int i = 0; i < numRows; i++) {
	        for (int j = 0; j < numCols; j++) {
	            copy.setTileValue(i, j, this.getTileAt(i, j));
	        }
	    }

	    return copy;
	}
	
	/**
	 * checking methods
	 */
	public void printBoard(){
		for(int i = 0; i < numRows;i++) {
			for(int j = 0;j<numRows;j++) {
				System.out.print(board[i][j] + " ");
				if(j == 3)
					System.out.println();
			}
		}
		System.out.println();
	}

	
}
