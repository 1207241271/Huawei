/**
 * 实现代码文件
 *
 * @author XXX
 * @version V1.0
 * @since 2016-3-4
 */
package com.routesearch.route;

import com.Model.MinValueHeap;
import com.Model.Point;
import com.Model.Topo;
import com.filetool.util.FileUtil;
import com.filetool.util.LogUtil;
import com.filetool.util.Util;

import java.util.*;

import static com.filetool.util.Util.FormatData;

public final class Route {
    //路径信息
//    private static short[][] data;
//    private static short[][] routenum;
    private static Topo[][] topo;
    private static Point[] pointsArray;
    private static MinValueHeap[]   HeapArray;

    //起点
    private static short start;
    //终点
    private static short end;
    //特殊点
    private static short[] pass;
    private static HashMap<Integer ,Integer > passIndex;


    private static ArrayList<ArrayList>   passSet;

    private static Point currentminpoint = null;


    /**
     * 你需要完成功能的入口
     *
     * @author XXX
     * @version V1
     * @since 2016-3-4
     */
    public static String searchRoute(String graphContent, String condition) {
        topo= FormatData(graphContent);
        FormatCondition(condition);
        initPointsArray();
        initPassSet();
        initHeapArray();

        findnextPointList();


        LogUtil.printLog("Format");


        return FormatResult();
    }

    private static void FormatGrade() {
        for (short pas : pass) {
            for (int i = 0; i <topo.length ; i++) {
                topo[i][pas].setGrade(0.4);
                for (int j = 0; j < topo.length; j++) {
                    topo[j][i].setGrade(0.6);
                    for (int k = 0; k < topo.length; k++) {
                        topo[k][j].setGrade(0.8);
                    }
                }
            }
        }
    }

    /**
     * 格式化结果输出
     * @return
     */
    private static String FormatResult() {
        if(currentminpoint == null) return "NA";
        Point pre = currentminpoint.getPrePoint();
        Point current = currentminpoint;
        StringBuffer result = new StringBuffer();
        while (pre !=null){
            result.insert(0,topo[pre.getPointID()][current.getPointID()].getLinkId());
            current = pre;
            pre = current.getPrePoint();
            if(pre !=null)
                result.insert(0,"|");
        }
        System.out.println(result.toString());
        return result.toString();
    }

    private static void findnextPointList() {
        if (checkIsInOneRoute(0,(pass.length+1)*2-1)){
            //TODO:第二阶段
        }else {
            Point tmp=HeapArray[(pass.length+1)*2].popMin();
            if (HeapArray[tmp.getHeapIndex()].isRun()) {
                if (tmp.getHeapIndex() / 2 == 0) {
                    searchFont(tmp, tmp.getHeapIndex());
                } else {
                    searchBack(tmp, tmp.getHeapIndex());
                }
            }else {
                findnextPointList();
            }
        }
    }




    /**
     * 格式化条件信息
     *
     * @param condition 条件字符串
     */
    public static void FormatCondition(String condition) {
        String[] Info = condition.split(",");
        start = Short.parseShort(Info[0]);
        end = Short.parseShort(Info[1]);
        //截取掉换行符
        Info[2] = Info[2].substring(0, Info[2].length() - 1);
        String[] passes = Info[2].split("\\|");
        pass = new short[passes.length];
        for (int i = 0; i < passes.length; i++) {
            pass[i] = Short.parseShort(passes[i]);
        }
    }

    /**
     * 初始化n个Point
     */
    public static void initPointsArray(){
        pointsArray=new Point[topo.length];
        for (int i = 0; i <topo.length ; i++) {
            pointsArray[i]=new Point(i);
        }
        for (short pas : pass) {
           pointsArray[pas].setPointLine(pas);
        }
        pointsArray[start].setPointLine(start);
        pointsArray[end].setPointLine(end);
    }

