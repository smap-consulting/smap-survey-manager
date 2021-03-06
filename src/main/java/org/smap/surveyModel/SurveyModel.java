package org.smap.surveyModel;

import org.javarosa.core.model.FormDef;
import org.javarosa.core.model.FormIndex;
import org.javarosa.core.model.instance.TreeElement;
import org.javarosa.core.model.instance.TreeReference;
import org.javarosa.core.model.instance.utils.DefaultAnswerResolver;
import org.javarosa.core.services.transport.payload.ByteArrayPayload;
import org.javarosa.form.api.FormEntryController;
import org.javarosa.form.api.FormEntryModel;
import org.javarosa.xform.parse.XFormParser;
import org.javarosa.xform.util.XFormUtils;
import org.odk.FileUtils;
import org.odk.FormController;
import org.odk.JavaRosaException;
import org.smap.surveyModel.events.GroupEvent;
import org.smap.surveyModel.events.ISurveyEvent;
import org.smap.surveyModel.events.QuestionEvent;
import org.smap.surveyModel.events.RepeatEvent;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
/**
 * @author Scott Wells
 *
 */

public class SurveyModel{
	public enum SurveyAction{forward, backward, stay, end, start,into,retry};
	
	private FormController formController;
	private FormDef formDef;
	private ISurveyEvent currentEvent;
	private final String xFormXml; 
	
	public static SurveyModel createSurveyModelFromXform(String xformXML){
		return new SurveyModel(xformXML);
	}
	
	/**
	 * Construct a suvey model given xform xml
	 * @param xformXML
	 */
	private SurveyModel(String xformXML){
		this.xFormXml=xformXML;
		FormDef formDef = createFormDef(xformXML);
		this.formDef=formDef;
		this.formController = initFormController(formDef,null);
		jumpToFirstAnswerableQuestion();
	}
	
	public static SurveyModel resumeSurveyModel(String xformXML, String savedInstanceXML, FormIndex index){
		return new SurveyModel(xformXML, savedInstanceXML, index);
	}
	
	/**
	 * Reload an existing survey instance
	 * @param xformXML
	 * @param savedInstancePath
	 */
	private SurveyModel(String xformXML, String savedInstanceXML, FormIndex index){
		this.xFormXml=xformXML;
		FormDef formDef = createFormDef(xformXML);
		this.formDef=formDef;
		this.formController = initFormController(formDef, savedInstanceXML);
		this.formController.jumpToIndex(index);
		this.setCurrentEvent();
	}
	
	public String getInitialFormDefXML(){
		return xFormXml;
	}
	
	/**
	 * Returns the prompt text for the current event
	 * @return
	 */
	public String getPrompt(){
		return currentEvent.getPromptText();
	}
	
	public String getSurveyName(){
		return formDef.getName();
	}
	
	public void answer(String answerText){
		processAction(currentEvent.answer(answerText));
		//currentEvent.answer(answerText);

	}
	
	public void processAction(SurveyAction action){
		switch(action){
			case forward:
				jumpToNextEvent();
				break;
			default:
				break;
		}
	}
	
	private FormController initFormController(FormDef formDef, String savedInstanceXML) {
		FormEntryModel formEntryModel = new FormEntryModel(formDef);
		FormEntryController formEntryController = new FormEntryController(formEntryModel);
		
		if(savedInstanceXML != null){
			loadSavedInstance(savedInstanceXML, formEntryController);
		}
		
		formEntryController.setLanguage(getLocale(0));
		formEntryController.jumpToIndex(FormIndex.createBeginningOfFormIndex());
		FormController formController = new FormController(null, formEntryController, null);
		return formController;
	}
	
	private String getLocale(int index) {
		return this.formDef.getLocalizer().getAvailableLocales()[index];
	}
	
	public FormIndex getCurrentIndex() {
		return formController.getCaptionPrompt().getIndex();
	}
	
