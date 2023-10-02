package logic;

import model.PuzzleBoard;

public class Node implements Comparable<Node> {
	
		private PuzzleBoard state;
	    private int h; // heuristic cost from this node to goal state
	    private Node parent;
	    private String move;
	    private int moveCount;
	
	    public Node(PuzzleBoard state, Node parent, String move) {
	        this.state = state;
	        this.h = calculateHeuristic();
	        this.moveCount = parent != null ? parent.getMoveCount() + 1 : 0;
	        this.parent = parent;
	        this.move = move;
	    }
	
	    public PuzzleBoard getState() {
	        return state;
	    }
	
	    public int getH() {
	        return h;
	    }
	
	    public Node getParent() {
	        return parent;
	    }
	
	    public String getMove() {
	        return move;
	    }
	    
	    public int getMoveCount() {
	        return moveCount;
	    }
	    
	    public void setParent(Node current) {
			this.parent = current;
		}
	    
	    @Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (o == null || getClass() != o.getClass()) return false;
	        Node other = (Node) o;
	        return state.equals(other.state);
	    }
	    
	    @Override
	    public int hashCode() {
	    	int code = 0;
	        int n = state.getNumCols();
	        for (int i = 0; i < n; i++) {
	            for (int j = 0; j < n; j++) {
	                code = code * 31 + state.getTileAt(i, j);
	            }
	        }
	        return code;
	    }

	    @Override
	    public int compareTo(Node other) {
	        return Integer.compare(this.h, other.h);
	    }
	
	    private int calculateHeuristic() {
	    	return state.calculateManhattanDistance();
	    }
	
	    public void printNode() {
	    	state.printBoard();
	    }
}
