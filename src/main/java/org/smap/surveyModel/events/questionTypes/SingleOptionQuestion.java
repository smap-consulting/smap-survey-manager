package org.smap.surveyModel.events.questionTypes;


import org.odk.FormController;

public class SingleOptionQuestion extends MultiOptionQuestion {
	
	public SingleOptionQuestion(FormController formController) {
		super(formController);
	}
	
	public String getQuestionTypeString(){
		return "Single Choice List";
	}
	
}
