import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main {
	JFrame frame;
	JPanel contentPanel, grid;
	JButton aStar, addEnd, addObst, bfsBtn, dfsBtn, clearBoardBtn;
	int START = 1, END = 2, OBST = 3;
	int tracker = 3;
	Boolean mouseP, pathDrawn = false;
	JPanel start, end = null;
	String startL = "10,10", endL = "890,890";
	Map<String, Point> maze;
	private Color pathColor = Color.gray;
	private Color clearColor = Color.black;

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		Main main = new Main();
	}

	public Main() {
		maze = new HashMap<>();
		mouseP = false;
		frame = new JFrame();
		frame.setTitle("Lets find a path");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPanel = new JPanel();
		contentPanel.setLayout(null);
		frame.setSize(960, 1010);
		frame.setContentPane(contentPanel);
		addMenuButtons();
		addGrid();
		frame.setVisible(true);
	}

	public void addMenuButtons() {
		aStar = new JButton("A* coming soon");
		aStar.setEnabled(false);
		aStar.setBounds(300, 20, 120, 30);
		addEnd = new JButton("UNUSED");
		addEnd.setEnabled(false);
		addEnd.setBounds(450, 20, 120, 30);
		addObst = new JButton("Gen Maze");
		addObst.setBounds(600, 20, 120, 30);
		bfsBtn = new JButton("BFS");
		bfsBtn.setBounds(20, 20, 120, 30);
		dfsBtn = new JButton("DFS");
		dfsBtn.setBounds(150, 20, 120, 30);
		clearBoardBtn = new JButton("Clear board");
		clearBoardBtn.setBounds(750, 20, 120, 30);

		aStar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		addEnd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});

		addObst.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Thread thread = new Thread(new Runnable() {

					@Override
					public void run() {
						clearBoard();
						pause(500);
						createMaze();
					}
				});
				thread.start();

			}
		});

		bfsBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (startL.equals("") || endL.equals("")) {
					return;
				}
				Thread thread = new Thread(new Runnable() {

					@Override
					public void run() {
						clearPath();
						pause(500);
						findPathBFS();
					}
				});
				thread.start();

			}
		});

		dfsBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (startL.equals("") || endL.equals("")) {
					return;
				}
				Thread thread = new Thread(new Runnable() {

					@Override
					public void run() {
						clearPath();
						pause(500);
						findPathDFS();
					}
				});
				thread.start();

			}
		});

		clearBoardBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				clearBoard();

			}
		});

		contentPanel.add(aStar);
		contentPanel.add(addEnd);
		contentPanel.add(addObst);
		contentPanel.add(bfsBtn);
		contentPanel.add(dfsBtn);
		contentPanel.add(clearBoardBtn);
	}

	public void addGrid() {
		clearBoard();
		grid = new JPanel();
		grid.setBounds(10, 60, 910, 910);
		grid.setBackground(Color.blue);
		grid.setLayout(null);
		contentPanel.add(grid);
		for (int i = 0; i < 910; i += 10) {
			for (int j = 0; j < 910; j += 10) {
				int x = i;
				int y = j;
				JPanel panel1 = new JPanel();
				panel1.setBounds(i, j, 9, 9);
				maze.put(i + "," + j, new Point(i + "," + j, panel1));
				panel1.setBackground(clearColor);
				panel1.addMouseListener(new MouseListener() {

					@Override
					public void mouseReleased(MouseEvent e) {
						tracker = 3;
						mouseP = false;
					}

					@Override
					public void mousePressed(MouseEvent e) {
						mouseP = true;
						if (panel1 == start) {
							tracker = START;
						} else if (panel1 == end) {
							tracker = END;
						} else {
							panel1.setBackground(Color.red);
						}
						if(pathDrawn) {
							clearPath();
						}
					}

					@Override
					public void mouseExited(MouseEvent e) {

					}

					@Override
					public void mouseEntered(MouseEvent e) {
						if (mouseP) {
							if (tracker == OBST) {
								panel1.setBackground(panel1.getBackground() == Color.red ? clearColor : Color.red);
							} else if (panel1.getBackground() == Color.red) {
								return;
							}
							if (tracker == START) {
								panel1.setBackground(Color.green);
								start.setBackground(clearColor);
								start = panel1;
								startL = x + "," + y;
							}
							if (tracker == END) {
								panel1.setBackground(Color.yellow);
								end.setBackground(clearColor);
								end = panel1;
								endL = x + "," + y;
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
		start = maze.get(startL).panel;
		end = maze.get(endL).panel;
		start.setBackground(Color.green);
		end.setBackground(Color.yellow);
	}

	public void genObsticle(double percent) {
		Random rand = new Random();
		for (Point p : maze.values()) {
			double val = rand.nextDouble();
			if (val <= percent) {
				p.setColor(Color.red);
			}
		}
	}

	public void createMaze() {
		for (int i = 0; i < 910; i += 10) {
			maze.get(i + ",0").setColor(Color.red);
			maze.get(i + ",900").setColor(Color.red);
			maze.get("0," + i).setColor(Color.red);
			maze.get("900," + i).setColor(Color.red);
		}

		createMazeHelper("20,20", new HashSet<String>(), new Random(), "l");
	}

	public void createMazeHelper(String loc, Set<String> seen, Random rand, String to) {
		pause(5);
		String[] yx = loc.split(",");
		int x = Integer.parseInt(yx[0]);
		int y = Integer.parseInt(yx[1]);
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				addWall(maze.get(loc));
				try {
					switch (to) {
					case "n":
						addWall(maze.get((x - 10) + "," + y));
						break;
					case "e":
						addWall(maze.get((x) + "," + (y - 10)));
						break;
					case "s":
						addWall(maze.get((x + 10) + "," + (y)));
						break;
					case "w":
						addWall(maze.get((x) + "," + (y + 10)));
						break;
					}

				} catch (NullPointerException e) {

				}
			}
		});
		thread.start();

		List<String> whereToNext = new ArrayList<String>();
		List<String> toA = new ArrayList<String>();
		// down
		String s = x + "," + (y + 20);
		if (y + 20 < 900 && !seen.contains(s) && !isWall(s)) {
			whereToNext.add(s);
			seen.add(s);
			toA.add("s");
		}
		// up
		String n = x + "," + (y - 20);
		if (y - 20 >= 0 && !seen.contains(n) && !isWall(n)) {
			whereToNext.add(n);
			seen.add(n);
			toA.add("n");
		}
		// left
		String w = (x - 20) + "," + y;
		if (x - 20 >= 0 && !seen.contains(w) && !isWall(w)) {
			whereToNext.add(w);
			seen.add(w);
			toA.add("w");
		}
		// right
		String e = (x + 20) + "," + y;
		if (x + 20 < 900 && !seen.contains(e) && !isWall(e)) {
			whereToNext.add(e);
			seen.add(e);
			toA.add("e");
		}

		List<Integer> p = new ArrayList<>();
		for (int i = 0; i < whereToNext.size(); i++) {
			p.add(i);
		}
		Collections.shuffle(p);

		for (Integer i : p) {
			createMazeHelper(whereToNext.get(i), seen, rand, toA.get(i));
		}

	}

	private boolean isWall(String loc) {
		return maze.get(loc).getColor() == Color.red;
	}

	public void findPathBFS() {
		Queue<String> qu = new LinkedList<String>();
		Set<String> seen = new HashSet<>();
		qu.add(startL);
		while (!qu.isEmpty()) {
			String str = qu.poll();
			if (!seen.contains(str)) {
				seen.add(str);
				if (!str.equals(startL)) {
					pause(1);
					drawSearchedPoint(maze.get(str));
				}

				// add all children ( up right down left)

				String[] yx = str.split(",");
				int x = Integer.parseInt(yx[0]);
				int y = Integer.parseInt(yx[1]);
				// down
				String down = x + "," + (y + 10);
				if (y + 10 < 910 && !seen.contains(down) && !isWall(down)) {
					if (down.equals(endL)) {
						maze.get(down).predecessor = maze.get(str);
						break;
					}
					if (Math.min(maze.get(down).weight, maze.get(str).weight) == maze.get(down).weight) {
						maze.get(down).predecessor = maze.get(str);
						maze.get(down).weight = maze.get(str).weight;
					}
					qu.add(down);
				}
				// up
				String up = x + "," + (y - 10);
				if (y - 10 >= 0 && !seen.contains(up) && !isWall(up)) {
					if ((up).equals(endL)) {
						maze.get(up).predecessor = maze.get(str);
						break;
					}
					if (Math.min(maze.get(up).weight, maze.get(str).weight) == maze.get(up).weight) {
						maze.get(up).predecessor = maze.get(str);
						maze.get(up).weight = maze.get(str).weight;
					}
					qu.add(up);
				}
				// left
				String left = (x - 10) + "," + y;
				if (x - 10 >= 0 && !seen.contains(left) && !isWall(left)) {
					if ((left).equals(endL)) {
						maze.get(left).predecessor = maze.get(str);
						break;
					}
					if (Math.min(maze.get((x - 10) + "," + y).weight, maze.get(str).weight) == maze.get(left).weight) {
						maze.get(left).predecessor = maze.get(str);
						maze.get(left).weight = maze.get(str).weight;
					}
					qu.add(left);
				}
				// right
				String right = (x + 10) + "," + y;
				if (x + 10 < 910 && !seen.contains(right) && !isWall(right)) {
					if (((x + 10) + "," + y).equals(endL)) {
						maze.get(right).predecessor = maze.get(str);
						break;
					}
					if (Math.min(maze.get((x + 10) + "," + y).weight, maze.get(str).weight) == maze.get(right).weight) {
						maze.get(right).predecessor = maze.get(str);
						maze.get(right).weight = maze.get(str).weight;
					}
					qu.add(right);
				}

			}
		}

		drawPath();
	}

	public void findPathDFS() {
		Stack<String> qu = new Stack<String>();
		Set<String> seen = new HashSet<>();
		qu.add(startL);
		while (!qu.isEmpty()) {
			String str = qu.pop();
			if (!seen.contains(str)) {
				seen.add(str);
				if (!str.equals(startL)) {
					pause(5);
					drawSearchedPoint(maze.get(str));
				}
				// add all children (up right down left)

				String[] yx = str.split(",");
				int x = Integer.parseInt(yx[0]);
				int y = Integer.parseInt(yx[1]);
				// down
				String down = x + "," + (y + 10);
				if (y + 10 < 910 && !seen.contains(down) && !isWall(down)) {
					if (down.equals(endL)) {
						maze.get(down).predecessor = maze.get(str);
						break;
					}
					if (Math.min(maze.get(down).weight, maze.get(str).weight) == maze.get(down).weight) {
						maze.get(down).predecessor = maze.get(str);
						maze.get(down).weight = maze.get(str).weight;
					}
					qu.add(down);
				}
				// up
				String up = x + "," + (y - 10);
				if (y - 10 >= 0 && !seen.contains(up) && !isWall(up)) {
					if ((up).equals(endL)) {
						maze.get(up).predecessor = maze.get(str);
						break;
					}
					if (Math.min(maze.get(up).weight, maze.get(str).weight) == maze.get(up).weight) {
						maze.get(up).predecessor = maze.get(str);
						maze.get(up).weight = maze.get(str).weight;
					}
					qu.add(up);
				}
				// left
				String left = (x - 10) + "," + y;
				if (x - 10 >= 0 && !seen.contains(left) && !isWall(left)) {
					if ((left).equals(endL)) {
						maze.get(left).predecessor = maze.get(str);
						break;
					}
					if (Math.min(maze.get((x - 10) + "," + y).weight, maze.get(str).weight) == maze.get(left).weight) {
						maze.get(left).predecessor = maze.get(str);
						maze.get(left).weight = maze.get(str).weight;
					}
					qu.add(left);
				}
				// right
				String right = (x + 10) + "," + y;
				if (x + 10 < 910 && !seen.contains(right) && !isWall(right)) {
					if (((x + 10) + "," + y).equals(endL)) {
						maze.get(right).predecessor = maze.get(str);
						break;
					}
					if (Math.min(maze.get((x + 10) + "," + y).weight, maze.get(str).weight) == maze.get(right).weight) {
						maze.get(right).predecessor = maze.get(str);
						maze.get(right).weight = maze.get(str).weight;
					}
					qu.add(right);
				}

			}
		}
		drawPath();
	}

	public void drawPath() {
		Point current = maze.get(endL);
		Stack<Point> line = new Stack<>();
		while (current.hasPredecessor()) {
			if (!current.predecessor.location.equals(startL)) {
				line.add(current.predecessor);
				current = current.predecessor;
			} else {
				break;
			}
		}
		while (!line.isEmpty()) {
			pause(5);
			line.pop().setColor(Color.PINK);
		}
		pathDrawn = true;
	}

	public void drawSearchedPoint(Point p) {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				p.setColor(Color.cyan);
				pause(100);
				p.setColor(Color.GRAY);
			}
		});
		thread.start();
	}

	public void pause(int millis) {
		try {
			Thread.sleep(millis);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addWall(Point p) {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				if (p.getColor() != Color.green && p.getColor() != Color.yellow) {
					p.setColor(Color.GREEN);
					pause(200);
					p.setColor(Color.red);
				}
			}
		});
		thread.start();
	}

	public void clearBoard() {
		for (Point p : maze.values()) {
			if (p.getColor() != Color.green && p.getColor() != Color.yellow) {
				p.setColor(clearColor);
			}
		}
	}

	public void clearPath() {
		pathDrawn = false;
		for (Point p : maze.values()) {
			if (p.getColor() == pathColor || p.getColor() == Color.pink) {
				p.setColor(clearColor);
			}
		}
	}
}
