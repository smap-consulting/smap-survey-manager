package org.smap.surveyModel;

import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.odk.JavaRosaException;
import org.smap.surveyModel.SurveyModel;




public class SurveyModelTests {

	private static SurveyModel surveyBot=null;

	@BeforeClass  
	public static void setUp() throws Exception {
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
		assertEquals(saveAnswerExtractResult("Answer Not Required", "textFieldRequired"),"Answer Not Required");
	}
	
	public String saveAnswerExtractResult(String answer, String tagName) throws JavaRosaException{
		surveyBot.answer(answer);
		String answeredXML = surveyBot.getAnsweredXML();
		return AnswerValidator.getFirstValueFromTag(answeredXML, tagName);
	}
}
