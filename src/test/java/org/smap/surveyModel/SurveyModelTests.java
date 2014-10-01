package org.smap.surveyModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.javarosa.core.model.FormIndex;
import org.javarosa.core.util.externalizable.DeserializationException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.odk.JavaRosaException;
import org.smap.surveyModel.utils.AnswerValidator;
import org.smap.surveyModel.utils.JRSerializer;

import com.google.common.io.Files;


public class SurveyModelTests {

	private SurveyModel stringSurvey = null;
	private SurveyModel groupSurvey = null;

	@Before
	public void setUp(){
		stringSurvey = createSurvey("data/String only form.xml");
		groupSurvey = createSurvey("data/GroupForm.xml");
	}

	private SurveyModel createSurvey(String surveyXMLFilePath){
		String contents= readFile(surveyXMLFilePath);
		SurveyModel model = SurveyModel.createSurveyModelFromXform(contents);
		assert (model) != null;
		return model;
	}

	@Test
	public void testGetPrompt() {
		assertEquals("Text Field Vanilla\n(no restrictions on this field)", stringSurvey.getPrompt());
	}

	@Test
	public void testGetCurrentIndex() {
		FormIndex index = stringSurvey.getCurrentIndex();
		assertEquals(0, index.getLocalIndex());
	}

	@Test
	public void testJumpToNextEvent() {
		stringSurvey.jumpToNextEvent();
		FormIndex index = stringSurvey.getCurrentIndex();
		assertEquals(1, index.getLocalIndex());
	}

	@Test
	public void testIsEndOfSurvey() {

		assertFalse(stringSurvey.isEndOfSurvey());
		stringSurvey.jumpToNextEvent();
		stringSurvey.jumpToNextEvent();
		stringSurvey.jumpToNextEvent();
		stringSurvey.jumpToNextEvent();
		stringSurvey.jumpToNextEvent();
		assertTrue(stringSurvey.isEndOfSurvey());
	}

	@Test
	public void testGetFullQuestionPromptListLength() {
		String[] questionArray = stringSurvey.getFullQuestionPromptList();
		assertEquals(3,questionArray.length);
	}

	@Test
	public void testPlainStringAnswer() throws JavaRosaException {
		assertEquals("Plain String", saveAnswerExtractResult("Plain String", "textFieldVanilla",stringSurvey));
	}

	@Test
	public void testEmptyPlainStringAnswer() throws JavaRosaException {
		assertEquals("", saveAnswerExtractResult("", "textFieldVanilla",stringSurvey));
	}

	@Test
	public void testRequiredStringAnswer() throws JavaRosaException {
		stringSurvey.jumpToNextEvent();
		assertEquals("Answer Required", saveAnswerExtractResult("Answer Required", "textFieldRequired",stringSurvey));
	}

	@Test
	public void testRequiredEmptyAnswer() throws JavaRosaException {
		stringSurvey.jumpToNextEvent();
		stringSurvey.answer(null);
		String prompt = stringSurvey.getPrompt();
		assertEquals("Answer is 'Required'.\nPlease try again.\nText Field Required\n(this is a required field)", prompt);
	}

	@Test
	public void testPostSaveMetadata() throws JavaRosaException{
		stringSurvey.setFinalMetadata();
		assertNotNull(stringSurvey.getAnsweredXML());
	}

	@Test
	public void testStringLength() throws JavaRosaException {
		stringSurvey.answer("Plain String");
		stringSurvey.answer("Answer Required");
		assertEquals("123456", saveAnswerExtractResult("123456", "textFieldLength", stringSurvey));
	}

	@Test
	public void testStringInvalidLength() throws JavaRosaException {
		stringSurvey.answer("Plain String");
		stringSurvey.answer("Answer Required");
		stringSurvey.answer("1234");
		assertEquals("Constraint Violated\nPlease try again.\nText Field Length\n(Length > 5 && < 10)", stringSurvey.getPrompt());
	}

	@Ignore
	public void testAnswerGroupQuestion() throws JavaRosaException {

		/* NOT YET IMPLEMENTED
		assertEquals("Answering Group Q1", saveAnswerExtractResult("Answering Group Q1", "GroupQ1",groupSurvey));
		*/
	}

	@Test
	public void testQuestionEventType() throws JavaRosaException {
		//System.out.println(stringSurvey.getEventInfo().substring(0, 20));
		assertEquals("Event Type: Question", stringSurvey.getEventInfo().substring(0, 20));
	}

	@Test
	public void testGetSurveyName() throws JavaRosaException {
		assertEquals("String only form", stringSurvey.getSurveyName());
	}

	@Test
	public void testGroupEventType() throws JavaRosaException {

		System.out.println(groupSurvey.getEventInfo());
		assertEquals("Event Type: Group", groupSurvey.getEventInfo().substring(0, 17));
	}

	@Test
	public void testRepeatEventType() throws JavaRosaException {
		groupSurvey.jumpToNextEvent();
		//System.out.println(groupSurvey.getEventInfo().substring(0, 18));
		assertEquals("Event Type: Repeat", groupSurvey.getEventInfo().substring(0, 18));
	}

	@Test
	public void resumeSavedSurvey() throws JavaRosaException{
		String answeredXML = stringSurvey.getAnsweredXML();
		stringSurvey = SurveyModel.resumeSurveyModel(getStringSuveyXML(),getSavedInstanceXML(), FormIndex.createBeginningOfFormIndex());
		answeredXML = stringSurvey.getAnsweredXML();
		assertEquals(AnswerValidator.getFirstValueFromTag(answeredXML, "textFieldVanilla"),"Plain String");
	}

	@Test
	public void serialTest() throws IOException, ClassNotFoundException, DeserializationException{
		//Make a survey
		stringSurvey.answer("Plain String");
		stringSurvey.answer("Answer Required");

		String serializedModel = JRSerializer.serializeSurveyModel(stringSurvey);
		SurveyModel newSurvey=JRSerializer.deserializeSurveyModel(serializedModel);
		assertEquals(stringSurvey.getPrompt(),newSurvey.getPrompt());
	}

	private String saveAnswerExtractResult(String answer, String tagName, SurveyModel model) throws JavaRosaException{
		model.answer(answer);
		String answeredXML = model.getAnsweredXML();
		return AnswerValidator.getFirstValueFromTag(answeredXML, tagName);
	}

	private void logXML(){
		System.out.println(stringSurvey.getAnsweredXML());
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
            File file = new File(path);
            return Files.toString(file, Charset.forName("UTF-8"));

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
