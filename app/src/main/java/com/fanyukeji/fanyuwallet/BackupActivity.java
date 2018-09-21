package com.fanyukeji.fanyuwallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class BackupActivity extends Activity {

    private String mnemonic;
//    private TextView mnemonicText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);

        mnemonic = getIntent().getStringExtra("mnemonic");

    }

    public void backupMnemonic(View view){

        Intent intent = new Intent();

        intent.putExtra("mnemonic", mnemonic);
        intent.setClass(this, GenerateMnemonicActivity.class);

        startActivity(intent);

    }

}
