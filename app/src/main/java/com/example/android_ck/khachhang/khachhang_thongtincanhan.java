package com.example.android_ck.khachhang;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.android_ck.DBHelper;
import com.example.android_ck.R;

public class khachhang_thongtincanhan extends AppCompatActivity {
    EditText edit_hoten, edit_ngaysinh, edit_email, edit_sdt;
    RadioGroup rb_gr;
    TextView tv_boqua;
    Button btn_thongtincanhan;

    DBHelper dbHelper;

    String regex_hoten = "^[a-zA-Zà-Ỹ ]+$";
    String regex_email = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";
    String regex_sdt = "^(0|\\+84)(\\d{1,2})(\\d{3})(\\d{4})$";
    String regex_ngaysinh = "^(0[1-9]|[12][0-9]|3[01])([-./])(0[1-9]|1[012])([-./])(19|20)\\d\\d$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_khachhang_thongtincanhan);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edit_hoten = findViewById(R.id.edit_hoten);
        edit_ngaysinh = findViewById(R.id.edit_ngaysinh);
        edit_email = findViewById(R.id.edit_email);
        edit_sdt = findViewById(R.id.edit_sdt);
        rb_gr = findViewById(R.id.rb_gr);
        tv_boqua = findViewById(R.id.tv_boqua);
        btn_thongtincanhan = findViewById(R.id.btn_thongtincanhan);

        dbHelper = new DBHelper(this);

        btn_thongtincanhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hoten, ngaysinh, gioitinh = "", email, sdt;
                int ktra_rb_gr;
                hoten = edit_hoten.getText().toString().trim();
                ngaysinh = edit_ngaysinh.getText().toString().trim();
                email = edit_email.getText().toString().trim();
                sdt = edit_sdt.getText().toString().trim();
                ktra_rb_gr = rb_gr.getCheckedRadioButtonId();

                // Nhận Intent
                Intent myintent1 = getIntent();
                // Lấy Bundle ra khỏi Intent
                Bundle mybundle = myintent1.getBundleExtra("dangkypackage");
                String tk = mybundle.getString("tk");

                if (ktra_rb_gr == R.id.rb_nam) {
                    gioitinh = "nam";
                } else if (ktra_rb_gr == R.id.rb_nu) {
                    gioitinh = "nu";
                }

                if (hoten.isEmpty()) {
                    edit_hoten.setError("Vui lòng nhập họ và tên");
                    edit_hoten.requestFocus();
                    return;
                } else if (!hoten.matches(regex_hoten)) {
                    edit_hoten.setError("Họ và tên yêu cầu chỉ nhập chữ cái");
                    edit_hoten.requestFocus();
                    edit_hoten.setText("");
                    return;
                }

                if (ngaysinh.isEmpty()) {
                    edit_ngaysinh.setError("Vui lòng nhập ngày sinh");
                    edit_ngaysinh.requestFocus();
                    return;
                } else if (!ngaysinh.matches(regex_ngaysinh)) {
                    edit_ngaysinh.setError("Yêu cầu định dạng ngày sinh DD-MM-YYYY");
                    edit_ngaysinh.requestFocus();
                    edit_ngaysinh.setText("");
                    return;
                }

                if (email.isEmpty()) {
                    edit_email.setError("Vui lòng nhập email");
                    edit_email.requestFocus();
                    return;
                } else if (!email.matches(regex_email)) {
                    edit_email.setError("Yêu cầu định dạng email email@gmail.com");
                    edit_email.requestFocus();
                    edit_email.setText("");
                    return;
                }

                if (sdt.isEmpty()) {
                    edit_sdt.setError("Vui lòng nhập số điện thoại");
                    edit_sdt.requestFocus();
                    return;
                } else if (!sdt.matches(regex_sdt)) {
                    edit_sdt.setError("Yêu cầu định dạng số điện thoại");
                    edit_sdt.requestFocus();
                    edit_sdt.setText("");
                    return;
                }

                boolean thongtincanhan = dbHelper.themThongTinCaNhan(hoten, gioitinh, ngaysinh, email, sdt, tk);
                if (thongtincanhan) {
                    Toast.makeText(khachhang_thongtincanhan.this, "Thêm thông tin thành công", Toast.LENGTH_SHORT).show();
                    Intent myintent = new Intent(khachhang_thongtincanhan.this, MainActivity_khachhang.class);
                    startActivity(myintent);
                } else {
                    Toast.makeText(khachhang_thongtincanhan.this, "Thêm thông tin không thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tv_boqua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Nhận Intent
                Intent myintent1 = getIntent();
                // Lấy Bundle ra khỏi Intent
                Bundle mybundle = myintent1.getBundleExtra("dangkypackage");
                String tk = mybundle.getString("tk");

                boolean thongtincanhan = dbHelper.themThongTinCaNhan("null", "null", "null", "null", "null", tk);
                if (thongtincanhan) {
                    Toast.makeText(khachhang_thongtincanhan.this, "Thêm thông tin thành công", Toast.LENGTH_SHORT).show();
                    Intent myintent = new Intent(khachhang_thongtincanhan.this, MainActivity_khachhang.class);
                    startActivity(myintent);
                } else {
                    Toast.makeText(khachhang_thongtincanhan.this, "Thêm thông tin không thành công", Toast.LENGTH_SHORT).show();
                }
                Intent myintent = new Intent(khachhang_thongtincanhan.this, MainActivity_khachhang.class);
                startActivity(myintent);
            }
        });

    }
}