package org.smap;

import org.smap.surveyConverser.SurveyConverser;

public interface DialogueHandler {
    /**
     * @return returns the saved data for a given dialogue
     */
    public String loadData();

    /**
     * Saves dialogue state
     * @param data serialized SurveyConverser
     * @param answers raw instance javarose xml
     */
    public void saveData(String data, String answers);

    /**
     * @return get last answerfrom user
     */
    public String getAnswerText();

    /**
     * reply to user with next question
     * @param reply message to send
     */
    public void reply(String reply);

    /**
     * callback to fire when survey is completed
     */
    public void handleComplete();

    /**
     * @return survey definition xml
     */
    public String getFormXml();

    /**
     * callback to allow client to record survey information
     * @param converser
     */
    void recordSurveyDetails(SurveyConversation converser);
}
