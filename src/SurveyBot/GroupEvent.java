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

	public String info() {
		StringBuilder sb = new StringBuilder("Event Type: Group\n");
		sb.append("Prompt: "+getPromptText());
		return sb.toString();
	}
}
