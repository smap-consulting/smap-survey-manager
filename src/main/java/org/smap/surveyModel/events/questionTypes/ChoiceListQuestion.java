package org.smap.surveyModel.events.questionTypes;

import org.odk.FormController;

public class ChoiceListQuestion extends MultiOptionQuestion {
	public ChoiceListQuestion(FormController formController) {
		super(formController);
	}
	
	public String getQuestionTypeString(){
		return "Choice List";
	}
}
