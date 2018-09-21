package com.fanyukeji.fanyuwallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class BackupCheckActivity extends Activity {

    private String mnemonic;

    private EditText mnemonicText;

    private final String ALERT_INFO = "助记词输入错误";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_check);
        mnemonic = getIntent().getStringExtra("mnemonic");

        mnemonicText = findViewById(R.id.checkedMnemonic);

    }

    public void onCheckBackup(View view) {

        if (mnemonicText.getText().toString().equals(mnemonic)) {
            startActivity(new Intent().setClass(this, EthereumWalletActivity.class));
        } else {
            Toast.makeText(this, ALERT_INFO, Toast.LENGTH_LONG).show();
        }

    }

}
