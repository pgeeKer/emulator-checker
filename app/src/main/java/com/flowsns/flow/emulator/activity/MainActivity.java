package com.flowsns.flow.emulator.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.flowsns.flow.emulator.EmulatorDetectManager;
import com.flowsns.flow.emulator.IEmulatorDetectListener;
import com.flowsns.flow.emulator.R;

public class MainActivity extends AppCompatActivity {

    private EmulatorDetectManager emulatorDetectManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emulatorDetectManager = new EmulatorDetectManager(this);
        emulatorDetectManager.setEmulatorDetectListener(new IEmulatorDetectListener() {
            @Override
            public void call(boolean isEmulator) {
                Toast.makeText(MainActivity.this, "是否是模拟器：" + isEmulator, Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emulatorDetectManager.startDetectService();
            }
        });
    }
}
