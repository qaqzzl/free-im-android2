package com.qaqzz.free_im;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.qaqzz.common.Common;
import com.qaqzz.common.app.Activity;

import butterknife.BindView;

public class MainActivity extends Activity {
    @BindView(R.id.txt_text)
    TextView mTestText;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mTestText.setText("TEST Hello World!");
    }
}
