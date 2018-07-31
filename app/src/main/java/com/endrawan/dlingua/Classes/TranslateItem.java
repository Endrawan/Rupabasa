package com.endrawan.dlingua.Classes;

/**
 * Endrawan made this on 23/01/2017.
 */
public class TranslateItem {
    String answer;
    String question;

    public TranslateItem() {

    }

    public TranslateItem(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
