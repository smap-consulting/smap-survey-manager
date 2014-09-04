package org.odk.surveyConverser;

import org.smap.SurveyConversation;
import org.smap.surveyModel.SurveyModel;

public class SurveyConverser implements SurveyConversation{

	private SurveyModel surveyModel;
	
	public void answerCurrentQuestion(String answerText) {
		surveyModel.answer(answerText);
	}

	public String getCurrentQuestion() {
		return surveyModel.getPrompt();
	}

	public String[] getAllQuestions() {
		return surveyModel.getFullQuestionPromptList();
	}

	public Boolean isComplete() {
		return surveyModel.isEndOfSurvey();
	}

}
