package SurveyBot;

import SurveyBot.SurveyModel.SurveyAction;
import controller.FormController;

public interface ISurveyEvent {
	public String getPromptText();
	public SurveyAction answer(String answerText, FormController formController);
	public String info();
}
