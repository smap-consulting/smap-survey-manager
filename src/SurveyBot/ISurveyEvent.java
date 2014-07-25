package SurveyBot;

import SurveyBot.SurveyModel.SurveyAction;
import controller.FormController;
import controller.JavaRosaException;

public interface ISurveyEvent {
	public String getPromptText();
	public SurveyAction answer(String answerText, FormController formController) throws JavaRosaException;
	public String info();
}
