package Assignment;

public class GGA {
	
	private double lat;
	private String NS;
	private double lon;
	private String EW;
	private int fix;
	private int satellites;
	private double precision;
	private double altitude;
	private double seperation;
	private String refandcheck;
	private String check;
	private int station;
	private double time;
	
	public double getTime() {
		return time;
	}
	public void setTime(double time) {
		this.time = time;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public String getNS() {
		return NS;
	}
	public void setNS(String p) {
		NS = p;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public String getEW() {
		return EW;
	}
	public void setEW(String eW) {
		EW = eW;
	}
	public int getFix() {
		return fix;
	}
	public void setFix(int fix) {
		this.fix = fix;
	}
	public int getSatellites() {
		return satellites;
	}
	public void setSatellites(int satellites) {
		this.satellites = satellites;
	}
	public double getPrecision() {
		return precision;
	}
	public void setPrecision(double precision) {
		this.precision = precision;
	}
	public double getAltitude() {
		return altitude;
	}
	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}
	
	public double getSeperation() {
		return seperation;
	}
	public void setSeperation(double seperation) {
		this.seperation = seperation;
	}

	public String getRefandcheck() {
		return refandcheck;
	}
	public void setRefandcheck(String refandcheck) {
		this.refandcheck = refandcheck;
	}
	public String getCheck() {
		return check;
	}
	public void setCheck(String check) {
		this.check = check;
	}
	public int getStation() {
		return station;
	}
	public void setStation(int station) {
		this.station = station;
	}

	
	



}
