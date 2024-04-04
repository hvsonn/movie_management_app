package com.example.android_ck;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.android_ck.model.item_user;

import java.util.ArrayList;
import java.util.List;

public class userAdapter extends RecyclerView.Adapter<userAdapter.userViewHolder> {

    private List<item_user> mylist;
    private Context context;

    public userAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<item_user> list) {
        this.mylist = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public userViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new userViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull userAdapter.userViewHolder holder, int position) {
        item_user itemUser = mylist.get(position);
        if (itemUser == null) {
            return;
        }
        holder.tv_tk.setText(itemUser.getTk());
        holder.tv_hoten.setText(itemUser.getHoten());
    }

    @Override
    public int getItemCount() {
        if (mylist != null) {
            return mylist.size();
        }
        return 0;
    }

    public class userViewHolder extends ViewHolder {
        private TextView tv_tk;
        private TextView tv_hoten;
        private CheckBox chb_item;
        public userViewHolder(@NonNull View itemView) {
            super((itemView));
            tv_tk = itemView.findViewById(R.id.item_tk);
            tv_hoten = itemView.findViewById(R.id.item_hoten);
        }

        // checkBox


    }
}