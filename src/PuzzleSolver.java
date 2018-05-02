import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class PuzzleSolver {
	
	public PuzzleSolver() {}
	
	public void printSolution(PuzzleState state, int numPopedFromOpenList) {
		double cost = 0.0;
		Stack<String> solution = new Stack<>();
		while(state.getFather() != null) {
			cost += state.getActionCostFromFather();
			solution.push(state.getActionFromFather());
			state = state.getFather();
		}
		
		while(!solution.isEmpty()) {
			System.out.print(solution.pop().substring(0, 1) + "-");
		}
		System.out.println("\nNum: " + numPopedFromOpenList);
		System.out.println("Cost: " + cost);
	}
	
	public Optional<SolutionData> bfs(Puzzle puzzle) {
		Set<PuzzleState> closedList = new HashSet<>();
		Queue<PuzzleState> openList = new LinkedList<>();
		int numPopedFromOpenList = 0;
		openList.add(puzzle.getStartState());
		
		while(!openList.isEmpty()) {
			PuzzleState headState = openList.poll();
			numPopedFromOpenList++;
			closedList.add(headState);
			String[] possibleActions = headState.getPossibleActions();
			for(int i=0 ; i<4 ; i++) {
				if(!possibleActions[i].equals(PuzzleState.EMPTY_ACTION)) {
					PuzzleState tempState = headState.preformAction(possibleActions[i]);
					if(!closedList.contains(tempState)) {
						if(puzzle.isEndState(tempState)) {
							// Finish!
							return Optional.of(new SolutionData(tempState, numPopedFromOpenList));
						}
						openList.add(tempState);
					}
				}
			}
		}
		
		return Optional.empty();
	}
	
}
