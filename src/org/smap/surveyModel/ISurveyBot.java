package org.smap.surveyModel;



public interface ISurveyBot {
	
	public enum AnswerResult{
	    OK,INVALID
	}
	
	public Boolean isComplete();
	
	public AnswerResult answer();
	
}
