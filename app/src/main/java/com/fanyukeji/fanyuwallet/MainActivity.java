package com.fanyukeji.fanyuwallet;

import android.content.ContextWrapper;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wang.avi.AVLoadingIndicatorView;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    private final String ETH_NETWORK = "https://rinkeby.infura.io/";

    private TextView mTextMessage;

    private Web3j web3j;

    private String myaddress;

    private ContextWrapper contextWrapper;

    private WalletFile walletFile;

    private EditText et_to;

    private EditText et_amount;

    private EditText et_password;

    private AVLoadingIndicatorView avi;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_amount = findViewById(R.id.ethAmountText);
        et_to = findViewById(R.id.toText);
        et_password = findViewById(R.id.passwordText);

        avi = findViewById(R.id.avi);

        contextWrapper = new ContextWrapper(this);

        WalletManager walletManager = WalletManager.getInstance();
        try {
            walletFile = walletManager.getWalletFile(this);

            if(walletFile != null) {
                myaddress = "0x" + walletFile.getAddress();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        web3j = Web3jFactory.build(new HttpService(ETH_NETWORK));

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(, EthereumWalletActivity.class);
//
//                startActivity(intent);
//            }
//        });
//        mTextMessage = (TextView) findViewById(R.id.message);
//        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }


    public void onstartEthWallet(View view) {
        Intent intent = new Intent();
        intent.setClass(this, EthereumWalletActivity.class);

        startActivity(intent);
    }

    public void onEthSend(View view) {

        avi.show();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    ECKeyPair ecKeyPair = Wallet.decrypt(et_password.getText().toString(), walletFile);

                    BigInteger transactionCount = web3j.ethGetTransactionCount(myaddress, DefaultBlockParameterName.LATEST).send().getTransactionCount();
                    BigInteger gasPrice = web3j.ethGasPrice().send().getGasPrice();

                    RawTransaction rawTransaction = RawTransaction.createTransaction(transactionCount, gasPrice, BigInteger.valueOf(3000000), et_to.getText().toString(), Convert.toWei(et_amount.getText().toString(), Convert.Unit.ETHER).toBigInteger(), "");


                    Credentials credentials = Credentials.create(ecKeyPair);

                    byte[] bytes = TransactionEncoder.signMessage(rawTransaction, credentials);

                    String transactionHash = web3j.ethSendRawTransaction(Numeric.toHexString(bytes)).send().getTransactionHash();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            avi.hide();
                            et_to.setText("");
                            et_amount.setText("");
                            et_password.setText("");
                        }
                    });

                    startActivity(new Intent().setClass(contextWrapper, TransactionSuccessActivity.class).putExtra(getPackageName() + ".hash", transactionHash));

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (CipherException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(contextWrapper, "密码错误", Toast.LENGTH_SHORT).show();
                            avi.hide();
                        }
                    });
                }

            }
        });

    }

    public void exportPrivateKey(View view) {

        if (walletFile == null) {
            Toast.makeText(this, "未导入钱包", Toast.LENGTH_SHORT).show();
            return;
        }

        startActivity(new Intent(this, ExportPrivateKeyActivity.class));

    }

    public void exportMnemonic(View view) {

        if (walletFile == null) {
            Toast.makeText(this, "未导入钱包", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(new Intent(this, ExportMnemonicActivity.class));

    }


    public void exportKeystore(View view) {
        if (walletFile == null) {
            Toast.makeText(this, "未导入钱包", Toast.LENGTH_SHORT).show();
            return;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String walletFileJson = objectMapper.writeValueAsString(walletFile);
            startActivity(new Intent(this, ShowKeystoreActivity.class).putExtra(getPackageName() + ".keystore", walletFileJson));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }
}
