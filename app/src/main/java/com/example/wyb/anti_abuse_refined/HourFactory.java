package com.example.wyb.anti_abuse_refined;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HourFactory {

    private static HashMap<Integer, Integer> dayMap = new LinkedHashMap<>(36);


    /**

     */
    public static List<Hour> getGrids() {
        List<Hour> days = new ArrayList<>();

        Hour hour;
        for(int j = 8; j < 20; j++){
            for (int i = 0; i < 6; i++) {
                hour = new Hour();
                hour.minute = i;
                hour.hour = j;
                days.add(hour);
            }
        }
        return days;
    }


    public static void main(String[] args){
        //test

    }
}
