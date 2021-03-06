import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

/**
 * This class contains all the solver algorithms
 * @author saar
 *
 */
public class PuzzleSolver {
	
	public static final int MAX_DFID_DEPTH = 10;
	/**
	 * Symbol for solution found in IDA* (because there cannot be a negative value for cost)
	 */
	private static final double FOUND_SOLUTION = -1.0;
	
	/**
	 * Comparator for PuzzleState (compare the F-score)
	 */
	private Comparator<PuzzleState> puzzleStateComparator = new Comparator<PuzzleState>() {

		@Override
		public int compare(PuzzleState state0, PuzzleState state1) {
			if(calcFn(state0) - calcFn(state1) == 0.0) {
				return 0;
			} 
			if(calcFn(state0) - calcFn(state1) > 0.0) {
				return 1;
			}
			return -1;
		}
	};
	
	public PuzzleSolver() {}
	
	/***
	 * This function is the BFS algorithm
	 * @param puzzle
	 * @return
	 */
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
					if(closedList.contains(tempState)) {
						continue;
					}
					if(!openList.contains(tempState)) {
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
	
	
	/***
	 * Calcs the heuristic function
	 * @param state
	 * @return
	 */
	private double calcFn(PuzzleState state) {
		return gn(state) + hn(state);
	}
	
	/**
	 * This function iterate ofer state fathers untill it is null (which is the start state)
	 * @param state
	 * @return
	 */
	private double gn(PuzzleState state) {
		double cost = 0;
		while(state.getFather() != null) {
			cost += state.getActionCostFromFather();
			state = state.getFather();
		}
		
		return cost;
	}

	/**
	 * This function calcs the estimated distance from state to endState - 
	 * The function calcs the manhetan distance between each cell in the current state to the same cell in the endState and multiply
	 * it by his color weight (because this cell should move the manhaten size steps and each one cost the color on the cell)
	 * @param endState
	 * @param state
	 * @return
	 */
	private double hn(PuzzleState state) {
		double dist = 0.0;
		int[][] stateBoardValues = state.getBoardValues();
		int[][] stateBoardColors = state.getBoardColors();
		int rows = stateBoardValues.length;
		int columns = stateBoardValues[0].length;
		
		for(int i=0 ; i<rows ; i++) {
			for(int j=0 ; j<columns ; j++) {
				int cell = stateBoardValues[i][j];
				if(cell == -1) {
					dist += Math.abs(i-(rows-1)) + Math.abs(j-(columns-1));
				}
				else {
					/**
					 * This part of code calculates the TRUE place of the cell in the final end state , this
					 * helps us to calculate the Manhattan distance
					 */
					int realCellRow = ((double)cell/columns) > (double)(cell/columns) ? cell/columns : (cell/columns) - 1;
					int realCellColumn = (cell % columns) == 0 ? columns - 1 : (cell % columns)  - 1;
					
					dist += ((Math.abs(i-realCellRow) + Math.abs(j-realCellColumn)) * stateBoardColors[i][j]);
				}
			}
		}
		
		return dist;
	}

	/***
	 * This function is the aStar algorithm
	 * @param puzzle
	 * @return
	 */
	public Optional<SolutionData> aStar(Puzzle puzzle) {
		int numPopedFromOpenList = 0;
		Set<PuzzleState> closedList = new HashSet<>();
		Queue<PuzzleState> openList = new PriorityQueue<>(puzzleStateComparator);
		Map<PuzzleState, Double> gScore = new HashMap<>();
		Map<PuzzleState, Double> fScore = new HashMap<>();
		gScore.put(puzzle.getStartState(), 0.0);
		fScore.put(puzzle.getStartState(), calcFn(puzzle.getStartState()));
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
					double tentative_g_score =  gn(headState) + tempState.getActionCostFromFather();
					if(closedList.contains(tempState) || ((gScore.containsKey(tempState) && gScore.get(tempState) <= tentative_g_score))) {
						continue;
					}
					if(!openList.contains(tempState) || ((gScore.containsKey(tempState) && gScore.get(tempState) > tentative_g_score))) {
						gScore.put(tempState, tentative_g_score);
						fScore.put(tempState, gScore.get(tempState) + hn(tempState));
						if(!openList.contains(tempState)) {
							openList.add(tempState);
						}
					}
				}
			}
		}
		
		
		
		return Optional.empty();
	}
	
	/***
	 * This function is the dfs algorithm (for the dfid)
	 * @param puzzle
	 * @return
	 */
	
	public Optional<SolutionData> dfs(PuzzleState state,PuzzleState endState, int depth, int numPopedFromOpenList) {
		if(depth == 0) {
			if(Arrays.deepEquals(endState.getBoardValues(),state.getBoardValues())) {
				return Optional.of(new SolutionData(state, numPopedFromOpenList));
			}
			return Optional.empty();
		}
		
		String[] possibleActions = state.getPossibleActions();
		Optional<SolutionData> childRes = Optional.empty();
		for(int i=0 ; i<4 ; i++) {
			if(!possibleActions[i].equals(PuzzleState.EMPTY_ACTION)) {
				PuzzleState tempState = state.preformAction(possibleActions[i]);
				if(Arrays.deepEquals(endState.getBoardValues(),tempState.getBoardValues())) {
					return Optional.of(new SolutionData(tempState, numPopedFromOpenList));
				}
				numPopedFromOpenList++;
				childRes = dfs(tempState, endState, depth-1, numPopedFromOpenList);
				if(childRes.isPresent()) {
					return childRes;
				}
			}
		}
		
		return childRes;
	}
	
	
	/***
	 * This function is the dfid algorithm
	 * @param puzzle
	 * @return
	 */
	public Optional<SolutionData> dfid(Puzzle puzzle) {
		int numPopedFromOpenList = 0;
		for(int i=0 ; i<MAX_DFID_DEPTH ; i++) {
			Optional<SolutionData> res = dfs(puzzle.getStartState(), puzzle.getEndState(), i, numPopedFromOpenList);
			if(res.isPresent()) {
				return res;
			}
		}
		
		return Optional.empty();
	}
	
	/***
	 * This function is the idsStar algorithm
	 * @param puzzle
	 * @return
	 */
	
	public Optional<SolutionData> idaStar(Puzzle puzzle) {
		double bound = hn(puzzle.getStartState());
		double numPopedFromOpenList = 0.0;
		Stack<PuzzleState> path = new Stack<>();
		path.push(puzzle.getStartState());
		
		while(true) {
			double[] res = idaStarSearch(path, 0, bound, puzzle, numPopedFromOpenList); 
			double t = res[0];
			numPopedFromOpenList = res[1];
			if(t == FOUND_SOLUTION) {
				return Optional.of(new SolutionData(path.pop(), (int)(res[1])));
			}
			if(t == Double.MAX_VALUE) {
				return Optional.empty();
			}
			
			bound = t;
		}
	} 
	
	/***
	 * This function is the idsSearch for the idsStar algorithm
	 * @param puzzle
	 * @return
	 */
	private double[] idaStarSearch(Stack<PuzzleState> path, double gScore, double bound, Puzzle puzzle, double numPopedFromOpenList) {
		PuzzleState node = path.peek();
		double fScore = gScore + hn(node);
		
		if(fScore > bound) {
			return new double[]{fScore,numPopedFromOpenList}; 
		}
		if(puzzle.isEndState(node)) {
			return new double[]{FOUND_SOLUTION,numPopedFromOpenList}; 
		}
		
		double min = Double.MAX_VALUE;
		String[] possibleActions = node.getPossibleActions();
		for(int i=0 ; i<4 ; i++) {
			if(!possibleActions[i].equals(PuzzleState.EMPTY_ACTION)) {
				PuzzleState tempState = node.preformAction(possibleActions[i]);
				if(!path.contains(tempState)) {
					path.push(tempState);
					numPopedFromOpenList++;
					double res[] = idaStarSearch(path, gScore + tempState.getActionCostFromFather(), bound, puzzle, numPopedFromOpenList);
					double t = res[0];
					if(t == FOUND_SOLUTION) {
						return new double[]{FOUND_SOLUTION,numPopedFromOpenList}; 
					}
					if(t < min) {
						min = t;
					}
					
					path.pop();
				}
			}
		}
		
		return new double[]{min,numPopedFromOpenList};
	}
	
	/***
	 * This function search if the searchState is in the past path of srcState
	 * @param srcState
	 * @param serachState
	 * @return
	 */
	private boolean isInPast(PuzzleState srcState, PuzzleState serachState) {
		while(srcState.getFather() != null) {
			if(srcState.getFather().equals(serachState)) {
				return true;
			}
			
			srcState = srcState.getFather();
		}
		
		return false;
	}
	
	/***
	 * This function is the dfbnb algorithm
	 * @param puzzle
	 * @return
	 */
	public Optional<SolutionData> dfbnb(Puzzle puzzle) {
		Stack<PuzzleState> basePath = new Stack<>();
		double bound = Double.MAX_VALUE;
		int numPopedFromOpenList = 0;
		basePath.push(puzzle.getStartState());
		
		
		Optional<SolutionData> res = solveBranch(basePath, bound, puzzle, numPopedFromOpenList);
		if(res.isPresent()) {
			return res;
		}
		
		return Optional.empty();
	}

	/***
	 * This function is the branchSearch method for help util the dfbnb algorithm 
	 * @param puzzle
	 * @return
	 */
	private Optional<SolutionData> solveBranch(Stack<PuzzleState> bestPath, double bound,Puzzle puzzle, int numPopedFromOpenList) {
		PuzzleState node = bestPath.peek();
		if(gn(node) + hn(node) < bound) {
			if(puzzle.isEndState(node)) {
				
				return Optional.of(new SolutionData(node, numPopedFromOpenList));
			}
			
			String[] possibleActions = node.getPossibleActions();
			PriorityQueue<PuzzleState> childs = new PriorityQueue<>(this.puzzleStateComparator);
			for(int i=0 ; i<4 ; i++) {
				if(!possibleActions[i].equals(PuzzleState.EMPTY_ACTION)) {
					PuzzleState tempState = node.preformAction(possibleActions[i]);
					if(!isInPast(node, tempState)) {
						childs.add(tempState);
					}
				}
			}
			while(!childs.isEmpty()) {
				bestPath.push(childs.poll());
				Optional<SolutionData> res = solveBranch(bestPath, bound, puzzle, ++numPopedFromOpenList);
				if(res.isPresent()) {
					return res;
				}
			}
			
		}
		
		return Optional.empty();
    }
}
