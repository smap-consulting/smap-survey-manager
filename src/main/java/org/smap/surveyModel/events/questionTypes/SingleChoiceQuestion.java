package org.smap.surveyModel.events.questionTypes;


import org.odk.FormController;

public class SingleChoiceQuestion extends MultiOptionQuestion {
	
	public SingleChoiceQuestion(FormController formController) {
		super(formController);
	}
	
	public String getQuestionTypeString(){
		return "Single Choice List";
	}
	
}
