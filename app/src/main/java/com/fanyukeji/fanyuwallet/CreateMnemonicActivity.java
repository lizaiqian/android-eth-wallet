package com.fanyukeji.fanyuwallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.bitcoinj.crypto.MnemonicException;

public class CreateMnemonicActivity extends Activity {

    private TextView mnemonicText;

    private final String ALERT_INFO = "您两次输入的密码不匹配";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        mnemonicText = findViewById(R.id.createText);

    }

    public void oncreateMnemonic(View view) {

        String mnemonic = null;
        try {
            mnemonic = WalletManager.getInstance().generateMnemonic();
        } catch (MnemonicException.MnemonicLengthException e) {
            e.printStackTrace();
        }

        System.out.println(mnemonic);

        Intent intent = new Intent();
        intent.putExtra("mnemonic", mnemonic);
        intent.setClass(this, BackupActivity.class);

        startActivity(intent);

    }

}
