package org.md2k.motionsenselibrary.example;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.md2k.motionsenselibrary.device.DeviceSettings;
import org.md2k.motionsenselibrary.device.Data;
import org.md2k.motionsenselibrary.device.Device;
import org.md2k.motionsenselibrary.device.DeviceInfo;
import org.md2k.motionsenselibrary.device.MotionSenseManager;
import org.md2k.motionsenselibrary.device.ReceiveCallback;
import org.md2k.motionsenselibrary.device.ScanCallback;
import org.md2k.motionsenselibrary.plot.ActivityPlot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    boolean scanStarted = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MotionSenseManager.init(this);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
        setScan();
        setConnectedDevice();


    }
    void setConnectedDevice(){
        LinearLayout l = findViewById(R.id.layout_connected_device);
        l.removeAllViews();
        ArrayList<Device> deviceArrayList = MotionSenseManager.getDevices();
        for(int i =0;i<deviceArrayList.size();i++){
            LinearLayout layout2 = new LinearLayout(this);

            layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            layout2.setOrientation(LinearLayout.VERTICAL);
            l.addView(layout2);
            TextView t = new TextView(this);
            t.setText(deviceArrayList.get(i).getDeviceInfo().toString());
            Button b = new Button(this);
            b.setText("Disconnect");
            b.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            int finalI = i;
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MotionSenseManager.removeDevice(deviceArrayList.get(finalI).getDeviceInfo().getDeviceId());
                    setConnectedDevice();
                }
            });
            layout2.addView(t);
            layout2.addView(b);
            for(int j=0;j<deviceArrayList.get(i).getSensorInfo().size();j++){
                Button x = new Button(this);
                x.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                x.setText(deviceArrayList.get(i).getSensorInfo().get(j).getTitle());
                int finalI1 = i;
                int finalJ = j;
                x.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, ActivityPlot.class);
                        intent.putExtra("deviceId",deviceArrayList.get(finalI1).getDeviceInfo().getDeviceId());
                        intent.putExtra("sensorType",deviceArrayList.get(finalI1).getSensorInfo().get(finalJ).getSensorType().name());
                        startActivity(intent);
                    }
                });
                layout2.addView(x);
            }

        }
    }
    void setScan(){
        Button scan = findViewById(R.id.button_scan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanStarted = !scanStarted;
                manageScanUI();
            }
        });
    }
    void manageScanUI(){
        Button scan = findViewById(R.id.button_scan);
        if(scanStarted){
            MotionSenseManager.removeDevices();
            setConnectedDevice();
            LinearLayout layout = findViewById(R.id.layout_devices);
            layout.removeAllViews();
            scan.setText("Stop Scan");
            scan.setBackgroundColor(Color.RED);
            scan.setTextColor(Color.WHITE);
            MotionSenseManager.startScan(new ScanCallback() {
                @Override
                public void onReceive(DeviceInfo deviceInfo) {
                    LinearLayout layout = findViewById(R.id.layout_devices);
                    Button b = new Button(MainActivity.this);
                    b.setText("CONNECT\n"+deviceInfo.toString());
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            scanStarted=false;
                            manageScanUI();
                            start(deviceInfo);
                        }
                    });

                    b.setAllCaps(false);
                    layout.addView(b);
                    Log.d("abc", deviceInfo.toString());
                }
            });
        }else{
            scan.setText("Start Scan");
            scan.setBackgroundColor(Color.LTGRAY);
            scan.setTextColor(Color.BLACK);
            MotionSenseManager.stopScan();
        }


    }
    void start(DeviceInfo deviceInfo){
        DeviceSettings deviceSettings=null;
        switch(deviceInfo.getDeviceType()){
            case MOTION_SENSE: deviceSettings = DeviceSettings.MotionSenseBuilder().setDefault().build();break;
            case MOTION_SENSE_HRV: deviceSettings = DeviceSettings.MotionSenseHRVBuilder().setDefault().build();break;
            case MOTION_SENSE_HRV_PLUS: deviceSettings = DeviceSettings.MotionSenseHRVPlusBuilder().setDefault().build();break;
            case MOTION_SENSE_HRV_PLUS_GEN2: deviceSettings = DeviceSettings.MotionSenseHRVPlusGen2Builder().setDefault().build();break;
            case MOTION_SENSE_HRV_PLUS_GEN2_IR: deviceSettings = DeviceSettings.MotionSenseHRVPlusGen2Builder().setDefault().build();break;
            case MOTION_SENSE_HRV_PLUS_GEN2_IG: deviceSettings = DeviceSettings.MotionSenseHRVPlusGen2Builder().setDefault().build();break;
        }
        Device d = MotionSenseManager.addDevice(deviceInfo, deviceSettings);
        setConnectedDevice();
        d.start(new ReceiveCallback() {
            @Override
            public void onReceive(Data d) {
                Log.d("abc",d.toString());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        if (requestCode == 1000) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                finish();
            }
        }
    }
}
