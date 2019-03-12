import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;

/** 
 * Implemented by:
 * - Kevin J Ramirez Pomales
 * - Irixa Vales Torres
 * - Christian Lopez Martinez
 * 
 * Giving an 8-puzzle with a 3×3 board with 8 tiles and one empty space (0), The objective 
 * is to implement the A* algorithm to solve this puzzle.
 *  - h(n) the heuristic component is the total Manhattan distance of the misplaced tiles.
 *  - g(n) is the actual cost incurred from the initial state to the current node. 
 *  - f(n) = g(n) + h(n), is the estimate of the distance from the current node to the goal.
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

	/** map integer with 2d array position.  [int -> "row,col"] */
	static Map<Integer, String> goalMap;

	public EightPuzzle(String initState, String goalState) {
		this(EightPuzzle.createMatrix(initState), 
				EightPuzzle.createMatrix(goalState));
	}
	public EightPuzzle(int[][] initMatrix, int[][] goalMatrix) {
		this.initMatrix = initMatrix;
		this.goalMatrix = goalMatrix;
		goalMap = new HashMap<>();

		for (int r = 0; r < goalMatrix.length; r++)
			for (int c = 0; c < goalMatrix[r].length; c++)
				goalMap.put(goalMatrix[r][c], r+","+c);

		qTrack.add(new State(initMatrix));
	}
	public EightPuzzle(int[][] goalMatrix) {
		this.initMatrix = this.shuffleMatrix(goalMatrix, 10000);
		this.goalMatrix = goalMatrix;
		goalMap = new HashMap<>();

		for (int r = 0; r < goalMatrix.length; r++)
			for (int c = 0; c < goalMatrix[r].length; c++)
				goalMap.put(goalMatrix[r][c], r+","+c);
		
		qTrack.add(new State(initMatrix));
	}


	/** creates a 2d array from the string given
	 *  @param s string representation as a string 
	 */
	public static int[][] createMatrix(String s) {
		int[][] matrix = new int[3][3];

		int i = 0;
		for (int r = 0; r < matrix.length; r++)
			for (int c = 0; c < matrix[r].length; c++)
				matrix[r][c] = Integer.parseInt(s.substring(i, i++ + 1));

		return matrix;
	}

	/** creates a string array in order from 
	 *  upper-left to lower-right of the 2d array.
	 *  @param matrix 2d representation of the puzzle
	 */
	public static String createMatrixString(int[][] matrix) {
		String s = "";
		for (int r = 0; r < matrix.length; r++)
			for (int c = 0; c < matrix[r].length; c++)
				s += matrix[r][c];

		return s;
	}


	/** prints the given matrix
	 *  @param matrix 2d array to be printed */
	public static void printMatrix(int[][] matrix) {
		for (int r = 0; r < matrix.length; r++) {
			for (int c = 0; c < matrix[r].length; c++)
				System.out.print(matrix[r][c] +" ");
			System.out.println();
		}
		System.out.println();
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

		return count%2 == 0;
	}



	/** print the path from initial state to goal state. */
	public void printPath() {
		System.out.println("----- PATH FROM INITIAL TO GOAL STATE -----");
		for (State state : this.path)
			printMatrix(state.matrix);
	}


	public int[][] shuffleMatrix(int[][] matrix, int n) {
		matrix = this.shallowCopyOfMatrix(matrix);
		int posC0 = 2, posR0 = 2;
		for (int i = 0; i < n; i++) {
			Random rand = new Random();
			int mv = rand.nextInt(3);
			switch (mv) {
			case 0:
				if (State.validRowOrCol(posR0 - 1)) {
					State.swap(matrix, posR0, posC0, posR0 - 1, posC0);
					posR0--;
				}
				break;
			case 1:
				if (State.validRowOrCol(posR0 + 1)) {
					State.swap(matrix, posR0, posC0, posR0 + 1, posC0);
					posR0++;
				}
				break;
			case 2:
				if (State.validRowOrCol(posC0 - 1)) {
					State.swap(matrix, posR0, posC0, posR0, posC0 - 1);
					posC0--;
				}
				break;
			case 3:
				if (State.validRowOrCol(posC0 + 1)) {
					State.swap(matrix, posR0, posC0, posR0, posC0 + 1);
					posC0++;
				}
				break;
			default:
				break;
			}
		}

		return matrix;
	}

	/**  */
	private int[][] shallowCopyOfMatrix(int[][] matrix) {
		int[][] newState = new int[3][3];
		for (int r = 0; r < matrix.length; r++)
			for (int c = 0; c < matrix[r].length; c++)
				newState[r][c] = matrix[r][c];
		return newState;
	}


	public void swap(int zr, int zc, int r, int c) {
		int temp = initMatrix[zr][zc];
		initMatrix[zr][zc] = initMatrix[r][c];
		initMatrix[r][c] = temp;
	}


	public static void main(String[] args) 
	{	
		/** STATIC EXAMPLES **/
		int[][] initMatrix = { {1, 8, 2}, {0, 4, 3}, {7, 6, 5} };
		int[][] goalMatrix = { {1, 2, 3}, {4, 5, 6}, {7, 8, 0} };

		//		int[][] initMatrix = { {1, 2, 3}, {0, 4, 6}, {7, 5, 8} };
		//		int[][] goalMatrix = { {1, 2, 3}, {4, 5, 6}, {7, 8, 0} };

		//		int[][] initMatrix = { {1, 3, 6}, {5, 0, 2}, {4, 7, 8} };
		//		int[][] goalMatrix = { {1, 2, 3}, {4, 5, 6}, {7, 8, 0} };


		String initState = "182043765";
		String goalState = "123456780";

//				EightPuzzle eightPuzzle = new EightPuzzle(initState, goalState);
//				EightPuzzle eightPuzzle = new EightPuzzle(initMatrix, goalMatrix);
		EightPuzzle eightPuzzle = new EightPuzzle(goalMatrix);

		System.out.println("INITIAL STATE");
		EightPuzzle.printMatrix(eightPuzzle.initMatrix);

		System.out.println("GOAL STATE");
		EightPuzzle.printMatrix(eightPuzzle.goalMatrix);


		if (eightPuzzle.isAdmissible(eightPuzzle.initMatrix)) {
			eightPuzzle.solve();
			eightPuzzle.printPath();
		}
		else System.out.println("Is not possible to solve the given initial state");
	}
}