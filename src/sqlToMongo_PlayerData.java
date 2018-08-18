import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.json.JSONObject;
import org.w3c.dom.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoCredential;
import com.mongodb.util.JSON;


public class sqlToMongo_PlayerData {

	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	static final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/worldcup2018";
	final String username = "root";
	final String password = "root";
	
	public static void main(String args[]) throws SQLException {
		
	//Making Connection to MySQL database and Querying the database to fetch desired results
		
	Connection conn = null;
	Statement stmt1 = null;
	final String username = "root";
	final String password = "root";
	
	try{
		Class.forName(JDBC_DRIVER).newInstance();
		System.out.println("Connecting to database");
		conn = DriverManager.getConnection(JDBC_URL, username, password);
		System.out.println("connected to database succesfully");
	
		stmt1 = conn.createStatement();
		//Querying the tables for Player_Data document
	
		String query1 = "select p.PName, p.Team, p.PID, p.Position, g.MDate, s.SCity, s.SName, t.Team, go.Time,st.SCity,st.SName,ga.MDate,te.Team" +
				" from (player as p join starting_lineups as sl on (p.TeamID = sl.TeamID and p.PID = sl.PID)" +
						" join game as g on (sl.GID = g.GID) join stadium as s on (s.SID = g.SID)" + 
						" join team as t on ((t.TeamID = g.T_ID1 and p.Team!=t.Team) or (t.TeamID=g.T_ID2 and p.Team!=t.Team))) " +
						" left OUTER join goals as go on (p.TeamID = go.TeamID and p.PID = go.PID)" + 
						" left outer join game as ga on (go.GID= ga.GID) left outer join stadium as st on (ga.SID= st.SID)" + 
						" left outer join team as te on ((ga.T_ID2 = te.TeamID and p.Team!=te.Team) or (ga.T_ID1=te.TeamID and p.Team!=te.Team)) ;#join stadium on);";

	
		ResultSet rslt = stmt1.executeQuery(query1);
	
	//COnverting the SQL ResultSet to JSON Object and storing in an ArrayList
		List<JSONObject> resList1 = convertToJSON.getFormattedResult(rslt);
		System.out.println(resList1);
		
	
    //Connecting to MongoDB and inserting the Player_Data document
		Mongo mongo = new Mongo("127.0.0.1", 27017);
		MongoCredential credential = MongoCredential.createCredential("sampleuser", "db", "password".toCharArray());
		System.out.println("Connected to the MongoDB successfully");
		System.out.println("Inserting the records into MongoDB...");
    
    // Accessing the database 
		DB database = mongo.getDB("db"); 
   
		DBCollection collection1 = database.getCollection("Player_Data");
   
		//Iterating the Arraylist to insert every JSONObject into MongoDB
		for(int n=0; n < resList1.size() ;n++)
		{
			JSONObject jObj = resList1.get(n);
			System.out.println(jObj);
			//jsonList.add((Document) jObj);
			String jOb = jObj.toString();
			DBObject dbObject = (DBObject)JSON.parse(jOb);
			collection1.insert(dbObject);
    	 //collection.insertMany((List<? extends org.bson.Document>) jObj);
		}

	System.out.println("----------------------------------------------------------------------------------");
	System.out.println("RUNNING QUERY ON MONGODB");
	System.out.println("----------------------------------------------------------------------------------");
		//Querying MongoDB to fetch records
     
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("PNAME", "ABDULLAH ALMUAIOUF");
			DBCursor cursor = collection1.find(whereQuery);
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
			
			ResultSet rs1 = stmt1.executeQuery(query1);
	
			Document doc1 = convertToXML1.convertToXMLL(rs1);
	   
			
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

			DOMSource source1 = new DOMSource(doc1);
			
            //Writing results to an XML file1
			StreamResult file1 = new StreamResult(new File("D:/Player_Data.xml"));
			transformer.transform(source1 , file1);
	

				}
	
	catch(Exception e){
		//System.out.println("Exception");
	}
	finally{
		stmt1.close();

		conn.close();
	}
  }				


}
