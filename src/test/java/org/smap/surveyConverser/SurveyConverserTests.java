package org.smap.surveyConverser;

import org.junit.Before;
import org.junit.Test;
import org.odk.FileUtils;
import org.smap.DialogueHandler;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SurveyConverserTests {

	private String xformXML;
    private DialogueHandler handler;
    private SurveyConverser subject;



	@Before
	public void setUp(){
		this.xformXML = FileUtils.readFileToString("data/String only form.xml");
        this.handler = mock(DialogueHandler.class);
        when(handler.getFormXml()).thenReturn(this.xformXML);
	}

	@Test
	public void testSecondQuestionCreate() {
        // arrange
		subject = SurveyConverser.createNewSurveyConverser(this.xformXML);

        // act
		subject.answerCurrentQuestion("yolo");

        // assert
        assertEquals("Text Field Required\n[this is a required field]",
                subject.getCurrentQuestion());
	}

	@Test
	public void testSecondQuestionResume() {
        // arrange
		SurveyConverser converser = SurveyConverser.createNewSurveyConverser(this.xformXML);
		converser.answerCurrentQuestion("yolo");
		String saved = converser.save();

        // act
		converser = SurveyConverser.resume(saved);

        // assert
		assertEquals("Text Field Required\n[this is a required field]",
				converser.getCurrentQuestion());
	}

    // beginDialogue

    @Test
    public void testBeginDialogueRecordsSurveyDetails() {
        // arrange

        // act
        SurveyConverser.beginDialogue(handler);

        // assert
        verify(handler, atLeastOnce())
                .recordSurveyDetails(any(SurveyConverser.class));
    }

    @Test
    public void testBeginDialogueSavesData() {
        // arrange

        // act
        SurveyConverser.beginDialogue(handler);

        // assert
        verify(handler, atLeastOnce())
                .saveData(anyString(), anyString());
    }

    @Test
    public void testBeginDialogueSendsMessage() {
        // arrange
        String expectedReply = "Text Field Vanilla\n[no restrictions on this field]";

        // act
        SurveyConverser.beginDialogue(handler);

        //
        verify(handler, atLeastOnce())
                .reply(expectedReply);
    }

    // handleDialog

    @Test
    public void testHandleDialog_incompleteSurvey_GetsAnswerQuestion() {
        // arrange
       stubConverserAtFirstQuestion();

        // act
        SurveyConverser.handleDialog(handler);

        // assert
        verify(handler, atLeastOnce()).getAnswerText();
    }

    @Test
    public void testHandleDialog_incompleteSurvey_sendsReply() {
        // arrange
        stubConverserAtFirstQuestion();

        // act
        SurveyConverser.handleDialog(handler);

        // assert
        verify(handler, atLeastOnce()).reply(anyString());
    }

    @Test
    public void testHandleDialog_completedSurvey_doesntSendReply() {
        // arrange
        stubConverserAtLastQuestion();
        // act
        SurveyConverser.handleDialog(handler);

        // assert
        verify(handler, never()).reply(anyString());
    }

    // helpers
    private void stubConverserAtFirstQuestion() {
        SurveyConverser converser = SurveyConverser.createNewSurveyConverser(xformXML);

        when(handler.loadData())
                .thenReturn(converser.save(), converser.getAnswers());
    }

    private void stubConverserAtLastQuestion() {
        SurveyConverser converser = SurveyConverser.createNewSurveyConverser(xformXML);

        converser.answerCurrentQuestion("sweet answer");
        converser.answerCurrentQuestion("another answer");
        when(handler.getAnswerText()).thenReturn("last ans");

        when(handler.loadData()).thenReturn(converser.save(), converser.getAnswers());
    }

}
