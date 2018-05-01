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
	
	private boolean searchInPositions(String[] positions, int position) {
		for(int i=0 ; i<positions.length ; i++) {
			if(Integer.valueOf(positions[i]) == position) {
				return true;
			}
		}
		
		return false;
	}
	
	private PuzzleState buildStartState(int height, int width, String[] redPositions, String[] yellowPositions, List<String> content) {
		int[][] boardValues = new int[height][width];
		int[][] boardColors = new int[height][width];
		
		for(int i=0 ; i<content.size() ; i++) {
			String[] lineElem = content.get(i).split(",");
			for(int j=0 ; j < lineElem.length ; j++) {
				if(lineElem[j].trim().contains("_")) {
					boardValues[i][j] = -1;
				} else {
					boardValues[i][j] = Integer.parseInt(lineElem[j].trim());
				}
			}
		}
		
		for(int i=0 ; i<height; i++) {
			for(int j=0 ; j<width; j++) {
				if(searchInPositions(redPositions, i*width + j + 1)) {
					boardColors[i][j] = PuzzleState.RED;
				}
				else if(searchInPositions(yellowPositions, i*width + j + 1)) {
					boardColors[i][j] = PuzzleState.YELLOW;
				} 
				else {
					boardColors[i][j] = PuzzleState.WHITE;
				}
			}
		}
		
		return new PuzzleState(boardValues, boardColors);
	}
	
	private PuzzleState buildEndState(int height, int width) {
		int[][] boardValues = new int[height][width];
		
		for(int i=0 ; i<height ; i++) {
			for(int j=0 ; j<width; j++ ) {
				if(i == height - 1 && j == width - 1) {
					boardValues[i][j] = -1;
				}
				else {
					boardValues[i][j] = i*width + j + 1;
				}
			}
		}
		
		return new PuzzleState(boardValues, null);
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
			
			start = buildStartState(height, width, redPositions, yellowPositions, lines);
			end = buildEndState(height, width); 
		}

		return new Puzzle(start, end);
	}
}
