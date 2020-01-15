import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main {
	JFrame frame;
	JPanel contentPanel, grid;
	JButton addStart, addEnd, addObst, searchForPath;
	Boolean addS, addE, addO;
	Boolean mouseP;
	JPanel start, end = null;
	Map<String, JPanel> maze;

	public static void main(String[] args) {
		Main main = new Main();
	}

	public Main() {
		maze = new HashMap<>();
		mouseP = false;
		addS = false;
		addE = false;
		addO = false;
		frame = new JFrame();
		frame.setTitle("Lets find a path");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPanel = new JPanel();
		contentPanel.setLayout(null);
		frame.setSize(1000, 1000);
		frame.setContentPane(contentPanel);
		addMenuButtons();
		addGrid();
		frame.setVisible(true);
	}

	public void addMenuButtons() {
		addStart = new JButton("Add Start");
		addStart.setBounds(300, 20, 120, 30);
		addEnd = new JButton("Add End");
		addEnd.setBounds(450, 20, 120, 30);
		addObst = new JButton("Add Obsticles");
		addObst.setBounds(600, 20, 120, 30);
		addStart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addE = false;
				addS = true;
				addO = false;

			}
		});
		addEnd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addE = true;
				addS = false;
				addO = false;

			}
		});
		addObst.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addE = false;
				addS = false;
				addO = true;
			}
		});
		contentPanel.add(addStart);
		contentPanel.add(addEnd);
		contentPanel.add(addObst);
	}

	public void addGrid() {
		grid = new JPanel();
		grid.setBounds(10, 60, 950, 900);
		grid.setBackground(Color.blue);
		grid.setLayout(null);
		contentPanel.add(grid);
		for (int i = 0; i < 950; i += 10) {
			for (int j = 0; j < 900; j += 10) {
				JPanel panel1 = new JPanel();
				panel1.setBounds(i, j, 9, 9);
				maze.put(i + "," + j, panel1);
				panel1.setBackground(Color.black);
				panel1.addMouseListener(new MouseListener() {

					@Override
					public void mouseReleased(MouseEvent e) {
						mouseP = false;
					}

					@Override
					public void mousePressed(MouseEvent e) {
						mouseP = true;
						if (addS) {
							if(panel1 == start) {
								panel1.setBackground(panel1.getBackground() == Color.green? Color.black:Color.green);
							}
							else {
								panel1.setBackground(Color.green);
								if(start!= null) {
									start.setBackground(Color.black);
									start = panel1;
								}else {
									start = panel1;
								}
							}
						} else if (addE) {
							if(panel1 == end) {
								panel1.setBackground(panel1.getBackground() == Color.yellow? Color.black:Color.yellow);
							}
							else {
								panel1.setBackground(Color.yellow);
								if(end!= null) {
									end.setBackground(Color.black);
									end = panel1;
								}else {
									end = panel1;
								}
							}
						} else if (addO) {
							panel1.setBackground(Color.red);
						}
					}

					@Override
					public void mouseExited(MouseEvent e) {
						
					}

					@Override
					public void mouseEntered(MouseEvent e) {
						if (addO) {
							if(mouseP) {
								panel1.setBackground(panel1.getBackground() == Color.red? Color.black:Color.red);
							}
						}
					}

					@Override
					public void mouseClicked(MouseEvent e) {
						// TODO Auto-generated method stub

					}
				});
				grid.add(panel1);
			}
		}
	}

}
