import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class EightPuzzle 
{
	/** first example **/
//	int[][] initMatrix = {
//			{1, 8, 2}, 
//			{0, 4, 3}, 
//			{7, 6, 5} 
//	};
//	int[][] goalMatrix = {
//			{1, 2, 3}, 
//			{4, 5, 6}, 
//			{7, 8, 0}
//	};
	/** second example **/
	int[][] initMatrix = {
			{1, 2, 3},
			{0, 4, 6},
			{7, 5, 8}
	};
	int[][] goalMatrix = {
			{1, 2, 3},
			{4, 5, 6},
			{7, 8, 0}
	};

	Queue<State> qTrack = new ArrayDeque<State>();
	LinkedList<State> path = new LinkedList<>();
	static Map<Integer, String> goalMap;
	
	
	public EightPuzzle() {
		goalMap = new HashMap<>();
		// map integer with 2d array position.  [int -> "row, col"]
		for (int i = 0; i < goalMatrix.length; i++)
			for (int k = 0; k < goalMatrix[i].length; k++)
				goalMap.put(goalMatrix[i][k], i+","+k);
		
		qTrack.add(new State(initMatrix));
	}
	
	
	public void solve() {
		State peek = qTrack.peek();
		if (peek.isSolved()) {
			while (peek != null) {
				path.addFirst(peek);
				peek = peek.getParent();
			}
			return;
		}

		peek.evaluateChildStates();
		
		for (State puzzleState : peek.childStates) {
			puzzleState.parent = qTrack.peek();
			qTrack.add(puzzleState);
		}
		
		qTrack.poll();
		solve();
	}
	
	public void printInitGoalStates() {
		System.out.println("INITIAL STATE");
		printMatrix(initMatrix);
		
		System.out.println("GOAL STATE");
		printMatrix(goalMatrix);
	}
	
	
	public void printPath() {
		System.out.println("----- PATH FROM INITIAL TO GOAL STATE -----");
		for (State state : this.path) {
			printMatrix(state.matrix);
		}
	}
	
	public void printMatrix(int[][] matrix) {
		for (int r = 0; r < matrix.length; r++) {
			for (int c = 0; c < matrix[r].length; c++)
				System.out.print(matrix[r][c] +" ");
			System.out.println();
		}
		System.out.println();
	}
	
	
	
	public static void main(String[] args) {
		EightPuzzle eightPuzzle = new EightPuzzle();
		eightPuzzle.printInitGoalStates();
		eightPuzzle.solve();
		eightPuzzle.printPath();
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
