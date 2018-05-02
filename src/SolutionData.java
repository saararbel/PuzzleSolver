
public class SolutionData {
		private PuzzleState endState;
		private int numPopedFromOpenList;
		
		public SolutionData(PuzzleState endState, int numPopedFromOpenList) {
			super();
			this.endState = endState;
			this.numPopedFromOpenList = numPopedFromOpenList;
		}
		
		public PuzzleState getEndState() {
			return endState;
		}
		
		public int getNumPopedFromOpenList() {
			return numPopedFromOpenList;
		}
		
	}
