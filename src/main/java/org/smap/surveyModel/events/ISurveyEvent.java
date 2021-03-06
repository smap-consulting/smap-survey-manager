package org.smap.surveyModel.events;

import javax.xml.bind.ValidationException;

import org.odk.JavaRosaException;
import org.smap.surveyModel.SurveyModel;
import org.smap.surveyModel.SurveyModel.SurveyAction;

public interface ISurveyEvent {
	public String getPromptText();
	public SurveyAction answer(String answerText);
	public String info();
}
