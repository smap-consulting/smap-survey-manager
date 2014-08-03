package org.smap.surveyModel;

import static org.junit.Assert.*;

import java.io.File;


import org.javarosa.core.model.FormIndex;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.odk.JavaRosaException;
import org.smap.surveyModel.SurveyModel;
import org.smap.surveyModel.utils.AnswerValidator;


public class SurveyModelTests {

	private SurveyModel surveyBot = null;

	@Before  
	public void setUp() throws Exception {
		surveyBot = null;
		surveyBot = new SurveyModel("data/String only form.xml");
		assert (surveyBot) != null;
	}

	@Test
	public void testPlainStringAnswer() throws JavaRosaException {
		assertEquals(saveAnswerExtractResult("Plain String", "textFieldVanilla"),"Plain String");
	}
	
	@Test
	public void testEmptyPlainStringAnswer() throws JavaRosaException {
		assertEquals(saveAnswerExtractResult("", "textFieldVanilla"),"");
	}
	
	@Test
	public void testRequiredStringAnswer() throws JavaRosaException {
		surveyBot.jumpToNextEvent();
		assertEquals(saveAnswerExtractResult("Answer Required", "textFieldRequired"),"Answer Required");
	}
	
	@Test
	public void testRequiredEmptyAnswer() throws JavaRosaException {
		surveyBot.jumpToNextEvent();
		try{
			surveyBot.answer(null);
			fail("should throw exception when saving empty in required field");
		}catch(JavaRosaException e){
			//expected
		}
	}
	
	@Test
	public void testOverwriteAnswer() throws JavaRosaException{
		surveyBot.jumpToNextEvent();
		surveyBot.answer("Answer Required");
		saveAnswerExtractResult("Answer Required", "textFieldRequired");
		assertEquals(saveAnswerExtractResult("Answer Not Required", "textFieldRequired"),"Answer Not Required");
	}
	
	@Test
	public void testPostSaveMetadata() throws JavaRosaException{
		surveyBot.setFinalMetadata();
		assertNotNull(surveyBot.getAnsweredXML());
	}
	
	@Test
	public void testStringLength() throws JavaRosaException {
		surveyBot.answer("Plain String");
		surveyBot.jumpToNextEvent();
		surveyBot.answer("Answer Required");
		surveyBot.jumpToNextEvent();
		assertEquals(saveAnswerExtractResult("123456", "textFieldLength"),"123456");
	}
	
	@Test
	public void testStringInvalidLength() throws JavaRosaException {
		surveyBot.answer("Plain String");
		surveyBot.jumpToNextEvent();
		surveyBot.answer("Answer Required");
		surveyBot.jumpToNextEvent();
		try{
		//Answer question with an input that is too short
		surveyBot.answer("1234");
		fail();
		}catch(JavaRosaException e){
			//Exception expected
		}
	}
	
	@Test
	public void resumeSavedSurvey() throws JavaRosaException{
		String answeredXML = surveyBot.getAnsweredXML();
		File instanceFile = new File("data/savedStringInstance.xml");
		surveyBot = new SurveyModel("data/String only form.xml",instanceFile, FormIndex.createBeginningOfFormIndex());
		logXML();
	}
	
	private String saveAnswerExtractResult(String answer, String tagName) throws JavaRosaException{
		surveyBot.answer(answer);
		String answeredXML = surveyBot.getAnsweredXML();
		return AnswerValidator.getFirstValueFromTag(answeredXML, tagName);
	}
	
	private void logXML(){
		System.out.println(surveyBot.getAnsweredXML());
	}
}
