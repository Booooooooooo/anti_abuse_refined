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
import android.widget.Button;
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
import java.util.Iterator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class ParticularFrag extends Fragment {

    private View view;
    private boolean isRed;
    private MyTable table;
    private List<item> itemList = new ArrayList<>();
    private ItemAdapter adapter;
    private Runnable runnable;
    private long lastTime = 1557286376;
    private long nowTime = 1557977576;
    private RecyclerView recyclerView;
    //private List<PlainText> txtList = new ArrayList<>();

    private int times = 0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.particular, container, false);


        RoundImageView imageView = (RoundImageView)view.findViewById(R.id.image);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.user_img);
        imageView.setmBitmap(bitmap);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
//        TextView dayTxt = (TextView)view.findViewById(R.id.day);
//        TextView monthTxt = (TextView)view.findViewById(R.id.month);
//        dayTxt.setText("a"+day);
//        monthTxt.setText("/"+month);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        table = (MyTable)view.findViewById(R.id.table);
        for(int i = 8; i < hour; i++){
            for(int j = 0; j < 6; j ++){
               table.setData(i, j, -1);
            }
        }
        for(int j = 0; j < minute; j+= 10){
            table.setData(hour, j / 10, -1);
        }

        table.setData(9, 4, 2);
        table.setData(9, 4, 0);
        itemList.add(new item("9:40", true, false, true));
        table.setData(13, 0, 0);
        table.setData(13, 0, 1);
        table.setData(13, 0, 2);
        itemList.add(new item("13:00", true, true, true));


        recyclerView = (RecyclerView)view.findViewById(R.id.list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ItemAdapter(itemList);
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);
        //getData();

        Button button = (Button)view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                switch (times){
                    //case 0:
                        //itemList.clear();
                        //break;
                    case 0:
                        table.setData(11, 0, 0);
                        itemList.add(new item("11:00", false, true, false));
                        break;
                        //哭声
                    case 1:
                        table.setData(11, 1, 2);
                        itemList.add(new item("11:10", false, false, true));
                        break;
                        //加速度
                    case 2:
                        table.setData(11, 2, 1);
                        itemList.add(new item("11:20", true, false, false));
                        break;//心率
                    case 3:
                        table.setData(11, 3, 0);
                        table.setData(11, 3, 1);
                        table.setData(11, 3, 2);
                        itemList.add(new item("11:30", true, true, true));
                        break;
                        //all
                }
                //Collections.reverse(itemList);
                adapter.notifyDataSetChanged();
                //recyclerView.scrollToPosition(itemList.size() - 1);
                table.invalidate();
                view.invalidate();
                times++;
            }
        });

        return view;
    }

    public void addList(){
        Iterator<Hour> iterator = table.mHours.iterator();
        itemList.clear();
        adapter.notifyDataSetChanged();
        for(int i = 0; i < table.mHours.size(); i++){
            Hour h = table.mHours.get(i);
            //if(table.mHours.get(i).vis)continue;

            String time = h.hour+":"+h.minute+"0";
            boolean heart = h.normal[0];
            boolean sound = h.normal[1];
            boolean drop = h.normal[2];
            if(h.contribution > 1){
                //table.mHours.get(i).setVis(true);
                itemList.add(new item(time, heart, sound, drop));
                Log.d("SoundFragment", time + heart+sound+drop);
                adapter.notifyDataSetChanged();
                //adapter.notifyAll();
            }
        }
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


    private void parseJSONWithJSONObject(String jsonData, int id){
        //Log.d("SoundFragment", "type:" + id);
        try{
            JSONObject jsonObject = new JSONObject(jsonData);
            String currentstamp = jsonObject.getString("currentStamp");
            JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
            String startStamp = jsonObject.getString("startStamp");

            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject dataObject = jsonArray.getJSONObject(i);
                String result = dataObject.getString("state");
                long stamp = Long.parseLong(dataObject.getString("stamp"));
                Date date = new Date(stamp * 1000L);
                Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
                calendar.setTime(date);
                if(result != "0.0"){
                    table.setData(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE) / 10, id);
                    //itemList.add()
                    String type;
                    if(id == 0)type = "哭声";
                    else if(id == 1)type = "心率";
                    else type = "加速度";
                    //txtList.add(new PlainText(calendar.getTime() + type + "异常"));
                }

            }
        }catch (Exception e){
            Log.d("SoundFragment", "error2" + id);
            e.printStackTrace();
        }
    }
    public void get(String type, final int id){
        //nowTime = lastTime + 10;
        String address = "http://47.102.151.34:8000/" + type +"?currentStamp="+nowTime+"&startStamp="+lastTime;
        HttpUtil.sendOkHttpRequest(address, new okhttp3.Callback(){
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                parseJSONWithJSONObject(responseData, id);
                //Log.d("SoundFragment", responseData);
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
        runnable = new Runnable() {
            @Override
            public void run() {
                get("iscry", 0);
                get("heart", 1);
                get("accelerate", 2);
                addList();
                Log.d("SoundFragment", "refreshed");
                handler.postDelayed(this, 5000);
////                Collections.reverse(array);
                //lastTime = nowTime;
                //nowTime = lastTime + 5;
                adapter.notifyDataSetChanged();
                table.invalidate();
                view.invalidate();

                //setListViewHeightBaseOnChildren(listView, 30);
            }
        };
        runnable.run();
    }
}
