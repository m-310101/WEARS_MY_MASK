package com.example.wearsmymask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    String[] ID, content = new String[10];
    Context context;

    public MyAdapter(Context ct, String[] data1, String[] data2){
        context = ct;
        ID = data1;
        content = data2;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.notification_recycler, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.notifTime.setText(ID[position]);
        holder.notifContent.setText(content[position]);
    }

    @Override
    public int getItemCount() {

        return ID.length;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView notifTime, notifContent;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            notifTime = itemView.findViewById(R.id.notifTime);
            notifContent = itemView.findViewById(R.id.notifContent);
        }
    }
}
