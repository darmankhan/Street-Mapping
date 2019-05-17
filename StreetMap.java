/*CSC172 PROJECT 3
 * Contributors: Darman Khan (dkhan2), Bahawar Dhillon (bdhillon)
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;

import javax.swing.*;

public class StreetMap extends JComponent {


	protected static double dis;
	protected static Intersection minX;
	protected static Intersection minY;
	protected static Intersection maxX;
	protected static Intersection maxY;
	protected static boolean show;
	protected static boolean directions;
	protected static Object color = "CYAN";
	protected static Object bgcolor = "DARK";
	protected static double maxLongi;
	protected static double maxLati;
	protected static double minLati;
	protected static double minLongi;
	//Hashmaps to store intersections, adjacency lists, and keep track of visited vertices with 
	//their previous vertex and shortest distances. Keys are the names(string).
	protected static HashMap<String, Intersection> intersections = new HashMap<>();
	protected static HashMap<String, LinkedList<Intersection>> Graph = new HashMap<>();
	protected static HashMap<String, Boolean> visited = new HashMap<>();
	protected static HashMap<String, String> prev = new HashMap<>();
	protected static HashMap<String, Double> distances = new HashMap<>();
	protected static HashMap<String, Road> roads = new HashMap<>();
	protected static ArrayList<String> path;


	public void paintComponent(Graphics g) {

		double rangex = xRange();
		double rangey = yRange();

		//draws map if 'show' is passed
		if (show == true) {
			if (bgcolor.equals("DARK")) {
				g.setColor(new Color(105, 105, 105));
				g.fillRect(0, 0, getWidth(), getHeight());
				g.setColor(Color.WHITE);

			} else {
				g.setColor(new Color(243, 242, 242));
				g.fillRect(0, 0, getWidth(), getHeight());
				g.setColor(Color.BLACK);
			}

			// draw the map
			for (Road r : roads.values()) {

				//gives coordinates to plot relative to the window's size
				double lat1 = this.getHeight() - ((this.getHeight() * (r.getStartLat() - minY.getLati())) / rangey);
				double long1 = this.getWidth() * (r.getStartLong() - minX.getLongi()) / rangex;
				double lat2 = this.getHeight() - ((this.getHeight() * (r.getEndLat() - minY.getLati())) / rangey);
				double long2 = this.getWidth() * (r.getEndLong() - minX.getLongi()) / rangex;
				g.drawLine((int) long1, (int) lat1, (int) long2, (int) lat2);
			}
		}

		//if 'directions' is passed 
		if (directions == true) {

			// draw the start and end spots
			double _lat = this.getHeight() - ((this.getHeight() * (intersections.get(path.get(path.size() - 1)).getLati() - minY.getLati()))/ rangey);
			double _long = (this.getWidth()* (intersections.get(path.get(path.size() - 1)).getLongi() - (minX.getLongi()))) / rangex;
			g.setColor(new Color(255, 165, 0));
			g.fillOval((int) _long - 6, (int) _lat, 15, 15);
			double _lat2 = this.getHeight() - ((this.getHeight() * (intersections.get(path.get(0)).getLati() - minY.getLati())) / rangey);
			double _long2 = (this.getWidth() * (intersections.get(path.get(0)).getLongi() - (minX.getLongi())))/ rangex;
			g.fillOval((int) _long2 - 6, (int) _lat2, 15, 15);

			//set color of path
			if (color.equals("CYAN")) {
				g.setColor(Color.CYAN);
			} else if (color.equals("GREEN")) {
				g.setColor(Color.GREEN);
			} else if (color.equals("PURPLE")) {
				g.setColor(new Color(128, 0, 128));
			} else if (color.equals("YELLOW")) {
				g.setColor(Color.YELLOW);
			} else if (color.equals("RED")) {
				g.setColor(Color.RED);
			} else if (color.equals("BLUE")) {
				g.setColor(Color.BLUE);
			}
			// draw the path
			for (int i = 0; i < path.size() - 1; i++) {
				double lat1 = this.getHeight()- ((this.getHeight() * (intersections.get(path.get(i)).getLati() - minY.getLati())) / rangey);
				double long1 = (this.getWidth() * (intersections.get(path.get(i)).getLongi() - (minX.getLongi())))/ rangex;
				double lat2 = this.getHeight()- ((this.getHeight() * (intersections.get(path.get(i + 1)).getLati() - minY.getLati()))
								/ rangey);
				double long2 = (this.getWidth() * (intersections.get(path.get(i + 1)).getLongi() - (minX.getLongi())))/ rangex;
				Graphics2D t = (Graphics2D) g;
				t.setStroke(new BasicStroke(3));
				t.drawLine((int) long1, (int) lat1, (int) long2, (int) lat2);
			}

		}

	}

	public static void main(String[] args) {

		File input = new File(args[0]);
		String point1 = null;
		String point2 = null;
		

		ArrayList<String> argslist = new ArrayList<String>();
		for (String temp : args) {
			argslist.add(temp);
		}
		if (argslist.contains("--show")) {
			show = true;
		}
		if (argslist.contains("--directions")) {
			directions = true;
			int index = argslist.indexOf("--directions");
			point1 = args[index + 1];
			point2 = args[index + 2];
		}

		Scanner scx = null;

		try {
			scx = new Scanner(input);

			while (scx.hasNext()) {
				String line = scx.nextLine();
				String[] inputs = line.split("\t");

				if (inputs[0].equals("i")) {
					distances.put(inputs[1], (double) Integer.MAX_VALUE);
					Intersection myInt = new Intersection(inputs[1], Double.parseDouble(inputs[2]),
							Double.parseDouble(inputs[3]));
					intersections.put(inputs[1], myInt);
					LinkedList<Intersection> myNodeList = new LinkedList<>();
					Graph.put(inputs[1], myNodeList);
					visited.put(inputs[1], false);

				}

				else if (inputs[0].equals("r")) {

					(Graph.get(inputs[2])).add(intersections.get(inputs[3]));
					(Graph.get(inputs[3])).add(intersections.get(inputs[2]));

					Road road = new Road(intersections.get(inputs[2]), intersections.get(inputs[3]));
					roads.put(inputs[1], road);
				}

			}
			scx.close();

		}

		catch (FileNotFoundException e) {
			e.printStackTrace();
		}


		if (directions == true) {
			path = dijktras(point1, point2);
		}
		if (show == true) {
			new Canvas().setVisible(true);
		}

	}

	//gives the index of the minimum distance vertex in the arraylist
	public static int extractMin(ArrayList<String> pq) {
		int min_index = 0;
		double min_weight = distances.get((pq.get(0)));
		String min_vertex = null;
		for (int i = 0; i < pq.size(); i++) {
			if (distances.get(pq.get(i)) < min_weight) {
				min_weight = distances.get((pq.get(i)));
				min_vertex = pq.get(i);
				min_index = i;
			}
		}
		return min_index;

	}

	//implements Dijktra's algorithm and uses an arraylist to act as a priority queue 
	public static ArrayList<String> dijktras(String v1, String v2) {
		distances.put(v1, 0.0);
		visited.put(v1, true);
		ArrayList<String> pq = new ArrayList<>();
		for (Intersection i : Graph.get(v1)) {
			prev.put(i.getName(), v1);
			Double dis = new Road(i, intersections.get(v1)).getWeight();
			distances.put(i.getName(), dis);
			pq.add(i.getName());

		}

		while (pq.size() > 0) {
			String v = pq.remove(extractMin(pq));
			visited.put(v, true);
			for (Intersection n : Graph.get(v)) {
				if (visited.get(n.getName()) == false) {
					if (pq.contains(n.getName()) == false) {
						pq.add(n.getName());
					}
					Road road = new Road(intersections.get(v), n);
					relax(v, n.getName(), road.getWeight());
					prev.put(n.getName(), v);
					visited.put(n.getName(), true);

				}

			}
			if (v.equals(v2)) {
				break;
			}
		}
		ArrayList<String> pathlist = new ArrayList<>();
		
		// trace back path and add to pathlist
		for (String string = v2; string != null; string = prev.get(string)) {
			pathlist.add(string);
		}
		Collections.reverse(pathlist);

		System.out.println("YOUR PATH: ");
		if (pathlist.size() == 1) {
			System.out.println("The intersections are not connected.");
		} else {
			for (int i = 0; i < pathlist.size(); i++) {
				if (i != pathlist.size() - 1) {
					System.out.print(pathlist.get(i) + " -> ");
				} else {
					System.out.println(pathlist.get(i));
				}
			}
			System.out.println();

		}
		dis = distances.get(v2);
		System.out.println("DISTANCE (miles): " + String.format("%.5g%n", dis));
		System.out.println();

		return pathlist;

	}

	public static void relax(String u, String v, double w) {
		if (distances.get(u) + w < distances.get(v)) {
			distances.put(v, distances.get(u) + w);
			prev.put(v, u);
		}
	}


	//calculates the range of the latitudes to help with window size
	public static double yRange() {
		String[] arr = intersections.keySet().toArray(new String[intersections.size()]);
		maxY = intersections.get(arr[0]);
		minY = intersections.get(arr[0]);

		for (String s : intersections.keySet()) {

			if (intersections.get(s).getLati() > maxY.getLati()) {
				maxY = intersections.get(s);
			} else if (intersections.get(s).getLati() < minY.getLati()) {
				minY = intersections.get(s);
			}
		}
		double rangey = maxY.getLati() - minY.getLati();
		return rangey;
	}
	
	//calculates the range of the longitudes to help with window size
	public static double xRange() {
		String[] arr = intersections.keySet().toArray(new String[intersections.size()]);
		maxX = intersections.get(arr[0]);
		minX = intersections.get(arr[0]);

		for (String s : intersections.keySet()) {
			if (intersections.get(s).getLongi() > maxX.getLongi()) {
				maxX = intersections.get(s);
			} else if (intersections.get(s).getLongi() < minX.getLongi()) {
				minX = intersections.get(s);
			}
		}
		double rangex = maxX.getLongi() - minX.getLongi();
		return rangex;
	}

	//resets the hashmaps for distance, visited vertices and previous of vertices
	public static void reset() {
		for (String s : distances.keySet()) {
			distances.put(s, (double) Integer.MAX_VALUE);
		}
		for (String s : visited.keySet()) {
			visited.put(s, false);
		}
		for (String s : prev.keySet()) {
			prev.put(s, null);
		}
	}

}

/* EXTRA CREDIT: We have implemented GUI in addition to just plotting the map for extra credit. The map is now 
 * interactive and allows the user to input new start and end points, choose the color of the background and choose
 * the color of the path. It then repaints the map according to the new inputs.
 */
