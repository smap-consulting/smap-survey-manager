package SurveyBot;

public interface ISurveyEvent {
	public String getPromptText();
	public void answer(String answerText);
	public String info();
}
