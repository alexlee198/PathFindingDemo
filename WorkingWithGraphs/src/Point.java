import java.awt.Color;

import javax.swing.JPanel;

public class Point {
	String location;
	Point predecessor = null;
	JPanel panel;
	int weight = Integer.MAX_VALUE;
	
	public Point(String location) {
		this.location = location;
	}
	public Point(String location, int weight, JPanel panel) {
		this.location = location;
		this.weight = weight;
		this.panel = panel;
	}
	public Point(String location,JPanel panel) {
		this.location = location;
		this.panel = panel;
	}
	public void setColor(Color color) {
		panel.setBackground(color);
	}
	public Color getColor() {
		return panel.getBackground();
	}
	public boolean hasPredecessor() {
		return predecessor == null? false:true;
	}
	
	
}
