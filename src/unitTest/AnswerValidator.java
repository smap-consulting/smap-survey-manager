package unitTest;

import java.io.IOException;
import java.io.StringReader;





import javax.lang.model.element.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Node;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class AnswerValidator {
	public static boolean xmlContainsAnswer(String xml, String node, String expectedVal) {

		
		
		return false;
	}

	public static String getValueFromXpath(String xml, String xpath){
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		 try {
	        Document dDoc = loadXMLFromString(xml);
	        XPath xPath = XPathFactory.newInstance().newXPath();
	        Node node = (Node) xPath.evaluate(xpath, dDoc, XPathConstants.NODE);
	        return node.getNodeValue();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	public static Document loadXMLFromString(String xml) throws Exception
	{
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    InputSource is = new InputSource(new StringReader(xml));
	    return builder.parse(is);
	}
}

