import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PuzzleState {
	
	private int[][] boardValues;
	private int[][] boardColors;
	private int blankLocationX;
	private int blankLocationY;
	
	public static final int WHITE = 1;
	public static final int RED= 2;
	public static final int YELLOW = 3;
			
	
	public PuzzleState(int[][] boardValues, int[][] boardColors) {
		this.boardColors = boardColors;
		this.boardValues = boardValues;
		
		for(int i=0 ; i<boardValues.length ; i++) {
			for(int j=0; j<boardValues[0].length ;j++) {
				if(boardValues[i][j] == -1) {
					blankLocationX = i;
					blankLocationY = j;
				}
			}
		}
	}
	
	public List<String> getPossibleActions() {
		List<String> possibleActions = new ArrayList<>();
		// UP
		if(blankLocationX - 1 >= 0) {
			possibleActions.add("UP");
		}
		
		//DOWN
		if(blankLocationX + 1 <= boardValues.length-1) {
			possibleActions.add("DOWN");
		}
		
		// RIGHT
		if(blankLocationY + 1 <= boardValues[0].length-1) {
			possibleActions.add("RIGHT");
		}
		
		// LEFT
		if(blankLocationY - 1 >= 0) {
			possibleActions.add("LEFT");
		}
		
		return possibleActions;
	}
	
	private void deepCopyMatrix(int[][] src, int [][] dst) {
		for(int i=0 ; i<src.length ; i++) {
			for(int j=0 ; j<src[0].length ; j++) {
				dst[i][j] = src[i][j];
			}
		}
	}
	
	private void swap(int[][] arr, int srcI, int srcJ, int dstI, int dstJ) {
		int temp = arr[srcI][srcJ];
		arr[srcI][srcJ] = arr[dstI][dstJ];
		arr[dstI][dstJ] = temp;
	}
	
	public PuzzleState preformAction(String action) {
		int height = boardValues.length;
		int width = boardValues[0].length;
		int[][] newBoardValues = new int[height][width];
		int[][] newBoardColors = new int[height][width];
		
		deepCopyMatrix(boardColors, newBoardColors);
		deepCopyMatrix(boardValues, newBoardValues);
		
		// UP
		if(action == "UP") {
			swap(newBoardValues, blankLocationX, blankLocationY, blankLocationX - 1 , blankLocationY);
			swap(newBoardColors, blankLocationX, blankLocationY, blankLocationX - 1 , blankLocationY);
		}
		
		// DOWN
		if(action == "DOWN") {
			swap(newBoardValues, blankLocationX, blankLocationY, blankLocationX + 1 , blankLocationY);
			swap(newBoardColors, blankLocationX, blankLocationY, blankLocationX + 1 , blankLocationY);
		}
		
		// RIGHT
		if(action == "RIGHT") {
			swap(newBoardValues, blankLocationX, blankLocationY, blankLocationX , blankLocationY + 1);
			swap(newBoardColors, blankLocationX, blankLocationY, blankLocationX , blankLocationY + 1);
		}
		
		// LEFT
		if(action == "LEFT") {
			swap(newBoardValues, blankLocationX, blankLocationY, blankLocationX , blankLocationY - 1);
			swap(newBoardColors, blankLocationX, blankLocationY, blankLocationX , blankLocationY - 1);
		}
		
		return new PuzzleState(newBoardValues, newBoardColors);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(boardColors);
		result = prime * result + Arrays.deepHashCode(boardValues);
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PuzzleState other = (PuzzleState) obj;
		if (!Arrays.deepEquals(boardColors, other.boardColors))
			return false;
		if (!Arrays.deepEquals(boardValues, other.boardValues))
			return false;
		return true;
	}
	
	
}
