import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PuzzleState {
	
	private int[][] boardValues;
	private int[][] boardColors;
	private int blankLocationX;
	private int blankLocationY;
	private PuzzleState father;
	// How did the father gets to the child (the move) 
	private String actionFromFather;
	private double actionCostFromFather;
	
	public static final String EMPTY_ACTION = "";
	
	public static final int WHITE = 1;
	public static final int RED= 2;
	public static final int YELLOW = 3;
	
	public static final double WHITE_COST = 1.0;
	public static final double YELLOW_COST = 0.5;
	public static final double RED_COST = 2.0;
			
	public PuzzleState(int[][] boardValues, int[][] boardColors, PuzzleState father, String actionFromFather,double actionCostFromFather) {
		this(boardValues, boardColors);
		this.father = father;
		this.actionFromFather = actionFromFather;
		this.actionCostFromFather = actionCostFromFather;
	}
	
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
	
	public String getActionFromFather() {
		return actionFromFather;
	}

	public double getActionCostFromFather() {
		return actionCostFromFather;
	}

	public PuzzleState getFather() {
		return father;
	}

	public int[][] getBoardValues() {
		return boardValues;
	}

	public void setBoardValues(int[][] boardValues) {
		this.boardValues = boardValues;
	}



	public String[] getPossibleActions() {
		String[] possibleActions = new String[4];
		possibleActions[0] = EMPTY_ACTION;
		possibleActions[1] = EMPTY_ACTION;
		possibleActions[2] = EMPTY_ACTION;
		possibleActions[3] = EMPTY_ACTION;
		
		// RIGHT
		if(blankLocationY + 1 <= boardValues[0].length-1) {
			possibleActions[0] = "RIGHT";
		}
		
		// DOWN
		if(blankLocationX + 1 <= boardValues.length-1) {
			possibleActions[1] = "DOWN";
		}
		
		// LEFT
		if(blankLocationY - 1 >= 0) {
			possibleActions[2] = "LEFT";
		}
		
		// UP
		if(blankLocationX - 1 >= 0) {
			possibleActions[3] = "UP";
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
		
		double cost;
		if(newBoardColors[blankLocationX][blankLocationY] == WHITE) {
			cost = WHITE_COST;
		}
		else if(newBoardColors[blankLocationX][blankLocationY] == YELLOW) {
			cost = YELLOW_COST;
		}
		else {
			cost = RED_COST;
		}
		
		return new PuzzleState(newBoardValues, newBoardColors, this, action, cost);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(actionCostFromFather);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((actionFromFather == null) ? 0 : actionFromFather.hashCode());
		result = prime * result + blankLocationX;
		result = prime * result + blankLocationY;
		result = prime * result + Arrays.deepHashCode(boardColors);
		result = prime * result + Arrays.deepHashCode(boardValues);
		result = prime * result + ((father == null) ? 0 : father.hashCode());
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
		if (Double.doubleToLongBits(actionCostFromFather) != Double.doubleToLongBits(other.actionCostFromFather))
			return false;
		if (actionFromFather == null) {
			if (other.actionFromFather != null)
				return false;
		} else if (!actionFromFather.equals(other.actionFromFather))
			return false;
		if (blankLocationX != other.blankLocationX)
			return false;
		if (blankLocationY != other.blankLocationY)
			return false;
		if (!Arrays.deepEquals(boardColors, other.boardColors))
			return false;
		if (!Arrays.deepEquals(boardValues, other.boardValues))
			return false;
		if (father == null) {
			if (other.father != null)
				return false;
		} else if (!father.equals(other.father))
			return false;
		return true;
	}
	
	
}
