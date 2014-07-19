package SurveyBot;

import org.javarosa.core.model.Constants;
import org.javarosa.core.model.data.AnswerDataFactory;
import org.javarosa.core.model.data.IAnswerData;
import org.javarosa.form.api.FormEntryPrompt;

import controller.FormController;

public class QuestionEvent implements ISurveyEvent {
	
	private FormEntryPrompt formEntryPrompt;
	private FormController formController;
	
	public QuestionEvent(FormController formController){
		this.formController = formController;
		this.formEntryPrompt = formController.getQuestionPrompt();
	}
	
	public String getPromptText() {
		return formEntryPrompt.getShortText();
	}

	@Override
	public void answer(String answerText) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean isChoiceQuestion(){
		return formEntryPrompt.getDataType()==Constants.DATATYPE_CHOICE;
	}
	
	public boolean isMulitChoiceQuestion(){
		return formEntryPrompt.getDataType()==Constants.DATATYPE_CHOICE_LIST;
	}
	
	/**Returns an IAnswerData object which can be used to answer the current question
	 * @return
	 */
	public IAnswerData getAnswerContainer(){
		return AnswerDataFactory.templateByDataType(formController.getQuestionPrompt().getControlType());
	}

	@Override
	public String info() {
		// TODO Auto-generated method stub
		return null;
	}
}
