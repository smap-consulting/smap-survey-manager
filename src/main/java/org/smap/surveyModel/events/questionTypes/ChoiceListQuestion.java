package org.smap.surveyModel.events.questionTypes;

import org.odk.FormController;

public class ChoiceListQuestion extends MultiAnswerQuestion {
	public ChoiceListQuestion(FormController formController) {
		super(formController);
	}
	
	public String getQuestionTypeString(){
		return "Choice List";
	}
}
