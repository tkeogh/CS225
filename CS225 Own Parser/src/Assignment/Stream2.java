package Assignment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;



public class Stream2 {



	public Stream2(){


	}



	public GGA searchStream(int time){

		File stream = new File("gps_2.dat");

		BufferedReader br = null;

		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader(stream));

			while ((sCurrentLine = br.readLine()) != null) {

				String[] p = sCurrentLine.toString().split(",");

				if(p[0].equalsIgnoreCase("$GPGGA")){
					if(time == Integer.parseInt(p[1])){

						System.out.println("---- WE HAVE A MATCH Passed in : " + time + " in file " +p[1]);
						
						GGA newGGA = new GGA();

						newGGA.setTime(Double.parseDouble(p[1]));
						newGGA.setLat(Double.parseDouble(p[2]));
						newGGA.setNS(p[3]);
						newGGA.setLon(Double.parseDouble(p[4]));
						newGGA.setEW(p[5]);
						newGGA.setFix(Integer.parseInt(p[6]));
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
		System.out.println("Could not Find a match woowowowowoowowo");
		return null;
	}





}