class Canvas extends JFrame implements ActionListener {

	private static JButton button1;
	private static JComboBox<Object> box1;
	private static JComboBox<Object> box2;
	private static JLabel label1, label2, label3, label4;
	protected JTextField field1, field2, field3;
	static int x1 = 0;
	static int y1;
	StreetMap map;

	private static final long serialVersionUID = 1L;

	public Canvas() {
		setTitle("Map");
		setLayout(new BorderLayout());
		setLayout(new BorderLayout()); // BorderLayout used
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel(); // JPanel panel to contain the buttons and canvas
		setSize(1000, 1000);

		y1 = this.getHeight();
		label1 = new JLabel("Start Point");
		panel.add(label1);

		field1 = new JTextField(6);
		field1.addActionListener(this);
		panel.add(field1);

		label2 = new JLabel("End Point");
		panel.add(label2);

		field2 = new JTextField(6);
		field2.addActionListener(this);
		panel.add(field2);

		JLabel color = new JLabel("PATH COLOR");
		panel.add(color);
		String[] colors = { "CYAN", "GREEN", "PURPLE", "YELLOW", "RED", "BLUE" };
		box1 = new JComboBox<Object>(colors);
		box1.addActionListener(this);
		panel.add(box1);

		JLabel bg = new JLabel("BACKGROUND");
		panel.add(bg);
		String[] bgs = { "DARK", "LIGHT" };
		box2 = new JComboBox<Object>(bgs);
		box2.addActionListener(this);
		panel.add(box2);

		label3 = new JLabel("DISTANCE (miles) ");
		panel.add(label3);

		label4 = new JLabel();
		panel.add(label4);
		if (StreetMap.directions == true) {
			label4.setText(String.format("%.5g%n", StreetMap.dis));
		}

		button1 = new JButton("SHOW PATH");
		button1.addActionListener(this);
		panel.add(button1);

		add(panel, BorderLayout.NORTH);
		map = new StreetMap();
		map.setPreferredSize(new Dimension(getWidth(), getHeight()));
		add(map, BorderLayout.CENTER);
		pack();
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String s = e.getActionCommand();
		if (s.equals("SHOW PATH")) {

			StreetMap.reset();
			String a = field1.getText();
			String b = field2.getText();
			StreetMap.path = StreetMap.dijktras(a, b);
			StreetMap.directions = true;
			label4.setText(String.format("%.5g%n", StreetMap.dis));
			repaint();

		}
		StreetMap.color = box1.getSelectedItem();
		StreetMap.bgcolor = box2.getSelectedItem();
		repaint();

	}
}
