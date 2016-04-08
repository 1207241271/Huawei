package com.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xingw on 2016/3/24.
 */
public class Point {
    private int pointID;
    private int totalValue;
    private int pointLine;
    private ArrayList<Point> NextPoints;
    private Point prePoint;
    private Point nextPoint;
    private int     heapIndex;


    public Point(int pointID) {
        this.pointID = pointID;
        this.NextPoints=new ArrayList<>();
        this.prePoint=null;
        this.nextPoint=null;
        this.totalValue=0;
        this.pointLine=-1;
        this.heapIndex=-1;
    }


    public int getTotalValue(){
        return totalValue;
    }

    public void setTotalValue(int totalValue){
        this.totalValue=totalValue;
    }

    public Point() {
        pointID = -1;
    }

    public int getPointID() {
        return pointID;
    }

    public Point getPrePoint(){
        return this.prePoint;
    }

    public void setPrePoint(Point prePoint) {
        this.prePoint = prePoint;
    }

    public Point getNextPoint(){
        return this.nextPoint;
    }

    public void setNextPoint(Point nextPoint) {
        this.nextPoint = nextPoint;
    }

//    public void addNextPointToNextPoints(Point point){
//        this.NextPoints.add(point);
//    };

    public void setPointLine(int pointLine) {
        this.pointLine = pointLine;
    }

    public int getPointLine() {
        return pointLine;
    }

    public int getHeapIndex() {
        return heapIndex;
    }

    public void setHeapIndex(int heapIndex) {
        this.heapIndex = heapIndex;
    }
}
