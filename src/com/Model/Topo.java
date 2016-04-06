package com.Model;

/**
 * Created by Xingw on 2016/4/1.
 */
public class Topo {
    //路径号
    short LinkId;
    //消费
    short cost;
    //等级
    double grade;

    public Topo(short linkId, short cost, int grade) {
        LinkId = linkId;
        this.cost = cost;
        this.grade = grade;
    }

    public Topo() {
    }

    public short getLinkId() {
        return LinkId;
    }

    public void setLinkId(short linkId) {
        LinkId = linkId;
    }

    public short getCost() {
        return cost;
    }

    public short getBestCost() {
        return (short) (cost * grade);
    }
    public void setCost(short cost) {
        this.cost = cost;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }
}
