import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class convertToXML {

	public static Document convertToXML(ResultSet rs) throws ParserConfigurationException, SQLException, TransformerException, IOException{
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder        = factory.newDocumentBuilder();
		Document doc                   = builder.newDocument();
		
		Element results = doc.createElement("TEAM_SCORES");
		doc.appendChild(results);
		
		ResultSetMetaData rsmd = rs.getMetaData();
		int colCount           = rsmd.getColumnCount();
		
		 while (rs.next())
		   {
		      Element row = doc.createElement("Game_scores");
		      results.appendChild(row);

		      for (int i = 1; i <= colCount; i++)
		      {
		         String columnName = rsmd.getColumnName(i);
		         Object value      = rs.getObject(i);

		         Element node      = doc.createElement(columnName);
		         node.appendChild(doc.createTextNode(value.toString()));
		         row.appendChild(node);
		      }
		   }
		  
		   //printDocument(doc,System.out);
		   return doc;
		
		
		
	}
}
