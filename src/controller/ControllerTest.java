package controller;

import java.io.IOException;
import SurveyBot.SurveyModel;

public class ControllerTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		stepThroughSurvey("data/Birds.xml");
		stepThroughSurvey("data/Household Survey.xml");
		stepThroughSurvey("data/Demo Form.xml");

		SurveyModel surveyBot = new SurveyModel("data/Household Survey.xml");
		
		while(!surveyBot.isEndOfSurvey()){
			System.out.println(surveyBot.getPrompt());
			surveyBot.answer("hello");
			System.out.println(surveyBot.getAnsweredXML());
		}
		
		//answer question
		
		//get instance + index
		
		//(save to file)
		
		//create new survey by reloading instance, set index
		
		//test result
		
	}
	public static void stepThroughSurvey(String formFilePath){
		SurveyModel surveyBot = new SurveyModel(formFilePath);
		System.out.println(surveyBot.getFullQuestionPromptList().length);
	}
}
