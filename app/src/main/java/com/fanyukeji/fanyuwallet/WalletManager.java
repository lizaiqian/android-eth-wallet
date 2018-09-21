package com.fanyukeji.fanyuwallet;


import android.content.Context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;

import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicHierarchy;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.crypto.MnemonicException;
import org.bitcoinj.wallet.DeterministicSeed;
import org.web3j.crypto.MnemonicUtils;
import org.web3j.crypto.WalletFile;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class WalletManager {

    private static WalletManager mnemonicManager;

    private final int ZERO = 0;

    public static final ImmutableList<ChildNumber> BIP44_ETH_ACCOUNT_ZERO_PATH =
            ImmutableList.of(new ChildNumber(44, true), new ChildNumber(60, true),
                    ChildNumber.ZERO_HARDENED, ChildNumber.ZERO);


    public static WalletManager getInstance() {

        if (mnemonicManager == null) {
            synchronized (WalletManager.class) {
                if (mnemonicManager == null) {
                    mnemonicManager = new WalletManager();
                }
            }
        }
        return mnemonicManager;
    }

    public String generateMnemonic() throws MnemonicException.MnemonicLengthException {

        SecureRandom secureRandom = new SecureRandom();
        byte[] entropy = new byte[DeterministicSeed.DEFAULT_SEED_ENTROPY_BITS / 8];
        secureRandom.nextBytes(entropy);

        List<String> words = MnemonicCode.INSTANCE.toMnemonic(entropy);

        StringBuffer stringBuffer = new StringBuffer();

        for (String word : words) {
            stringBuffer.append(" ").append(word);
        }

        return stringBuffer.substring(1).toString();

    }

    public BigInteger generateKeysFromMnemonic(String mnemonic) {

        byte[] seed = MnemonicUtils.generateSeed(mnemonic, null);

        DeterministicKey rootKey = HDKeyDerivation.createMasterPrivateKey(seed);

        DeterministicHierarchy hierarchy = new DeterministicHierarchy(rootKey);

        DeterministicKey deterministicKey = hierarchy.deriveChild(BIP44_ETH_ACCOUNT_ZERO_PATH, false, true, ChildNumber.ZERO);

        BigInteger privKey = deterministicKey.getPrivKey();

        return privKey;

    }


    //获取walletFile文件名
    public String getWalletFileName(WalletFile walletFile) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("'UTC--'yyyy-MM-dd'T'HH-mm-ss.SSS'--'");
        return dateFormat.format(new Date()) + walletFile.getAddress() + ".json";
    }


    public WalletFile getWalletFile(Context context) throws IOException {
        File walletDir = context.getDir("eth", Context.MODE_PRIVATE);
        File[] files = walletDir.listFiles();
        WalletFile walletFile = null;
        ObjectMapper objectMapper = new ObjectMapper();

        if (files.length != 0) {
            walletFile = objectMapper.readValue(files[0], WalletFile.class);
        }

        return walletFile;
    }

}
