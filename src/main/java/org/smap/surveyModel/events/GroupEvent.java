package org.smap.surveyModel.events;

import org.odk.FormController;
import org.smap.surveyModel.SurveyModel;
import org.smap.surveyModel.SurveyModel.SurveyAction;


public class GroupEvent implements ISurveyEvent {

	final private FormController formController;

	public GroupEvent(FormController formController){
		this.formController = formController;
	}
	
	public String getPromptText() {
		return formController.getLastGroupText();
	}

	@Override
	public SurveyAction answer(String answerText) {
		return SurveyAction.forward;
	}

	public String info() {
		StringBuilder sb = new StringBuilder("Event Type: Group\n");
		sb.append("Prompt: "+getPromptText());
		return sb.toString();
	}
}
