package org.remdev.timlogtest.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.remdev.timlog.Log;
import org.remdev.timlog.LogFactory;
import org.remdev.timlogtest.R;

public class TestActivity extends AppCompatActivity {

    private static final Log log = LogFactory.create(TestActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        log.i("onCreate at %d", System.currentTimeMillis());
    }

    @Override
    protected void onResume() {
        super.onResume();
        log.i("onResume at %d", System.currentTimeMillis());
    }
}
