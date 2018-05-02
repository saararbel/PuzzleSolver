import java.io.IOException;
import java.util.Optional;

public class Main {
	
	public static void main(String[] args) throws IOException {
		Puzzle puzzle = new PuzzleBuilder().parseInputFile();
		PuzzleSolver puzzleSolver = new PuzzleSolver();
		
		Optional<SolutionData> solution = puzzleSolver.bfs(puzzle);
		if(solution.isPresent()) {
			puzzleSolver.printSolution(solution.get().getEndState(), solution.get().getNumPopedFromOpenList());
		}
	}
}
