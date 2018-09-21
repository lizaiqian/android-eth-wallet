package com.fanyukeji.fanyuwallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

public class ShowMnemonicActivity extends Activity {

    private TextView tv_mnemonic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_mnemonic);

        tv_mnemonic = findViewById(R.id.mnemonic);

        String mnemonic = getIntent().getStringExtra(getPackageName() + ".mnemonic");

        tv_mnemonic.setText(mnemonic);

    }

    public void returnMainActivity(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }
}
