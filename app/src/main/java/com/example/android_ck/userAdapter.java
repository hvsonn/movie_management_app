package com.example.android_ck;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.android_ck.khachhang.khachhang_dangnhap;
import com.example.android_ck.khachhang.khachhang_quanlytaikhoan;
import com.example.android_ck.model.item_user;
import com.example.android_ck.quanly.quanly_xoataikhoankhachhang;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class userAdapter extends RecyclerView.Adapter<userAdapter.userViewHolder> implements Filterable {
    private List<item_user> mylist;
    private List<item_user> filteredList; // Danh sách được lọc sau khi tìm kiếm
    private Context context;
    private DBHelper dbHelper;

    public userAdapter(Context context, DBHelper dbHelper, List<item_user> mylist) {
        this.context = context;
        this.dbHelper = dbHelper;
        this.mylist = mylist;
        this.filteredList = new ArrayList<>(mylist); // Khởi tạo danh sách lọc từ danh sách gốc
    }

    public class userViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_tk;
        private TextView tv_email;
        private ImageView img_delete;

        public userViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_tk = itemView.findViewById(R.id.item_tk);
            tv_email = itemView.findViewById(R.id.item_email);
            img_delete = itemView.findViewById(R.id.img_delete);

            img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        item_user itemUser = filteredList.get(position);
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Bạn có chắc chắn muốn xóa?").setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Boolean ktra = dbHelper.xoaTaiKhoan(itemUser.getTk());

                                if (ktra) {
                                    removeItem(position);
                                    mylist.remove(itemUser);
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                                }

                            }

                        }).setNegativeButton("Hủy", null).show();

                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public userViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new userViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull userAdapter.userViewHolder holder, int position) {
        item_user itemUser = filteredList.get(position);
        if (itemUser == null) {
            return;
        }
        holder.tv_tk.setText(itemUser.getTk());
        holder.tv_email.setText(itemUser.getEmail());
    }

    @Override
    public int getItemCount() {
        if (filteredList != null) {
            return filteredList.size();
        }
        return 0;
    }

    public void removeItem(int position) {
        filteredList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, filteredList.size());
    }

    public void updateData(List<item_user> newList) {
        mylist.clear();
        mylist.addAll(newList);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<item_user> filteredList = new ArrayList<>();
                if (TextUtils.isEmpty(constraint)) {
                    // Nếu không có ràng buộc hoặc ràng buộc trống, hiển thị toàn bộ danh sách
                    filteredList.addAll(mylist);
                } else {
                    // Nếu có ràng buộc, lọc danh sách dựa trên ràng buộc
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (item_user itemUser : mylist) {
                        // Kiểm tra xem tên tài khoản có chứa chuỗi tìm kiếm hay không
                        if (removeAccents(itemUser.getTk().toLowerCase()).contains(removeAccents(filterPattern))) {
                            // Nếu có, thêm vào danh sách lọc
                            filteredList.add(itemUser);
                        }
                    }
                }
                // Tạo kết quả lọc
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                // Xóa danh sách đã lọc và thêm danh sách lọc mới
                filteredList.clear();
                filteredList.addAll((List<item_user>) results.values);
                // Thông báo cho Adapter cập nhật giao diện
                notifyDataSetChanged();
            }
        };
    }

    public static String removeAccents(String input) {
        String regex = "\\p{InCombiningDiacriticalMarks}+";
        String temp = Normalizer.normalize(input, Normalizer.Form.NFD);
        return temp.replaceAll(regex, "");
    }
}