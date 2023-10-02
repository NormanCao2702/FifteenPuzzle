package logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

import model.PuzzleBoard;

public class AStarSolver {

	 public static List<String> solvePuzzle(PuzzleBoard initialBoard) {
	        PriorityQueue<Node> open = new PriorityQueue<>();
	        HashMap<Node, Node> closed = new HashMap<>();

	        Node start = new Node(initialBoard, null, "initial");
	        PuzzleBoard goalBoard = PuzzleBoard.generateGoalBoard(initialBoard.getNumRows(), initialBoard.getNumCols());
	        
	        open.add(start);
	        closed.put(start, start);

	        while (!open.isEmpty()) {
	            Node current = open.poll();         
	            if (current.getState().equals(goalBoard)) {
	                return constructSolutionPath(current);
	            }

	            List<Node> neighbors = generateNeighbors(current, goalBoard);
	            for (Node element : neighbors) {
	                if (!closed.containsKey(element)) {
	                    open.add(element);
	                    closed.put(element, element);
	                }
	            }
	        }

	        return null; 
	    }

	    private static List<String> constructSolutionPath(Node finalNode) {
	        List<String> path = new ArrayList<String>();
	        Node current = finalNode;
	        while (current.getParent() != null) {
	            path.add(0, current.getMove());
	            current = current.getParent();
	        }
	        return path;
	    }
	    
	    private static List<Node> generateNeighbors(Node node, PuzzleBoard goalBoard) {
	    	List<Node> neighbors = new ArrayList<>();

	        int blankRow = node.getState().getEmptyTileRow();
	        int blankCol = node.getState().getEmptyTileCol();
	        int n = node.getState().getNumRows();

	        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // Up, Down, Left, Right

	        for (int[] dir : directions) {
	            int newBlankRow = blankRow + dir[0];
	            int newBlankCol = blankCol + dir[1];

	            if (newBlankRow >= 0 && newBlankRow < n && newBlankCol >= 0 && newBlankCol < n) {
	                PuzzleBoard newState = node.getState().copy(); 
	                
	                //swap the blank tile with the adjacent tile
	                newState.setTileValue(blankRow, blankCol, newState.getTileAt(newBlankRow, newBlankCol));
	                newState.setTileValue(newBlankRow, newBlankCol, 0);
	                
	                Node neighbor = new Node(newState, node, newState.getTileAt(blankRow,blankCol) + ""+ getDirectionLetter(dir));
//	                neighbor.printNode();
	                neighbors.add(neighbor);
	            }
	        }

	        return neighbors;
	    }

	    
	    private static String getDirectionLetter(int[] dir) {
	    	if (dir[0] == 1) {
		        return "U"; 
		    } else if (dir[0] == -1) {
		        return "D"; 
		    } else if (dir[1] == 1) {
		        return "L"; 
		    } else {
		        return "R"; 
		    }
	    }
}
