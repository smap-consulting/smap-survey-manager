package org.smap.surveyModel;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;


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
		String contents=getStringSuveyXML();
		byte[] encoded = Files.readAllBytes(Paths.get("data/String only form.xml"));
		contents = new String(encoded, StandardCharsets.UTF_8);
		surveyBot = null;
		surveyBot = new SurveyModel(contents);
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
		surveyBot.answer(null);
		String prompt = surveyBot.getPrompt();
		assertEquals("Answer is 'Required'.\nPlease try again.\nText Field Required\n[this is a required field]", prompt);
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
		surveyBot.answer("1234");
		assertEquals("Constraint Violated\nPlease try again.\nText Field Length\n[Length > 5 && < 10]", surveyBot.getPrompt());
	}
	
	@Test
	public void resumeSavedSurvey() throws JavaRosaException{
		String answeredXML = surveyBot.getAnsweredXML();
		surveyBot = new SurveyModel(getStringSuveyXML(),getSavedInstanceXML(), FormIndex.createBeginningOfFormIndex());
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
	
	private String getStringSuveyXML(){
		return readFile("data/String only form.xml");
	}
	
	private String getSavedInstanceXML(){
		return readFile("data/savedStringInstance.xml");
	}
	
	private String readFile(String path){
		byte[] encoded=null;
		try {
			encoded = Files.readAllBytes(Paths.get(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String(encoded, StandardCharsets.UTF_8);
	}
}
