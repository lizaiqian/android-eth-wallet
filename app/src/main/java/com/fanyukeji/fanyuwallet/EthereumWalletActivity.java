package com.fanyukeji.fanyuwallet;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fanyukeji.fanyuwallet.utils.CryptoUtil;
import com.fanyukeji.fanyuwallet.utils.FileUtil;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wang.avi.AVLoadingIndicatorView;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class EthereumWalletActivity extends Activity {

    private TextView mTextMessage;

    private TextView prikeyText;

    private EditText et_mnemonic;

    private EditText et_password;

    private EditText et_comfirm_password;

    private AVLoadingIndicatorView avi;

    private final int HEX = 16;

    private final String ALERT_INFO = "输入密码不匹配";

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
        setContentView(R.layout.activity_ethereum_wallet);

        prikeyText = findViewById(R.id.privkey);
        et_mnemonic = findViewById(R.id.importMnemonic);
        et_password = findViewById(R.id.mnemonicPassword);
        et_comfirm_password = findViewById(R.id.confirmMnemonicPassword);
        avi = findViewById(R.id.avi);

    }


    //导入助记词
    public void onimportMnemonic(View view) {

        final String password = et_password.getText().toString();
        final String comfirmPassword = et_comfirm_password.getText().toString();
        final String mnemonic = et_mnemonic.getText().toString();

        if (password.isEmpty() || !password.equals(comfirmPassword)) {
            Toast.makeText(this, ALERT_INFO, Toast.LENGTH_LONG).show();
            return;
        }

        avi.show();

        final ContextWrapper contextWrapper = new ContextWrapper(this);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                WalletManager walletManager = WalletManager.getInstance();

                BigInteger privateKey = walletManager.generateKeysFromMnemonic(mnemonic);

                ECKeyPair ecKeyPair = ECKeyPair.create(privateKey);
                try {
                    WalletFile walletFile = Wallet.createLight(password, ecKeyPair);
                    String walletFileName = walletManager.getWalletFileName(walletFile);

                    File walletDir = contextWrapper.getDir("eth", Context.MODE_PRIVATE);

                    File[] files = walletDir.listFiles();
                    if(files.length != 0) {
                        for (File file: files) {
                            file.delete();
                        }
                    }

                    File file = new File(walletDir, walletFileName);

                    File mnemonicFile = new File(walletDir, "mnemonic");
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.writeValue(file, walletFile);

                    String encrypt = null;

                    try {
                        encrypt = CryptoUtil.DesEncrypt(mnemonic, CryptoUtil.md5Util(password));
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (InvalidKeySpecException e) {
                        e.printStackTrace();
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    }

                    System.out.println(encrypt);

                    if (encrypt != null) {
                        objectMapper.writeValue(mnemonicFile, encrypt);
                    }

                    startActivity(new Intent().setClass(contextWrapper, MainActivity.class));
                    finish();

                } catch (CipherException e) {
                    e.printStackTrace();
                } catch (JsonGenerationException e) {
                    e.printStackTrace();
                } catch (JsonMappingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    // 创建助记词页面
    public void oncreateMnemonic(View view) {
        startActivity(new Intent().setClass(this, CreateMnemonicActivity.class));
    }


}
