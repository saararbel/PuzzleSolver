import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.PriorityQueue;
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
					if(!closedList.contains(tempState) && !openList.contains(tempState)) {
						if(puzzle.isEndState(tempState)) {
							return Optional.of(new SolutionData(tempState, numPopedFromOpenList));
						}
						openList.add(tempState);
					}
				}
			}
		}
		
		return Optional.empty();
	}
	
	
	
	private int calcFn(PuzzleState state) {
		return gn(state) + hn(state);
	}
	
	/**
	 * This function iterate ofer state fathers untill it is null (which is the start state)
	 * @param state
	 * @return
	 */
	private int gn(PuzzleState state) {
		int counter = 0;
		while(state.getFather() != null) {
			state = state.getFather();
			counter++;
		}
		
		return counter;
	}
	
	

	/**
	 * This function calcs the estimated distance from state to endState - 
	 * The function calcs the 
	 * @param endState
	 * @param state
	 * @return
	 */
	private int hn(PuzzleState state) {
		int dist = 0;
		int[][] stateBoardValues = state.getBoardValues();
		int rows = stateBoardValues.length;
		int columns = stateBoardValues[0].length;
		
		for(int i=0 ; i<rows ; i++) {
			for(int j=0 ; j<columns ; j++) {
				int cell = stateBoardValues[i][j];
				/**
				 * This part of code calculates the TRUE place of the cell in the final end state , this
				 * helps us to calculate the Manhattan distance
				 */
				int realCellRow = ((double)cell/columns) > (double)(cell/columns) ? cell/columns : (cell/columns) - 1;
				int realCellColumn = (cell % columns) == 0 ? columns - 1 : (cell % columns)  - 1;
				
				dist += Math.abs(i-realCellRow) + Math.abs(j-realCellColumn);
			}
		}
		
		return dist;
	}

	public Optional<SolutionData> aStar(Puzzle puzzle) {
		int numPopedFromOpenList = 0;
		Set<PuzzleState> closedList = new HashSet<>();
		Queue<PuzzleState> openList = new PriorityQueue<>(new Comparator<PuzzleState>() {

			@Override
			public int compare(PuzzleState state0, PuzzleState state1) {
				return calcFn(state0) - calcFn(state1);
			}
		});
		
		openList.add(puzzle.getStartState());
		
		while(!openList.isEmpty()) {
			PuzzleState headState = openList.poll();
			numPopedFromOpenList++;
			
			if(puzzle.isEndState(headState)) {
				return Optional.of(new SolutionData(headState, numPopedFromOpenList));
			}
			
			closedList.add(headState);
			String[] possibleActions = headState.getPossibleActions();
			for(int i=0 ; i<4 ; i++) {
				if(!possibleActions[i].equals(PuzzleState.EMPTY_ACTION)) {
					PuzzleState tempState = headState.preformAction(possibleActions[i]);
					if(!closedList.contains(tempState) && !openList.contains(tempState)) {
						openList.add(tempState);
					}
				}
			}
		}
		
		
		
		return Optional.empty();
	}
	
	public Optional<SolutionData> dfid(Puzzle puzzle) {
		
		
		return null;
	}
}
