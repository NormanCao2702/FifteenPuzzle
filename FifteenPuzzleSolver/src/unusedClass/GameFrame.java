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

/**
 * The main GUI class for the 15 Puzzle Game.
 */
public class GameFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel puzzleBoardPanel;
	private PuzzleBoard puzzleBoard;
	private JButton[][] tileButtons; 
	private static final int ANIMATION_DELAY = 100; 


	
	 /**
     * Constructs the main game frame.
     */
	public GameFrame() {
        setTitle("15 Puzzle Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(640, 480);
        setLocationRelativeTo(null);

        puzzleBoard = new PuzzleBoard(4, 4);
        initializeComponents();
        addComponentsToFrame();

        setVisible(true);
    }

    private void initializeComponents() {
    	puzzleBoardPanel = new JPanel();
        puzzleBoardPanel.setLayout(new GridLayout(puzzleBoard.getNumRows(), puzzleBoard.getNumCols()));
        tileButtons = new JButton[puzzleBoard.getNumRows()][puzzleBoard.getNumCols()];

        for (int i = 0; i < puzzleBoard.getNumRows(); i++) {
            for (int j = 0; j < puzzleBoard.getNumCols(); j++) {
                int row = i; // Capture the value of i for the ActionListener
                int col = j; // Capture the value of j for the ActionListener
                
                int tile = puzzleBoard.getTileAt(i, j);
                JButton tileButton = new JButton(Integer.toString(tile));
                tileButton.setVisible(tile != 0);
                tileButtons[i][j] = tileButton;
                
                tileButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        handleTileClick(row, col); // Use the captured row and col
                    }
                });
                
                puzzleBoardPanel.add(tileButton);
            }
        }
    }

    private void addComponentsToFrame() {
    	JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout()); // Set BorderLayout for the headerPanel
        JButton solveButton = new JButton("Automatically solve");
        JButton showSolutionButton = new JButton("Show Solution");
        ImageIcon placeholderIcon= new ImageIcon(this.getClass().getResource("/resource/img2.png"));
        JButton backButton = new JButton(placeholderIcon);
        
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
                JDialog hintDialog = new JDialog(GameFrame.this, "Solution", false);
                JTextArea hintTextArea = new JTextArea(hintMessage);
                hintDialog.add(new JScrollPane(hintTextArea));
                hintDialog.pack();
                hintDialog.setVisible(true);
            }
        });
        
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
                        	System.out.println(puzzleBoard.getTileAt(i, j));
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

   

	private void goToWelcomeScreen() {
        WelcomeScreen welcomeScreen = new WelcomeScreen(); 
        welcomeScreen.setVisible(true); // Show the welcome screen
        
        dispose(); // Close the current game frame
    }

    /**
     * Handles the click event on a tile button.
     *
     * @param row The row index of the clicked tile.
     * @param col The column index of the clicked tile.
     */
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
                // Restart game
                restartGame();
            } else {
            	goToWelcomeScreen();
            }
        }
	}
    
	/*Method to update the tile buttons based on the current state of the puzzle board*/
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
    
    /*restart board*/
    private void restartGame() {
//    	dispose();
        puzzleBoard = new PuzzleBoard(4, 4); // Or use the appropriate board dimensions
        puzzleBoardPanel.removeAll(); // Clear the puzzle board panel
        initializeComponents(); // Reinitialize components
        addComponentsToFrame(); // Add components to the frame
        revalidate(); // Refresh the frame
        repaint();
//    	new GameFrame();
    }

    
    
}


