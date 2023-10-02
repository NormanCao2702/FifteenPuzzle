package unusedClass;

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
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import gui.WelcomeScreen;
import logic.AStarSolver;
import logic.SolutionCalculator;
import model.PuzzleBoard;

public class CustomBoard extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final int NUM_ROWS = 4;
	private static final int NUM_COLS = 4;
	private static final int ANIMATION_DELAY = 100; 
	private int[][] customBoard;
	private JPanel puzzleBoardPanel;
	private JButton[][] tileButtons; 
	private PuzzleBoard puzzleBoard;
	private JButton solveButton;
	private JButton showSolutionButton;
	
	public CustomBoard() {
	    setTitle("15 Puzzle Custom Board");
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setSize(640, 480);
	    setLocationRelativeTo(null);
	
	    customBoard = new int[NUM_ROWS][NUM_COLS]; // Initialize the custom board
	
	    initializeComponents();
	    addComponentsToFrame();
	
	    setVisible(true);
	}
	
	private void initializeComponents() {
		// Create a custom board panel with a GridLayout
		puzzleBoardPanel = new JPanel(new GridLayout(NUM_ROWS, NUM_COLS));
		tileButtons = new JButton[NUM_ROWS][NUM_COLS];
		
		
	    // Create and initialize buttons for each tile on the custom board
	    for (int i = 0; i < NUM_ROWS; i++) {
	        for (int j = 0; j < NUM_COLS; j++) {
	            int row = i;
	            int col = j;

	            // Create a button for the tile
	            JButton tileButton = new JButton();

	            // Add an ActionListener to handle tile customization
	            tileButton.addActionListener(new ActionListener() {
	            	@Override
	                public void actionPerformed(ActionEvent e) {
	                    // Check if the button already has a number
	                    if (!tileButton.getText().isEmpty()) {
	                        // Remove the number from the current tile
	                        customBoard[row][col] = 0;
	                        tileButton.setText(""); // Clear the button's text
	                    } else {
	                        // Handle button click to customize the board
	                        customBoard[row][col] = getNextTileValue(row, col);
	                        tileButton.setText(Integer.toString(customBoard[row][col]));
	                    }
	                    // Check if the board is finished after each tile placement
	                    if (isCustomBoardFinished()) {
	                        // Handle board completion (e.g., show a message or take action)
	                    	puzzleBoard = new PuzzleBoard(customBoard);
	                    	puzzleBoard.printBoard();
	                    	checkSolvable(puzzleBoard.isSolvable());
	                    	
	                    }
	                }
	            });
	            
	            // Store the tile button reference in the array
	            tileButtons[row][col] = tileButton;
	            // Add the tile button to the custom board panel
	            puzzleBoardPanel.add(tileButton);
	        }
	    }

	    // Add the custom board panel to the frame
	    add(puzzleBoardPanel, BorderLayout.CENTER);
	}
	
	private void checkSolvable(boolean solvable) {
		if (solvable == true) {
			transitionToGamePhase();
			JOptionPane.showMessageDialog(CustomBoard.this, "The Board is Solvable!");
			enableSolveButtons();
        }
		else {
			String[] options = {"Rebuild the Board", "Quit"};
			int choice = JOptionPane.showOptionDialog(
		            CustomBoard.this,
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

	private void enableSolveButtons() {
		solveButton.setEnabled(true);
	    showSolutionButton.setEnabled(true);
	}

	private void disableSolveButtons() {
		solveButton.setEnabled(false);
        showSolutionButton.setEnabled(false);
	}
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

	    // Add new listeners for tile movement
	    for (int i = 0; i < NUM_ROWS; i++) {
	        for (int j = 0; j < NUM_COLS; j++) {
	            int row = i;
	            int col = j;
	            JButton tileButton = tileButtons[i][j];
	            
	            tileButton.addActionListener(new ActionListener() {
	                @Override
	                public void actionPerformed(ActionEvent e) {
	                	handleTileClick(row, col); // Use the captured row and col
	                }
	            });
	        }
	    }
	}




	protected boolean isCustomBoardFinished() {
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

	private void addComponentsToFrame() {
		JPanel headerPanel = new JPanel(new BorderLayout());

	    // Create buttons
		solveButton = new JButton("Automatically Solve");
		showSolutionButton = new JButton("Show Solution");
	    ImageIcon backButtonIcon = new ImageIcon(this.getClass().getResource("/resource/img2.png"));
	    JButton backButton = new JButton(backButtonIcon);
	    
        
        disableSolveButtons();

	    // Add an ActionListener to the "Back" button
	    backButton.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            // Handle "Back" button click (e.g., return to the welcome screen)
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
                JDialog hintDialog = new JDialog(CustomBoard.this, "Solution", false);
                JTextArea hintTextArea = new JTextArea(hintMessage);
                hintDialog.add(new JScrollPane(hintTextArea));
                hintDialog.pack();
                hintDialog.setVisible(true);
            }
        });
	    
        
        
	    // Create an empty border with right margin of 20 pixels for the "Back" button
        backButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 135));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Create a new panel for buttons
        buttonPanel.add(backButton); 
        buttonPanel.add(showSolutionButton);
        buttonPanel.add(solveButton);
        
        headerPanel.add(buttonPanel, BorderLayout.WEST); // Add the buttonPanel to the west of headerPanel

        add(headerPanel, BorderLayout.NORTH);
        add(puzzleBoardPanel, BorderLayout.CENTER);

	}
	
	private void animateSolution(List<String> solution) {
		disableTileButtons();
    	
        Thread animationThread = new Thread(() -> {
            for (int step = 0; step < solution.size();step++) {
            	String move = solution.get(step);
                int tileNumber = Integer.parseInt(move.substring(0, move.length() - 1)); // Extract tile number
                String direction = move.substring(move.length() - 1); // Extract direction
                // Find the row and column of the tile
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
                        break; // No need to continue searching
                    }
                }
//              puzzleBoard.printBoard();
                // Apply the tile movement
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

                // Update the tile buttons to reflect the new board state
                SwingUtilities.invokeLater(() -> {
                    updateTileButtons();
//                    System.out.println("moveing");
                });

                // Introduce a delay to visualize the animation
                try {
                    Thread.sleep(ANIMATION_DELAY);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
//                    System.out.println("error");
                }
            }
            // Enable user interaction after animation
            SwingUtilities.invokeLater(() -> {
            	checkFinished();
                enableTileButtons();
            });
        });
        
        animationThread.start();
	}
	
	private void disableTileButtons() {
    	for (int i = 0; i < puzzleBoard.getNumRows(); i++) {
            for (int j = 0; j < puzzleBoard.getNumCols(); j++) {
                tileButtons[i][j].setEnabled(false);
            }
        }
    }
    private void enableTileButtons() {
        for (int i = 0; i < puzzleBoard.getNumRows(); i++) {
            for (int j = 0; j < puzzleBoard.getNumCols(); j++) {
                tileButtons[i][j].setEnabled(true);
            }
        }
    }

	private void updateTileButtons() {
		for (int i = 0; i < puzzleBoard.getNumRows(); i++) {
            for (int j = 0; j < puzzleBoard.getNumCols(); j++) {
            	int tile = puzzleBoard.getTileAt(i, j);
                JButton tileButton = tileButtons[i][j];
                tileButton.setVisible(tile != 0);
                tileButton.setText(Integer.toString(tile));
            }
        }
	}
	
	private void goToWelcomeScreen() {
        WelcomeScreen welcomeScreen = new WelcomeScreen(); 
        welcomeScreen.setVisible(true); // Show the welcome screen
        
        dispose(); // Close the current game frame
    }
	

	// Helper method to get the next tile value (incrementing from 1 to 15)
	private int getNextTileValue(int row, int col) {
		// Check if the tile value is already in use, and find the next available value
	    boolean[] usedValues = new boolean[NUM_ROWS * NUM_COLS + 1]; // Initialize an array to track used values
	    for (int i = 0; i < NUM_ROWS; i++) {
	        for (int j = 0; j < NUM_COLS; j++) {
	            int value = customBoard[i][j];
	            if (value > 0) {
	                usedValues[value] = true; // Mark the value as used
	            }
	        }
	    }

	    // Find the next available value
	    for (int value = 1; value <= NUM_ROWS * NUM_COLS; value++) {
	        if (!usedValues[value]) {
	            return value;
	        }
	    }

	    // If all values are used, return 0 (empty)
	    return 0;
	}
	
	private void handleTileClick(int row, int col) {
		puzzleBoard.moveTile(row, col);
        updateTileButtons();
        checkFinished();
	}

	
	private void checkFinished() {
		if (puzzleBoard.isSolved()) {
            // Display winning message with restart option
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
	
	
	private void restartGame() {
		dispose();
//		customBoard = new int[NUM_ROWS][NUM_COLS];
//		puzzleBoardPanel.removeAll(); // Clear the puzzle board panel
//        initializeComponents(); // Reinitialize components
//        addComponentsToFrame(); // Add components to the frames
//        revalidate(); // Refresh the frame
//        repaint();
		new CustomBoard();
	}

	public static void main(String[] args) {
	    SwingUtilities.invokeLater(new Runnable() {
	        @Override
	        public void run() {
	            new CustomBoard();
	        }
	    });
	}
}