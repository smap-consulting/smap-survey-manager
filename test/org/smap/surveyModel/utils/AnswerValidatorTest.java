package org.smap.surveyModel.utils;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.smap.surveyModel.utils.AnswerValidator;
import org.w3c.dom.Document;

public class AnswerValidatorTest {
	
	private String XMLString = "<?xml version='1.1' ?><data id=\"sampleXMLstring\"><testtag>Hello!</testtag></data>";


	@Test
	public void testLoadXMLFromString() {
		
		try {
			Document xmlDoc = AnswerValidator.loadXMLFromString(XMLString);
			assertEquals("1.1",xmlDoc.getXmlVersion());
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
	}
	
	@Test
	public void testGetFirstValueFromTag() {
		assertEquals("Hello!",AnswerValidator.getFirstValueFromTag(XMLString, "testtag"));
	}

}
