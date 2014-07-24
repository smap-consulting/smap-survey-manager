package SurveyBot;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.javarosa.core.model.FormDef;
import org.javarosa.core.model.FormIndex;
import org.javarosa.core.services.transport.payload.ByteArrayPayload;
import org.javarosa.form.api.FormEntryController;
import org.javarosa.form.api.FormEntryModel;
import org.javarosa.xform.util.XFormUtils;
import controller.FormController;
import controller.JavaRosaException;

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
		this.formController = initFormController(formDef,null);
		if(!formController.currentPromptIsQuestion())
			jumpToFirstAnswerableQuestion();
	}
	
	/**
	 * Reload an existing survey instance
	 * @param xformFilePath
	 * @param savedInstancePath
	 */
	public SurveyModel(String xformFilePath, String savedInstancePath, FormIndex index){
		FormDef formDef = createFormDef(xformFilePath);
		this.formDef=formDef;
		this.formController = initFormController(formDef, new File(savedInstancePath));
		this.formController.jumpToIndex(index);
	}
	
	
	/**
	 * Returns the prompt text for the current event
	 * @return
	 */
	public String getPrompt(){
		return currentEvent.getPromptText();
	}
	
	private FormController initFormController(FormDef formDef, File savedInstancePath) {
		FormEntryModel formEntryModel = new FormEntryModel(formDef);
		FormEntryController formEntryController = new FormEntryController(formEntryModel);
		formEntryController.setLanguage(getLocale(0));
		formEntryController.jumpToIndex(FormIndex.createBeginningOfFormIndex());
		FormController formController = new FormController(new File("a path"), formEntryController, null);
		return formController;
	}
	
	private String getLocale(int index) {
		return this.formDef.getLocalizer().getAvailableLocales()[index];
	}
	
	public FormIndex getCurrentIndex() {
		return formController.getCaptionPrompt().getIndex();
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
			jumpToNextEvent();
		setCurrentEvent();
	}
	
	/**
	 *Steps to the next event, will always dive into group questions, but will skip over repeats 
	 */
	public void jumpToNextEvent(){
		try {
			formController.stepToNextScreenEvent();
		} catch (JavaRosaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		jumpToNextEvent();
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
	
	public String getEventInfo(){
		StringBuilder sb = new StringBuilder(currentEvent.info());
		return sb.toString();
	}
	
	public String[] getFullQuestionPromptList(){
		ArrayList<String> questionList = new ArrayList<String>();
		
		while(!isEndOfSurvey()){
			ISurveyEvent event = getCurrentEvent();
			questionList.add(getCurrentEvent().getPromptText());
			if(event instanceof RepeatEvent && ((RepeatEvent) event).getRepeatCount() < 2){
				stepIntoRepeat();
			}else
				jumpToNextEvent();
		}
		String[] returnArray = new String[questionList.size()]; 
		return questionList.toArray(returnArray);
	}
	
	public String getAnsweredXML(){
		ByteArrayPayload bap = null;
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(baos);
			bap = formController.getFilledInFormXml();
			bap.writeExternal(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String str=null;
		try {
			str=baos.toString("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
}
