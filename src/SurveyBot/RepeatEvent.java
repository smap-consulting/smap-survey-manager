package SurveyBot;

import controller.FormController;

public class RepeatEvent implements ISurveyEvent {

	private FormController formController;

	public RepeatEvent(FormController formController){
		this.formController = formController;
	}
	
	@Override
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

	@Override
	public String info() {
		// TODO Auto-generated method stub
		return null;
	}
}
