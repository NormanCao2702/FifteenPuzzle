package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import logic.AStarSolver;
import logic.SolutionCalculator;
import model.PuzzleBoard;
import unusedClass.CustomBoard;

/**
 * represents a customized puzzle game frame for the 15 Puzzle game.
 * extends the PuzzleGameFrame class.
 */
public class CustomizedPuzzle extends PuzzleGameFrame {
	private static final long serialVersionUID = 1L;
	private int[][] customBoard;
	private JButton solveButton;
	private JButton showSolutionButton;
	
	/**
     * creates a new CustomizedPuzzle instance with the specified title, number of rows, and number of columns.
     * @param title The title of the game frame.
     * @param numRows The number of rows in the puzzle board.
     * @param numCols The number of columns in the puzzle board.
     */
	public CustomizedPuzzle(String title, int numRows, int numCols) {
		super(title, numRows, numCols);
	}

	/**
     * creates a new CustomizedPuzzle instance with default dimensions.
     */
	public CustomizedPuzzle() {
        super("15 Puzzle Custom Board", NUM_ROWS, NUM_COLS);
        customBoard = new int[NUM_ROWS][NUM_COLS];
        disableSolveButtons();
    }


	@Override
	protected void initializeComponents() {
		// create a custom board panel with a GridLayout
				puzzleBoardPanel = new JPanel(new GridLayout(NUM_ROWS, NUM_COLS));
				tileButtons = new JButton[NUM_ROWS][NUM_COLS];
				
			    for (int i = 0; i < NUM_ROWS; i++) {
			        for (int j = 0; j < NUM_COLS; j++) {
			            int row = i;
			            int col = j;

			            JButton tileButton = new JButton();

			            tileButton.addActionListener(new ActionListener() {
			            	@Override
			                public void actionPerformed(ActionEvent e) {
			                    if (!tileButton.getText().isEmpty()) {
			                        customBoard[row][col] = 0;
			                        tileButton.setText(""); 
			                    } else {
			                        customBoard[row][col] = getNextTileValue(row, col);
			                        tileButton.setText(Integer.toString(customBoard[row][col]));
			                    }
			                    if (isCustomBoardFinished()) {
			                    	puzzleBoard = new PuzzleBoard(customBoard);
			                    	puzzleBoard.printBoard();
			                    	checkSolvable(puzzleBoard.isSolvable());
			                    	
			                    }
			                }
			            });
			        
			            tileButtons[row][col] = tileButton;
			            puzzleBoardPanel.add(tileButton);
			        }
			    }

			    add(puzzleBoardPanel, BorderLayout.CENTER);
	}
	
	
	@Override
	protected void addComponentsToFrame() {
		JPanel headerPanel = new JPanel(new BorderLayout());

		solveButton = new JButton("Automatically Solve");
		showSolutionButton = new JButton("Show Solution");
	    ImageIcon backButtonIcon = new ImageIcon(this.getClass().getResource("/resource/img2.png"));
	    JButton backButton = new JButton(backButtonIcon);
	    
        
        disableSolveButtons();

	    backButton.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            goToWelcomeScreen();
	        }
	    });
	    
	    solveButton.addActionListener(new ActionListener(){
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		  List<String> solution = AStarSolver.solvePuzzle(puzzleBoard);
        		  animateSolution(solution);
        	}
        });	
        
        showSolutionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String hintMessage = SolutionCalculator.calculateSolution(puzzleBoard);
                JDialog hintDialog = new JDialog(CustomizedPuzzle.this, "Solution", false);
                JTextArea hintTextArea = new JTextArea(hintMessage);
                hintDialog.add(new JScrollPane(hintTextArea));
                hintDialog.pack();
                hintDialog.setVisible(true);
            }
        });
	    
        
        
        backButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 135));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); 
        buttonPanel.add(backButton); 
        buttonPanel.add(showSolutionButton);
        buttonPanel.add(solveButton);
        
        headerPanel.add(buttonPanel, BorderLayout.WEST); 

        add(headerPanel, BorderLayout.NORTH);
        add(puzzleBoardPanel, BorderLayout.CENTER);
	}

	@Override
	protected void restartGame() {
		dispose();
		new CustomBoard();
	}

	/**
	 * this method announce user if their board is solvable or not
	 * @param solvable which indicates if the board is solvable or not
	 */
	private void checkSolvable(boolean solvable) {
		if (solvable == true) {
			transitionToGamePhase();
			JOptionPane.showMessageDialog(CustomizedPuzzle.this, "The Board is Solvable!");
			enableSolveButtons();
        }
		else {
			String[] options = {"Rebuild the Board", "Quit"};
			int choice = JOptionPane.showOptionDialog(
		            CustomizedPuzzle.this,
		            "The Board is not Solvable. Do you want to rebuild the board?",
		            "Board Not Solvable",
		            JOptionPane.YES_NO_OPTION,
		            JOptionPane.WARNING_MESSAGE,
		            null,
		            options,
		            options[0]
		        );
		        
		        if (choice == 0) {
		            restartGame();
		        }
		        else {
		        	goToWelcomeScreen();
		        }
		}
	}
	

	private void disableSolveButtons() {
		solveButton.setEnabled(false);
        showSolutionButton.setEnabled(false);
	}

	private void enableSolveButtons() {
		solveButton.setEnabled(true);
	    showSolutionButton.setEnabled(true);
	}

	/**
	 * initial listener of the tileButton is that the user can place the tile, after finishing placing the tile
	 * this method will help to switch from the above function to be able to normally play the game
	 */
	private void transitionToGamePhase() {
		// Remove the customization listeners
	    for (int i = 0; i < NUM_ROWS; i++) {
	        for (int j = 0; j < NUM_COLS; j++) {
	            JButton tileButton = tileButtons[i][j];
	            for (ActionListener listener : tileButton.getActionListeners()) {
	                tileButton.removeActionListener(listener);
	            }
	        }
	    }


	    for (int i = 0; i < NUM_ROWS; i++) {
	        for (int j = 0; j < NUM_COLS; j++) {
	            int row = i;
	            int col = j;
	            JButton tileButton = tileButtons[i][j];
	            
	            tileButton.addActionListener(new ActionListener() {
	                @Override
	                public void actionPerformed(ActionEvent e) {
	                	handleTileClick(row, col); 
	                }
	            });
	        }
	    }
	}

	/**
	 * @return true if the user finish placing the tile
	 */
	private boolean isCustomBoardFinished() {
		int count = 0;
		for(int i = 0; i<NUM_ROWS; i++) {
			for(int j = 0; j < NUM_COLS; j++) {
				if(customBoard[i][j]==0)
					count++;
			}
		}
		if(count == 1)
			return true;
		return false;
	}

	/**
	 * this method helps to track the number the player place on the tile
	 * @param row
	 * @param col
	 * @return the number appear on the tile
	 */
	private int getNextTileValue(int row, int col) {
	    boolean[] usedValues = new boolean[NUM_ROWS * NUM_COLS + 1]; 
	    for (int i = 0; i < NUM_ROWS; i++) {
	        for (int j = 0; j < NUM_COLS; j++) {
	            int value = customBoard[i][j];
	            if (value > 0) {
	                usedValues[value] = true; // mark the value as used
	            }
	        }
	    }

	    // find the next available value
	    for (int value = 1; value <= NUM_ROWS * NUM_COLS; value++) {
	        if (!usedValues[value]) {
	            return value;
	        }
	    }

	    return 0;
	}

}
