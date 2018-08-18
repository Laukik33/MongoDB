import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.MongoCredential;


public class mongo {
public static void main(String args[]){
	Mongo mongo = new Mongo("127.0.0.1", 27017);
	MongoCredential credential = MongoCredential.createCredential("sampleuser", "db", "password".toCharArray());
	System.out.println("Connected to the MongoDB successfully");
	
	DB database = mongo.getDB("db"); 
	DBCollection collection = database.getCollection("Player_Data");
	
	
/*BasicDBObject document = new BasicDBObject();
	
	collection.remove(document);

    DBCursor cursor = collection.find();
    while(cursor.hasNext()){
    	
    	
    	collection.remove(cursor.next());
    } */
    
	//Gets all document where T1S = 0
	BasicDBObject whereQuery = new BasicDBObject();
	whereQuery.put("PNAME", "ABDULLAH ALMUAIOUF");
	DBCursor cursor = collection.find(whereQuery);
while(cursor.hasNext()) {
	System.out.println(cursor.next());
	
	} 
	
//query to remove player with player name "TAGLIAFICO Nicolas"
BasicDBObject searchQuery = new BasicDBObject();
searchQuery.put("PNAME", "TAGLIAFICO Nicolas");
 
collection.remove(searchQuery);

System.out.println();
System.out.println();
System.out.println();

//query to find player with name "MASCHERANO Javier"

System.out.println("---------------------------------------------------------------------------------------");
System.out.println("QUERY 1 Results:");
System.out.println("---------------------------------------------------------------------------------------");

BasicDBObject whereQuery1 = new BasicDBObject();
whereQuery1.put("PNAME", "MASCHERANO Javier");
DBCursor cursor1 = collection.find(whereQuery1);
while(cursor1.hasNext()) {
System.out.println(cursor1.next()); 
} 

System.out.println();
System.out.println();
System.out.println();

System.out.println("---------------------------------------------------------------------------------------");
System.out.println("QUERY 2 Results:");
System.out.println("----------------------------------------------------------------------------------------");

//query to retrieve all the players with Player ID 1
BasicDBObject whereQuery2 = new BasicDBObject();
whereQuery2.put("PNAME", "REBIC Ante");
DBCursor cursor2 = collection.find(whereQuery2);
while(cursor2.hasNext()) {
System.out.println(cursor2.next());  

} 



System.out.println();
System.out.println();
System.out.println();


}
}

