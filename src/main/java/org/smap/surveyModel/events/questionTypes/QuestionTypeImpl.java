package org.smap.surveyModel.events.questionTypes;

import org.javarosa.core.model.data.AnswerDataFactory;
import org.javarosa.core.model.data.IAnswerData;
import org.javarosa.core.model.data.UncastData;
import org.javarosa.form.api.FormEntryController;
import org.javarosa.form.api.FormEntryPrompt;
import org.odk.FormController;
import org.odk.JavaRosaException;
import org.smap.surveyModel.SurveyModel;
import org.smap.surveyModel.utils.SMSConstants;
import org.smap.surveyModel.utils.SurveyMessageConstants;

public class QuestionTypeImpl implements QuestionType {
	private FormController formController;
    private boolean invalidAnswerProvided;
    private String invalidAnswerMessage;

	public FormController getFormController() {
		return formController;
	}

	public void setFormController(FormController formController) {
		this.formController = formController;
	}

	public FormEntryPrompt getFormEntryPrompt() {
		return formEntryPrompt;
	}

	private FormEntryPrompt formEntryPrompt;

	public QuestionTypeImpl(FormController formController){
		this.formController = formController;
		this.formEntryPrompt = formController.getQuestionPrompt();
        this.invalidAnswerProvided = false;
	}

	public String info() {
        StringBuilder sb = new StringBuilder();
        sb.append("Prompt:"+getPrompt()+"\n");
        sb.append("Answer Type: " + getAnswerContainer().getClass());
        sb.append("Question Type:" + getQuestionTypeString());
        return sb.toString();
	}


	public String getQuestionTypeString(){
		return "Generic Question";
	}

	public String getPrompt() {
		StringBuilder sb = new StringBuilder();
        if(invalidAnswerProvided){
            sb.append(invalidAnswerMessage + SMSConstants.NEWLINE);
            sb.append(SurveyMessageConstants.TRY_AGAIN_TEXT + SMSConstants.NEWLINE);
        }
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
        return (formEntryPrompt.getHelpText() != null)
                && (formEntryPrompt.getHelpText().compareTo("") != 0);
    }

	protected String getQuestionText(){
		return formEntryPrompt.getShortText();
	}


    public SurveyModel.SurveyAction answer(String answerText) {
        int answerResult=0;
        try{
            if(answerText == null || answerText.equals("")){
                //try to add empty answer
                answerResult = formController.answerQuestion(formController.getFormIndex(), null);
            }else{
                IAnswerData answerContainer = getAnswerContainer();
                UncastData uncastData = new UncastData(answerText);
                IAnswerData answeredContainer = answerContainer.cast(uncastData);
                answerResult = formController.answerQuestion(formController.getFormIndex(),answeredContainer);
            }
        }catch(JavaRosaException e){
            flagInvalidAnswer(e.getLocalizedMessage());
        }

        if(answerResult == FormEntryController.ANSWER_CONSTRAINT_VIOLATED){
            String customText = formController.getQuestionPromptConstraintText(formController.getFormIndex());
            if(customText!=null)
                flagInvalidAnswer(customText);
            else
                flagInvalidAnswer("Constraint Violated");
        }else if(answerResult == FormEntryController.ANSWER_REQUIRED_BUT_EMPTY)
            flagInvalidAnswer(SurveyMessageConstants.ANSWER_REQUIRED_TEXT);
        if(invalidAnswerProvided)
            return SurveyModel.SurveyAction.stay;
        return SurveyModel.SurveyAction.forward;
    }

    private void flagInvalidAnswer(String message){
        invalidAnswerProvided = true;
        invalidAnswerMessage = message;
    }

    /**Returns an IAnswerData object which can be used to answer the current question
     * @return
     */
    public IAnswerData getAnswerContainer(){
        return AnswerDataFactory.templateByDataType(formController.getQuestionPrompt().getControlType());
    }

}
