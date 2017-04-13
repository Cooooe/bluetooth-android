package com.github.coe.bluetoothtestapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.github.coe.bluetoothtestapp.constant.Constant;
import com.github.coe.bluetoothtestapp.service.BluetoothService;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.Buffer;
import java.util.logging.LogRecord;


public class MainActivity extends Activity implements View.OnClickListener{
    // debugging
    private static final String TAG = "MAIN_ACTIVITY";

    // layout
    private Button button1;
    private TextView txt_result;
    private Button sendBtn;
    private BluetoothService bluetoothService;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    private int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = (Button) findViewById(R.id.button1);
        sendBtn = (Button) findViewById(R.id.sendBtn);
        txt_result = (TextView) findViewById(R.id.txt_result);

        button1.setOnClickListener(this);
        sendBtn.setOnClickListener(this);


        if ( bluetoothService == null ) {
            bluetoothService = new BluetoothService(this, mHandler);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
        case R.id.button1:
            if ( bluetoothService.getDeviceState() ) {
                bluetoothService.enabledBluetooth();
            } else {
                finish();
            }
            break;
        case R.id.sendBtn:
            try {

//                String str = "Funcking Serial";
                JSONObject json = new JSONObject();

                if ( flag == 0 ) {
                    json.put("power", 1);
                    flag = 1;
                } else {
                    flag = 0;
                    json.put("power", 0);
                }

                bluetoothService.write(json.toString().getBytes());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "ACTIVITY RESULT CODE: " + resultCode);

        switch(requestCode) {
        case Constant.REQUEST_CONNECT_DEVICE:
            if ( resultCode == Activity.RESULT_OK ) {
                bluetoothService.getDeviceInfo(data);
            }
            break;
        case Constant.REQUEST_ENABLE_BT:
            if ( resultCode == Activity.RESULT_OK ) {
                bluetoothService.scanDevice();
            } else {
                Log.d(TAG, "Bluetooth is not enabled");
            }

            break;
        }
    }
}
