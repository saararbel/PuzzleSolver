import java.util.List;

public class PuzzleState {
	
	private int[][] boardValues;
	private int[][] boardColors;
	
	public static final int WHITE = 1;
	public static final int RED= 2;
	public static final int YELLOW = 3;
			
	
	public PuzzleState(int[][] boardValues, int[][] boardColors) {
		this.boardColors = boardColors;
		this.boardValues = boardValues;
		
	}
	
	
}
