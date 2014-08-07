package org.smap;



public interface SurveyConversation {
	
	public void answerCurrentQuestion(String answerText);
	
	public String getCurrentQuestion();
	
	public String[] getAllQuestions();
	
	public Boolean isComplete();
	
}
