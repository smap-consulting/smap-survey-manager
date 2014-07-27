package unitTest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import controller.JavaRosaException;

import SurveyBot.SurveyModel;

public class StringTests {

	private SurveyModel surveyBot;

	public void setUp() throws Exception {
		this.surveyBot = new SurveyModel("data/String only form.xml");
	}

	@Test
	public void testPlainStringAnswer() throws JavaRosaException {
		surveyBot.answer("Plain String");

		//fail("Not yet implemented");
	}

}
