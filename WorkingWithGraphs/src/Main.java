import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
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
	String startL, endL = "";
	Map<String, Point> maze;
	

	public static void main(String[] args) {
		@SuppressWarnings("unused")
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
		searchForPath = new JButton("Search for path");
		searchForPath.setBounds(750,20,120,30);
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
		searchForPath.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Thread thread = new Thread(new Runnable() {
					
					@Override
					public void run() {
						findPathBFS();
					}
				});
				thread.start();
				
				
			}
		});
		contentPanel.add(addStart);
		contentPanel.add(addEnd);
		contentPanel.add(addObst);
		contentPanel.add(searchForPath);
	}

	public void addGrid() {
		grid = new JPanel();
		grid.setBounds(10, 60, 950, 900);
		grid.setBackground(Color.blue);
		grid.setLayout(null);
		contentPanel.add(grid);
		for (int i = 0; i < 950; i += 10) {
			for (int j = 0; j < 900; j += 10) {
				int x = i;
				int y = j;
				JPanel panel1 = new JPanel();
				panel1.setBounds(i, j, 9, 9);
				maze.put(i + "," + j, new Point(i + "," + j, panel1));
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
							if (panel1 == start) {
								panel1.setBackground(panel1.getBackground() == Color.green ? Color.black : Color.green);
							} else {
								panel1.setBackground(Color.green);
								if (start != null) {
									start.setBackground(Color.black);
								}
								start = panel1;
								startL = x + "," + y;
								
							}
						} else if (addE) {
							if (panel1 == end) {
								panel1.setBackground(
										panel1.getBackground() == Color.yellow ? Color.black : Color.yellow);
							} else {
								panel1.setBackground(Color.yellow);
								if (end != null) {
									end.setBackground(Color.black);
								}
								end = panel1;
								endL = x + "," + y;
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
							if (mouseP) {
								panel1.setBackground(panel1.getBackground() == Color.red ? Color.black : Color.red);
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

	public void findPathBFS() {
		//Point vertex = new Point(startL,0);
		Queue<String> qu = new LinkedList<String>();
		Set<String> seen = new HashSet<>();
		qu.add(startL);
		while(!qu.isEmpty()) {
			String str = qu.poll();
			if(!seen.contains(str)) {
				seen.add(str);
				if(!str.equals(startL)) {
					pause(1);
					maze.get(str).setColor(Color.GRAY);
				}
				//add all children
				//up
				//right
				//down
				//left
				String[] yx = str.split(",");
				int x = Integer.parseInt(yx[0]);
				int y = Integer.parseInt(yx[1]);
				//down
				if(y + 10 < 900 && !seen.contains(x + "," + (y+10)) && maze.get(x + "," + (y+10)).getColor() != Color.red) {
					if((x + "," + (y+10)).equals(endL)) {
						maze.get(x + "," + (y+10)).predecessor = maze.get(str);
						break;
					}
					if(Math.min(maze.get(x + "," + (y+10)).weight,maze.get(str).weight) == maze.get(x + "," + (y+10)).weight) {
						maze.get(x + "," + (y+10)).predecessor = maze.get(str);
						maze.get(x + "," + (y+10)).weight = maze.get(str).weight;
					}
					qu.add(x + "," + (y+10));
				}
				//up
				if(y - 10 >= 0 && !seen.contains(x + "," + (y-10)) && maze.get(x + "," + (y-10)).getColor() != Color.red) {
					if((x + "," + (y-10)).equals(endL)) {
						maze.get(x + "," + (y-10)).predecessor = maze.get(str);
						break;
					}
					if(Math.min(maze.get(x + "," + (y-10)).weight,maze.get(str).weight) == maze.get(x + "," + (y - 10)).weight) {
						maze.get(x + "," + (y-10)).predecessor = maze.get(str);
						maze.get(x + "," + (y-10)).weight = maze.get(str).weight;
					}
					qu.add(x + "," + (y-10));
				}
				//left
				if(x - 10 >= 0 && !seen.contains((x-10) + "," + y) && maze.get((x-10) + "," + y).getColor() != Color.red) {
					if(((x - 10) + "," + y).equals(endL)) {
						maze.get((x - 10) + "," + y).predecessor = maze.get(str);
						break;
					}
					if(Math.min(maze.get((x - 10) + "," + y).weight,maze.get(str).weight) == maze.get((x - 10) + "," + y).weight) {
						maze.get((x - 10) + "," + y).predecessor = maze.get(str);
						maze.get((x - 10) + "," + y).weight = maze.get(str).weight;
					}
					qu.add((x-10) + "," + y);
				}
				//right
				if(x + 10 < 950 && !seen.contains((x+10) + "," + y) && maze.get((x+10) + "," + y).getColor() != Color.red) {
					if(((x + 10) + "," + y).equals(endL)) {
						maze.get((x + 10) + "," + y).predecessor = maze.get(str);
						break;
					}
					if(Math.min(maze.get((x + 10) + "," + y).weight,maze.get(str).weight) == maze.get((x + 10) + "," + y).weight) {
						maze.get((x + 10) + "," + y).predecessor = maze.get(str);
						maze.get((x + 10) + "," + y).weight = maze.get(str).weight;
					}
					qu.add((x+10) + "," + y);
				}
			}
		}
		Point current = maze.get(endL);
		while(current.hasPredecessor()) {
			System.out.println("here");
			if(!current.predecessor.location.equals(startL)) {
				pause(10);
				current.predecessor.setColor(Color.PINK);
				current = current.predecessor;
			}
			else {
				System.out.println("help");
				return;
			}
		}
	}
	
	public void pause(int millis) {
		try {
			Thread.sleep(millis);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
