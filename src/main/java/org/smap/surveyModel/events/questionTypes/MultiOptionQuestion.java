package org.smap.surveyModel.events.questionTypes;

import java.util.Vector;

import org.javarosa.core.model.SelectChoice;
import org.odk.FormController;
import org.smap.surveyModel.utils.SMSConstants;

public abstract class MultiOptionQuestion extends QuestionTypeImpl {

	public MultiOptionQuestion(FormController formController){
		super(formController);
	}

	public String getQuestionText(){
		return super.getQuestionText()
                +"\n"+getSelectChoicesString();
	}

	public String getSelectChoicesString(){
		Vector<SelectChoice> selectChoices = super.getFormEntryPrompt().getSelectChoices();
		StringBuilder sb = new StringBuilder();
		for(SelectChoice choice : selectChoices){
			sb.append(super.getFormEntryPrompt().getSelectItemText(choice.selection()));
			sb.append(SMSConstants.NEWLINE);
		}
		return sb.toString();
	}
}
