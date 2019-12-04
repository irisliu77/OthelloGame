
package a7;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class OthelloGame {
	public static void main(String[] args) {
		//create top level window
		JFrame mainFrame = new JFrame();
		mainFrame.setTitle("Othello");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//create panel for content
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		mainFrame.setContentPane(topPanel);
		
		OthelloWidget tt = new OthelloWidget();
		topPanel.add(tt, BorderLayout.CENTER);
		
		mainFrame.pack();
		mainFrame.setVisible(true);
	}
}
