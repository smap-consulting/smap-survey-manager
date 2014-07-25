package SurveyBot;

import SurveyBot.SurveyModel.SurveyAction;
import controller.FormController;

public class RepeatEvent implements ISurveyEvent {

	final private FormController formController;

	public RepeatEvent(FormController formController){
		this.formController = formController;
	}
	
	public String getPromptText() {
		return formController.getLastRepeatedGroupName();
	}

	public SurveyAction answer(String answerText, FormController formController) {
		
		return SurveyAction.forward;
	}
	
	public int getRepeatCount(){
		return formController.getLastRepeatCount();
	}

	public String info() {
		StringBuilder sb = new StringBuilder("Event Type: Repeat\n");
		sb.append("Prompt: "+getPromptText()+"\n");
		sb.append("RepeatNumber: "+getRepeatCount());
		return sb.toString();
	}
}
