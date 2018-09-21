package com.fanyukeji.fanyuwallet;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.utils.Async;

import java.io.IOException;

public class ExportPrivateKeyActivity extends Activity {

    private final String TAG = "ExportPrivKeyActivity";

    private EditText et_password;
    private WalletFile walletFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_prikey);

        et_password = findViewById(R.id.password);

        try {
            walletFile = WalletManager.getInstance().getWalletFile(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void onexportPrivateKey(View view) {

        final String password = et_password.getText().toString();
        final Context context = new ContextWrapper(this);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                ECKeyPair ecKeyPair;
                try {
                    ecKeyPair = Wallet.decrypt(password, walletFile);

                } catch (CipherException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "密码错误", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }

                String privateKey = "0x" + ecKeyPair.getPrivateKey().toString();
                startActivity(new Intent(context, ShowPrivateKeyActivity.class).putExtra(getPackageName() + ".privateKey", privateKey));

            }
        });

    }
}
