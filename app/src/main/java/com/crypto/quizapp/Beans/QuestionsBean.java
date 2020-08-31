package com.crypto.quizapp.Beans;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class QuestionsBean implements Serializable {


    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "topic")
    private String topic;

    @ColumnInfo(name = "level")
    private int level;

    @ColumnInfo(name = "cleared")
    private String cleared;

    @ColumnInfo(name = "questionno")
    private String questionno;

    @ColumnInfo(name = "question")
    private String question;

    @ColumnInfo(name = "option1")
    private String option1;

    @ColumnInfo(name = "option2")
    private String option2;

    @ColumnInfo(name = "option3")
    private String option3;

    @ColumnInfo(name = "option4")
    private String option4;

    @ColumnInfo(name = "correctoption")
    private String correctoption;

    @ColumnInfo(name = "useranswer")
    private String useranswer;

    @ColumnInfo(name = "SelectedRadioButton")
    private int SelectedRadioButton;

    public QuestionsBean(String topic, int level, String cleared, String questionno, String question, String option1, String option2, String option3, String option4, String correctoption, String useranswer) {
        this.topic = topic;
        this.level = level;
        this.cleared = cleared;
        this.questionno = questionno;
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.correctoption = correctoption;
        this.useranswer = useranswer;
    }

    public QuestionsBean() {

    }

    public String getCleared() {
        return cleared;
    }

    public void setCleared(String cleared) {
        this.cleared = cleared;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getQuestionno() {
        return questionno;
    }

    public void setQuestionno(String questionno) {
        this.questionno = questionno;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public String getCorrectoption() {
        return correctoption;
    }

    public void setCorrectoption(String correctoption) {
        this.correctoption = correctoption;
    }

    public String getUseranswer() {
        return useranswer;
    }

    public void setUseranswer(String useranswer) {
        this.useranswer = useranswer;
    }

    public int getSelectedRadioButton() {
        return SelectedRadioButton;
    }

    public void setSelectedRadioButton(int selectedRadioButton) {
        SelectedRadioButton = selectedRadioButton;
    }



}

