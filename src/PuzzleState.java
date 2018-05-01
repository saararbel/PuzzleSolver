import java.util.List;

public class PuzzleState {
	
	private int[][] boardValues;
	private int[][] boardColors;
	
	public PuzzleState(int height, int width, String[] redPositions, String[] yellowPositions, List<String> contnet) {
		boardColors = new int[height][width];
		boardValues = new int[height][width];		
	}
	
	
	
	
}
