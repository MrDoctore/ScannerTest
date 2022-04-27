package com.scanner.models;

import java.io.Serializable;

public class Question implements Serializable {


    private Integer questionNumber;

    private Coordinate coordinate;

    public Question() {
    }


    public Integer getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(Integer questionNumber) {
        this.questionNumber = questionNumber;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }


}
