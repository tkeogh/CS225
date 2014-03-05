package Assignment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Stream {

	private ArrayList<GGA> ggas;
	private ArrayList<Satellite> sats;


	public Stream(){

		ggas = new ArrayList<GGA>();	
		sats = new ArrayList<Satellite>();

		readin();
		//check();


	}

	public void readin(){

		File file = new File("gps_1.dat");		

		BufferedReader br = null;

		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader(file));

			while ((sCurrentLine = br.readLine()) != null) {


				String[] p = sCurrentLine.toString().split(","); //splits current line into array

				if(p[0].equalsIgnoreCase("$GPGGA")){  //Finds a GGA line used for co ordinates


					int pass = countReliable();

					if(pass>3){

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

						ggas.add(newGGA);
					}

				}
				else if(p[0].equalsIgnoreCase("$GPGSV")){


					String gsv[] = sCurrentLine.toString().split(",");
				//	System.out.println(sCurrentLine);



				}
				else if(p[0].equalsIgnoreCase("$GPGSA")){

					String[] gsa = sCurrentLine.toString().split(",");

					for(int i = 3;i < gsa.length - 3;i++){
						if(!gsa[i].isEmpty()){		

							int curr = Integer.parseInt(gsa[i]);
							boolean contains = false;
							for(int j =0;j<sats.size();j++){							
								int	comp = sats.get(j).getId();
								if(comp == curr){
									contains = true;
								}
							}
							if(contains == false){
								Satellite sat = new Satellite();
								sat.setId(curr);
								sats.add(sat);	
							}

						}
					}


				}


			}




		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (br != null)br.close();

			} catch (IOException ex) {

				ex.printStackTrace();
			}
		}

	}

	public void check(){
		for(int i =0;i<ggas.size();i++){
			GGA temp = new GGA();
			temp = ggas.get(i);
			System.out.println(temp.getTime() +" " + temp.getLat() + " " +temp.getLon());
		}


	}

	public int countReliable( ){

		int j=0;
		return j;
	}

}
