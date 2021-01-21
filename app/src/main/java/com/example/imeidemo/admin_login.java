package com.example.imeidemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class admin_login extends AppCompatActivity {

    private EditText u,p;
    private Button a_log;
    String u_admin,p_admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        u=findViewById(R.id.user);
        p=findViewById(R.id.pass);
        a_log=findViewById(R.id.button_log);



        a_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              verify();
            }
        });

    }

    public void verify()
    {
        u_admin=u.getText().toString();
        p_admin=p.getText().toString();

        if (u_admin.equalsIgnoreCase("admin") && p_admin.equalsIgnoreCase("admin"))
        {
            Toast.makeText(admin_login.this, "Welcome Admin ", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(admin_login.this,admin.class));

        }

        else
        {
            Toast.makeText(admin_login.this, "Invalid Login Details", Toast.LENGTH_SHORT).show();
        }

    }
}