    /**
     *初始化化(pas+1)*2个堆
     */
    public static void initHeapArray(){
        passIndex=new HashMap<Integer, Integer>();
        HeapArray=new MinValueHeap[2*(pass.length+1)+1];
        passIndex.put(0,(int)start);
        HeapArray[0]=new MinValueHeap();

        HeapArray[2*(pass.length+1)]=new MinValueHeap();

        searchFont(pointsArray[start],0);
        int index=1;
        for (short pas:pass) {
            passIndex.put(index,(int)pas);
            HeapArray[index*2-1]=new MinValueHeap();
            searchBack(pointsArray[pas],index*2-1);
            HeapArray[index*2]=new MinValueHeap();
            searchFont(pointsArray[pas],index*2);
            index++;
        }

        passIndex.put(index,(int)end);
        HeapArray[index*2-1]=new MinValueHeap();
        searchBack(pointsArray[end],index*2-1);


    }

    public static void initPassSet() {
        passSet = new ArrayList<ArrayList>();
        for (int i = 0; i < pass.length + 2; i++) {
            passSet.add(new ArrayList<Integer>());
        }
        for (int i = 0; i < pass.length+2; i++) {
            passSet.get(i).add(i);
        }
    }

    public static boolean checkIsInOneRoute(int routeA,int routeB){
        int point1=(routeA+1)/2;
        int point2=(routeB+1)/2;
        boolean hasOne=false;
        for (ArrayList<Integer> arrayList:passSet) {
            for (Integer num:arrayList){
                if (num==point1||num==point2){
                    if (!hasOne){
                        hasOne=true;
                    }else {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public static void moveSetFromIndexToIndex(int start,int end){
        int preArrayIndex=0;
        int nextArrayIndex=0;
        for (int j = 0; j < passSet.size(); j++) {
            for (int k = 0; k < passSet.get(j).size(); k++) {
                if ((int)passSet.get(j).get(k)==start){
                    preArrayIndex=j;
                }
                if ((int)passSet.get(j).get(k)==end){
                    nextArrayIndex=j;
                }
            }
        }
        ArrayList<Integer> tmpArray=passSet.get(nextArrayIndex);
        for (Integer num:tmpArray){
            passSet.get(preArrayIndex).add(num);
        }
        passSet.remove(nextArrayIndex);

        return;
    }

    public static void searchFont(Point point,int heapindex){
        if (heapindex!=-1){
            int pointID=point.getPointID();
            int totalValue=point.getTotalValue();
            for (int i = 0; i <topo.length ; i++) {
                if (topo[pointID][i].getCost()!=-1){
                    Point tmp=pointsArray[i];
                    if (tmp.getPointLine()==-1){
                        tmp.setPointLine(0);
                        tmp.setPrePoint(point);
                        tmp.setTotalValue(totalValue+topo[pointID][i].getCost());
                        tmp.setHeapIndex(heapindex);
                        HeapArray[heapindex].insert(point);
                    }else if (tmp.getPointLine()==0){
                        if (HeapArray[tmp.getHeapIndex()].isRun()){
                            //遇到同方向的暂时不取
                            if (tmp.getPrePoint()!=null){
//                                continue;
                            }else{
                                //遇到可连接点
                                if (!checkIsInOneRoute(point.getHeapIndex(),tmp.getHeapIndex())) {
                                    //如果不是已经在一条路上

                                    Point setPre = point;
                                    int routeIndex = (heapindex + 1) / 2;
                                    while (setPre.getPrePoint() != null && setPre.getPointLine() == 0) {
                                        setPre.getPrePoint().setNextPoint(setPre);
                                        setPre.setPointLine(routeIndex);
                                        setPre = setPre.getPrePoint();
                                    }
                                    HeapArray[heapindex].stop();

                                    Point setNext = tmp;
                                    while (setNext.getNextPoint() != null && setNext.getPointLine() == 0) {
                                        setNext.getNextPoint().setPrePoint(setNext);
                                        setNext.setPointLine(routeIndex);
                                        setNext = setNext.getNextPoint();
                                    }
                                    HeapArray[setNext.getHeapIndex()].stop();

                                    moveSetFromIndexToIndex((tmp.getHeapIndex()+1)/2,(point.getHeapIndex()+1)/2);
                                    HeapArray[tmp.getHeapIndex()].stop();
                                    HeapArray[point.getHeapIndex()].stop();
                                    return;
                                }
                            }
                        }else{
                            //如果直接连在点上

                                //延伸到了已死点,继续加入
                                tmp.setPointLine(0);
                                tmp.setPrePoint(point);
                                tmp.setTotalValue(totalValue + topo[pointID][i].getCost());
                                tmp.setHeapIndex(heapindex);
                                HeapArray[heapindex].insert(point);

                        }
                    }else {
                        if (tmp.getPointLine() == tmp.getPointID()) {
                            moveSetFromIndexToIndex((tmp.getHeapIndex() + 1) / 2, (point.getHeapIndex() + 1) / 2);
                            HeapArray[tmp.getHeapIndex()].stop();
                            HeapArray[point.getHeapIndex()].stop();
                        }
                        //延伸到了路上
                    }
                    HeapArray[(pass.length+1)*2].insert(HeapArray[heapindex].popMin());
                    findnextPointList();
                }
            }
        }
    }

    public static void searchBack(Point point,int heapindex){
        if (heapindex!=-1){
            int pointID=point.getPointID();
            int totalValue=point.getTotalValue();
            for (int i = 1; i <topo.length ; i++) {
                if (topo[i][pointID].getCost()!=-1){
                    Point tmp=pointsArray[i];
                    if (tmp.getPointLine()==-1){
                        tmp.setPointLine(0);
                        tmp.setNextPoint(point);
                        tmp.setTotalValue(totalValue+topo[i][pointID].getCost());
                        tmp.setHeapIndex(heapindex);
                        HeapArray[heapindex].insert(point);
                    }else if (tmp.getPointLine()==0){
                        if (HeapArray[tmp.getHeapIndex()].isRun()){
                            //遇到同方向的暂时不取
                            if (tmp.getPrePoint()!=null){
//                                continue;
                            }else{
                                //遇到可连接点
                                if (!checkIsInOneRoute(point.getHeapIndex(),tmp.getHeapIndex())) {
                                    //如果不是已经在一条路上

                                    Point setPre = tmp;
                                    int routeIndex = (heapindex + 1) / 2;
                                    while (setPre.getPrePoint() != null && setPre.getPointLine() == 0) {
                                        setPre.getPrePoint().setNextPoint(setPre);
                                        setPre.setPointLine(routeIndex);
                                        setPre = setPre.getPrePoint();
                                    }
                                    HeapArray[heapindex].stop();

                                    Point setNext = point;
                                    while (setNext.getNextPoint() != null && setNext.getPointLine() == 0) {
                                        setNext.getNextPoint().setPrePoint(setNext);
                                        setNext.setPointLine(routeIndex);
                                        setNext = setNext.getNextPoint();
                                    }
                                    HeapArray[setNext.getHeapIndex()].stop();
                                    moveSetFromIndexToIndex((tmp.getHeapIndex()+1)/2,(point.getHeapIndex()+1)/2);
                                    HeapArray[tmp.getHeapIndex()].stop();
                                    HeapArray[point.getHeapIndex()].stop();
                                    return;
                                }
                            }
                        }else{

                                //延伸到了已死点,继续加入
                                tmp.setPointLine(0);
                                tmp.setNextPoint(point);
                                tmp.setTotalValue(totalValue + topo[pointID][i].getCost());
                                tmp.setHeapIndex(heapindex);
                                HeapArray[heapindex].insert(point);

                        }
                    }else {
                        if (tmp.getPointLine() == tmp.getPointID()) {
                            moveSetFromIndexToIndex((tmp.getHeapIndex() + 1) / 2, (point.getHeapIndex() + 1) / 2);
                            HeapArray[tmp.getHeapIndex()].stop();
                            HeapArray[point.getHeapIndex()].stop();
                        }
                        //延伸到了路上
                    }
                    HeapArray[(pass.length+1)*2].insert(HeapArray[heapindex].popMin());
                    findnextPointList();;
                }
            }
        }
    }


}