package ru.mirea.ovsyannikov.cryptoloader;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.loader.content.AsyncTaskLoader;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class CryptoLoader extends AsyncTaskLoader<String> {
    public static final String ARG_CIPHER = "cipher";
    public static final String ARG_KEY = "key";
    private final Bundle args;

    public CryptoLoader(@NonNull Context context, Bundle args) {
        super(context);
        this.args = args;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        byte[] cipherText = args.getByteArray(ARG_CIPHER);
        byte[] keyBytes = args.getByteArray(ARG_KEY);
        SecretKey originalKey = new SecretKeySpec(keyBytes, 0, keyBytes.length, "AES");
        return CryptoUtils.decryptMsg(cipherText, originalKey);
    }
}