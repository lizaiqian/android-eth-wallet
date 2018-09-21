package com.fanyukeji.fanyuwallet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.fanyukeji.fanyuwallet.utils.CryptoUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class ExportMnemonicActivity extends Activity{

    private final String TAG = "ExportMenmonicActivity";

    private EditText et_password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_mnemonic);

        et_password = findViewById(R.id.password);
    }

    public void onexportMnemonic(View view) {

        File walletDir = getDir("eth", Context.MODE_PRIVATE);

        File mnemonicFile = new File(walletDir, "mnemonic");

        ObjectMapper objectMapper = new ObjectMapper();
        String decrypt = null;
        String password = et_password.getText().toString();

        try {
            String mnemonic = objectMapper.readValue(mnemonicFile, String.class);
            decrypt = CryptoUtil.DesDecrypt(mnemonic, CryptoUtil.md5Util(password));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        if(decrypt != null) {
            startActivity(new Intent(this, ShowMnemonicActivity.class).putExtra(getPackageName() + ".mnemonic", decrypt));
        } else {
            Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show();
        }

    }
}
