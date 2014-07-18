package SurveyBot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.javarosa.core.model.FormDef;
import org.javarosa.core.model.FormIndex;
import org.javarosa.core.model.data.AnswerDataFactory;
import org.javarosa.core.model.data.IAnswerData;
import org.javarosa.core.services.locale.Localizer;
import org.javarosa.form.api.FormEntryCaption;
import org.javarosa.form.api.FormEntryController;
import org.javarosa.form.api.FormEntryModel;
import org.javarosa.form.api.FormEntryPrompt;
import org.javarosa.xform.util.XFormUtils;

import controller.FormController;
import controller.JavaRosaException;
import controller.FormController.InstanceMetadata;

public class SurveyModel {
	private FormController formController;
	private FormDef formDef;
	
	public SurveyModel(String xformFilePath){
		FormDef formDef = createFormDef(xformFilePath);
		this.formDef=formDef;
		this.formController = initFormController(formDef);
		if(!formController.currentPromptIsQuestion())
			jumpToFirstAnswerableQuestion();
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
	
	public String getPrompt(){
		if(isQuestionGroup())
			return getGroupPropmt();
		FormEntryPrompt entryPrompt =  formController.getQuestionPrompt();
		return entryPrompt.getShortText();
	}
	
	private void jumpToFirstAnswerableQuestion(){
		if(!formController.currentPromptIsQuestion())
			jumpToNextScreenEvent();
	}
	
	private void jumpToNextScreenEvent(){
		try {
			formController.stepToNextScreenEvent();
		} catch (JavaRosaException e) {
			e.printStackTrace();
		}
	}
	
	private boolean isQuestionGroup(){
		if(formController.getEvent()== FormEntryController.EVENT_GROUP)
			return true;
		return false;
	}
	
	private boolean isRepeatGroup(){
		if(formController.getEvent()== FormEntryController.EVENT_REPEAT)
			return true;
		return false;
	}
	
	private String getGroupPropmt(){
		return formController.getLastGroupText();
	}
	
	public IAnswerData getAnswerContainer(){
		return AnswerDataFactory.templateByDataType(formController.getQuestionPrompt().getControlType());
	}
	
	public void getQuestionInfo(){
		System.out.println("Prompt: "+getPrompt());
		System.out.print("Event Type: ");
		switch(formController.getEvent()){
		case FormEntryController.EVENT_GROUP:
			System.out.println("EVENT_GROUP");
			break;
		case FormEntryController.EVENT_QUESTION:
			System.out.println("EVENT_QUESTION");
			IAnswerData answerTemplate = getAnswerContainer();
			System.out.println(answerTemplate.getClass());
			break;	
		case FormEntryController.EVENT_REPEAT:
			System.out.println("EVENT_REPEAT");
			break;
		case FormEntryController.EVENT_PROMPT_NEW_REPEAT:
			System.out.println("EVENT_PROMPT_NEW_REPEAT");
			break;
		case FormEntryController.EVENT_REPEAT_JUNCTURE:
			System.out.println("EVENT_REPEAT_JUNCTURE");
			break;
		}
	}
}
