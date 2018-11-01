package Utility.XMLUtility;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;





public class XMLUtility {
	
	public static Document getDocumentFromFile(File file){
		
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
	}
	
}
