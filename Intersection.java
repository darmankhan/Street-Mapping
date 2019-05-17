/*CSC172 PROJECT 3
 * Contributors: Darman Khan (dkhan2), Bahawar Dhillon (bdhillon)
 */
public class Intersection {
	
	//Vertex class 
	
	private double lati;
	private double longi;
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLati() {
		return lati;
	}

	public void setLati(double lati) {
		this.lati = lati;
	}

	public double getLongi() {
		return longi;
	}

	public void setLongi(double longi) {
		this.longi = longi;
	}

	public Intersection(String IName, double latitude, double longitude) {
		this.lati = latitude;
		this.longi = longitude;
		this.name = IName;
	}

}
