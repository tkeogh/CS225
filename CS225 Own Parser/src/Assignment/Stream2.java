package Assignment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;



public class Stream2 {



	public Stream2(){


	}



	public GGA searchStream(int time){  //take a time

		File stream = new File("gps_2.dat");  //use this file.s

		BufferedReader br = null;

		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader(stream));

			while ((sCurrentLine = br.readLine()) != null) {

				String[] p = sCurrentLine.toString().split(",");

				if(p[0].equalsIgnoreCase("$GPGGA")){
					if(time == Integer.parseInt(p[1])){   //if GGA sentence and time matches go into loop

						System.out.println("---- WE HAVE A MATCH Passed in : " + time + " in file " +p[1]);
						
						GGA newGGA = new GGA();

						newGGA.setTime(Double.parseDouble(p[1]));
						newGGA.setLat(turnToLat(Double.parseDouble(p[2])));
						newGGA.setNS(p[3]);
						if(newGGA.getNS().equals("S")){
							double currLat = newGGA.getLat();
							newGGA.setLat(currLat*-1);
						}
						newGGA.setLon(turnToLon(Double.parseDouble(p[4])));
						newGGA.setEW(p[5]);
						if(newGGA.getEW().equals("W")){
							double currLon = newGGA.getLon();
							newGGA.setLon(currLon*-1);
						}
						newGGA.setFix(Integer.parseInt(p[6]));  //create new GGA and return itf or use in other class.
						newGGA.setSatellites(Integer.parseInt(p[7]));
						newGGA.setPrecision(Double.parseDouble(p[8]));
						newGGA.setAltitude(Double.parseDouble(p[9]));
						newGGA.setSeperation(Double.parseDouble(p[11]));
						
						return newGGA;

					}
				


				}


			}
		}
		catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (br != null)br.close();

			} catch (IOException ex) {

				ex.printStackTrace();
			}

		}
		System.out.println("-----------Could not Find a match-------");
		return null;
	}


	
	public double turnToLat(double lat){
		
		int degrees = (int) lat/100;
		double decDegrees = (lat  - (degrees*100)) / 60;
		double totalLat = degrees + decDegrees;
		return totalLat;
		
		
	}
	
	public double turnToLon(double lon){
		
		
		DecimalFormat df = new DecimalFormat("#.0000");
		String longString = Double.toString(lon);
		
		String stringDegrees = longString.substring(0,longString.indexOf("."));
		
		if(stringDegrees.length() == 3){
			stringDegrees = "00"+stringDegrees;
		}
		if(stringDegrees.length() == 4){
			stringDegrees = "0"+stringDegrees;
		}
	
		String nextSub = stringDegrees.substring(0,3);
		
		
	
		
		int degrees = Integer.parseInt(nextSub);
		double decDegrees = (lon  - (degrees*100)) / 60;
		double totalLon = degrees + decDegrees;
		
		totalLon = Double.parseDouble(df.format(totalLon)); 
		return totalLon;
		
	}



}
