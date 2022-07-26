package com.example.biometric;

import androidx.annotation.NonNull;
import androidx.annotation.PluralsRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.biometric.BiometricPrompt.AuthenticationCallback;
import androidx.core.content.ContextCompat;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private BiometricManager biometricManager;
    private Button button;
    //123
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        biometricManager = BiometricManager.from(this);

        checkBiometricSupport();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                build();
            }
        });

    }

    private void build() {
        BiometricPrompt.PromptInfo promptInfo
                = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("驗證")
                .setSubtitle("請輸入指紋")
                .setNegativeButtonText("取消")
                .build();

        new BiometricPrompt(this,
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
                }).authenticate(promptInfo);
    }
    private void checkBiometricSupport(){
        switch (biometricManager.canAuthenticate()) {
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
}