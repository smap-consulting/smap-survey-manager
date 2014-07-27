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
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class AnswerValidator {
	/**Sourced from
	 * http://stackoverflow.com/questions/4076910/how-to-retrieve-element-value-of-xml-using-java/4077986#4077986
	 * @param xml
	 * @return
	 * @throws Exception
	 */
	public static String getFirstValueFromTag(String xml, String nodeName){
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		 try {
	        Document dDoc = loadXMLFromString(xml);
	        NodeList nl = dDoc.getElementsByTagName(nodeName);
	        return nl.item(0).getTextContent();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	/**Sourced from
	 * http://stackoverflow.com/questions/562160/in-java-how-do-i-parse-xml-as-a-string-instead-of-a-file
	 * @param xml
	 * @return
	 * @throws Exception
	 */
	public static Document loadXMLFromString(String xml) throws Exception
	{
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    InputSource is = new InputSource(new StringReader(xml));
	    return builder.parse(is);
	}
}