	private FormDef createFormDef(String xformXML){
		InputStream inputStream = FileUtils.convertStringToInputStream(xformXML);
		return XFormUtils.getFormFromInputStream(inputStream);
	}
	
	private void jumpToFirstAnswerableQuestion(){
		getFormController().jumpToIndex(FormIndex.createBeginningOfFormIndex());
		if(!formController.currentPromptIsQuestion())
			jumpToNextEvent();
		setCurrentEvent();
	}
	
	/**
	 *Steps to the next event,but will skip over repeats 
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
		SurveyModel model = this;
		model.jumpToFirstAnswerableQuestion();
		
		while(!model.isEndOfSurvey()){
			ISurveyEvent event = model.getCurrentEvent();
			questionList.add(model.getCurrentEvent().getPromptText());
			if(event instanceof RepeatEvent && ((RepeatEvent) event).getRepeatCount() == 0){
				model.stepIntoRepeat();
			}else
				model.jumpToNextEvent();
		}
		String[] returnArray = new String[questionList.size()]; 
		return questionList.toArray(returnArray);
	}
	
	public String byteArrayToString(ByteArrayPayload bap){
		ByteArrayOutputStream baos = null;
		String str=null;
		try {
			baos = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(baos);
			bap.writeExternal(out);
			str=baos.toString("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}
	
	/**Sets the endTime metadata (and other actions if set in jr:preload)
	 * @return
	 */
	public boolean setFinalMetadata(){
		return formController.postProcessInstance();
	}
	
	public FormController getFormController(){
		return formController;
	}
	
	public String getAnsweredXML(){
		int SIZEOF_UNWANTED_DATA = 4;
		try {
			return byteArrayToString(formController.getFilledInFormXml()).substring(SIZEOF_UNWANTED_DATA).trim();
		} catch (IOException e) {
			return null;
		}
	}
	
	
	
    /**  
     * Resumes a saved survey
     * 
     * Modified from code by:
     * @author Carl Hartung (carlhartung@gmail.com)
     * @author Yaw Anokwa (yanokwa@gmail.com)
     * 
     * From ODKCollect: FormLoaderTask.java
     * 
     * @author Scott
     * @param instanceFile
     * @param fec
     * @return
     */
    private boolean loadSavedInstance(String instanceXML, FormEntryController fec) {
        // convert instance into a byte array
        byte[] fileBytes = instanceXML.getBytes(Charset.forName("UTF-8"));
        
        // get the root of the saved and template instances
        TreeElement savedRoot = XFormParser.restoreDataModel(fileBytes,null).getRoot();
        TreeElement templateRoot = fec.getModel().getForm().getInstance().getRoot().deepCopy(true);

        // weak check for matching forms
        if (!savedRoot.getName().equals(templateRoot.getName()) || savedRoot.getMult() != 0) {
        	System.out.println("Saved form instance does not match template form definition");
            return false;
        } else {
            // populate the data model
            TreeReference tr = TreeReference.rootRef();
            tr.add(templateRoot.getName(), TreeReference.INDEX_UNBOUND);
            
            
            /*
             * Saves select choices, heavily coupled to andriod/collect.
             * TODO add select functionality 
             * 
            // Here we set the Collect's implementation of the IAnswerResolver.
            // We set it back to the default after select choices have been populated.
            XFormParser.setAnswerResolver(new ExternalAnswerResolver());
            templateRoot.populate(savedRoot, fec.getModel().getForm());
            */
            
            XFormParser.setAnswerResolver(new DefaultAnswerResolver());
            templateRoot.populate(savedRoot, fec.getModel().getForm());
            // populated model to current form
            fec.getModel().getForm().getInstance().setRoot(templateRoot);

            // fix any language issues
            // : http://bitbucket.org/javarosa/main/issue/5/itext-n-appearing-in-restored-instances
            if (fec.getModel().getLanguages() != null) {
                fec.getModel()
                        .getForm()
                        .localeChanged(fec.getModel().getLanguage(),
                            fec.getModel().getForm().getLocalizer());
            }
            return true;
        }
    }
}
