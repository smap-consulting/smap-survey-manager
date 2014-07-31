package org.smap.surveyModel.events;

import java.util.Vector;

import javax.xml.bind.ValidationException;


import org.javarosa.core.model.Constants;
import org.javarosa.core.model.SelectChoice;
import org.javarosa.core.model.data.AnswerDataFactory;
import org.javarosa.core.model.data.IAnswerData;
import org.javarosa.core.model.data.UncastData;
import org.javarosa.form.api.FormEntryPrompt;
import org.odk.FormController;
import org.odk.JavaRosaException;
import org.smap.surveyModel.SurveyModel;
import org.smap.surveyModel.SurveyModel.SurveyAction;
import org.smap.surveyModel.utils.SMSConstants;




public class QuestionEvent implements ISurveyEvent {
	
	final private FormEntryPrompt formEntryPrompt;
	private FormController formController;
	
	public QuestionEvent(FormController formController){
		this.formController = formController;
		this.formEntryPrompt = formController.getQuestionPrompt();
	}
	
	public String getPromptText() {
		StringBuilder sb = new StringBuilder(formEntryPrompt.getShortText());
		if(isChoiceQuestion() || isMulitChoiceQuestion())
			sb.append(SMSConstants.NEWLINE+getSelectChoicesString());
		if(formEntryPrompt.getHelpText()!=null){
			sb.append(SMSConstants.NEWLINE);
			sb.append(SMSConstants.PRE_HINT+formEntryPrompt.getHelpText()+SMSConstants.POST_HINT);
		}
		return sb.toString();
	}

	public SurveyAction answer(String answerText) throws JavaRosaException {
		IAnswerData answerContainer = getAnswerContainer();
		UncastData uncastData = new UncastData(answerText);
		IAnswerData answeredContainer = answerContainer.cast(uncastData);
		formController.answerQuestion(formController.getFormIndex(),answeredContainer);
		System.out.println(formController.validateAnswers(false));
		return SurveyAction.forward;
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

	public String info() {
		StringBuilder sb = new StringBuilder("Event Type: Question\n");
		sb.append("Prompt:"+getPromptText()+"\n");
		sb.append("Answer Type: " + getAnswerContainer().getClass());
		return sb.toString();
	}
	
	public String getSelectChoicesString(){
		Vector<SelectChoice> selectChoices = formEntryPrompt.getSelectChoices();
		StringBuilder sb = new StringBuilder();
		for(SelectChoice choice : selectChoices){
			sb.append(formEntryPrompt.getSelectItemText(choice.selection()));
			sb.append(SMSConstants.NEWLINE);
		}
		return sb.toString();
	}
}
