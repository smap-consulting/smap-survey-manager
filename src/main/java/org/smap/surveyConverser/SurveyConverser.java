package org.smap.surveyConverser;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import org.smap.DialogueHandler;
import org.smap.SurveyConversation;
import org.smap.surveyModel.SurveyModel;
import org.smap.surveyModel.utils.JRSerializer;

public class SurveyConverser implements SurveyConversation{

	private SurveyModel surveyModel;

	public static SurveyConverser createNewSurveyConverser(
            String surveyDefinitionXML) {
		return new SurveyConverser(surveyDefinitionXML);
	}

	public static SurveyConverser resume(String savedConverser) {
		return new SurveyConverser(
				JRSerializer.deserializeSurveyModel(savedConverser));
	}

    public static void beginDialogue(DialogueHandler handler) {
        SurveyConverser converser = createNewSurveyConverser(handler.getFormXml());

        handler.recordSurveyDetails(converser);

        handler.saveData(converser.save(), converser.getAnswers(), converser.getQuestionNumber());

        handler.reply(converser.getCurrentQuestion());
    }

    public static void handleDialog(DialogueHandler handler) {
        SurveyConverser converser = resume(handler.loadData());

        converser.answerCurrentQuestion(handler.getAnswerText());

        if (! converser.isComplete()) {
            handler.reply(converser.getCurrentQuestion());
        }
        handler.saveData(converser.save(), converser.getAnswers(), converser.getQuestionNumber());

        handler.recordSurveyDetails(converser);

        if (converser.isComplete()) {
            handler.handleComplete();
        }
    }

	public String save() {
		return JRSerializer.serializeSurveyModel(this.surveyModel);
	}

    public int getQuestionNumber(){
        return surveyModel.getCurrentQuestionNumber();
    }

	private SurveyConverser(SurveyModel surveyModel) {
		this.surveyModel = surveyModel;
	}

	private SurveyConverser(String surveyDefinitionXML) {
		this.surveyModel = SurveyModel
				.createSurveyModelFromXform(surveyDefinitionXML);
	}

	public void answerCurrentQuestion(String answerText) {
		surveyModel.answer(answerText);
	}

	public String getCurrentQuestion() {
		return surveyModel.getPrompt();
	}

	public String[] getAllQuestions() {
		return surveyModel.getFullQuestionPromptList();
	}

	public Boolean isComplete() {
		return surveyModel.isEndOfSurvey();
	}

	public String getAnswers() {
		return surveyModel.getAnsweredXML();
	}
}
