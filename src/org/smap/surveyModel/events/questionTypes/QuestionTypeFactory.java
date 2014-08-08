package org.smap.surveyModel.events.questionTypes;

import org.javarosa.core.model.Constants;
import org.javarosa.form.api.FormEntryPrompt;
import org.odk.FormController;

public class QuestionTypeFactory {
	public static QuestionType getQuestionType(FormController formController) throws Exception{
		FormEntryPrompt formEntryPrompt = formController.getQuestionPrompt();
		
		//System.out.println(formEntryPrompt.getDataType());
		
		switch(formEntryPrompt.getDataType()){
		case Constants.DATATYPE_TEXT:
			return new StringQuestion(formController);
		case Constants.DATATYPE_INTEGER:
			return new StringQuestion(formController);
		case Constants.DATATYPE_CHOICE:
			return new SingleChoiceQuestion(formController);
		case Constants.DATATYPE_CHOICE_LIST:
			return new ChoiceListQuestion(formController);
		default:
			System.out.println("Nooooo");
			throw new Exception("Unsupported Question Type");
		}
	}
}
