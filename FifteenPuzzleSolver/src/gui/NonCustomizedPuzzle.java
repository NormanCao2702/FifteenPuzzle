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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import logic.AStarSolver;
import logic.SolutionCalculator;
import model.PuzzleBoard;

/**
 * represents a non-customized puzzle game frame for the 15 Puzzle game.
 * extends the PuzzleGameFrame class.
 */
public class NonCustomizedPuzzle extends PuzzleGameFrame {

	private static final long serialVersionUID = 1L;
	
	/**
     * creates a new NonCustomizedPuzzle instance with the specified title, number of rows, and number of columns.
     * @param title The title of the game frame.
     * @param numRows The number of rows in the puzzle board.
     * @param numCols The number of columns in the puzzle board.
     */
	public NonCustomizedPuzzle(String title, int numRows, int numCols) {
		super(title, numRows, numCols);
	}
	
	/**
     * creates a new NonCustomizedPuzzle instance with default dimensions.
     */
	public NonCustomizedPuzzle() {
		super("15 Puzzle Game", NUM_ROWS, NUM_COLS);
	}

	@Override
	protected void initializeComponents() {
		puzzleBoardPanel = new JPanel();
        puzzleBoardPanel.setLayout(new GridLayout(puzzleBoard.getNumRows(), puzzleBoard.getNumCols()));
        tileButtons = new JButton[puzzleBoard.getNumRows()][puzzleBoard.getNumCols()];

        for (int i = 0; i < puzzleBoard.getNumRows(); i++) {
            for (int j = 0; j < puzzleBoard.getNumCols(); j++) {
                int row = i; 
                int col = j; 
                
                int tile = puzzleBoard.getTileAt(i, j);
                JButton tileButton = new JButton(Integer.toString(tile));
                tileButton.setVisible(tile != 0);
                tileButtons[i][j] = tileButton;
                
                tileButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        handleTileClick(row, col);
                    }
                });
                
                puzzleBoardPanel.add(tileButton);
            }
        }
	}

	@Override
	protected void addComponentsToFrame() {
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
                JDialog hintDialog = new JDialog(NonCustomizedPuzzle.this, "Solution", false);
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
		puzzleBoard = new PuzzleBoard(4, 4); 
        puzzleBoardPanel.removeAll(); 
        initializeComponents(); 
        addComponentsToFrame(); 
        revalidate(); 
        repaint();
	}

}
