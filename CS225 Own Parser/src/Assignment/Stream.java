package Assignment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Stream {

	private ArrayList<GGA> ggas;
	private ArrayList<Satellite> sats;
	private ArrayList<Integer> satids;
	private int failed = 0;



	public Stream(){

		ggas = new ArrayList<GGA>();	
		sats = new ArrayList<Satellite>();
		satids = new ArrayList<Integer>();

		readin();
		// check();

	}

	public void readin(){

		File file = new File("gps_2.dat");		

		BufferedReader br = null;

		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader(file));

			while ((sCurrentLine = br.readLine()) != null) {


				String[] p = sCurrentLine.toString().split(","); //splits current line into array

				if(p[0].equalsIgnoreCase("$GPGGA")){  //Finds a GGA line used for co ordinates


					int pass = countReliable();

					if(pass>=3){

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
						System.out.println("----------WOOOOOOH YEAH ADDED   " + ggas.size() + " ---------");
						for(int l =0;l<sats.size();l++){
							//System.out.println("Using Satellite "+ sats.get(l).getId() + " Logged SNR : "+ sats.get(l).getSnr());

						}
					}
					else{
						System.out.println("================FAILED HERE ======================");
						for(int l =0;l<sats.size();l++){
							System.out.println("Using Satellite "+ sats.get(l).getId() + " Logged SNR : "+ sats.get(l).getSnr());

						}
						failed++;
						System.out.println(failed);
					}

				}
				else if(p[0].equalsIgnoreCase("$GPGSV")){

					satids.clear();
					String gsv[] = sCurrentLine.toString().split(",");
					String[] finalone = gsv[gsv.length-1].toString().split("\\*");
					gsv[gsv.length-1] = finalone[0];        //removes checksum so just the satellite snr left.
					
					int expectedsats   = 0;
					int lineno = Integer.parseInt(gsv[2]);
					int satelliteno = Integer.parseInt(gsv[3]);


					int reaminingsats = satelliteno - ((lineno-1) * 4);

					if(reaminingsats >=4){
						expectedsats = 4;
					}

					for(int i =0;i<expectedsats;i++){
						int position = 4 *(i+1);
						satids.add(Integer.parseInt(gsv[position]));
					}
					
					for(int i =0;i<satids.size();i++){
						int currentsat = satids.get(i);
						for(int j =0;j<sats.size();j++){
							int cur = sats.get(j).getId();	
							if(currentsat == cur){
								int snrposition = ((i+1)*4)+3;
								int snr = Integer.parseInt(gsv[snrposition]);
								sats.get(j).setSnr(snr);
							}
							
						
						}
						
					}
					

					/*first = Integer.parseInt(gsv[4]);
					second = Integer.parseInt(gsv[8]);
					third = Integer.parseInt(gsv[12]);
					fourth = Integer.parseInt(gsv[16]);


					for(int i =0;i<sats.size();i++){

						int curr = sats.get(i).getId();

						if(curr == first){
							if(!gsv[7].isEmpty()){
								sats.get(i).setSnr(Integer.parseInt(gsv[7]));
								//	System.out.println("Satellite " + curr + " set to  " + gsv[7]);
							}
						}
						if(curr == second){
							if(!gsv[11].isEmpty()){
								sats.get(i).setSnr(Integer.parseInt(gsv[11]));									
							}
						}
						if(curr == third){
							if(!gsv[15].isEmpty()){
								sats.get(i).setSnr(Integer.parseInt(gsv[15]));							
							}
						}
						if(curr == fourth){
							if(!finalone[0].isEmpty()){
								sats.get(i).setSnr(Integer.parseInt(finalone[0]));
							}
						}

					}*/






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

					for (int i =0;i<sats.size();i++){

						int curr = sats.get(i).getId();
						boolean contains = false;
						for(int j =3;j<gsa.length -3;j++){
							if(!gsa[j].isEmpty()){
								int compare = Integer.parseInt(gsa[j]);
								if(compare == curr){
									contains = true;
								}

							}
						}
						if(!contains){
							sats.remove(i);
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
			System.out.println(temp.getTime() +" " + temp.getLat() + " " +temp.getLon()+"------");
		}


	}

	public int countReliable(){

		int reliable =0;

		for(int i = 0;i<sats.size();i++){
			if(sats.get(i).getSnr() >= 30){
				reliable++;
				//System.out.println(reliable + " This is reliable count");
			}
		}

		return reliable;
	}

}
