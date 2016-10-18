package com.beiing.customview;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.beiing.customview.widgets.VerticalLinearLayout;

public class LaunchActivity extends AppCompatActivity {

    private VerticalLinearLayout mMianLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        mMianLayout = (VerticalLinearLayout) findViewById(R.id.id_main_ly);
        mMianLayout.setOnPageChangeListener(new VerticalLinearLayout.OnPageChangeListener()
        {
            @Override
            public void onPageChange(int currentPage)
            {
//				mMianLayout.getChildAt(currentPage);
                Toast.makeText(LaunchActivity.this, "第"+(currentPage+1)+"页", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
