package org.smap.surveyModel.events.questionTypes;

import org.javarosa.form.api.FormEntryPrompt;
import org.odk.FormController;
import org.smap.surveyModel.utils.SMSConstants;

public class QuestionTypeImpl implements QuestionType {
	private FormController formController;
	public FormController getFormController() {
		return formController;
	}

	public void setFormController(FormController formController) {
		this.formController = formController;
	}

	public FormEntryPrompt getFormEntryPrompt() {
		return formEntryPrompt;
	}

	public void setFormEntryPrompt(FormEntryPrompt formEntryPrompt) {
		this.formEntryPrompt = formEntryPrompt;
	}

	private FormEntryPrompt formEntryPrompt;

	public QuestionTypeImpl(FormController formController){
		this.formController = formController;
		this.formEntryPrompt = formController.getQuestionPrompt();
	}

	public String info() {
		return "Question Type:" + getQuestionTypeString();
	}

	public String getQuestionTypeString(){
		return "Generic Question";
	}

	public String getPrompt() {
		StringBuilder sb = new StringBuilder();
		sb.append(getQuestionText());
		if(hasHelpText())
        {
			sb.append(SMSConstants.NEWLINE);
            sb.append(SMSConstants.PRE_HINT +
                    formEntryPrompt.getHelpText() +
                    SMSConstants.POST_HINT);
		}
		return sb.toString();
	}

    private boolean hasHelpText(){
        return formEntryPrompt.getHelpText()!=null
                && formEntryPrompt.getHelpText().compareTo("")!=0;
    }

	protected String getQuestionText(){
		return formEntryPrompt.getShortText();
	}
}
