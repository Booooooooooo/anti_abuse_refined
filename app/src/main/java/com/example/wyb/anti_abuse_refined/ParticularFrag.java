package com.example.wyb.anti_abuse_refined;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.table.ArrayTableData;
import com.bin.david.form.utils.DensityUtils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class ParticularFrag extends Fragment {

    private View view;
    private boolean isRed;
    private MyTable table;
    private List<item> itemList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.particular, container, false);

        RoundImageView imageView = (RoundImageView)view.findViewById(R.id.image);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.boy);
        imageView.setmBitmap(bitmap);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        TextView dayTxt = (TextView)view.findViewById(R.id.day);
        TextView monthTxt = (TextView)view.findViewById(R.id.month);
        dayTxt.setText("a"+day);
        monthTxt.setText("/"+month);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        table = (MyTable)view.findViewById(R.id.table);
        for(int i = 8; i < hour; i++){
            for(int j = 0; j < 6; j ++){
               table.setData(i, j, 0);
            }
        }
        for(int j = 0; j < minute; j+= 10){
            table.setData(hour, j / 10, 0);
        }

        getData();
//        table.setData(8,0);
//        table.setData(9,1);
//        table.setData(14,5);
        itemList.add(new item("ceshi", true, true, true));
        itemList.add(new item("ceshi2", true, false, true));
        itemList.add(new item("cheshi3", false, false, false));

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        ItemAdapter adapter = new ItemAdapter(itemList);
        recyclerView.setAdapter(adapter);
        return view;
    }

    public static String getTime(String user_time) {
        String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        Date d;
        try {
            d = sdf.parse(user_time);
            long l = d.getTime();
            String str = String.valueOf(l);
            re_time = str.substring(0, 10);
        }catch (ParseException e) {
            // TODO Auto-generated catch block e.printStackTrace();
        }
        return re_time;
    }
    // 将时间戳转为字符串
    public static String getStrTime(String cc_time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        // 例如：
        //cc_time=1291778220 ;
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));
        return re_StrTime;
    }


    private void parseJSONWithJSONObject(String jsonData, int id){
        try{
            JSONObject jsonObject = new JSONObject(jsonData);
            String currentstamp = jsonObject.getString("currentStamp");
            JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
            String startStamp = jsonObject.getString("startStamp");
            Log.d("SoundFragment", "currentStamp: " + currentstamp);
            Log.d("SoundFragment", "startStamp: " + startStamp);
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject dataObject = jsonArray.getJSONObject(i);
                String result = dataObject.getString("state");
                long stamp = Long.parseLong(dataObject.getString("stamp"));
                Date date = new Date(stamp);
                Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
                calendar.setTime(date);
                table.setData(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), id);

                //                array.add("日期"+stamp+result+(datanum++));
                Log.d("SoundFragment", "result: " + result);
                Log.d("SoundFragment", "stamp:" + date);
            }
        }catch (Exception e){
            Log.d("SoundFragment", "error2");
            e.printStackTrace();
        }
    }
    public void get(String type, final int id){
        String address = "http://47.102.151.34:8000/" + type +"?currentStamp=156254234&startStamp=1554733643";
        HttpUtil.sendOkHttpRequest(address, new okhttp3.Callback(){
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                parseJSONWithJSONObject(responseData, id);
                Log.d("SoundFragment", responseData);
            }

            @Override
            public void onFailure(Call call, IOException e){
                e.printStackTrace();
                Log.d("SoundFragment", "error3");
            }
        });
    }
    private void getData() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //get("iscry", 0);
                //get("heart", 1);
                get("accelerate", 2);
                Log.d("SoundFragment", "refreshed");
                handler.postDelayed(this, 2000);
////                Collections.reverse(array);
//                adapter.notifyDataSetChanged();
                //setListViewHeightBaseOnChildren(listView, 30);
            }
        };
        runnable.run();
    }
}
