package com.example.wyb.anti_abuse_refined;

import android.media.Image;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<item>items;
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView icon1, icon2, icon3;
        TextView timetxt, statetxt;
        public ViewHolder(View view){
            super(view);
            icon1 = (ImageView)view.findViewById(R.id.icon1);
            icon2 = (ImageView)view.findViewById(R.id.icon2);
            icon3 = (ImageView)view.findViewById(R.id.icon3);
            timetxt = (TextView)view.findViewById(R.id.time);
            statetxt = (TextView)view.findViewById(R.id.state);
        }
    }

    public ItemAdapter(List<item>itemlist){
        items = itemlist;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        item it = items.get(position);
        holder.timetxt.setText(it.getTimeTxt());
        holder.statetxt.setText(it.getStateTxt());
        int[] img = it.getIconImg();
        int num = it.getNum();
        if(num == 3){
            holder.icon1.setImageResource(img[0]);
            holder.icon2.setImageResource(img[1]);
            holder.icon3.setImageResource(img[2]);
        }
        else if(num == 2){
            holder.icon1.setImageResource(img[0]);
            holder.icon2.setImageResource(img[1]);
        }
        else{
            holder.icon1.setImageResource(img[0]);
        }
    }

    @Override
    public int getItemCount(){
        return items.size();
    }
}
