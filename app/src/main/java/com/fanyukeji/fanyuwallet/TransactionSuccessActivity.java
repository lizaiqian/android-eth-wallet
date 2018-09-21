package com.fanyukeji.fanyuwallet;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

public class TransactionSuccessActivity extends Activity {

    private TextView transactionHash;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_success);

        String hash = getIntent().getStringExtra(getPackageName() + ".hash");

        transactionHash = findViewById(R.id.transactionHash);
        transactionHash.setText(hash);

    }
}
