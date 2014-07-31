package org.smap.surveyModel;

import static org.junit.Assert.*;


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
		surveyBot = new SurveyModel("data/String only form.xml");
		assert (surveyBot) != null;
	}

	@Test
	public void testPlainStringAnswer() throws JavaRosaException {
		assertEquals(saveAnswerExtractResult("Plain String", "textFieldVanilla"),"Plain String");
	}
	
	@Test
	public void testRequiredStringAnswer() throws JavaRosaException {
		surveyBot.jumpToNextEvent();
		assertEquals(saveAnswerExtractResult("Answer Required", "textFieldRequired"),"Answer Required");
	}
	
	@Test
	public void testOverwriteAnswer() throws JavaRosaException{
		surveyBot.jumpToNextEvent();
		saveAnswerExtractResult("Answer Required", "textFieldRequired");
		logXML();
		assertEquals(saveAnswerExtractResult("Answer Not Required", "textFieldRequired"),"Answer Not Required");
	}
	
	@Test
	public void testPostSaveMetadata() throws JavaRosaException{
		surveyBot.setFinalMetadata();
		logXML();
		assertNotNull(AnswerValidator.getFirstValueFromTag(surveyBot.getAnsweredXML(),"Date"));
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
