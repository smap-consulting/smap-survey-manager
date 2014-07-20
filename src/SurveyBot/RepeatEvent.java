package SurveyBot;

import controller.FormController;

public class RepeatEvent implements ISurveyEvent {

	private FormController formController;

	public RepeatEvent(FormController formController){
		this.formController = formController;
	}
	
	public String getPromptText() {
		return formController.getLastRepeatedGroupName();
	}

	@Override
	public void answer(String answerText) {
		// TODO Auto-generated method stub

	}
	
	public int getRepeatCount(){
		return formController.getLastRepeatCount();
	}

	public String info() {
		StringBuilder sb = new StringBuilder("Event Type: Repeat\n");
		sb.append("Prompt: "+getPromptText()+"\n");
		sb.append("RepeatNumber: "+getRepeatCount());
		return sb.toString();
	}
}
