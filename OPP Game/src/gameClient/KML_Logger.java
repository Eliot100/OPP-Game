package gameClient;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import dataStructure.fruit_data;
import dataStructure.fruits;
import dataStructure.robot_data;
import utils.Point3D;

public class KML_Logger implements Runnable {

	private String fileName;
	private game_support support;
	private static String data;
	private File OutputFile;
	private String XMLcontent;
	
	//header file is the opening of the file required for every kml file
	private static final String headerFile = 
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
			"<kml xmlns=\"http://earth.google.com/kml/2.2\">\r\n" + 
			"  <Document>\r\n" + 
			"    <name>Matala 3</name>\n" +
			"<Style id=\"check-hide-children\">" + 
			"      <ListStyle>\r\n" + 
			"        <listItemType>checkHideChildren</listItemType>\r\n" + 
			"      </ListStyle>\r\n" + 
			"    </Style>\r\n" + 
			"\r\n" + 
			"    <styleUrl>#check-hide-children</styleUrl>";
	
	//footer file is the ending of the file required for every kml file
	private static final String footerFile = "</Document>\r\n" + "</kml>";
	
	// kml string representing an apple object
	private static final String appleStyle = 
			" <Style id=\"apple\">\r\n" + 
			"     <IconStyle>\r\n" + 
			"    <scale>1.0</scale>\r\n" + 
			"       <Icon>\r\n" + 
			"         <href>http://maps.google.com/mapfiles/kml/paddle/grn-blank.png</href>\r\n" + 
			"       </Icon>\r\n" + 
			"     </IconStyle>\r\n" + 
			"   </Style>\n";
	
	// kml string representing a banana object
	private static final String bananaStyle = 
			"<Style id=\"banana\">\r\n" + 
			"  <IconStyle>\r\n" + 
			"    <scale>1.0</scale>\r\n" + 
			"    <Icon>\r\n" + 
			"      <href>http://maps.google.com/mapfiles/kml/paddle/ylw-blank.png</href>\r\n" + 
			"    </Icon>\r\n" + 
			"  </IconStyle>\r\n" + 
			"</Style>\n";
	
	// kml string representing a robot object
	private static final String robotStyle = 
			"<Style id=\"robot\">\r\n" + 
			"  <IconStyle>\r\n" + 
			"    <scale>1.0</scale>\r\n" + 
			"    <Icon>\r\n" + 
			"      <href>http://maps.google.com/mapfiles/kml/pal4/icon54.png</href>\r\n" + 
			"    </Icon>\r\n" + 
			"  </IconStyle>\r\n" + 
			"</Style>\n";

	// kml string representing a place mark and a time stamp 
	private static final String placeMark = 
			" <Placemark>\r\n" + 
			"   <TimeStamp>\r\n" + 
			"     <when>date</when>\r\n" + 
			"   </TimeStamp>" +
			"   <styleUrl>#icon</styleUrl>\r\n" + 
			"   <Point>\r\n" + 
			"      <coordinates>(x,y)</coordinates>\r\n" + 
			"   </Point>\r\n" + 
			" </Placemark>\n";

	/****************************************************************************************************************/
	// function implementation : 
	
	/**
	 * private constructor restricted to this class itself
	 * building a kml file and insert the standard header kml and the style for every object
	 * @param fileName
	 * @throws IOException 
	 */
	public KML_Logger(game_support support , int stage ) throws IOException {
		this.support = support;
		this.fileName = "data/" + stage + ".kml";
		OutputFile = new File("data/" + stage + ".kml");
		OutputFile.createNewFile();
		XMLcontent = headerFile + appleStyle + bananaStyle + robotStyle;
		writeToFile(XMLcontent); 
	}

	/**
	 * this method updates the data to the current date in a kml format
	 */
	private static void getKmlFormatDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		Date date = new Date();  
		data = formatter.format(date);
	}
	/**
	 * Writes the log of the game in a KML format to a file 
	 * this method takes the string that the class built during the game and adds it to the kml file.
	 * it also adds the footer of the kml file
	 * @param string 
	 * @throws IOException 
	 */
	public void writeToFile(String string) throws IOException{
		PrintWriter pw = new PrintWriter(OutputFile);
		pw.println(string);
		pw.close();
	}
	/**
	 * add a place mark to the kml file
	 * @param p x and y coordinate of the place mark
	 * @icon to pick the place mark style
	 */
	private String addPlaceMark(Point3D p , String icon) {
		String placeMark = KML_Logger.placeMark;
		placeMark = placeMark.replace("(x,y)",p.x()+","+p.y());
		placeMark = placeMark.replace("date", data); //replace date to the current data kml format
		placeMark = placeMark.replace("icon", icon); //replace the word "icon" to specific icon match the current object
		return placeMark;
	}
	/**
	 * 10 times in a second this class takes a screenshot of the game converts it to a kml format and adds it the the logger
	 */
	@Override
	public void run() {
		while(support.isRunning()) {
			try {
				XMLcontent += screenShot();
				writeToFile(XMLcontent);
				Thread.sleep(50);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		XMLcontent += footerFile;
		try {
			writeToFile(XMLcontent);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * convert the positions of all the robots and fruits in kml format to adds it to the content
	 */
	private String screenShot() {
		String PlaceMark = "";
		KML_Logger.getKmlFormatDate(); //update current date
		//fruits
		fruit_data[] fruitsArr = support.getFruits();
		for (fruit_data fruit : fruitsArr) {
			if(fruit.getType() == fruits.banana)
				PlaceMark += this.addPlaceMark(fruit.getPos(), "banana");
			else if (fruit.getType() == fruits.apple)
				PlaceMark += this.addPlaceMark(fruit.getPos(), "apple");
		}

		//robots
		robot_data[] robotsArr = support.getRobots();
		for (robot_data robot : robotsArr) {
			PlaceMark += this.addPlaceMark(robot.getPos(), "robot");
		}
		return PlaceMark;
	}

	//  my first implementation :
	//
	//	private final String robotIcon = "robotIcon";
	//	private final String fruitIcon = "fruitIcon";
	//
	//	// use example
	//	public static void main(String[] args)  {
	//
	//	}
	//
	//	public void AddRobotsPlaceMark(String str, robot_data[] robots) {
	//		for (robot_data robot : robots) {
	//			PlaceMark(robotIcon, robot.getPos());
	//		}
	//	}
	//
	//	public void AddFruitsPlaceMark(String str, fruit_data[] fruits) {
	//		for (fruit_data fruit : fruits) {
	//			str += PlaceMark(fruitIcon, fruit.getPos());
	//		}
	//	}
	//
	//	private String PlaceMark(String icon, Point3D location) {
	//		return icon;
	//	}


}
