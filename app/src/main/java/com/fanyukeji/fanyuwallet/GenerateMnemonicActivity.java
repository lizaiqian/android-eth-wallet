package com.fanyukeji.fanyuwallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

public class GenerateMnemonicActivity extends Activity {

    private TextView mnemonicText;
    private String mnemonic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geneate_mnemonic);
        mnemonic = getIntent().getStringExtra("mnemonic");

        mnemonicText = findViewById(R.id.mnemonicText);
        mnemonicText.setText(mnemonic);
    }

    public void backupOver(View view) {
        Intent intent = new Intent();
        intent.setClass(this, BackupCheckActivity.class);
        intent.putExtra("mnemonic", mnemonic);

        startActivity(intent);
    }

}
