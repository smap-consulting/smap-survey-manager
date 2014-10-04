package org.smap.surveyModel.events.questionTypes;

import org.smap.surveyModel.SurveyModel;

public interface QuestionType {
	public String info();
	public String getPrompt();
    public SurveyModel.SurveyAction answer(String answerText);
}
