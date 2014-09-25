package org.odk.surveyConverser;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import org.smap.SurveyConversation;
import org.smap.surveyModel.SurveyModel;

public class SurveyConverser implements SurveyConversation{

	private SurveyModel surveyModel;
	
	public SurveyConverser(String surveyDefinitionXML){
		
		this.surveyModel=new SurveyModel(surveyDefinitionXML);
	}
	
	public void answerCurrentQuestion(String answerText) {
		surveyModel.answer(answerText);
	}
	
	public String getCurrentQuestion() {
		return surveyModel.getPrompt();
	}

	public String[] getAllQuestions() {
		return surveyModel.getFullQuestionPromptList();
	}

	public Boolean isComplete() {
		return surveyModel.isEndOfSurvey();
	}

	@Override
	public Object save() {
		// TODO Auto-generated method stub
		return null;
	}

}
