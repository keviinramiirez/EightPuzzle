import java.util.LinkedList;

/** */
public class State
{
	int[][] matrix;
	LinkedList<State> childStates;
	int costIncurred;
	
	State parent;
	private String zeroPos;
	private int totalManhattanDistance;

	public State(int[][] matrix) { 
		this(matrix, null, 0);
	}
	public State (int[][] matrix, int costIncurred) {
		this(matrix, null, costIncurred);
	}
	public State(int[][] matrix, State parent, int costIncurred) { 
		this.matrix = matrix;
		this.parent = parent;
		this.costIncurred = costIncurred;
		this.childStates = new LinkedList<>();

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
		

	public boolean isSolved() {
		return totalManhattanDistance == 0;
	}


	public int estimatedDistance() {
		return totalMisplaced() + totalManhattanDistance;
	}


	public int totalMisplaced() {
		int count = 0;
		for (int r = 0; r < matrix.length; r++)
			for (int c = 0; c < matrix[r].length; c++)
				if (matrix[r][c] != 0 && !(r+","+c).equals(EightPuzzle.goalMap.get(matrix[r][c])))
					count++;
		return count;
	}


	public void evaluateChildStates() {
		if (!childStates.isEmpty()) return;
			
		childStates = new LinkedList<>();
		int rZero = Integer.parseInt(zeroPos.substring(0, 1));
		int cZero = Integer.parseInt(zeroPos.substring(2));

		// store the integer value that are above, right, below, and left of zero
		// (-1 means that is out of bounds)
		int up = -1, right = -1, down = -1, left = -1;
		if (validRowOrCol(rZero-1)) up    = matrix[rZero-1][cZero];
		if (validRowOrCol(cZero-1)) left  = matrix[rZero][cZero-1];
		if (validRowOrCol(cZero+1)) right = matrix[rZero][cZero+1];
		if (validRowOrCol(rZero+1)) down  = matrix[rZero+1][cZero];

		int prevDist = Integer.MAX_VALUE;
		for (int r = 0; r < matrix.length; r++) {
			for (int c = 0; c < matrix[r].length; c++) {
				int curr = matrix[r][c];

				if (curr == up || curr == right || curr == down || curr == left) {
					int[][] newState = shallowCopyInstanceMatrix();
					swap(newState, rZero, cZero, r, c);
//					childPuzzleState.add(new PuzzleState(newState, this.costIncurred+1));

					/*************/
					State currState = new State(newState, this.costIncurred+1);
					int currDist = currState.estimatedDistance();

					// ensures to only store the child states that 
					// have the least amount of estimated distance.
					if (currDist < prevDist)
						childStates.removeAll(childStates);

					if (currDist <= prevDist) {
						prevDist = currDist;
						childStates.add(currState);
					}
					/*************/
				}
			}
		}
	}


	private int[][] shallowCopyInstanceMatrix() {
		int[][] newState = new int[3][3];
		for (int r = 0; r < matrix.length; r++)
			for (int c = 0; c < matrix[r].length; c++)
				newState[r][c] = matrix[r][c];
		return newState;
	}


	private void swap(int[][] matrix, int zr, int zc, int r, int c) {
		int temp = matrix[zr][zc];
		matrix[zr][zc] = matrix[r][c];
		matrix[r][c] = temp;
	}

	/** validates if the given row or column index is within the 3x3 array range */
	private boolean validRowOrCol(int g) {
		return g >= 0 && g < 3;
	}
}









//if (currState.isSolved())
//	return;
//
//currState.evaluateChildStates();
//int fMin = Integer.MAX_VALUE;
//
//int i = 0;
//int[] estimatedDistances = new int[currState.childPuzzleState.size()];
//for (PuzzleState puzzleState : currState.childPuzzleState) {
//	int estimatedDistance = puzzleState.estimatedDistance();
//	estimatedDistances[i++] = estimatedDistance;
//	if (estimatedDistance < fMin)
//		fMin = estimatedDistance;
//}
//
//
//for (i = 0; i < currState.childPuzzleState.size(); i++) {
//	if (estimatedDistances[i] == fMin) {
//		currState.childPuzzleState.get(i).costIncurred++;
//		this.currState = currState.childPuzzleState.get(i);
//		solve();
//		return;
//	}
//}
