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
	
	public static void main(String[] args) throws IOException {
		Puzzle puzzle = new PuzzleBuilder().parseInputFile();
		PuzzleSolver puzzleSolver = new PuzzleSolver();
		
		Optional<SolutionData> solution = puzzleSolver.dfbnb(puzzle);
		if(solution.isPresent()) {
			writeOutputFile(solution.get().getEndState(), solution.get().getNumPopedFromOpenList());
		}
		else {
			System.out.println("There is no solution");
		}
	}
}
