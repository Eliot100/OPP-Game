package gameClient;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import dataStructure.fruit_data;
import dataStructure.FruitsType;
import dataStructure.robot_data;
import utils.Point3D;
/**
 * @author Eli Ruvinov
 */
public class KML_Logger implements Runnable {

	private String fileName;
	private game_server server;
	private String date;
	private File OutputFile;
	private String content;
	
	private static final String header =
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
			"<kml xmlns=\"http://earth.google.com/kml/2.2\">\r\n" + 
			"  <Document>\r\n" + 
			"    <name> OPP Game </name>\n" +
			"    <Style id=\"check-hide-children\">" + 
			"      <ListStyle>\r\n" + 
			"        <listItemType>checkHideChildren</listItemType>\r\n" + 
			"      </ListStyle>\r\n" + 
			"    </Style>\r\n" + 
			"    <styleUrl>#check-hide-children</styleUrl>";
	
	private static final String footer = "  </Document>\r\n" + 
										 "</kml>";
	// get icons from http://kml4earth.appspot.com/icons.html 
	
	private static final String appleIcon = 
			"    <Style id=\"apple\">\r\n" + 
			"      <IconStyle>\r\n" + 
			"        <scale>1.0</scale>\r\n" + 
			"        <Icon>\r\n" + 
			"          <href>http://maps.google.com/mapfiles/kml/paddle/grn-blank.png</href>\r\n" + 
			"        </Icon>\r\n" + 
			"      </IconStyle>\r\n" + 
			"    </Style>\n";
	
	private static final String bananaIcon =
			"    <Style id=\"banana\">\r\n" + 
			"      <IconStyle>\r\n" + 
			"        <scale>1.0</scale>\r\n" + 
			"        <Icon>\r\n" + 
			"          <href>http://maps.google.com/mapfiles/kml/paddle/ylw-blank.png</href>\r\n" + 
			"        </Icon>\r\n" + 
			"      </IconStyle>\r\n" + 
			"    </Style>\n";
	
	private static final String robotIcon = 
			"    <Style id=\"robot\">\r\n" + 
			"      <IconStyle>\r\n" + 
			"        <scale>1.0</scale>\r\n" + 
			"        <Icon>\r\n" + 
			"          <href>http://maps.google.com/mapfiles/kml/pal2/icon1.png</href>\r\n" + 
			"        </Icon>\r\n" + 
			"      </IconStyle>\r\n" + 
			"    </Style>\n";

	private static final String placeMark = 
			"    <Placemark>\r\n" + 
			"      <TimeStamp>\r\n" + 
			"        <when>date</when>\r\n" + 
			"      </TimeStamp>" +
			"      <styleUrl>#icon</styleUrl>\r\n" + 
			"      <Point>\r\n" + 
			"        <coordinates>(x,y)</coordinates>\r\n" + 
			"      </Point>\r\n" + 
			"    </Placemark>\n";

	/********************                      function implementation                                  ****************/
	
	public KML_Logger(game_server support , int stage ) throws IOException {
		this.server = support;
		fileName = "data/" + stage + ".kml";
		OutputFile = new File(fileName);
		OutputFile.createNewFile();
		content = header + appleIcon + bananaIcon + robotIcon;
		writeToFile(content); 
	}
	
	private void setDate() { 
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		Date date = new Date();  
		this.date = dateFormat.format(date);
	}
	
	public void writeToFile(String string) throws IOException{
		PrintWriter pw = new PrintWriter(OutputFile);
		pw.println(string);
		pw.close();
	}
	
	private String addPlaceMark(Point3D p , String icon) {
		String placeMark = KML_Logger.placeMark;
		placeMark = placeMark.replace("(x,y)",p.x()+","+p.y());
		placeMark = placeMark.replace("date", date);
		placeMark = placeMark.replace("icon", icon); 
		return placeMark;
	}
	
	@Override
	public void run() {
		while(server.isRunning()) {
			try {
				content += screenShot();
				Thread.sleep(50);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		content += footer;
		try {
			writeToFile(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String screenShot() {
		String PlaceMark = "";
		setDate(); 
		fruit_data[] fruitsArr = server.getFruits();
		robot_data[] robotsArr = server.getRobots();
		for (fruit_data fruit : fruitsArr) {
			if(fruit.getType() == FruitsType.banana)
				PlaceMark += addPlaceMark(fruit.getPos(), "banana");
			else if (fruit.getType() == FruitsType.apple)
				PlaceMark += addPlaceMark(fruit.getPos(), "apple");
		}
		for (robot_data robot : robotsArr) {
			PlaceMark += addPlaceMark(robot.getPos(), "robot");
		}
		return PlaceMark;
	}
}
