package controller;

import java.io.IOException;

import org.odk.JavaRosaException;
import org.smap.surveyModel.AnswerValidator;
import org.smap.surveyModel.SurveyModel;



public class ControllerTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		stepThroughSurvey("data/Birds.xml");
		stepThroughSurvey("data/Household Survey.xml");
		stepThroughSurvey("data/Demo Form.xml");

		SurveyModel surveyBot = new SurveyModel("data/String only form.xml");
		
		
		System.out.println(surveyBot.getPrompt());
		try {
			surveyBot.answer("Plain String");
		} catch (JavaRosaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String answeredXML = surveyBot.getAnsweredXML();
		System.out.println(answeredXML);
		System.out.println(AnswerValidator.getFirstValueFromTag(answeredXML, "textFieldVanilla"));
		
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
