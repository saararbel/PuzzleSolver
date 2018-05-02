
public class Puzzle {
	
	private PuzzleState startState;
	private PuzzleState endState;
	
	public Puzzle(PuzzleState startState, PuzzleState endState) {
		this.startState = startState;
		this.endState = endState;
	}

	public PuzzleState getStartState() {
		return startState;
	}

	public PuzzleState getEndState() {
		return endState;
	}
	
	

	public boolean isEndState(PuzzleState tempState) {
		int[][] tempBoardValues = tempState.getBoardValues();
		int[][] endBoardValues = endState.getBoardValues();
		
		for(int i=0 ; i< endBoardValues.length ; i++) {
			for(int j=0 ; j<endBoardValues[0].length ; j++) {
				if(endBoardValues[i][j] != tempBoardValues[i][j]) {
					return false;
				}
			}
		}
		
		return true;
	}
	
}
