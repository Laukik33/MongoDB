import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.bson.BasicBSONObject;
import org.json.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;




public class sqlToMongo {

	//Declaring sql connection credentials 
	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	static final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/worldcup2018";
	final String username = "root";
	final String password = "root";
	
	public static void main(String args[]) throws SQLException {
		
	//Making Connection to MySQL database and Querying the database to fetch desired results
		
	Connection con = null;
	Statement stmt = null;
	final String username = "root";
	final String password = "root";
	
	try{
		Class.forName(JDBC_DRIVER).newInstance();
		System.out.println("Connecting to database");
		con = DriverManager.getConnection(JDBC_URL, username, password);
		System.out.println("connected to database succesfully");
	
		stmt = con.createStatement();
		//Querying the tables for Team_scores document
		String query = "select t1.team ,t2.team, g.MDate, s.SName, s.SCity , g.T1S, g.T2S "
			+ "FROM team as t1, team as t2, game as g, stadium as s "
			+ "WHERE g.T_ID1 = t1.TeamID AND g.T_ID2 = t2.TeamID AND g.SID = s.SID" + " ORDER BY t1.team ASC ;";
	
		ResultSet rs = stmt.executeQuery(query);
	
	//COnverting the SQL ResultSet to JSON Object and storing in an ArrayList
		List<JSONObject> resList = convertToJSON.getFormattedResult(rs);
		System.out.println(resList);
		
	
    //Connecting to MongoDB and inserting the Team_Scores document
		Mongo mongo = new Mongo("127.0.0.1", 27017);
		MongoCredential credential = MongoCredential.createCredential("sampleuser", "db", "password".toCharArray());
		System.out.println("Connected to the MongoDB successfully");
		System.out.println("Inserting the records into MongoDB...");
    
    // Accessing the database 
		DB database = mongo.getDB("db"); 
   
		DBCollection collection = database.getCollection("Team_scores");
   
		//Iterating the Arraylist to insert every JSONObject into MongoDB
		for(int n=0; n < resList.size() ;n++)
		{
			JSONObject jObj = resList.get(n);
			System.out.println(jObj);
			//jsonList.add((Document) jObj);
			String jOb = jObj.toString();
			DBObject dbObject = (DBObject)JSON.parse(jOb);
			collection.insert(dbObject);
    	 //collection.insertMany((List<? extends org.bson.Document>) jObj);
		}

	
		
		//Querying MongoDB to fetch records
     
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("MDATE", "6/26/2018");
			DBCursor cursor = collection.find(whereQuery);
		while(cursor.hasNext()) {
			System.out.println(cursor.next());
			}
			System.out.println("SUccessfully inserted records");
    
			//Closing MongoDB connection
			//mongo.close();
			
			
	
			/*Calling the convertToXML class and 
			 *Converting the Team_scores document into XML FORMAT 
			 *and printing an XML FILE as result
			 */
			
			ResultSet rs1 = stmt.executeQuery(query);
	
			Document doc = convertToXML.convertToXML(rs1);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

			DOMSource source = new DOMSource(doc);
			
            //Writing results to an XML file
			StreamResult file = new StreamResult(new File("D:/team_score.xml"));
			transformer.transform(source , file);
	

				}
	
	catch(Exception e){
		System.out.println("Exception");
	}
	finally{
		stmt.close();

		con.close();
	}
  }				
}
