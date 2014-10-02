package org.smap.surveyModel.events;

import org.javarosa.core.model.FormIndex;
import org.odk.FormController;
import org.smap.surveyModel.SurveyModel.SurveyAction;

public abstract class SurveyEvent {
    protected FormController formController;
    public SurveyEvent(FormController formController){
        this.formController = formController;
    }

    public abstract SurveyAction answer(String answerText);

    public abstract String info();

    public abstract String getPromptText();

    public FormIndex getIndex() {
        return formController.getFormIndex();
    }
}
