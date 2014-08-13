package org.smap.surveyModel.events.questionTypes;

import org.odk.FormController;

public class StringQuestion extends QuestionTypeImpl {
	public StringQuestion(FormController formController){
		super(formController);
	}
	
	public String getQuestionTypeString(){
		return "String";
	}
}
