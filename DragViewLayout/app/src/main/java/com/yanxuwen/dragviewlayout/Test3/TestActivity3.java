package com.yanxuwen.dragviewlayout.Test3;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.yanxuwen.dragview.DragViewActivity;
import com.yanxuwen.dragviewlayout.R;
import com.yanxuwen.dragviewlayout.Test1.MyFragment;

import java.util.ArrayList;

public class TestActivity3 extends Activity {
    public ImageView v1;
    final ArrayList<View> views=new ArrayList<>();
    final ArrayList<Object> listdata=new ArrayList<>();
    final ArrayList<Class> listfragemnt=new ArrayList<>();
    private View view_test3;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test3);
        context=this;
        v1= (ImageView) findViewById(R.id.image);
        views.add(v1);
        listfragemnt.add(MyFragment.class);
        listdata.add("sdsds1");
        v1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open();
            }
        });
        RequestOptions options3 = new RequestOptions()
                .centerCrop()
                .priority(Priority.HIGH);
        Glide.with(context).load("http://p3.pstatp.com/large/pgc-image/1530136376452729ffef687").transition(DrawableTransitionOptions.withCrossFade()).apply(options3).into(v1);

    }
    public void open(){
        DragViewActivity.startActivity(this,0,new DragViewActivity.OnDataListener() {
            TextView text_abstract = null;

            @Override
            public View getCurView(int position) {
                return views.get(position);
            }

            @Override
            public ArrayList<Object> getListData() {
                return listdata;
            }
            @Override
            public ArrayList<Class> getListFragmentClass() {
                return listfragemnt;
            }
            @Override
            public void onPageSelected(int position) {
                if(position==0&&listdata.size()<3) {
                    //更加数据
                    listdata.add("sdsds3");
                    listdata.add("sdsds4");
                    views.add(v1);
                    views.add(v1);
                    listfragemnt.add(MyFragment.class);
                    listfragemnt.add(MyFragment.class);
                    DragViewActivity.getInstance(TestActivity3.this).notifyDataSetChanged();
                }
                if(text_abstract!=null) {
                    text_abstract.setText((position + 1) + "/" + 3 + " 从第一张照片XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
                }

            }
            @Override
            public boolean onBackPressed() {
                return true;
            }

            @Override
            public void init() {
                if(view_test3==null){
                    view_test3=getLayoutInflater().inflate(R.layout.view_test3,null);
                    DragViewActivity.getInstance(context).getDragViewLayout().addView(view_test3);
                    text_abstract= (TextView) view_test3.findViewById(R.id.text);
                    DragViewActivity.getInstance(context).setOnDrawerOffsetListener(new DragViewActivity.OnDrawerOffsetListener() {
                        @Override
                        public void onDrawerOffset(@FloatRange(from = 0, to = 1) float offset) {
                            text_abstract.setAlpha(offset-0.3f);
                            if(offset==0){
                                view_test3=null;
                            }
                        }
                    });
                }
            }
        });
    }
}
