package org.smap;



public interface SurveyConversation {
	
	public void answerCurrentQuestion(String answerText);
	
	//Save the survey, need to think about how this can be saved in DB
	public Object save();
	
	public String getCurrentQuestion();
	
	public String[] getAllQuestions();
	
	public Boolean isComplete();
	
}
