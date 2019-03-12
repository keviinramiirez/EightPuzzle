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
	
	/**  */
	int costIncurred;
	State parent;
	LinkedList<State> childStates;
	private String zeroPos;
	private int totalManhattanDistance;


	public State(int[][] matrix) { 
		this(matrix, null, 0);
	}
	public State (int[][] matrix, int costIncurred) {
		this(matrix, null, costIncurred);
	}
	/** 
	 *  
	 */
	public State(int[][] matrix, State parent, int costIncurred) { 
		this.matrix = matrix;
		this.parent = parent;
		this.costIncurred = costIncurred;
		this.childStates = new LinkedList<>();
		this.zeroPos = "";

		// locate where zero is positioned
		outerloop:
			for (int r = 0; r < matrix.length; r++) {
				for (int c = 0; c < matrix[r].length; c++) {
					if (matrix[r][c] == 0) {
						zeroPos = r+","+c;
						break outerloop;
					}
				}
			}

		// calculate the total Manhattan distance of this state
		totalManhattanDistance = 0;
		for (int r = 0; r < matrix.length; r++) {
			for (int c = 0; c < matrix[r].length; c++) {
				if (matrix[r][c] != 0) {
					int rGoal = Integer.parseInt(EightPuzzle.goalMap.get(matrix[r][c]).substring(0, 1));
					int cGoal = Integer.parseInt(EightPuzzle.goalMap.get(matrix[r][c]).substring(2));
					totalManhattanDistance += Math.abs(r - rGoal) + Math.abs(c - cGoal);
				}
			}
		}
	}
	
	public State getParent() { return this.parent; }
		
	/** true if the instance containing the total Manhattan Distance 
	 *  equals zero.  Otherwise false. */
	public boolean isSolved() {
		return totalManhattanDistance == 0;
	}


	public int estimatedDistance() {
		return costIncurred + totalManhattanDistance;
	}


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
		if (validRowOrCol(rZero-1)) up    = matrix[rZero-1][cZero];
		if (validRowOrCol(cZero-1)) left  = matrix[rZero][cZero-1];
		if (validRowOrCol(cZero+1)) right = matrix[rZero][cZero+1];
		if (validRowOrCol(rZero+1)) down  = matrix[rZero+1][cZero];

		// keeps track of previous estimated distances along the process
		int prevDist = Integer.MAX_VALUE;
		
		// evaluate/iterate over all values within the 2d array
		for (int r = 0; r < matrix.length; r++) {
			for (int c = 0; c < matrix[r].length; c++) {
				int currVal = matrix[r][c];
				
				// check if current value is either above, right, below, or left of the zero value
				if (currVal == up || currVal == right || currVal == down || currVal == left) {
					int[][] newState = shallowCopyInstanceMatrix();
					swap(newState, rZero, cZero, r, c);

					State currState = new State(newState, this.costIncurred+1);
					int currDist = currState.estimatedDistance();

					/*********************/
					// ensures to only store the child states that 
					// have the least estimated distance.
					if (currDist < prevDist)
						childStates.removeAll(childStates);

					if (currDist <= prevDist) {
						prevDist = currDist;
						childStates.add(currState);
					}
					/*********************/
				}
			}
		}
	}

	/**  */
	private int[][] shallowCopyInstanceMatrix() {
		int[][] newState = new int[3][3];
		for (int r = 0; r < matrix.length; r++)
			for (int c = 0; c < matrix[r].length; c++)
				newState[r][c] = matrix[r][c];
		return newState;
	}

	/** Swaps the the zero value that is in the <i>(zr, zc)</i> position 
	 *  within the given matrix with the value in position <i>(r, c)</i>
	 *  @param matrix the 2d dimensional array representation of the puzzle
	 *  @param zr the row index of the zero value within the matrix
	 *  @param zc the column index of the zero value within the matrix
	 *  @param r  a row index within the matrix
	 *  @param c  a column index within the matrix
	 */
	public static void swap(int[][] matrix, int zr, int zc, int r, int c) {
		int temp = matrix[zr][zc];
		matrix[zr][zc] = matrix[r][c];
		matrix[r][c] = temp;
	}


	/** Validates if the given row or column index is within the 3x3 array range 
	 *  @param i row or column index within the matrix
	 *  @return true if index is between [0, 3) */
	public static boolean validRowOrCol(int i) {
		return i >= 0 && i < 3;
	}
	
	/** Print the given matrix and the given distance  */
	public void printMatrix(int[][] matrix, int dist) {
		for (int r = 0; r < matrix.length; r++) {
			for (int c = 0; c < matrix[r].length; c++)
				System.out.print(matrix[r][c] +" ");
			System.out.println();
		}
		System.out.println(dist);
	}
}