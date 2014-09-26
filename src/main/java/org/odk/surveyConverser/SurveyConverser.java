package org.odk.surveyConverser;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import org.smap.SurveyConversation;
import org.smap.surveyModel.SurveyModel;
import org.smap.surveyModel.utils.JRSerializer;

public class SurveyConverser implements SurveyConversation{

	private SurveyModel surveyModel;
	
	public static SurveyConverser createNewSurveyConverser(String surveyDefinitionXML){
		return new SurveyConverser(surveyDefinitionXML);
	}
	
	public static SurveyConverser resume(String savedConverser){
		return new SurveyConverser(JRSerializer.deserializeSurveyModel(savedConverser));
	}
	
	public String save() {
		return JRSerializer.serializeSurveyModel(this.surveyModel);
	}
	
	private SurveyConverser(SurveyModel surveyModel){
		this.surveyModel=surveyModel;
	}
	
	private SurveyConverser(String surveyDefinitionXML){
		this.surveyModel=SurveyModel.createSurveyModelFromXform(surveyDefinitionXML);
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
}
