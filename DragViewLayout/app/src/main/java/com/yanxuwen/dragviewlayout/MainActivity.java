package com.yanxuwen.dragviewlayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yanxuwen.dragviewlayout.Test1.TestActivity;
import com.yanxuwen.dragviewlayout.Test2.TestActivity2;
import com.yanxuwen.dragviewlayout.Test3.TestActivity3;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    /**多个*/
    public void onClickView(View v){
         startActivity(new Intent(this,TestActivity.class));
    }
    /**单个*/
    public void onClickView2(View v){
        startActivity(new Intent(this,TestActivity2.class));

    }
    /**单个*/
    public void onClickView3(View v){
        startActivity(new Intent(this,TestActivity3.class));

    }
}
