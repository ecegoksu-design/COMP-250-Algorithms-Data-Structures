import java.awt.Color;

public class PerimeterGoal extends Goal{

	public PerimeterGoal(Color c) {
		super(c);
	}

	@Override
	public int score(Block board) {
		// Flatten block
		Color[][] flattened = board.flatten();

		// Initialize
		int score = 0;
		int size = flattened.length;

		// Size 1
		if (size == 1) {
			if (flattened[0][0].equals(this.targetGoal)) {
				return 2;
			} else {
				return 0;
			}
		}

		for (int i = 0; i < size; i++) {
			if (flattened[0][i].equals(this.targetGoal)) {
				score += (i == 0 || i == size - 1) ? 2 : 1;
			}

			if (flattened[size - 1][i].equals(this.targetGoal)) {
				score += (i == 0 || i == size - 1) ? 2 : 1;
			}

			if (i != 0 && i != size - 1 && flattened[i][0].equals(this.targetGoal)) {
				score += 1;
			}

			if (i != 0 && i != size - 1 && flattened[i][size - 1].equals(this.targetGoal)) {
				score += 1;
			}
		}

		return score;
	}

	@Override
	public String description() {
		return "Place the highest number of " + GameColors.colorToString(targetGoal)
		+ " unit cells along the outer perimeter of the board. Corner cell count twice toward the final score!";
	}

}
