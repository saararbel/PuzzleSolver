
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
		return tempState.equals(startState);
	}
	
}
