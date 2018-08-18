import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;


public class convertToJSON
{
  public static List<JSONObject> getFormattedResult(ResultSet rs){
	  List<JSONObject> resList = new ArrayList<JSONObject>();
	  try{
		  ResultSetMetaData rsMeta = rs.getMetaData();
		 
		  int columnCount = rsMeta.getColumnCount();
		  //System.out.print(columnCount);
		  List<String> columnNames =new ArrayList<String>();
		  
		  
		  for(int i=1;i<=columnCount;i++){
			 columnNames.add(rsMeta.getColumnName(i).toUpperCase());
			  
		  }
		  
		  while(rs.next())
		  {
			  JSONObject obj = new JSONObject();
			  for(int i=1;i<=columnCount;i++){
				  String key = columnNames.get(i-1);
				  String value = rs.getString(i);
				  
				  obj.accumulate(key,value);
				  
				  
				  
			  }
			  resList.add(obj);
			  
		  }
	  }catch(Exception e){
		  e.printStackTrace();
	  }finally{
		  try{
			  rs.close();
		  }catch(SQLException e){
			  e.printStackTrace();
		  }
	  }
	  return resList;
  }
}
