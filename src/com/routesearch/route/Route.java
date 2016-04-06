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



        LogUtil.printLog("Format");


        findMinValueRoute();
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
        Point pre = currentminpoint.getPrevious();
        Point current = currentminpoint;
        StringBuffer result = new StringBuffer();
        while (pre !=null){
            result.insert(0,topo[pre.getPointID()][current.getPointID()].getLinkId());
            current = pre;
            pre = current.getPrevious();
            if(pre !=null)
                result.insert(0,"|");
        }
        System.out.println(result.toString());
        return result.toString();
    }

    private static void findMinValueRoute() {
        Point startpoint = new Point(start, 0 , 0, null, null);
        startpoint.setNextPoints(findnextPointList(startpoint));
        while (!minValueHeap.isHeapEmpty()) {
            Point point=minValueHeap.popMin();
            point.setNextPoints(findnextPointList(point));
            //int second = LogUtil.getTimeUsed().get(Calendar.SECOND);
            //if (second >= 9)break;
//            minValueHeap.insert();
        }
    }

    private static List<Point> findnextPointList(Point parent) {
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < topo[0].length; i++) {
            int value = topo[parent.getPointID()][i].getCost();
            int bestvalue = topo[parent.getPointID()][i].getBestCost();
            int depth=parent.getDepth();
            if (value != 0) {
                Point point = new Point(i, parent.getTotalValue() + value,(int)((parent.getTotalValue()+value)*bestvalue-parent.getDepth()*0.4), null, parent);
                //判断这个点是不是结尾
                point.setDepth(depth+1);
                if (point.getPointID() == end) {
                    //有没有通过所有特殊点
                    if (hasallspecialpoint(point)) {
                        //如果通过了所有特殊点 它是不是最短路径 如果不是则丢弃
                        if (currentminpoint == null) {
                            LogUtil.printLog("First route");
                            currentminpoint = point;
                        } else if (point.getTotalValue() < currentminpoint.getTotalValue()) {
                            currentminpoint = point;
                        }
                    }
                } else {
                    if(haspassedpoint(point)){
                        continue;
                    }
                    //判断这个点如果超出了最短路径则丢弃
                    if (currentminpoint != null) {
                        if (point.getTotalBestValue() < currentminpoint.getTotalBestValue() * 0.9) {
                            minValueHeap.insert(point);
                            points.add(point);
                        }
                    } else {
                        minValueHeap.insert(point);
                        points.add(point);
                    }
                }
            }
        }
        return points;
    }

    /**
     * 判断是否重复经过某点
     * @param point
     * @return
     */
    private static boolean haspassedpoint(Point point) {
        int pointId = point.getPointID();
        point = point.getPrevious();
        while (point !=null){
            if(point.getPointID() == pointId)
                return true;
            point = point.getPrevious();
        }
        return false;
    }

    /**
     * 是否通过所有特殊点
     * @param point
     * @return
     */
    private static boolean hasallspecialpoint(Point point) {
        int passnumber = pass.length;
        while (!(point==null || passnumber==0)){
            for (int i = 0; i < pass.length; i++) {
                if(point.getPointID() == pass[i])
                {
                    passnumber--;
                    break;
                }
            }
            point = point.getPrevious();
        }
        if (passnumber == 0)return true;
        return false;
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
        passIndex.put((int)start,0);
        HeapArray[0]=new MinValueHeap(pointsArray[start]);

        int index=1;
        for (short pas:pass) {
            passIndex.put((int)pas,index);
            HeapArray[index*2-1]=new MinValueHeap(pointsArray[pas]);
            HeapArray[index*2]=new MinValueHeap(pointsArray[pas]);
            index++;
        }

        passIndex.put((int)end,index);
        HeapArray[index*2-1]=new MinValueHeap(pointsArray[end]);
    }

    public static void searchFont(){

    }

}