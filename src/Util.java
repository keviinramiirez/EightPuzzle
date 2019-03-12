import java.util.Random;

public class Util 
{
	/** creates a 2d array from the string given
	 *  @param s string representation as a string 
	 */
	public static int[][] createMatrix(String s) {
		s = s.replaceAll(" ", "");
		int[][] matrix = new int[3][3];

		int i = 0;
		for (int r = 0; r < matrix.length; r++)
			for (int c = 0; c < matrix[r].length; c++)
				matrix[r][c] = Integer.parseInt(s.substring(i, i++ + 1));

		return matrix;
	}
	
	/** return a string of the values within the given matrix. */
	public static String toString(int[][] matrix) {
		String s = "";
		for (int r = 0; r < matrix.length; r++)
			for (int c = 0; c < matrix[r].length; c++)
				s += matrix[r][c];
		return s;
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
	
	
	/** Checks if given state has a parent nearby that has same matrix */
	public static boolean equalsNearbyParent(State state) {
		int i = 10;
		State parent = state.parent;
		while (parent != null && i > 0) {
			if (state.equals(parent)) {
				System.out.println("equals nearby parent");
				return true;
			}
			parent = parent.parent;
		}
		return false;
	}
	
	
	/** creates a shallow copy of the given matrix
	 *  @param matrix 2d array to be copied
	 *  @return shallow copy of given matrix */
	public static int[][] shallowCopyOf(int[][] matrix) {
		int[][] newState = new int[3][3];
		for (int r = 0; r < matrix.length; r++)
			for (int c = 0; c < matrix[r].length; c++)
				newState[r][c] = matrix[r][c];
		return newState;
	}

	
	/** shuffles the given matrix.  More formally, swaps every 
	 *  two neighbor values within the matrix various times. */
	public static int[][] shuffleMatrix(int[][] matrix) {
		matrix = Util.shallowCopyOf(matrix);
		int r = 2, c = 2;
		for (int i = 0; i < 100; i++) {
			switch (new Random().nextInt(4)) {
				case 0:
					// move up
					if (Util.validRowOrCol(r - 1))
						Util.swap(matrix, r, c, r-- - 1, c);
					break;
				case 1:
					// move right
					if (Util.validRowOrCol(c + 1))
						Util.swap(matrix, r, c, r, c++ + 1);
					break;
				case 2:
					// move down
					if (Util.validRowOrCol(r + 1))
						Util.swap(matrix, r, c, r++ + 1, c);
					break;
				default:
					// move left
					if (Util.validRowOrCol(c - 1))
						Util.swap(matrix, r, c, r, c-- - 1);
			}
		}

		return matrix;
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
}
