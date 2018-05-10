import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.Stack;

public class Ex1 {
	
	public static void writeOutputFile(PuzzleState state, int numPopedFromOpenList) throws FileNotFoundException, UnsupportedEncodingException {
		double cost = 0.0;
		Stack<String> solution = new Stack<>();
		while(state.getFather() != null) {
			cost += state.getActionCostFromFather();
			solution.push(state.getActionFromFather());
			state = state.getFather();
		}
		
		PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
		
		while(!solution.isEmpty()) {
			String move = solution.pop().substring(0, 1);
			if(solution.isEmpty()) {
				writer.print(move);
			} else {
				writer.print(move + "-");
			}
		}
		
		writer.println("\nNum: " + numPopedFromOpenList);
		writer.print("Cost: " + cost);
		writer.close();
	}
	
	public static void writeNoSolutionOutputFile() throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
		writer.println("There is no solution");
		writer.close();
	}
	
	public static void main(String[] args) throws IOException {
		PuzzleBuilder builder = new PuzzleBuilder();
		Puzzle puzzle = builder.parseInputFile();
		PuzzleSolver puzzleSolver = new PuzzleSolver();
		
		String algoType = builder.getAlgorithmType().trim();
		Optional<SolutionData> solution = Optional.empty();
		if(algoType.equals("1")) {
			solution = puzzleSolver.bfs(puzzle);
		} else if(algoType.equals("2")) {
			solution = puzzleSolver.dfid(puzzle);
		} else if(algoType.equals("3")) {
			solution = puzzleSolver.aStar(puzzle);
		} else if(algoType.equals("4")) {
			solution = puzzleSolver.idaStar(puzzle);
		} else if(algoType.equals("5")) {
			solution = puzzleSolver.dfbnb(puzzle);
		} 
		
		if(solution.isPresent()) {
			writeOutputFile(solution.get().getEndState(), solution.get().getNumPopedFromOpenList());
		}
		else {
			writeNoSolutionOutputFile();
		}
	}
}
