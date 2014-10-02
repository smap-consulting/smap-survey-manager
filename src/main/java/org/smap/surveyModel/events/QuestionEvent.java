package org.smap.surveyModel.events;

import org.javarosa.core.model.FormIndex;
import org.javarosa.core.model.data.AnswerDataFactory;
import org.javarosa.core.model.data.IAnswerData;
import org.javarosa.core.model.data.UncastData;
import org.javarosa.form.api.FormEntryController;
import org.odk.FormController;
import org.odk.JavaRosaException;
import org.smap.surveyModel.SurveyModel.SurveyAction;
import org.smap.surveyModel.events.questionTypes.QuestionType;
import org.smap.surveyModel.events.questionTypes.QuestionTypeFactory;
import org.smap.surveyModel.utils.SMSConstants;
import org.smap.surveyModel.utils.SurveyMessageConstants;




public class QuestionEvent extends SurveyEvent {

	private boolean invalidAnswerProvided;
	private String invalidAnswerMessage;
	private QuestionType question;

	public QuestionEvent(FormController formController){
        super(formController);
		this.invalidAnswerProvided = false;
		try {
			this.question = QuestionTypeFactory.getQuestionType(formController);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public String getPromptText() {
		StringBuilder sb = new StringBuilder();
		if(invalidAnswerProvided){
			sb.append(invalidAnswerMessage + SMSConstants.NEWLINE);
			sb.append(SurveyMessageConstants.TRY_AGAIN_TEXT + SMSConstants.NEWLINE);
		}
		sb.append(question.getPrompt());
		return sb.toString();
	}

    public SurveyAction answer(String answerText){
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
			return SurveyAction.stay;
		return SurveyAction.forward;
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


	public String info() {
		StringBuilder sb = new StringBuilder("Event Type: Question\n");
		sb.append(question.info()+"\n");
		sb.append("Prompt:"+getPromptText()+"\n");
		sb.append("Answer Type: " + getAnswerContainer().getClass());
		return sb.toString();
	}
}
