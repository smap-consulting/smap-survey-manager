package org.smap.surveyModel.events.questionTypes;

import org.odk.FormController;
import org.smap.surveyModel.SurveyModel;
import org.smap.surveyModel.utils.SurveyMessageConstants;

/**
 * Created by Scott on 4/10/2014.
 */
public class NumericQuestion extends QuestionTypeImpl implements QuestionType{

    public NumericQuestion(FormController formController) {
        super(formController);
    }

    @Override
    public SurveyModel.SurveyAction answer(String answerText) {
        try {
            Integer.parseInt(answerText);
        } catch (NumberFormatException e) {
            super.flagInvalidAnswer(SurveyMessageConstants.INVALID_NUMBER_FORMAT);
            return SurveyModel.SurveyAction.stay;
        }
        return super.answer(answerText);
    }


}
