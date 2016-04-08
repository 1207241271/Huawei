package com.Model;
import java.util.ArrayList;
/**
 * Created by yangxu on 16/3/28.
 */
public class MinValueHeap {
    private  ArrayList<Point>    minHeap;
    private boolean             run;

    public MinValueHeap(Point point){
        this.minHeap=new ArrayList<Point>();
        //添加一个最小站位
        Point zeroPoint=new Point();
        zeroPoint.setTotalValue(Integer.MIN_VALUE);
        this.minHeap.add(zeroPoint);
        this.minHeap.add(point);
        this.run=true;
    }

//    private static MinValueHeap single=null;
//
//    public static MinValueHeap getInstance(){
//        if (single == null){
//            single = new MinValueHeap();
//        }
//        return single;
//    }

    private int leftChild(int pos){
        return 2*pos;
    }

    private int rightChild(int pos){
        return 2*pos+1;
    }

    private int parent(int pos){
        return pos/2;
    }

    private boolean isLeaf(int pos){
        return ((pos>minHeap.size()/2)&&(pos<minHeap.size()));
    }

    private void swap(int pos1,int pos2){
        Point tmp=minHeap.get(pos1);
        minHeap.set(pos1,minHeap.get(pos2));
        minHeap.set(pos2,tmp);
    }

    public void insert(Point point){
        if (run) {
            minHeap.add(point);
            int current = minHeap.size() - 1;
            while (minHeap.get(current).getTotalValue() < minHeap.get(parent(current)).getTotalValue()) {
                swap(current, parent(current));
                current = parent(current);
            }
        }
    }

    //退出最小值
    public Point popMin(){
        if (run) {
            swap(1, minHeap.size() - 1);
            Point tmp = minHeap.get(minHeap.size() - 1);
            minHeap.remove(minHeap.size() - 1);
            if (minHeap.size() > 2)
                pushDown(1);
            return tmp;
        }
        return null;
    }
    //下滤过程
    private void pushDown(int pos){
        if (run) {
            int smallestChild;
            while (!isLeaf(pos)) {
                smallestChild = leftChild(pos);
                if (smallestChild >= minHeap.size()) return;
                if (smallestChild + 1 < minHeap.size()) {
                    if ((smallestChild < minHeap.size()) && (minHeap.get(smallestChild).getTotalValue() > minHeap.get(smallestChild + 1).getTotalValue()))
                        smallestChild = smallestChild + 1;
                }
                if (minHeap.get(pos).getTotalValue() <= minHeap.get(smallestChild).getTotalValue())
                    return;
                swap(pos, smallestChild);
                pos = smallestChild;
            }
        }
    }

    public boolean isHeapEmpty(){
        if(minHeap.size() <2)
            return true;
        return false;
    }

    public void stop(){
        this.run=false;
    }
    public void run(){
        this.run=true;
    }

    public boolean isRun(){
        return this.run;
    }
}
