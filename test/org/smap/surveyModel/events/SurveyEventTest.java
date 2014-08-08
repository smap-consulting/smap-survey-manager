package org.smap.surveyModel.events;

/** Temporary class, may get consolidate into other test classes.
 * 
 */
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.smap.surveyModel.SurveyModel;
import org.smap.surveyModel.utils.AnswerValidator;

public class SurveyEventTest {

	private SurveyModel surveyBot = null;

	@Before  
	public void setUp() throws Exception {
		surveyBot = null;
		surveyBot = new SurveyModel("data/multiple_choice_form.xml");
		assert (surveyBot) != null;
	}
	
	/* Needs rewrite, question type logic has been pushed out of the event
	//QuestionEvent
	@Test
	public void testSelectChoiceString() {
		assertEquals("Choice 1\nChoice 2\nChoice 3\n",((QuestionEvent) surveyBot.getCurrentEvent()).getSelectChoicesString());
	}
	
	@Test
	public void testisMulitChoiceQuestion() {
		assertTrue(((QuestionEvent) surveyBot.getCurrentEvent()).isMulitChoiceQuestion());
	}
	
	@Test
	public void testisChoiceQuestion() {
		surveyBot.jumpToNextEvent();
		assertTrue(((QuestionEvent) surveyBot.getCurrentEvent()).isChoiceQuestion());
	}
	*/
}
