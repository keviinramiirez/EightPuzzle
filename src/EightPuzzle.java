import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/** 
 * Authors:
 * - Kevin J Ramirez Pomales
 * - Irixa Vales Torres
 * - Christian Lopez Martinez
 * 
 * Giving an 8-puzzle with a 3×3 board with 8 tiles and one empty space (0), The objective 
 * is to implement the A* algorithm to solve this puzzle.
 *  <ul>
 *  	<li>h(n) the heuristic component is the total Manhattan distance of the misplaced tiles.</li>
 *      <li>g(n) is the actual cost incurred from the initial state to the current node.</li>
 *  	<li>f(n) = g(n) + h(n), is the estimate of the distance from the current node to the goal.</li>
 *  </ul>
 */
public class EightPuzzle 
{	
	/** initial state matrix to begin solving the puzzle */
	int[][] initMatrix = new int[3][3];

	/** goal state matrix to reach from initial state */
	int[][] goalMatrix = new int[3][3];

	/** queue that keeps track of all states (frontier) */
	Queue<State> qTrack = new ArrayDeque<State>();

	/** shall contain the path from initial state to goal state. */
	LinkedList<State> path = new LinkedList<>();

	/** maps each integer values of goal matrix 
	    with its position as a string.  
	    map = [int -> "row,col"]
	 */
	static Map<Integer, String> goalMap;

	/** Initializes both initial state and goal state as matrices (2d arrays)
	 *  with the given states represented as strings */
	public EightPuzzle(String initState, String goalState) {
		this(Util.createMatrix(initState), 
				Util.createMatrix(goalState));
	}
	
	/** Initializes goal state and a randomly generated initial state as matrices (2d arrays) */
	public EightPuzzle(int[][] goalMatrix) {
		this(Util.shuffleMatrix(goalMatrix), 
				goalMatrix);
	}
	
	/** Initializes both initial state and goal state as matrices (2d arrays)
	 *  with the given 2d arrays */
	public EightPuzzle(int[][] initMatrix, int[][] goalMatrix) {
		this.initMatrix = initMatrix;
		this.goalMatrix = goalMatrix;
		goalMap = new HashMap<>();

		for (int r = 0; r < goalMatrix.length; r++)
			for (int c = 0; c < goalMatrix[r].length; c++)
				goalMap.put(goalMatrix[r][c], r+","+c);

		qTrack.add(new State(initMatrix));
	}


	/** stores the path that it takes to solve the puzzle from the 
	 *  initial state only goal state on the instance list <i>path</i>. */
	private void storePath() {
		State peek = qTrack.peek();
		while (peek != null) {
			path.addFirst(peek);
			peek = peek.getParent();
		}
	}


	/** iterative process of solving the puzzle. */
	public void solve() {
		while (!qTrack.isEmpty()) {
			State currState = qTrack.peek();
			if (currState.isSolved()) {
				this.storePath();
				return;
			}

			currState.evaluateChildStates();

			// add the Next States to be evaluated into the queue
			for (State puzzleState : currState.childStates) {
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
	public boolean isAdmissible() {
		Integer[] arr = new Integer[initMatrix.length*initMatrix[0].length];
		int count = 0, k = 0;		

		for (int i = 0; i < initMatrix.length; i++)
			for (int j = 0; j < initMatrix[i].length; j++)
				arr[k++]= initMatrix[i][j];

		// count how many inversions the list has
		for (int i = 0; i < arr.length-1; i++)
			for (int j = i+1; j < arr.length; j++)
				if (arr[i] != 0 && arr[j] != 0 && arr[i] > arr[j])
					count++;

		return count%2 == 0;
	}



	/** print the path from initial state to goal state. */
	public void printPath() {
		System.out.println("----- PATH FROM INITIAL TO GOAL STATE -----");
		for (State state : this.path)
			Util.printMatrix(state.matrix);
	}


	public static void main(String[] args) 
	{	
		/*
		 * Code to instantiate specific initial and goal matrix. 
		 */
//		int[][] initMatrix = { {1, 8, 2}, {0, 4, 3}, {7, 6, 5} };
//		int[][] goalMatrix = { {1, 2, 3}, {4, 5, 6}, {7, 8, 0} };
//		EightPuzzle eightPuzzle = new EightPuzzle(initMatrix, goalMatrix);

		
		/*
		 * Code to instantiate specific initial and goal matrix
		 * by writing them as strings.
		 */
//		String initState = "182043765";
//		String goalState = "123456780";
//		EightPuzzle eightPuzzle = new EightPuzzle(initState, goalState);
		
		
		/* 
		 * Code to instantiate specific goal matrix
		 * and a random initial matrix
		 */
		int[][] goalMatrix = { {1, 2, 3}, {4, 5, 6}, {7, 8, 0} };
		EightPuzzle eightPuzzle = new EightPuzzle(goalMatrix);
		
		
		/**********************************/
		System.out.println("INITIAL STATE");
		Util.printMatrix(eightPuzzle.initMatrix);

		System.out.println("GOAL STATE");
		Util.printMatrix(eightPuzzle.goalMatrix);


		if (eightPuzzle.isAdmissible()) {
			eightPuzzle.solve();
			eightPuzzle.printPath();
		}
		else System.out.println("Is not possible to solve the given initial state");
		/**********************************/
	}
}