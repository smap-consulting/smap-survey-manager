package SurveyBot;

import controller.FormController;

public class GroupEvent implements ISurveyEvent {

	private FormController formController;

	public GroupEvent(FormController formController){
		this.formController = formController;
	}
	
	public String getPromptText() {
		return formController.getLastGroupText();
	}

	@Override
	public void answer(String answerText) {
		// TODO Auto-generated method stub
	}

	@Override
	public String info() {
		// TODO Auto-generated method stub
		return null;
	}
}
