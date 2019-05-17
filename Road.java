/*CSC172 PROJECT 3
 * Contributors: Darman Khan (dkhan2), Bahawar Dhillon (bdhillon)
 */
public class Road {
	
	//Edge class 
	
	private Intersection i1;
	private Intersection i2;
	private double weight;
	private static final int EARTH_RADIUS = 6371; // Approx Earth radius in KM
	private double startLat;
	private double startLong;
	private double endLat;
	private double endLong;


	public Road(Intersection i1, Intersection i2) {

		startLat = i1. getLati();
		startLong = i1.getLongi();
		endLat = i2.getLati();
		endLong = i2.getLongi();
		weight = distance(startLat, startLong, endLat, endLong);

	}
	public double getStartLat() {
		return startLat;
	}

	public void setStartLat(double startLat) {
		this.startLat = startLat;
	}

	public double getStartLong() {
		return startLong;
	}

	public void setStartLong(double startLong) {
		this.startLong = startLong;
	}

	public double getEndLat() {
		return endLat;
	}

	public void setEndLat(double endLat) {
		this.endLat = endLat;
	}

	public double getEndLong() {
		return endLong;
	}

	public void setEndLong(double endLong) {
		this.endLong = endLong;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	// Haversine distance implementation obtained from Jason Winn
	// https://github.com/jasonwinn/haversine/blob/master/Haversine.java

	public static double distance(double startLat, double startLong, double endLat, double endLong) {
		double dLat = Math.toRadians((endLat - startLat));
		double dLong = Math.toRadians((endLong - startLong));
		startLat = Math.toRadians(startLat);
		endLat = Math.toRadians(endLat);
		double a = haversin(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversin(dLong);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return EARTH_RADIUS * c; // <-- d
	}

	public static double haversin(double val) {
		return Math.pow(Math.sin(val / 2), 2);
	}

}
