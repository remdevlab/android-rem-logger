package org.remdev.timlogtest.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.remdev.timlog.Log;
import org.remdev.timlog.LogFactory;
import org.remdev.timlogtest.R;

public class TestActivity extends AppCompatActivity {

    private static final Log log = LogFactory.create(TestActivity.class);
    private static Log logObj;
    private static final Log logString = LogFactory.create("StringTag");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        logObj = LogFactory.create(this);
        log.i("onCreate at %d", System.currentTimeMillis());
        logString.i("onCreate at %d", System.currentTimeMillis());
        logObj.i("onCreate at %d", System.currentTimeMillis());
    }

    @Override
    protected void onResume() {
        super.onResume();
        log.i("onResume at %d", System.currentTimeMillis());
        logString.i("onResume at %d", System.currentTimeMillis());
        logObj.i("onResume at %d", System.currentTimeMillis());
    }
}
