package org.smap.surveyModel.events;

import org.odk.FormController;
import org.smap.surveyModel.SurveyModel.SurveyAction;
import org.smap.surveyModel.events.questionTypes.QuestionType;
import org.smap.surveyModel.events.questionTypes.QuestionTypeFactory;


public class QuestionEvent extends SurveyEvent {


	private QuestionType question;

	public QuestionEvent(FormController formController){
        super(formController);
		try {
			this.question = QuestionTypeFactory.getQuestionType(formController);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public String getPromptText() {
		return question.getPrompt();
	}

    public SurveyAction answer(String answerText){
		return this.question.answer(answerText);
	}

	public String info() {
		return "Event Type: Question\n" +
		    question.info()+"\n";
	}
}
