package gui;

import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import model.PuzzleBoard;

/**
 * an abstract class that serves as the base for the GUI frames of the 15 Puzzle Game.
 * This class contains common functionality shared between NonCustomizedPuzzle class and CustomizedPuzzle class.
 */

public abstract class PuzzleGameFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	protected JPanel puzzleBoardPanel;
    protected PuzzleBoard puzzleBoard;
    protected JButton[][] tileButtons;
    protected static final int NUM_ROWS = 4;
    protected static final int NUM_COLS = 4;
    protected static final int ANIMATION_DELAY = 130;
    
    /**
     * constructor for the PuzzleGameFrame.
     *
     * @param title   The title of the frame.
     * @param numRows The number of rows in the puzzle board.
     * @param numCols The number of columns in the puzzle board.
     */
    public PuzzleGameFrame(String title, int numRows, int numCols) {
        setTitle(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(640, 480);
        setLocationRelativeTo(null);

        puzzleBoard = new PuzzleBoard(numRows, numCols);
        initializeComponents();
        addComponentsToFrame();

        setVisible(true);
    }
    
    protected abstract void initializeComponents();

    protected abstract void addComponentsToFrame();
    
    protected abstract void restartGame();
	
    /**
     * animates the solution to the puzzle.
     *
     * @param solution A list of moves representing the solution.
     */
	protected void animateSolution(List<String> solution) {
		disableTileButtons();
    	
        Thread animationThread = new Thread(() -> {
            for (int step = 0; step < solution.size();step++) {
            	String move = solution.get(step);
                int tileNumber = Integer.parseInt(move.substring(0, move.length() - 1)); 
                String direction = move.substring(move.length() - 1); 
                
                int row = -1;
                int col = -1;
                for (int i = 0; i < puzzleBoard.getNumRows(); i++) {
                    for (int j = 0; j < puzzleBoard.getNumCols(); j++) {
                        if (puzzleBoard.getTileAt(i, j) == tileNumber) {
//                        	System.out.println(puzzleBoard.getTileAt(i, j));
                            row = i;
                            col = j;
                            break;
                        }
                    }
                    if (row != -1 && col != -1) {
                        break; 
                    }
                }
//              puzzleBoard.printBoard();
                
        
                if (direction.equals("L")) {
                    puzzleBoard.moveTile(row, col - 1);
//                    System.out.println("L");
                } else if (direction.equals("R")) {
                    puzzleBoard.moveTile(row, col + 1);
//                    System.out.println("R");
                } else if (direction.equals("U")) {
                    puzzleBoard.moveTile(row - 1, col);
//                    System.out.println("U");
                } else if (direction.equals("D")) {
                    puzzleBoard.moveTile(row + 1, col);
//                    System.out.println("D");
                }

                SwingUtilities.invokeLater(() -> {
                    updateTileButtons();
//                    System.out.println("moveing");
                });

                try {
                    Thread.sleep(ANIMATION_DELAY);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
//                    System.out.println("error");
                }
            }
            SwingUtilities.invokeLater(() -> {
            	checkFinished();
                enableTileButtons();
            });
        });
        
        animationThread.start();
    }
	
    protected void updateTileButtons() {
    	for (int i = 0; i < puzzleBoard.getNumRows(); i++) {
            for (int j = 0; j < puzzleBoard.getNumCols(); j++) {
            	int tile = puzzleBoard.getTileAt(i, j);
                JButton tileButton = tileButtons[i][j];
                tileButton.setVisible(tile != 0);
                tileButton.setText(Integer.toString(tile));
            }
        }
	}

	protected void disableTileButtons() {
		for (int i = 0; i < puzzleBoard.getNumRows(); i++) {
            for (int j = 0; j < puzzleBoard.getNumCols(); j++) {
                tileButtons[i][j].setEnabled(false);
            }
        }
    }


    protected void enableTileButtons() {
    	for (int i = 0; i < puzzleBoard.getNumRows(); i++) {
            for (int j = 0; j < puzzleBoard.getNumCols(); j++) {
                tileButtons[i][j].setEnabled(true);
            }
        }
    }

    /**
     * handles a tile click event at the specified row and column.
     *
     * @param row The row of the clicked tile.
     * @param col The column of the clicked tile.
     */
    protected void handleTileClick(int row, int col) {
    	puzzleBoard.moveTile(row, col);
        updateTileButtons();
        checkFinished();
    }

    protected void checkFinished() {
    	if (puzzleBoard.isSolved()) {

            String message = "Congratulations, you solved the puzzle!";
            String[] options = {"Restart Game", "Quit"};
            
            int choice = JOptionPane.showOptionDialog(
                    this,
                    message,
                    "Congratulations!",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]
            );
            
            if (choice == 0) {
                restartGame();
            } else {
            	goToWelcomeScreen();
            }
        }
    }
   
    /**
     * navigates to the welcome screen and disposes of the current frame.
     */
    protected void goToWelcomeScreen() {
        WelcomeScreen welcomeScreen = new WelcomeScreen();
        welcomeScreen.setVisible(true);
        dispose();
    }

    

}
