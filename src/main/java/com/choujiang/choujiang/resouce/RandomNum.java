package com.choujiang.choujiang.resouce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RandomNum {
    private static Integer maxNum = 2000;
    private static Integer leNum = 0;
   public static List<Integer> list = new ArrayList<>();

    static {
//        for (int i = 0; i < 2000; i++) {
//            list.add(i);
//        }
//        for (int i = 1647; i >=0; i--) {
//            list.remove(i);
//        }

    }

  public   static int a = 0, b = 0, c = 0, d = 0;
    static Map<String, Object> map = new HashMap<>();
    public static void main(String[] args) {

//        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < 3000; i++) {
//            int random = random();
//            Integer integer = list.get(random);
//            map.put(integer + "", integer);
//            if (integer >= 1990 && integer < 2000) {
//                a++;
//            } else if (integer >= 1890 && integer < 1990) {
//                b++;
//            }else if(integer >= 1650&& integer < 1890){
//                c++;
//            }else {
//                d++;
//            }
            new Runnable() {
                @Override
                public void run() {
                    init();
                }
            }.run();
//            list.remove(random);

        }
        System.out.println("MapSize=" + map.size());
        System.out.println("a-"+a);
        System.out.println("b-"+b);
        System.out.println("c-"+c);
        System.out.println("d-"+d);
        System.out.println("listSize=" + list.size());

    }

    public static int random() {
        return ((int) (Math.random() * (list.size())));
    }

    public synchronized static int  init(){
        Integer success=-1;
        if(list.size()>0){
            int random = random();
            Integer integer = list.get(random);
            map.put(integer + "", integer);
            if (integer >= 1990 && integer < 2000) {
                a++;
               success=0;
            } else if (integer >= 1890 && integer < 1990) {
                b++;
                success=1;
            }else if(integer >= 1650&& integer < 1890){
                c++;
                success=2;
            }else {
                d++;
                success=3;
            }
            list.remove(random);
        }
        return success;
    }
}
