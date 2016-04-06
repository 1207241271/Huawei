package com.filetool.util;

import com.Model.Topo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xingw on 2016/3/24.
 */
public class Util {
//    public static short[][][] FormatData(String graphContent){
//        String[] routes=graphContent.split("\\n");
//        int size = 0;
//        //找到最大的路径值
//        for (String route : routes) {
//            String[] info=route.split(",");
//            int start = Integer.parseInt(info[1]);
//            int end = Integer.parseInt(info[2]);
//            if(size < start){
//                size = start;
//            }
//            if(size<end){
//                size = end;
//            }
//        }
//        short[][] data =new short[size+1][size+1];
//        short[][] routenum = new short[size+1][size+1];
//        for (String route : routes) {
//            String[] info=route.split(",");
//            routenum[Integer.parseInt(info[1])][Integer.parseInt(info[2])] = Short.parseShort
//                    (info[0]);
//            data[Integer.parseInt(info[1])][Integer.parseInt(info[2])] = Short.parseShort(info[3]);
//        }
//        short[][][] callback = {data,routenum};
//        return callback;
//    }

    public static Topo[][] FormatData(String graphContent){
        String[] routes=graphContent.split("\\n");
        int size = 0;
        //找到最大的路径值
        for (String route : routes) {
            String[] info=route.split(",");
            int start = Integer.parseInt(info[1]);
            int end = Integer.parseInt(info[2]);
            if(size < start){
                size = start;
            }
            if(size<end){
                size = end;
            }
        }
        Topo[][] data =new Topo[size+1][size+1];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data.length; j++) {
                data[i][j] = new Topo();
            }
        }
        for (String route : routes) {
            String[] info=route.split(",");
            Topo topo = new Topo(Short.parseShort(info[0]),Short.parseShort(info[3]),1);
            data[Integer.parseInt(info[1])][Integer.parseInt(info[2])]=topo;
        }
        return data;
    }
}
