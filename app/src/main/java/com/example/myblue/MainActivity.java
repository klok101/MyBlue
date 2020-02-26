package com.example.myblue;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {


    BluetoothAdapter bluetoothAdapter;
    ArrayAdapter<String> arrayAdapter;
    ListView l1;
    ArrayList arrayList;
    Button enableButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        l1 = (ListView)findViewById(R.id.listView);
        arrayList = new ArrayList();

        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,arrayList);
        l1.setAdapter(arrayAdapter);

        findPairedDevices();

        enableButton = (Button) findViewById(R.id.enableButton);
        enableButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,300);
                startActivity(intent);
            }
        });
    }

    public void discoverDevices(View v){

        bluetoothAdapter.startDiscovery();

    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                arrayList.add(device.getName());
                arrayAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(broadcastReceiver,intentFilter);

    }

    public void findPairedDevices(){

        int index = 0;
        Set<BluetoothDevice> bluetoothSet = bluetoothAdapter.getBondedDevices();
        String[] str = new String[bluetoothSet.size()];

        if(bluetoothSet.size()>0){
            for(BluetoothDevice device: bluetoothSet){
                str[index] = device.getName();
                index++;
            }
            arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,str);
            l1.setAdapter(arrayAdapter);
        }
        else{



        }

    }

    public void on(View v){

        if(bluetoothAdapter == null){
            Toast.makeText(getApplicationContext(), "Bluetooth is not supported in this phone", Toast.LENGTH_SHORT).show();
        }
        else{
            if(!bluetoothAdapter.isEnabled()){
                Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(i,1);
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                Toast.makeText(getApplicationContext(),"The bluetooth is enabled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void scan(View v){

        if(bluetoothAdapter.isEnabled()){
            Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            i.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,600);
            startActivity(i);
        }

    }
}
