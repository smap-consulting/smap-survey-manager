package org.smap.surveyConverser;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.Test;
import org.odk.FileUtils;

import com.google.common.io.Files;

public class SurveyConverserTests {
	
	private String xformXML;
	
	@Before
	public void setUp(){ 
		this.xformXML=FileUtils.readFileToString("data/String only form.xml");
	}
	
	@Test
	public void testSecondQuestionCreate() {
		SurveyConverser converser = SurveyConverser.createNewSurveyConverser(this.xformXML);
		converser.answerCurrentQuestion("yolo");
		assertEquals("Text Field Required\n[this is a required field]", 
				converser.getCurrentQuestion());
	}
	
	@Test
	public void testSecondQuestionResume() {
		SurveyConverser converser = SurveyConverser.createNewSurveyConverser(this.xformXML);
		converser.answerCurrentQuestion("yolo");
		String saved = converser.save();
		
		converser = SurveyConverser.resume(saved);
		System.out.println(converser.getAnswers());
		assertEquals("Text Field Required\n[this is a required field]", 
				converser.getCurrentQuestion());
	}
}
