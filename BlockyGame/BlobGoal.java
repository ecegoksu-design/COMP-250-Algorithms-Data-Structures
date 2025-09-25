import java.awt.Color;

public class BlobGoal extends Goal {

	public BlobGoal(Color c) {
		super(c);
	}

	@Override
	public int score(Block board) {
		Color[][] flattened = board.flatten();

		boolean[][] visited = new boolean[flattened.length][flattened[0].length];

		int maximumSize = 0;

		for (int i = 0; i < flattened.length; i++) {
			for (int j = 0; j < flattened[i].length; j++) {
				if (!visited[i][j] && flattened[i][j].equals(this.targetGoal)) {
					int currentSize = undiscoveredBlobSize(i, j, flattened, visited);

					if (currentSize > maximumSize) {
						maximumSize = currentSize;
					}
				}
			}
		}

		return maximumSize;
	}

	@Override
	public String description() {
		return "Create the largest connected blob of " + GameColors.colorToString(targetGoal) + " blocks, anywhere within the block";
	}

	public int undiscoveredBlobSize(int i, int j, Color[][] unitCells, boolean[][] visited) {
		if (i < 0 || i >= unitCells.length || j < 0 || j >= unitCells[0].length) {
			return 0;
		}
		if (visited[i][j] || !unitCells[i][j].equals(this.targetGoal)) {
			return 0;
		}

		visited[i][j] = true;

		int currentSize = 1;

		currentSize += undiscoveredBlobSize(i - 1, j, unitCells, visited);
		currentSize += undiscoveredBlobSize(i + 1, j, unitCells, visited);
		currentSize += undiscoveredBlobSize(i, j - 1, unitCells, visited);
		currentSize += undiscoveredBlobSize(i, j + 1, unitCells, visited);

		return currentSize;
	}
}

