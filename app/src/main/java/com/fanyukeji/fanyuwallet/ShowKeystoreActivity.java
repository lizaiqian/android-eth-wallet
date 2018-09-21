package com.fanyukeji.fanyuwallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

public class ShowKeystoreActivity extends Activity{
    private final String TAG = "ShowKeystoreActivity";

    private TextView tv_keystore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_keystore);
        tv_keystore = findViewById(R.id.keystore);
        String keystore = getIntent().getStringExtra(getPackageName() + ".keystore");

        tv_keystore.setText(keystore);
    }

    public void returnMainActivity(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }
}
