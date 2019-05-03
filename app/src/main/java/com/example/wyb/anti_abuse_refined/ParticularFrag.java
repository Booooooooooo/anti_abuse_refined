package com.example.wyb.anti_abuse_refined;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.table.ArrayTableData;
import com.bin.david.form.utils.DensityUtils;

import java.util.Calendar;

public class ParticularFrag extends Fragment {

    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.particular, container, false);

        RoundImageView imageView = (RoundImageView)view.findViewById(R.id.image);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.boy);
        imageView.setmBitmap(bitmap);
//        GitHubContributionView github = (GitHubContributionView)view.findViewById(R.id.table);
//        github.setData(2016,12,9,2);
//        github.setData(2016,11,9,1);
//        github.setData(2016,10,5,10);
//        github.setData(2016,8,9,3);
//        github.setData(2016,4,20,2);
//        github.setData(2016,12,13,3);
//        github.setData(2016,12,14,3);
//        github.setData(2016,2,15,4);
        MyTable table = (MyTable)view.findViewById(R.id.table);
        table.setData(8,0,3);
        table.setData(9,1,2);
        table.setData(14,5,1);
        return view;
    }


}
