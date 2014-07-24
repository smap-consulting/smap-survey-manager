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

import SurveyBot.ISurveyEvent;
import SurveyBot.RepeatEvent;
import SurveyBot.SurveyModel;

public class ControllerTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		stepThroughSurvey("data/Household Survey.xml");
		stepThroughSurvey("data/Demo Form.xml");
		stepThroughSurvey("data/Birds.xml");
		SurveyModel surveyBot = new SurveyModel("data/Birds.xml");
		System.out.println(surveyBot.getAnsweredXML());
	}
	public static void stepThroughSurvey(String formFilePath){
		SurveyModel surveyBot = new SurveyModel(formFilePath);
		System.out.println(surveyBot.getFullQuestionPromptList().length);
	}
}
