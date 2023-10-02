package logic;

import java.util.List;

import model.PuzzleBoard;

public class SolutionCalculator {

	public static String calculateSolution(PuzzleBoard puzzleBoard) {
		// TODO Auto-generated method stub
		Node initialNode = new Node(puzzleBoard, null, "initial");
        String hint = getSolution(initialNode);
        return hint;
	}
	
	private static String getSolution(Node initialNode) {
        List<String> solution = AStarSolver.solvePuzzle(initialNode.getState());
        
        if (solution != null && !solution.isEmpty()) {
            return formatSolution(solution);
        } else {
            return "No valid hint move available.";
        }
    }

	private static String formatSolution(List<String> solution) {
		// TODO Auto-generated method stub
		StringBuilder solutionBuilder = new StringBuilder("Solution:\n");
	    for (int i = 0; i < solution.size(); i++) {
	        solutionBuilder.append(i + 1).append(". ").append(solution.get(i)).append("\n");
	    }
	    return solutionBuilder.toString();
	}

}
