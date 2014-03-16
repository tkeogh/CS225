package Assignment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class gpxwriter {
	
	public gpxwriter(){
		
		
	}
	
	public void writeGPXfile(ArrayList<GGA> ggas){
		
		
		File file = new File("track.gpx");
		try {
			
		
			FileWriter writer = new FileWriter(file);
			
			writer.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>");
			writer.append("<gpx version=\"1.0\">");  //add standard xml tags
			writer.append("<name>CS225 Assignment</name>");
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"); //get date
			
			writer.append("<trk>");
			writer.append("<trkseg>");
			
			for(int i =0;i<ggas.size();i++){
				writer.append("<trkpt lat=\"" + ggas.get(i).getLat() + "\" lon =\"" + ggas.get(i).getLon() + "\"></trkpt>");
				writer.append("<ele>" + ggas.get(i).getAltitude() + "</ele>");
				
			} //write all gpx info down.
			
			writer.append("</trkseg></trk></gpx>");
			
			writer.close();
			// <time>" + "</time>
			
			
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
