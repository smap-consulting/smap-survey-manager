package SurveyBot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


import org.javarosa.core.model.FormDef;
import org.javarosa.core.model.FormIndex;
import org.javarosa.core.services.locale.Localizer;
import org.javarosa.form.api.FormEntryController;
import org.javarosa.form.api.FormEntryModel;
import org.javarosa.xform.util.XFormUtils;
import controller.FormController;

/**
 * @author Scott Wells
 *
 */

public class SurveyModel {
	private FormController formController;
	private FormDef formDef;
	private ISurveyEvent currentEvent;
	
	/**
	 * Construct a suvey model given xform xml
	 * @param xformFilePath
	 */
	public SurveyModel(String xformFilePath){
		FormDef formDef = createFormDef(xformFilePath);
		this.formDef=formDef;
		this.formController = initFormController(formDef);
		if(!formController.currentPromptIsQuestion())
			jumpToFirstAnswerableQuestion();
	}
	
	public String getPrompt(){
		return currentEvent.getPromptText();
	}
	
	private FormController initFormController(FormDef formDef) {
		FormEntryModel formEntryModel = new FormEntryModel(formDef);
		FormEntryController formEntryController = new FormEntryController(formEntryModel);
		formEntryController.setLanguage(getLocale(0));
		formEntryController.jumpToIndex(FormIndex.createBeginningOfFormIndex());
		FormController formController = new FormController(new File("a path"), formEntryController, null);
		return formController;
	}
	
	private String getLocale(int index) {
		Localizer l = this.formDef.getLocalizer();
		l.setDefaultLocale(l.getAvailableLocales()[index]);
		l.setLocale(l.getAvailableLocales()[index]);
		return l.getLocale();
	}
	
	private FormDef createFormDef(String xformFilePath){
		FileInputStream inputStream;
		try {
			inputStream = new FileInputStream(xformFilePath);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Error: the file '" + xformFilePath
					+ "' could not be found!");
		}
		return XFormUtils.getFormFromInputStream(inputStream);
	}
	
	private void jumpToFirstAnswerableQuestion(){
		if(!formController.currentPromptIsQuestion())
			jumpToNextScreenEvent();
		setCurrentEvent();
	}
	
	/**
	 *Steps to the next question, will always dive into group questions, but will skip over repeats 
	 */
	public void jumpToNextScreenEvent(){
		formController.stepToNextEvent(true);
		setCurrentEvent();
	}
	
	private void setCurrentEvent(){
		if(isGroup())
			currentEvent=new GroupEvent(formController);
		else if(isQuestion())
			currentEvent=new QuestionEvent(formController);
		else if(isRepeat())
			currentEvent=new RepeatEvent(formController);
		else currentEvent=null;
	}
	
	public void stepIntoRepeat(){
		formController.newRepeat();
		setCurrentEvent();
	}
	
	private boolean isGroup(){
		return formController.getEvent() == FormEntryController.EVENT_GROUP;
	}
	
	private boolean isQuestion(){
		return formController.getEvent() == FormEntryController.EVENT_QUESTION;
	}

	public boolean isRepeat(){
		return formController.getEvent() == FormEntryController.EVENT_REPEAT ||
			formController.getEvent() == FormEntryController.EVENT_PROMPT_NEW_REPEAT ||
			formController.getEvent() == FormEntryController.EVENT_REPEAT_JUNCTURE;
	}
	
	public boolean isEndOfSurvey(){
		return formController.getEvent() == FormEntryController.EVENT_END_OF_FORM;
	}
	
	public ISurveyEvent getCurrentEvent(){
		return currentEvent;
	}
	
	public void getQuestionInfo(){
		System.out.print("Event Type: ");
		switch(formController.getEvent()){
		case FormEntryController.EVENT_GROUP:
			System.out.println("EVENT_GROUP");
			break;
		case FormEntryController.EVENT_QUESTION:
			System.out.println("EVENT_QUESTION");
		case FormEntryController.EVENT_REPEAT:
			System.out.println("EVENT_REPEAT");
			break;
		case FormEntryController.EVENT_PROMPT_NEW_REPEAT:
			System.out.println("EVENT_PROMPT_NEW_REPEAT");
			break;
		case FormEntryController.EVENT_REPEAT_JUNCTURE:
			System.out.println("EVENT_REPEAT_JUNCTURE");
			break;
		case FormEntryController.EVENT_END_OF_FORM:
			System.out.println("END_OF_FORM");
			return;
		}
		System.out.println("Prompt: "+getPrompt());
	}
}
