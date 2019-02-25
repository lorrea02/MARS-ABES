package com.example.election;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ConnectPrinter extends AppCompatActivity {
    BluetoothAdapter mBluetoothAdapter;
    Button btnConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_printer);

        //turning off bluetooth
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.disable();
            Intent enableBluetooth = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }
        else{
            Intent enableBluetooth = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }

        btnConnect = findViewById(R.id.btnConnect);

        btnConnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(ConnectPrinter.this,"Connecting...", Toast.LENGTH_LONG).show();
                try{
                    stopService(new Intent(ConnectPrinter.this, MyService.class));
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                startService(new Intent(ConnectPrinter.this, MyService.class));
            }
        });
    }



    private BroadcastReceiver mReceiver;
    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter("com.example.election");
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String msg_for_me = intent.getStringExtra("some_msg");
                //log our message value
                if(msg_for_me.equalsIgnoreCase("connected")) {
                    Toast.makeText(ConnectPrinter.this, "Printer successfully connected", Toast.LENGTH_LONG).show();
                    Intent goToMain = new Intent(ConnectPrinter.this,MainActivity.class);
                    startActivity(goToMain);
                }
            }
        };
        this.registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(this.mReceiver);
    }
}
