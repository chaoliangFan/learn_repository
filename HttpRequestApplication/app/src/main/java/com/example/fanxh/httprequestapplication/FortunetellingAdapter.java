package com.example.fanxh.httprequestapplication;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by fanxh on 2017/10/12.
 */

public class FortunetellingAdapter extends RecyclerView.Adapter <FortunetellingAdapter.ViewHolder>{
    private List<FortunetellingBean> mFortunetellingBeanList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        ImageView iconUrl;
        public ViewHolder(View view){
            super(view);
            name = (TextView) view.findViewById(R.id.fortunetelling_title);
            iconUrl = (ImageView) view.findViewById(R.id.fortunetelling_photo);
        }
    }

    public FortunetellingAdapter(List<FortunetellingBean> fortunetellingBeanList){
        mFortunetellingBeanList = fortunetellingBeanList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fortunetelling_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
    FortunetellingBean fortunetellingBean = mFortunetellingBeanList.get(position);
   //     holder.iconUrl.setImageBitmap();
        holder.name.setText(fortunetellingBean.getName());
    }

    @Override
    public int getItemCount() {
        return mFortunetellingBeanList.size();
    }

}
