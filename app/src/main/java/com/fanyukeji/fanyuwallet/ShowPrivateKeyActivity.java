package com.fanyukeji.fanyuwallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

public class ShowPrivateKeyActivity extends Activity{

    private TextView tv_privatekey;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_privatekey);
        tv_privatekey = findViewById(R.id.privateKey);

        String privateKey = getIntent().getStringExtra(getPackageName() + ".privateKey");
        tv_privatekey.setText(privateKey);
    }

    public void returnMainActivity(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }
}
