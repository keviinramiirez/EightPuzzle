import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/** Giving an 8-puzzle with a 3×3 board with 8 tiles and one empty space (0), The objective 
 *  is to implement the A* algorithm to solve this puzzle.
 *  - h(n) the heuristic component is the total Manhattan distance of the misplaced tiles.
 *  - g(n) is the actual cost incurred from the initial state to the current node. 
 *  - f(n) = g(n) + h(n), is the estimate of the distance from the current node to the goal.
 */
public class EightPuzzle 
{	
	// queue that keeps track of all states (frontier)
	Queue<State> qTrack = new ArrayDeque<State>();
	
	// shall contain the path from initial state to goal state.
	LinkedList<State> path = new LinkedList<>();
	
	// map integer with 2d array position.  [int -> "row,col"]
	static Map<Integer, String> goalMap;
	
	
	public EightPuzzle(int[][] initMatrix, int[][] goalMatrix) {
		goalMap = new HashMap<>();
		
		for (int i = 0; i < goalMatrix.length; i++)
			for (int k = 0; k < goalMatrix[i].length; k++)
				goalMap.put(goalMatrix[i][k], i+","+k);
		
		qTrack.add(new State(initMatrix));
	}
	
	/** Stores the path that it takes to solve the puzzle from the 
	 *  initial state only goal state on the instance list <i>path</i>. */
	private void storePath() {
		State peek = qTrack.peek();
		while (peek != null) {
			path.addFirst(peek);
			peek = peek.getParent();
		}
	}

	/** Iterative process of solving the puzzle. */
	public void solve() {
		while (!qTrack.isEmpty()) {
			State peek = qTrack.peek();
			if (peek.isSolved()) {
				this.storePath();
				return;
			}
			
			peek.evaluateChildStates();
			
			// add the Next States to be evaluated into the queue
			for (State puzzleState : peek.childStates) {
				puzzleState.parent = qTrack.peek();
				qTrack.add(puzzleState);
			}
			
			// dequeue the first state for it to not be evaluated again
			qTrack.poll();
		}
	}
	
	/** checks if the given matrix is admissible by checking if the 
	 * total number of inversion is even.
	 * @param matrix the 2d dimensional array representation of the puzzle
	 * @return true if total number of inversion is even.  Otherwise, false.
	 */
	public boolean isAdmissible(int[][] matrix) {
		Integer[] arr = new Integer[matrix.length*matrix[0].length];
		int count = 0, k = 0;		
		
		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix[i].length; j++)
				arr[k++]= matrix[i][j];
		
		// count how many inversions the list has
		for (int i = 0; i < arr.length-1; i++)
			for (int j = i+1; j < arr.length; j++)
				if (arr[i] != 0 && arr[j] != 0 && arr[i] > arr[j])
					count++;
		
		return count % 2 == 0;
	}

	
	public void printMatrix(int[][] matrix) {
		for (int r = 0; r < matrix.length; r++) {
			for (int c = 0; c < matrix[r].length; c++)
				System.out.print(matrix[r][c] +" ");
			System.out.println();
		}
		System.out.println();
	}
	

	public void printPath() {
		System.out.println("----- PATH FROM INITIAL TO GOAL STATE -----");
		for (State state : this.path)
			printMatrix(state.matrix);
	}

	
	public static void main(String[] args) 
	{	
		/** examples that pass **/
		int[][] initMatrix = { {1, 8, 2}, {0, 4, 3}, {7, 6, 5} };
		int[][] goalMatrix = { {1, 2, 3}, {4, 5, 6}, {7, 8, 0} };
		
//		int[][] initMatrix = { {1, 2, 3}, {0, 4, 6}, {7, 5, 8} };
//		int[][] goalMatrix = { {1, 2, 3}, {4, 5, 6}, {7, 8, 0} };
		
//		int[][] initMatrix = { {1, 3, 6}, {5, 0, 2}, {4, 7, 8} };
//		int[][] goalMatrix = { {1, 2, 3}, {4, 5, 6}, {7, 8, 0} };
		
		
		/** examples that DON'T PASS **/
//		int[][] initMatrix = {
//				{8, 0, 6}, 
//				{5, 4, 7},
//				{2, 3, 1}
//		};
//		int[][] goalMatrix = {
//				{0, 1, 2}, 
//				{3, 4, 5}, 
//				{6, 7, 8}
//		};
		
		EightPuzzle eightPuzzle = new EightPuzzle(initMatrix, goalMatrix);
		System.out.println("INITIAL STATE");
		eightPuzzle.printMatrix(initMatrix);
		
		System.out.println("GOAL STATE");
		eightPuzzle.printMatrix(goalMatrix);

		
		if (eightPuzzle.isAdmissible(initMatrix)) {
			eightPuzzle.solve();
			eightPuzzle.printPath();
		}
		else System.out.println("Is not possible to solve the given initial state");
	}
}