package a7;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class OthelloWidget extends JPanel implements ActionListener, SpotListener {
	private enum Player {
		BLACK, WHITE
	};

	private JSpotBoard board;
	private JLabel message;
	private Player nextPlayer;
	private Color playerColor;
	private boolean[] directions = new boolean[8];
	private int[] count = new int[8];
	private int blackScore = 2;
	private int whiteScore = 2;

	public OthelloWidget() {
		board = new JSpotBoard(8, 8);
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if ((i + j) % 2 == 0) {
					board.getSpotAt(i, j).setBackground(new Color(0.8f, 0.8f, 0.8f));
				} else {
					board.getSpotAt(i, j).setBackground(new Color(0.5f, 0.5f, 0.5f));
				}
			}
		}
		message = new JLabel();
		setLayout(new BorderLayout());
		add(board, BorderLayout.CENTER);
		JPanel resetMessagePanel = new JPanel();
		resetMessagePanel.setLayout(new BorderLayout());
		JButton resetButton = new JButton("Restart");
		resetButton.addActionListener(this);
		resetMessagePanel.add(resetButton, BorderLayout.EAST);
		resetMessagePanel.add(message, BorderLayout.CENTER);
		add(resetMessagePanel, BorderLayout.SOUTH);
		board.addSpotListener(this);
		resetGame();
	}

	private void resetGame() {
		for (Spot s : board) {
			s.clearSpot();
		}
		board.getSpotAt(3, 3).setSpot();
		board.getSpotAt(3, 3).setSpotColor(Color.WHITE);
		board.getSpotAt(3, 4).setSpot();
		board.getSpotAt(3, 4).setSpotColor(Color.BLACK);
		board.getSpotAt(4, 3).setSpot();
		board.getSpotAt(4, 3).setSpotColor(Color.BLACK);
		board.getSpotAt(4, 4).setSpot();
		board.getSpotAt(4, 4).setSpotColor(Color.WHITE);
		nextPlayer = Player.BLACK;
		playerColor = Color.BLACK;
		for (int i = 0; i < 8; i++) {
			directions[i] = false;
			count[i] = 0;
		}
		blackScore = 2;
		whiteScore = 2;
		message.setText("Welcome to Othello. Black" + " to play.");

	}

	@Override
	public void spotClicked(Spot spot) {
		if (!spot.isEmpty() || !spot.isHighlighted()) {
			return;
		}
		if (nextPlayer == Player.BLACK) {

			spot.setSpot();
			spot.setSpotColor(Color.BLACK);
			paint(spot);
			playerColor = Color.WHITE;
			nextPlayer = Player.WHITE;
			message.setText("White to play.");
			if (!hasHighlighted()) {
				playerColor = Color.BLACK;
				nextPlayer = Player.BLACK;
				message.setText("Black to play");
			}
			;

		} else {
			spot.setSpot();
			spot.setSpotColor(Color.WHITE);
			paint(spot);
			playerColor = Color.BLACK;
			nextPlayer = Player.BLACK;
			message.setText("Black to play");
			if (!hasHighlighted()) {
				playerColor = Color.WHITE;
				nextPlayer = Player.WHITE;
				message.setText("White to play.");
			}
		}
		if (blackScore + whiteScore == 64 || whiteScore == 0 || blackScore == 0) {
			if (blackScore > whiteScore) {
				message.setText("Game Over." + "Black wins. Score: " + whiteScore + " to " + blackScore);
			} else if (blackScore < whiteScore) {
				message.setText("Game Over." + "White wins. Score: " + whiteScore + " to " + blackScore);
			} else {
				message.setText("Game Over." + "Draw game. Score: " + whiteScore + " to " + blackScore);
			}
		}
	}

	private boolean hasHighlighted() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {

				Spot s = board.getSpotAt(i, j);
				if (s.isHighlighted()) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void spotEntered(Spot spot) {
		if (!spot.isEmpty()) {
			spot.unhighlightSpot();
		} else {
			for (int i = 0; i < 8; i++) {
				directions[i] = false;
				count[i] = 0;
			}
			checkPath(spot);
			if (findSpot(spot)) {
				spot.highlightSpot();
			}

		}
	}

	@Override
	public void spotExited(Spot spot) {
		spot.unhighlightSpot();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		resetGame();
	}

	private void checkPath(Spot spot) {
		for (int i = 0; i < 8; i++) {
			directions[i] = false;
			count[i] = 0;
		}
		Color opp = playerColor == Color.BLACK ? Color.WHITE : Color.BLACK;
		// to right
		if (spot.getSpotX() + 1 < 8 && board.getSpotAt(spot.getSpotX() + 1, spot.getSpotY()).isEmpty() == false
				&& board.getSpotAt(spot.getSpotX() + 1, spot.getSpotY()).getSpotColor() == opp) {
			directions[0] = true;
		}
		// to left
		if (spot.getSpotX() - 1 >= 0 && board.getSpotAt(spot.getSpotX() - 1, spot.getSpotY()).isEmpty() == false
				&& board.getSpotAt(spot.getSpotX() - 1, spot.getSpotY()).getSpotColor() == opp) {
			directions[1] = true;
		}
		// down
		if (spot.getSpotY() + 1 < 8 && board.getSpotAt(spot.getSpotX(), spot.getSpotY() + 1).isEmpty() == false
				&& board.getSpotAt(spot.getSpotX(), spot.getSpotY() + 1).getSpotColor() == opp) {
			directions[2] = true;
		}
		// up
		if (spot.getSpotY() - 1 >= 0 && board.getSpotAt(spot.getSpotX(), spot.getSpotY() - 1).isEmpty() == false
				&& board.getSpotAt(spot.getSpotX(), spot.getSpotY() - 1).getSpotColor() == opp) {
			directions[3] = true;
		}
		// right down
		if (spot.getSpotX() + 1 < 8 && spot.getSpotY() + 1 < 8
				&& board.getSpotAt(spot.getSpotX() + 1, spot.getSpotY() + 1).isEmpty() == false
				&& board.getSpotAt(spot.getSpotX() + 1, spot.getSpotY() + 1).getSpotColor() == opp) {
			directions[4] = true;
		}
		// left up
		if (spot.getSpotX() - 1 >= 0 && spot.getSpotY() - 1 >= 0
				&& board.getSpotAt(spot.getSpotX() - 1, spot.getSpotY() - 1).isEmpty() == false
				&& board.getSpotAt(spot.getSpotX() - 1, spot.getSpotY() - 1).getSpotColor() == opp) {
			directions[5] = true;
		}
		// right up
		if (spot.getSpotX() + 1 < 8 && spot.getSpotY() - 1 >= 0
				&& board.getSpotAt(spot.getSpotX() + 1, spot.getSpotY() - 1).isEmpty() == false
				&& board.getSpotAt(spot.getSpotX() + 1, spot.getSpotY() - 1).getSpotColor() == opp) {
			directions[6] = true;
		}
		// left down
		if (spot.getSpotX() - 1 >= 0 && spot.getSpotY() + 1 < 8
				&& board.getSpotAt(spot.getSpotX() - 1, spot.getSpotY() + 1).isEmpty() == false
				&& board.getSpotAt(spot.getSpotX() - 1, spot.getSpotY() + 1).getSpotColor() == opp) {
			directions[7] = true;
		}
	}

	private boolean findSpot(Spot spot) {
		int x = spot.getSpotX();
		int y = spot.getSpotY();
		Color c = playerColor == Color.WHITE ? Color.BLACK : Color.WHITE;
		if (directions[0] == true) {
			int i = 1;
			while (x + i < 7 && board.getSpotAt(x + i, y).getSpotColor() == c) {
				i++;
			}
			if (board.getSpotAt(x + i, y).getSpotColor() == playerColor) {
				count[0] += (i - 1);
			}
			if (count[0] > 0) {
				spot.highlightSpot();
			} else {
				count[0] = 0;
			}
		}
		if (directions[1] == true) {
			int i = 1;
			while (x - i > 0 && board.getSpotAt(x - i, y).getSpotColor() == c) {
				i++;
			}
			if (board.getSpotAt(x - i, y).getSpotColor() == playerColor) {
				count[1] += (i - 1);
			}
			if (count[1] > 0) {
				spot.highlightSpot();
			} else {
				count[1] = 0;
			}
		}
		if (directions[2] == true) {
			int i = 1;
			while (y + i < 7 && board.getSpotAt(x, y + i).getSpotColor() == c) {
				i++;
			}
			if (board.getSpotAt(x, y + i).getSpotColor() == playerColor) {
				count[2] += (i - 1);
			}
			if (count[2] > 0) {
				spot.highlightSpot();
			} else {
				count[2] = 0;
			}
		}

		if (directions[3] == true) {
			int i = 1;
			while (y - i > 0 && board.getSpotAt(x, y - i).getSpotColor() == c) {
				i++;
			}
			if (board.getSpotAt(x, y - i).getSpotColor() == playerColor) {
				count[3] += (i - 1);
			}
			if (count[3] > 0) {
				spot.highlightSpot();
			} else {
				count[3] = 0;
			}
		}

		if (directions[4] == true) {
			int i = 1;
			while (x + i < 7 && y + i < 7 && board.getSpotAt(x + i, y + i).getSpotColor() == c) {
				i++;
			}
			if (board.getSpotAt(x + i, y + i).getSpotColor() == playerColor) {
				count[4] += (i - 1);
			}
			if (count[4] > 0) {
				spot.highlightSpot();
			} else {
				count[4] = 0;
			}
		}

		if (directions[5] == true) {
			int i = 1;
			while (x - i > 0 && y - i > 0 && board.getSpotAt(x - i, y - i).getSpotColor() == c) {
				i++;
			}
			if (board.getSpotAt(x - i, y - i).getSpotColor() == playerColor) {
				count[5] += (i - 1);
			}
			if (count[5] > 0) {
				spot.highlightSpot();
			} else {
				count[5] = 0;
			}
		}

		if (directions[6] == true) {
			int i = 1;
			while (x + i < 7 && y - i > 0 && board.getSpotAt(x + i, y - i).getSpotColor() == c) {
				i++;
			}
			if (board.getSpotAt(x + i, y - i).getSpotColor() == playerColor) {
				count[6] += (i - 1);
			}
			if (count[6] > 0) {
				spot.highlightSpot();
			} else {
				count[6] = 0;
			}
		}

		if (directions[7] == true) {
			int i = 1;
			while (x - i > 0 && y + i < 7 && board.getSpotAt(x - i, y + i).getSpotColor() == c) {
				i++;
			}
			if (board.getSpotAt(x - i, y + i).getSpotColor() == playerColor) {
				count[7] += (i - 1);
			}
			if (count[7] > 0) {
				spot.highlightSpot();
			} else {
				count[7] = 0;
			}
		}
		for (int i = 0; i < 8; i++) {
			if (count[i] > 0) {
				return true;
			}
		}
		return false;
	}

	private void paint(Spot spot) {
		int x = spot.getSpotX();
		int y = spot.getSpotY();
		int sum = 0;
		for (int i = 0; i < 8; i++) {
			sum += count[i];
		}
		sum++;
		if (playerColor == Color.BLACK) {
			blackScore += sum;
			whiteScore -= (sum - 1);
		} else {
			whiteScore += sum;
			blackScore -= (sum - 1);
		}
		while (count[0] > 0) {
			board.getSpotAt(x + count[0], y).setSpotColor(playerColor);
			;
			count[0]--;
		}
		while (count[1] > 0) {
			board.getSpotAt(x - count[1], y).setSpotColor(playerColor);
			;
			count[1]--;
		}
		while (count[2] > 0) {
			board.getSpotAt(x, y + count[2]).setSpotColor(playerColor);
			count[2]--;
		}
		while (count[3] > 0) {
			board.getSpotAt(x, y - count[3]).setSpotColor(playerColor);
			count[3]--;
		}
		while (count[4] > 0) {
			board.getSpotAt(x + count[4], y + count[4]).setSpotColor(playerColor);
			count[4]--;
		}
		while (count[5] > 0) {
			board.getSpotAt(x - count[5], y - count[5]).setSpotColor(playerColor);
			count[5]--;
		}
		while (count[6] > 0) {
			board.getSpotAt(x + count[6], y - count[6]).setSpotColor(playerColor);
			count[6]--;
		}
		while (count[7] > 0) {
			board.getSpotAt(x - count[7], y + count[7]).setSpotColor(playerColor);
			count[7]--;
		}
	}
}