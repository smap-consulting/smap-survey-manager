package org.smap.surveyModel.events;

import org.javarosa.core.model.FormIndex;
import org.odk.FormController;
import org.smap.surveyModel.SurveyModel.SurveyAction;


public class RepeatEvent extends SurveyEvent {

	public RepeatEvent(FormController formController){
		super(formController);
	}

	public String getPromptText() {
		return formController.getLastRepeatedGroupName();
	}

    public SurveyAction answer(String answerText) {
		return SurveyAction.into;
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
