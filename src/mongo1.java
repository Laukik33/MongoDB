import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.MongoCredential;


public class mongo1 {
public static void main(String args[]){
	
	Mongo mongo = new Mongo("127.0.0.1", 27017);
	MongoCredential credential = MongoCredential.createCredential("sampleuser", "db", "password".toCharArray());
	System.out.println("Connected to the MongoDB successfully");
	
	DB database1 = mongo.getDB("db"); 
	DBCollection collection1 = database1.getCollection("Team_scores");
	
	/*BasicDBObject document = new BasicDBObject();
	
	collection1.remove(document);

    DBCursor cursor = collection1.find();
    while(cursor.hasNext()){
    	
    	
    	collection1.remove(cursor.next());
    } */
	System.out.println("---------------------------------------------------------------------------------------");
	System.out.println("QUERY 1 Results:");
	System.out.println("---------------------------------------------------------------------------------------");
	
	//query to retrieve all the match details of match who have teamscore 1
	BasicDBObject whereQuery1 = new BasicDBObject();
	whereQuery1.put("T1S", "5");
	DBCursor cursor1 = collection1.find(whereQuery1);
	while(cursor1.hasNext()) {
	System.out.println(cursor1.next()); 

	}

	System.out.println();
	System.out.println();
	System.out.println();
	System.out.println();
	System.out.println();
	System.out.println();
	

	//query to retireve all the 
	System.out.println("---------------------------------------------------------------------------------------");
	System.out.println("QUERY 2 Results:");
	System.out.println("---------------------------------------------------------------------------------------");

	BasicDBObject whereQuery4 = new BasicDBObject();
	whereQuery4.put("SNAME", "Samara Arena");
	DBCursor cursor4 = collection1.find(whereQuery4);
	while(cursor4.hasNext()) {
	System.out.println(cursor4.next()); 

	} 
}
}
