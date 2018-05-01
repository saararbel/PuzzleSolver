import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class PuzzleSolver {
	
	public PuzzleSolver() {}
	
	public void bfs(Puzzle puzzle) {
		Set<PuzzleState> closedList = new HashSet<>();
		Queue<PuzzleState> openList = new LinkedList<>();
		openList.add(puzzle.getStartState());
		while(!openList.isEmpty()) {
			PuzzleState headState = openList.poll();
			closedList.add(headState);
			for(String action : headState.getPossibleActions()) {
				PuzzleState tempState = headState.preformAction(action);
				if(!closedList.contains(tempState)) {
					if(puzzle.isEndState(tempState)) {
						// Finish!
						return;
					}
					
					openList.add(tempState);
				}
			}
		}
		
	}
}
