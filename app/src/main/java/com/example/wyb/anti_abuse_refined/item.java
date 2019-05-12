package com.example.wyb.anti_abuse_refined;

public class item {
    private final int[][] iconId = new int[][]{{R.drawable.blue_heart, R.drawable.blue_micro, R.drawable.blue_drop},
            {R.drawable.yellow_heart, R.drawable.yellow_micro, R.drawable.yellow_drop},
            {R.drawable.red_heart, R.drawable.red_micro, R.drawable.red_drop}};

    private int[] iconImg = new int[3];
    private int num;
    private String timeTxt;
    private String stateTxt;

    public item(String t, boolean heart, boolean sound, boolean drop) {
        timeTxt = t;
        if(heart)num++;
        if(sound)num++;
        if(drop)num++;
        if(num == 3)stateTxt = "可能出现虐待";
        if(num == 2)stateTxt = "可能出现摔倒";
        if(num == 1)stateTxt = "           ";
        int tmp = 0;
        if(heart){
            iconImg[tmp++] = iconId[num- 1][0];
        }
        if(sound){
            iconImg[tmp++] = iconId[num - 1][1];
        }
        if(drop){
            iconImg[tmp++] = iconId[num - 1][2];
        }
    }

    public int getNum() {
        return num;
    }

    public String getTimeTxt(){
        return timeTxt;
    }
    public String getStateTxt(){
        return stateTxt;
    }

    public int[] getIconImg() {
        return iconImg;
    }
}