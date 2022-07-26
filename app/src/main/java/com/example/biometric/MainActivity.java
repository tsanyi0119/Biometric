package com.example.biometric;

import static android.hardware.biometrics.BiometricPrompt.AUTHENTICATION_RESULT_TYPE_DEVICE_CREDENTIAL;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.PluralsRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.biometric.BiometricPrompt.AuthenticationCallback;
import androidx.core.content.ContextCompat;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import javax.security.auth.login.LoginException;

public class MainActivity extends AppCompatActivity {

    private BiometricManager biometricManager;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo.Builder promptInfo;
    private Button fingerprint_btn,pin_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fingerprint_btn = findViewById(R.id.fingerprint_btn);
        pin_btn = findViewById(R.id.pin_btn);
        biometricManager = BiometricManager.from(this);

        checkBiometricSupport();
        createBiometricPrompt();

        fingerprint_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildFingerPrint();
                biometricPrompt.authenticate(promptInfo.build());
            }
        });
        pin_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                buildPin();
                Log.e("TAG", "onClick: "+promptInfo.build().getAllowedAuthenticators());
                biometricPrompt.authenticate(promptInfo.build());

            }
        });

    }

    private void buildFingerPrint() {
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
                .setTitle("驗證")
                .setSubtitle("請輸入指紋")
                .setNegativeButtonText("取消");
    }

    private void buildPin(){
        //不能設setNegativeButtonText() ，且只有成功callBack
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                //設定驗證(DEVICE_CREDENTIAL 為最基本PIN、圖形等、BIOMETRIC_WEAK、BIOMETRIC_STRONG(最高等級)(BIOMETRIC表生物辨識逼本也有指紋))
                .setAllowedAuthenticators(BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                .setTitle("PIN")
                .setSubtitle("請輸入PIN");
    }

    private void createBiometricPrompt(){
        biometricPrompt = new BiometricPrompt(this,
                ContextCompat.getMainExecutor(this),
                new AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        Log.e("GOGO", errString.toString());
                    }
                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        Log.e("GOGO", "成功");
                    }
                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        Log.e("GOGO", "失敗");
                    }
                });
    }


    private void checkBiometricSupport(){
        switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.e("TAG", "App can authenticate using biometrics.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Log.e("TAG", "No biometric features available on this device.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Log.e("TAG", "Biometric features are currently unavailable.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Log.e("TAG", "The user hasn't associated any biometric credentials with their account.");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + biometricManager.canAuthenticate());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0){
            if (resultCode == RESULT_OK) {

            }
        }
    }
}