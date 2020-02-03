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
 * This class is used to make a kml file, to "see" the game at google earth. 
 * @author Eli Ruvinov
 */
public class KML_Logger implements Runnable {
	/**
	 * The kml output file
	 */
	private File OutputFile;
	/**
	 * The file name 
	 */
	private String fileName;
	/**
	 * The content of the kml output file
	 */
	public String content;
	/**
	 * The server of the game
	 */
	private game_server server;
	/**
	 * String of the date + time
	 */
	private String date;
	/**
	 * The first lines in the kml output file
	 */
	private String header =
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
			"<kml xmlns=\"http://earth.google.com/kml/2.2\">\r\n" + 
			"  <Document>\r\n" + 
			"    <name> OPP Game stage theStage </name>\r\n" +
			"    <Style id=\"check-hide-children\">\r\n" + 
			"      <ListStyle>\r\n" + 
			"        <listItemType>checkHideChildren</listItemType>\r\n" + 
			"      </ListStyle>\r\n" + 
			"    </Style>\r\n" + 
			"    <styleUrl>#check-hide-children</styleUrl>\r\n";
	/**
	 * The last lines in the kml output file
	 */
	private final String footer = 
			"  </Document>\r\n" + 
			"</kml>\r\n";
	// Got icons from http://kml4earth.appspot.com/icons.html 
	/**
	 * The lines for the apple Icon
	 */
	private final String appleIcon = 
			"    <Style id=\"apple\">\r\n" + 
			"      <IconStyle>\r\n" + 
			"        <scale>1.0</scale>\r\n" + 
			"        <Icon>\r\n" + 
			"          <href>http://maps.google.com/mapfiles/kml/paddle/grn-blank.png</href>\r\n" + 
			"        </Icon>\r\n" + 
			"      </IconStyle>\r\n" + 
			"    </Style>\r\n";
	/**
	 * The lines for the banana Icon
	 */
	private final String bananaIcon =
			"    <Style id=\"banana\">\r\n" + 
			"      <IconStyle>\r\n" + 
			"        <scale>1.0</scale>\r\n" + 
			"        <Icon>\r\n" + 
			"          <href>http://maps.google.com/mapfiles/kml/paddle/ylw-blank.png</href>\r\n" + 
			"        </Icon>\r\n" + 
			"      </IconStyle>\r\n" + 
			"    </Style>\r\n";
	/**
	 * The lines for the robot Icon
	 */
	private final String robotIcon = 
			"    <Style id=\"robot\">\r\n" + 
			"      <IconStyle>\r\n" + 
			"        <scale>1.0</scale>\r\n" + 
			"        <Icon>\r\n" + 
			"          <href>http://maps.google.com/mapfiles/kml/pal2/icon1.png</href>\r\n" + 
			"        </Icon>\r\n" + 
			"      </IconStyle>\r\n" + 
			"    </Style>\n";
	/**
	 * The lines for a placeMark with Unknown date, icon and point
	 */
	private final String placeMark = 
			"    <Placemark>\r\n" + 
			"      <TimeStamp>\r\n" + 
			"        <when>date</when>\r\n" + 
			"      </TimeStamp>\r\n" +
			"      <styleUrl>#icon</styleUrl>\r\n" + 
			"      <Point>\r\n" + 
			"        <coordinates>point</coordinates>\r\n" + 
			"      </Point>\r\n" + 
			"    </Placemark>\r\n";
	/**
	 * Constructor of a KML_Logger
	 * @param support - the server of the game (game_server)
	 * @param stage - the stage of the game
	 * @throws IOException
	 */
	public KML_Logger(game_server support , int stage ) throws IOException {
		server = support;
		fileName = "data/" + stage + ".kml";
		String finishHeader = header.replace("theStage", ""+stage);
		OutputFile = new File(fileName);
		OutputFile.createNewFile();
		content = finishHeader + appleIcon + bananaIcon + robotIcon;
	}
	@Override
	/**
	 * This function is the action that make the kml output file
	 */
	public void run() {
		while(server.isRunning()) {
			try {
				content += screenShot();
				Thread.sleep(50);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		content += footer;
	}
	/**
	 * This function is replace the content of output file with a given string
	 * @param string - the new content of the output file
	 * @throws IOException
	 */
	public void writeToFile(String string) throws IOException{
		PrintWriter pw = new PrintWriter(OutputFile);
		pw.println(string);
		pw.close();
	}
	/**
	 * This function create a new kml file by the given fileName,
	 * that contain this KML_Logger content.
	 * @param fileName - the name of the new file.
	 * @throws IOException
	 */
	public void newFile(String fileName) throws IOException{
		fileName = "data/" + fileName + ".kml";
		File out = new File(fileName);
		PrintWriter pw = new PrintWriter(out);
		pw.println(content);
		pw.close();
	}
	/**
	 * @return the current situation of the game in kml format
	 */
	private String screenShot() {
		String PlaceMark = "";
		fruit_data[] fruitsArr = server.getFruits();
		robot_data[] robotsArr = server.getRobots();
		setDate(); 
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
	/**
	 * This function update the date field of this KML_Logger
	 */
	private void setDate() { 
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");  
		this.date = dateFormat.format(date);
	}
	/**
	 * This function is replace the date, icon and point strings with the reals date, icon and point
	 * @param icon - the placeMark icon
	 * @param p - the placeMark point
	 * @return finish placeMark
	 */
	private String addPlaceMark(Point3D p , String icon) {
		String placeMark = this.placeMark;
		placeMark = placeMark.replace("date", date);
		placeMark = placeMark.replace("icon", icon); 
		placeMark = placeMark.replace("point", p.x()+","+p.y());
		return placeMark;
	}
}
