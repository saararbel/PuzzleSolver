import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Main {
	
	
	public static void main(String[] args) throws IOException {
		Puzzle puzzle = new PuzzleBuilder().parseInputFile();
		PuzzleSolver puzzleSolver = new PuzzleSolver();
		
		puzzleSolver.bfs(puzzle);
	}
}
