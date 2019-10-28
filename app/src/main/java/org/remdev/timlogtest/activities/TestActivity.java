package org.remdev.timlogtest.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import org.remdev.timlog.Log;
import org.remdev.timlog.LogFactory;
import org.remdev.timlogtest.R;

public class TestActivity extends AppCompatActivity {

    private static final Log log = LogFactory.create(TestActivity.class);
//    private static Log logObj;
//    private static final Log logString = LogFactory.create("StringTag");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        log.i("onCreate at %d", System.currentTimeMillis());
//        logObj = LogFactory.create(this);
//        logString.i("onCreate at %d", System.currentTimeMillis());
//        logObj.i("onCreate at %d", System.currentTimeMillis());
    }

    @Override
    protected void onResume() {
        super.onResume();
        log.i("onResume at %d", System.currentTimeMillis());
//        logString.i("onResume at %d", System.currentTimeMillis());
//        logObj.i("onResume at %d", System.currentTimeMillis());
        log.e(new Exception("TEST"),"test");
    }
}
