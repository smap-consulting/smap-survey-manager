package controller;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import org.javarosa.core.model.FormDef;
import org.javarosa.core.model.FormIndex;
import org.javarosa.core.model.GroupDef;
import org.javarosa.core.model.QuestionDef;
import org.javarosa.core.model.data.IAnswerData;
import org.javarosa.core.model.data.StringData;
import org.javarosa.core.model.instance.FormInstance;
import org.javarosa.core.model.instance.TreeElement;
import org.javarosa.core.services.locale.Localizer;
import org.javarosa.core.services.transport.payload.ByteArrayPayload;
import org.javarosa.form.api.FormEntryCaption;
import org.javarosa.form.api.FormEntryController;
import org.javarosa.form.api.FormEntryModel;
import org.javarosa.xform.util.XFormUtils;

import SurveyBot.SurveyModel;

public class ControllerTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		//String formFilePath = "data/Household Survey.xml";
		String formFilePath = "data/Birds.xml";
		SurveyModel surveyBot = new SurveyModel(formFilePath);
		while(true){
			surveyBot.getQuestionInfo();
			surveyBot.jumpToNextScreenEvent();
		}
		
		/*
		// Parse the form
		FormDef formDef = XFormUtils.getFormFromInputStream(is);
		FormEntryModel formEntryModel = new FormEntryModel(formDef);
		FormEntryController formEntryController = new FormEntryController(formEntryModel);
		// Set localizer
		Localizer l = formDef.getLocalizer();
		l.setDefaultLocale(l.getAvailableLocales()[0]);
		l.setLocale(l.getAvailableLocales()[0]);
		
		
		formEntryController.jumpToIndex(FormIndex.createBeginningOfFormIndex());
		formEntryController.stepToNextEvent();
		formEntryController.stepToNextEvent();
		formEntryController.stepToNextEvent();
		formEntryController.stepToNextEvent();
		formEntryController.stepToNextEvent();
		formEntryController.stepToNextEvent();
		formEntryController.answerQuestion(new StringData("Answer Value"));
		formEntryController.stepToNextEvent();
		formEntryController.stepToNextEvent();
		formEntryController.stepToNextEvent();
		System.out.println(">>>");
		System.out.println(formEntryController.answerQuestion(new StringData("Second Answer Value")));
		formEntryController.stepToNextEvent();
		System.out.println(">>>");
		System.out.println(formEntryController.answerQuestion(new StringData("Date Answer Value")));
		FormController fc = new FormController(new File("a path"), formEntryController, null);
		
		System.out.println(fc.validateAnswers(false));
		
		DataOutputStream out = new DataOutputStream(new FileOutputStream("test1.txt"));
		fc.getFilledInFormXml().writeExternal(out);
		System.out.println(fc.getSubmissionMetadata());
		
		System.out.println(formEntryController.getModel());
		*/
	}
	
	public static String printStuff(FormEntryController fec){
		String stuff = "";
		//go to the beginning of the form
		FormEntryModel femodel = fec.getModel();
		fec.jumpToIndex(FormIndex.createBeginningOfFormIndex());
		do{
			FormEntryCaption fep = femodel.getCaptionPrompt();
			boolean choiceFlag = false;
			
			if(fep.getFormElement() instanceof QuestionDef){
				stuff+="\t[Type:QuestionDef, ";
				Vector s = ((QuestionDef)fep.getFormElement()).getChoices();
				stuff+="ContainsChoices: "+ ((s != null && s.size() > 0) ? "true " : "false" ) +", ";
				if(s != null && s.size() > 0) choiceFlag = true;
			}else if(fep.getFormElement() instanceof FormDef){
				stuff+="\t[Type:FormDef, ";
			}else if(fep.getFormElement() instanceof GroupDef){
				stuff+="\t[Type:GroupDef, ";
			}else{
				stuff+="\t[Type:Unknown]\n";
				continue;
			}
			
			stuff+="ID:"+fep.getFormElement().getID()+", TextID:"+fep.getFormElement().getTextID()+",LongText:"+fep.getLongText();
			if(choiceFlag){
				
				stuff+="] \n\t\t---Choices:"+((QuestionDef)fep.getFormElement()).getChoices().toString()+"\n";
			}else{
				stuff+="]\n";
			}
		}while(fec.stepToNextEvent()!=fec.EVENT_END_OF_FORM);
		
		return stuff;
	}
}
