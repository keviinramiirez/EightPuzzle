import java.util.LinkedList;

/** A State is an object representing a state of the 8-puzzle.  More formally,
 *  represents one node of the binary tree that all path of properties of a State include the following:
 *  - The matrix: values from 0 to 8 of the puzzle within a 2d array. 
 *  - Child States: list of the next states of the binary tree
 */
public class State
{
	/** representation of the puzzle */
	int[][] matrix;
	
	/** 
	    formally, cost incurred represents the 
	    depth of where this state is on the tree 
	 */
	int costIncurred;
	
	/** link to its parent state */
	State parent;
	
	/** list of all its next states */
	LinkedList<State> childStates;
	
	/** position (as a string) of the zero value within the matrix */
	private String zeroPos;
	
	/** string of the values within the given matrix */
	private String strMatrix;
	
	/** total Manhattan Distance */
	private int totalManhattanDistance;


	public State(int[][] matrix) { 
		this(matrix, null, 0);
	}
	public State(int[][] matrix, int costIncurred) {
		this(matrix, null, costIncurred);
	}
	/** 
     *  <ul>
	 *  	<li>initializes the matrix with the one given.</li>
	 *  	<li>initializes the parent with the one given.</li>
	 *  	<li>initializes the cost incurred with the one given.</li>
	 *  	<li>initializes an empty list of child states.</li>
	 *  	<li>locates/initializes the position of the zero value.</li>
	 *  	<li>calculates/initializes the total Manhattan Distance.</li>
     *  </ul>
	 */
	public State(int[][] matrix, State parent, int costIncurred) { 
		this.matrix = matrix;
		this.parent = parent;
		this.costIncurred = costIncurred;
		this.childStates = new LinkedList<>();
		this.zeroPos = "";
		this.strMatrix = "";

		// locate where zero is positioned
		outerloop:
			for (int r = 0; r < matrix.length; r++) {
				for (int c = 0; c < matrix[r].length; c++) {
					strMatrix += matrix[r][c];
					if (matrix[r][c] == 0) {
						zeroPos = r+","+c;
						break outerloop;
					}
				}
			}

		// calculate the total Manhattan distance of this state.
		// Also, concatenates all values of this state's matrix
		totalManhattanDistance = 0;
		for (int r = 0; r < matrix.length; r++) {
			for (int c = 0; c < matrix[r].length; c++) {
				strMatrix += matrix[r][c];
				if (matrix[r][c] != 0) {
					int rGoal = Integer.parseInt(EightPuzzle.goalMap.get(matrix[r][c]).substring(0, 1));
					int cGoal = Integer.parseInt(EightPuzzle.goalMap.get(matrix[r][c]).substring(2));
					totalManhattanDistance += Math.abs(r - rGoal) + Math.abs(c - cGoal);
				}
			}
		}
	}
	
	/** returns the parent instance */
	public State getParent() { return this.parent; }
	
	/** returns a string containing all the values within the instance matrix  */
	public String getMatrixString() { return this.strMatrix; }
		
	/** true if the instance containing the total Manhattan Distance 
	 *  equals zero.  Otherwise false. */
	public boolean isSolved() {
		return totalManhattanDistance == 0;
	}

	/** return the estimated distance which is the sum
	 *  of the cost incurred and total Manhattan Distance. */
	public int estimatedDistance() {
		return costIncurred + totalManhattanDistance;
	}


	/** counts how many misplaced values are within the instance matrix */
	public int totalMisplaced() {
		int count = 0;
		for (int r = 0; r < matrix.length; r++)
			for (int c = 0; c < matrix[r].length; c++)
				if (matrix[r][c] != 0 && !(r+","+c).equals(EightPuzzle.goalMap.get(matrix[r][c])))
					count++;
		return count;
	}
	

	/** Evaluate each child state and only stores the ones
	 *  that have the least amount of estimated distance f(n).
	 *  <br>*Note: There are a maximum of 4 child states 
	 */
	public void evaluateChildStates() {
		if (!childStates.isEmpty()) return;
		
		childStates = new LinkedList<>();
		
		// row and column position of the zero value within goal state
		int rZero = Integer.parseInt(zeroPos.substring(0, 1));
		int cZero = Integer.parseInt(zeroPos.substring(2));

		// store the integer values that are above, right, below, and 
		// left of the zero value.  -1 represents that the particular 
		// direction is out of bounds
		int up = -1, right = -1, down = -1, left = -1;
		if (Util.validRowOrCol(rZero-1)) up    = matrix[rZero-1][cZero];
		if (Util.validRowOrCol(cZero-1)) left  = matrix[rZero][cZero-1];
		if (Util.validRowOrCol(cZero+1)) right = matrix[rZero][cZero+1];
		if (Util.validRowOrCol(rZero+1)) down  = matrix[rZero+1][cZero];

		// shall keep track of previous estimated distances along the process
		int prevDist = Integer.MAX_VALUE;

		// evaluate/iterate over all values within the 2d array
		for (int r = 0; r < matrix.length; r++) {
			for (int c = 0; c < matrix[r].length; c++) {
				int currVal = matrix[r][c];
				
				// check if current value is either above, right, below, or left of the zero value
				if (currVal == up || currVal == right || currVal == down || currVal == left) {
					int[][] newState = Util.shallowCopyOf(matrix);
					Util.swap(newState, rZero, cZero, r, c);

					State currState = new State(newState, this, this.costIncurred+1);
					int currDist = currState.estimatedDistance();

					// is statement ensures to not evaluate a state that has already been evaluated
					if (!EightPuzzle.visited.contains(currState.strMatrix)) {
						/********************************************/
						// ensures to only store the child states that 
						// have the least estimated distance.
						if (currDist < prevDist)
							childStates.removeAll(childStates);

						if (currDist <= prevDist) {
							prevDist = currDist;
							childStates.add(currState);
						}
						/********************************************/
					}
				}
			}
		}
	}
}