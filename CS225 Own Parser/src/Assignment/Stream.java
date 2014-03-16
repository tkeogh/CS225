package Assignment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Stream {

	private ArrayList<GGA> ggas;
	private ArrayList<Satellite> sats;
	private ArrayList<Integer> satids;
	private Stream2 stream2;
	private gpxwriter gpx;
	private int lasttime;
	private double offset;



	public Stream(){



		ggas = new ArrayList<GGA>();	
		sats = new ArrayList<Satellite>();
		satids = new ArrayList<Integer>();
		stream2 = new Stream2();
		gpx = new gpxwriter();
		readin();          //Parse data essentially
		writeFile();      //Write GPX file.

	}

	public void readin(){

		File file = new File("gps_1.dat");		//Reference to file.

		BufferedReader br = null;

		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader(file));

			while ((sCurrentLine = br.readLine()) != null) {   //while current line isnt null, line set to line buffered reader is on


				String[] p = sCurrentLine.toString().split(","); //splits current line into array

				if(p[0].equalsIgnoreCase("$GPGGA")){  //Finds a GGA line using string array


					int currtime = Math.round(Float.parseFloat(p[1]));   //round the time so its same format as stream 2

					if(currtime - lasttime > 1){   //if time since last succesful add is over a second.


						System.out.println("SECOND MISSING LOOKING IN SECOND STREAM");
						System.out.println("SEARCHING FOR " + (lasttime+1));
						GGA returned = stream2.searchStream(lasttime+1);   //search second stream for a matching time.
						if(returned != null){
							ggas.add(returned);  //add succesful return to array list 
							lasttime = (int) Math.round(returned.getTime());  //update last time.

						}
						else{
							System.out.println("No match found: Possibly due to last recorded time being just under minute.");
						}


					}



					int pass = countReliable(); //counts reliable satellites

					if(pass>=3){

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
						newGGA.setFix(Integer.parseInt(p[6]));
						newGGA.setSatellites(Integer.parseInt(p[7]));   //Put the GGA sentence into object.
						newGGA.setPrecision(Double.parseDouble(p[8]));
						newGGA.setAltitude(Double.parseDouble(p[9]));
						newGGA.setSeperation(Double.parseDouble(p[11]));
						ggas.add(newGGA);
						System.out.println("----------ADDED NEW GGA " + ggas.size() + " IN LIST---------");
						lasttime = (int) Math.round(newGGA.getTime()); //Set last time

						GGA matched = stream2.searchStream(lasttime);
						if(matched != null){  //find matching stream for offset.

							offset = newGGA.getLat() / matched.getLat();  //set new offset based on latitude.
							System.out.println("NEW OFFSET IS : " + offset);
						}



					}
					else{
						System.out.println("================FAILED HERE ======================");
						System.out.println("-----Attempting to pull from other stream -----");
						int time = Math.round(Float.parseFloat(p[1])); //Second stream does not use .0000 like first receiver.
						lasttime = time;
						GGA test = new GGA();
						test = stream2.searchStream(time); //search stream for time when satellites failed quality checks

						if(test != null){		
							DecimalFormat df = new DecimalFormat("#.0000");

							double newlat = test.getLat() * offset;   //include offset
							double newlon = test.getLon() * offset;
							System.out.println("OLD LAT: "+ test.getLat() + " OLD LON : " +test.getLon());
							double formatlat = Double.parseDouble(df.format(newlat));
							double formatlon = Double.parseDouble(df.format(newlon));   //format new values
							System.out.println("NEW LAT: " +formatlat + " NEW LON : " + formatlon);
							test.setLon(formatlon);   //set formatted values
							test.setLat(formatlat);
							ggas.add(test);
							System.out.println("@@@@@@@@@ Added salvaged GGA data. @@@@@@@@@");
						}			
					}
				}
				else if(p[0].equalsIgnoreCase("$GPGSV")){   //on GSV setence - holds satellite snr info

					satids.clear();
					String gsv[] = sCurrentLine.toString().split(",");   //split sentence
					String[] finalone = gsv[gsv.length-1].toString().split("\\*");
					gsv[gsv.length-1] = finalone[0];        //removes checksum so just the satellite snr left.

					int expectedsats   = 0;
					int lineno = Integer.parseInt(gsv[2]);  //get references needed for calculation of satellitess later on
					int satelliteno = Integer.parseInt(gsv[3]);


					int reaminingsats = satelliteno - ((lineno-1) * 4);  //total satellites * satellites already processed

					if(reaminingsats >=4){
						expectedsats = 4;  //if greater than 4, expect 4, 4 is maximum on a line
					}

					for(int i =0;i<expectedsats;i++){
						int position = 4 *(i+1);
						satids.add(Integer.parseInt(gsv[position])); //get each sat id, based on format they follow
					}

					for(int i =0;i<satids.size();i++){
						int currentsat = satids.get(i);
						for(int j =0;j<sats.size();j++){
							int cur = sats.get(j).getId();	
							if(currentsat == cur){         //get current sat and find matching loaded from GSA sentence
								int snrposition = ((i+1)*4)+3;
								int snr = Integer.parseInt(gsv[snrposition]);
								sats.get(j).setSnr(snr);   //find SNR position and set it to the satellite loaded from GSA
							}


						}

					}

				}
				else if(p[0].equalsIgnoreCase("$GPGSA")){   //lines containing which satellites are currently being used to plot points

					String[] gsa = sCurrentLine.toString().split(",");


					for(int i = 3;i < gsa.length - 3;i++){  //the positions in array where satellite ID's are stored
						if(!gsa[i].isEmpty()){		

							int curr = Integer.parseInt(gsa[i]);
							boolean contains = false;
							for(int j =0;j<sats.size();j++){							
								int	comp = sats.get(j).getId();
								if(comp == curr){
									contains = true;  //if satellite is already logged dont do anything so we preserve SNR if its there
								}
							}
							if(contains == false){
								Satellite sat = new Satellite();
								sat.setId(curr);   //if not logged add the satellite
								sats.add(sat);	
							}

						}
					}

					for (int i =0;i<sats.size();i++){

						int curr = sats.get(i).getId();
						boolean contains = false;
						for(int j =3;j<gsa.length -3;j++){
							if(!gsa[j].isEmpty()){
								int compare = Integer.parseInt(gsa[j]);
								if(compare == curr){
									contains = true;  //if the id is contained in the gsa set to true
								}

							}
						}
						if(!contains){
							sats.remove(i);  //if sat no longer active remove from list.
						}
					}


				}

			}




		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (br != null)br.close();
				System.out.println("Stream finished. Thank You!");

			} catch (IOException ex) {

				ex.printStackTrace();
			}
		}

	}

	public void check(){
		for(int i =0;i<ggas.size();i++){
			GGA temp = new GGA();
			temp = ggas.get(i); //Debuggging Method to check reading in of gga's.
			System.out.println(temp.getTime() +" " + temp.getLat() + " " +temp.getLon()+"------");
		}


	}

	public int countReliable(){

		int reliable =0;

		for(int i = 0;i<sats.size();i++){  //counts reliable satellites based on latest information from stream.
			if(sats.get(i).getSnr() >= 30){
				reliable++;
				//System.out.println(reliable + " This is reliable count");   
			}
		}

		return reliable;
	}

	public void writeFile(){

		gpx.writeGPXfile(ggas); //write out ggas to gpx file.

	}

	public double turnToLat(double lat){

		DecimalFormat df = new DecimalFormat("#.0000");

		int degrees = (int) lat/100;
		double decDegrees = (lat  - (degrees*100)) / 60;
		double totalLat = degrees + decDegrees;
		totalLat = Double.parseDouble(df.format(totalLat)); 
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
