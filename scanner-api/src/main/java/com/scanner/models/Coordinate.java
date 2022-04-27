package com.scanner.models;


public class Coordinate {
    private Integer y;
    private Integer x;
    private Integer width;
    private Integer height;


    public Coordinate() {
    }


    public Coordinate(Integer y, Integer x, Integer width, Integer height) {
        this.y = y;
        this.x = x;
        this.width = width;
        this.height = height;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

}
