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
		surveyBot = new SurveyModel("data/String only form 2.xml");
		assert (surveyBot) != null;
	}
	
	//Detailed method testing
	@Test
	public void testGetPrompt() {
		assertEquals("Text Field Vanilla\n[no restrictions on this field]", surveyBot.getPrompt());
	}

	
	@Test
	public void testGetCurrentIndex() {
		FormIndex index = surveyBot.getCurrentIndex();
		assertEquals(0, index.getLocalIndex());
	}

	@Test
	public void testJumpToNextEvent() {
		surveyBot.jumpToNextEvent();
		FormIndex index = surveyBot.getCurrentIndex();
		assertEquals(1, index.getLocalIndex());
	}

	@Test
	public void testIsEndOfSurvey() {
		assertFalse(surveyBot.isEndOfSurvey());
		surveyBot.jumpToNextEvent();
		surveyBot.jumpToNextEvent();
		surveyBot.jumpToNextEvent();
		surveyBot.jumpToNextEvent();
		surveyBot.jumpToNextEvent();
		surveyBot.jumpToNextEvent();
		surveyBot.jumpToNextEvent();
		assertTrue(surveyBot.isEndOfSurvey());
		
	}

	@Test
	public void testGetFullQuestionPromptList() {
		String[] questionArray = surveyBot.getFullQuestionPromptList();
		assertEquals("Text Field Vanilla\n[no restrictions on this field]",questionArray[0]);
		assertEquals("Text Field Required\n[this is a required field]",questionArray[1]);
	}
	
	//end

	@Test
	public void testPlainStringAnswer() throws JavaRosaException {
		assertEquals("Plain String", saveAnswerExtractResult("Plain String", "textFieldVanilla"));
	}
	
	@Test
	public void testEmptyPlainStringAnswer() throws JavaRosaException {
		assertEquals("", saveAnswerExtractResult("", "textFieldVanilla"));
	}
	
	@Test
	public void testRequiredStringAnswer() throws JavaRosaException {
		surveyBot.jumpToNextEvent();
		assertEquals("Answer Required", saveAnswerExtractResult("Answer Required", "textFieldRequired"));
	}
	
	@Test
	public void testRequiredEmptyAnswer() throws JavaRosaException {
		surveyBot.jumpToNextEvent();
		surveyBot.answer(null);
		String prompt = surveyBot.getPrompt();
		assertEquals("Answer is 'Required'.\nPlease try again.\nText Field Required\n[this is a required field]", prompt);
	}
	
	@Test
	public void testOverwriteAnswer() throws JavaRosaException{
		surveyBot.jumpToNextEvent();
		surveyBot.answer("Answer Required");
		saveAnswerExtractResult("Answer Required", "textFieldRequired");
		assertEquals("Answer Not Required", saveAnswerExtractResult("Answer Not Required", "textFieldRequired"));
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
		assertEquals("123456", saveAnswerExtractResult("123456", "textFieldLength"));
	}
	
	@Test
	public void testStringInvalidLength() throws JavaRosaException {
		surveyBot.answer("Plain String");
		surveyBot.jumpToNextEvent();
		surveyBot.answer("Answer Required");
		surveyBot.jumpToNextEvent();
		surveyBot.answer("1234");
		assertEquals("Constraint Violated\nPlease try again.\nText Field Length\n[Length > 5 && < 10]", surveyBot.getPrompt());
	}
	
	@Test
	public void testAnswerGroupQuestion() throws JavaRosaException {
		surveyBot.jumpToNextEvent();
		surveyBot.jumpToNextEvent();
		surveyBot.jumpToNextEvent();
		assertEquals("Answering Group Q1", saveAnswerExtractResult("Answering Group Q1", "GroupQ1"));
	}
	
	@Test
	public void testQuestionEventType() throws JavaRosaException {
		//System.out.println(surveyBot.getEventInfo().substring(0, 20));
		assertEquals("Event Type: Question", surveyBot.getEventInfo().substring(0, 20));
	}
	
	@Test
	public void testGroupEventType() throws JavaRosaException {
		surveyBot.jumpToNextEvent();
		surveyBot.jumpToNextEvent();
		surveyBot.jumpToNextEvent();
		//System.out.println(surveyBot.getEventInfo().substring(0, 17));
		assertEquals("Event Type: Group", surveyBot.getEventInfo().substring(0, 17));
	}
	
	@Test
	public void testRepeatEventType() throws JavaRosaException {
		surveyBot.jumpToNextEvent();
		surveyBot.jumpToNextEvent();
		surveyBot.jumpToNextEvent();
		surveyBot.jumpToNextEvent();
		surveyBot.jumpToNextEvent();
		//System.out.println(surveyBot.getEventInfo().substring(0, 18));
		assertEquals("Event Type: Repeat", surveyBot.getEventInfo().substring(0, 18));
	}
	
	@Test
	public void resumeSavedSurvey() throws JavaRosaException{
		String answeredXML = surveyBot.getAnsweredXML();
		File instanceFile = new File("data/savedStringInstance.xml");
		surveyBot = new SurveyModel("data/String only form.xml",instanceFile, FormIndex.createBeginningOfFormIndex());
		answeredXML = surveyBot.getAnsweredXML();
		assertEquals(AnswerValidator.getFirstValueFromTag(answeredXML, "textFieldVanilla"),"Plain String");
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
