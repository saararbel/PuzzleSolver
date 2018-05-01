import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PuzzleBuilder {

	public PuzzleBuilder() {
	}

	public Puzzle parseInputFile() throws FileNotFoundException, IOException {
		URL url = getClass().getResource("input.txt");
		PuzzleState start, end;
		try (BufferedReader input = new BufferedReader(new FileReader(new File(url.getPath())))) {
			// algorithm type
			String algorithm = input.readLine();

			// board size
			String dim = input.readLine();
			int height = Integer.valueOf(dim.split("x")[0]);
			int width = Integer.valueOf(dim.split("x")[1]);

			// red positions
			String redPositionsString = input.readLine();
			String[] redPositions = redPositionsString.split(":")[1].split(" ")[1].split(",");

			// yellow positions
			String yellowPositionsString = input.readLine();
			String[] yellowPositions = yellowPositionsString.split(":")[1].split(" ")[1].split(",");
			
			List<String> lines = new ArrayList<>();
			String line;
			while((line = input.readLine()) != null ) {
				lines.add(line);
			}
			
			start = new PuzzleState(height, width, redPositions, yellowPositions, lines);
			end = new PuzzleState(height, width, redPositions, yellowPositions, lines);
		}

		return new Puzzle(start, end);
	}
}
