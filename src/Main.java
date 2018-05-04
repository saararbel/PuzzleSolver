import java.io.IOException;
import java.util.Optional;

public class Main {
	
	public static void p(int cell) {
		int columns = 4;
		int realCellRow = ((double)cell/columns) > (double)(cell/columns) ? cell/columns : (cell/columns) - 1;
		int realCellColumn = (cell % columns) == 0 ? columns - 1 : (cell % columns)  - 1;
		
		System.out.println("cell " + cell + " position is: (" + realCellRow + "," + realCellColumn + ")");
	}
	
	public static void main(String[] args) throws IOException {
		Puzzle puzzle = new PuzzleBuilder().parseInputFile();
		PuzzleSolver puzzleSolver = new PuzzleSolver();
		
		Optional<SolutionData> solution = puzzleSolver.aStar(puzzle);
		if(solution.isPresent()) {
			puzzleSolver.printSolution(solution.get().getEndState(), solution.get().getNumPopedFromOpenList());
		}
//		for(int i=1 ; i<=11 ; i++) {
//			p(i);			
//		}
	}
}
