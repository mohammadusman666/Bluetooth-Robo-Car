package com.fcc.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button connect;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connect=findViewById(R.id.connect);
        connect.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent myIntent = new Intent(MainActivity.this, connect.class);
        MainActivity.this.startActivity(myIntent);
    }
}
