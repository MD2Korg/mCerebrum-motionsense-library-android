package org.md2k.motionsenselibrary.example;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.md2k.motionsenselibrary.device.Data;
import org.md2k.motionsenselibrary.device.Device;
import org.md2k.motionsenselibrary.device.DeviceInfo;
import org.md2k.motionsenselibrary.device.DeviceSettings;
import org.md2k.motionsenselibrary.device.MotionSenseManager;
import org.md2k.motionsenselibrary.device.ReceiveCallback;
import org.md2k.motionsenselibrary.device.ScanCallback;
import org.md2k.motionsenselibrary.device.Version;
import org.md2k.motionsenselibrary.device.VersionCallback;
import org.md2k.motionsenselibrary.plot.ActivityPlot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    boolean scanStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MotionSenseManager.init(this);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
        setScan();
        setConnectedDevice();


    }

    void setConnectedDevice() {
        LinearLayout l = findViewById(R.id.layout_connected_device);
        l.removeAllViews();
        ArrayList<Device> deviceArrayList = MotionSenseManager.getDevices();
        for (int i = 0; i < deviceArrayList.size(); i++) {
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
                    String deviceId = deviceArrayList.get(finalI).getDeviceInfo().getDeviceId();
                    MotionSenseManager.removeDevice(deviceId);
                    setConnectedDevice();
                }
            });
            layout2.addView(t);
            layout2.addView(b);
            for (int j = 0; j < deviceArrayList.get(i).getSensorInfo().size(); j++) {
                Button x = new Button(this);
                x.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                x.setText(deviceArrayList.get(i).getSensorInfo().get(j).getTitle());
                int finalI1 = i;
                int finalJ = j;
                x.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, ActivityPlot.class);
                        intent.putExtra("deviceId", deviceArrayList.get(finalI1).getDeviceInfo().getDeviceId());
                        intent.putExtra("sensorType", deviceArrayList.get(finalI1).getSensorInfo().get(finalJ).getSensorType().name());
                        startActivity(intent);
                    }
                });
                layout2.addView(x);
            }

        }
    }

    void setScan() {
        Button scan = findViewById(R.id.button_scan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanStarted = !scanStarted;
                manageScanUI();
            }
        });
    }

    void manageScanUI() {
        Button scan = findViewById(R.id.button_scan);
        if (scanStarted) {
            MotionSenseManager.removeDevices();
            setConnectedDevice();
            LinearLayout layout = findViewById(R.id.layout_devices);
            layout.removeAllViews();
            scan.setText("Stop Scan");
            scan.setBackgroundColor(Color.RED);
            scan.setTextColor(Color.WHITE);
            MotionSenseManager.startScan(new ScanCallback() {
                @Override
                public void onReceive(String deviceName, String deviceId) {
                    final DeviceInfo[] deviceInfo = {null};
                    Button b = new Button(MainActivity.this);
                    LinearLayout layout = findViewById(R.id.layout_devices);
                    LinearLayout horizontal = new LinearLayout(MainActivity.this);
                    horizontal.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    horizontal.setOrientation(LinearLayout.HORIZONTAL);
                    horizontal.setPadding(16, 0, 16, 0);
                    TextView deviceText = new TextView(MainActivity.this);
                    deviceText.setText(deviceName + "\n" + deviceId);
                    Button bVersion = new Button(MainActivity.this);
                    bVersion.setText("Version");
                    bVersion.setAllCaps(false);
                    bVersion.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            scanStarted = false;
                            manageScanUI();
                            getDeviceVersion(deviceName, deviceId, new VersionCallback() {
                                @Override
                                public void onReceive(Version version) {
                                    if (version == null || version.getType() == null)
                                        deviceText.setText("ERROR " + deviceName + "\n" + deviceId);
                                    else {
                                        deviceText.setText("Name: " + deviceName + "\nID: " + deviceId + "\n" + version.getType() + "\nVersion:" + version.toString() + "\n");
                                        deviceInfo[0] = new DeviceInfo(deviceName, deviceId, version);
                                        b.setEnabled(true);
                                        bVersion.setEnabled(false);
                                    }

                                }
                            });
                        }
                    });

                    b.setText("Connect");
                    b.setEnabled(false);
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            scanStarted = false;
                            manageScanUI();
                            start(deviceInfo[0]);
                        }
                    });

                    b.setAllCaps(false);
                    horizontal.addView(bVersion);
                    horizontal.addView(b);
                    horizontal.addView(deviceText);
                    layout.addView(horizontal);
                }
            });
        } else {
            scan.setText("Start Scan");
            scan.setBackgroundColor(Color.LTGRAY);
            scan.setTextColor(Color.BLACK);
            MotionSenseManager.stopScan();
        }
    }

    void getDeviceVersion(String deviceName, String deviceId, VersionCallback versionCallback) {
        MotionSenseManager.getDeviceVersion(deviceName, deviceId, versionCallback);
    }


/*
    @Override
    public void onDestroy(){

        for(int i=0;i<outputStreamWriters.size();i++){
            try {
                outputStreamWriters.get(i).close();
            }catch (Exception e){}
        }
        super.onDestroy();
    }
*/


    void start(DeviceInfo deviceInfo) {
        try {

            ArrayList<String> results = new ArrayList<>();
            DeviceSettings deviceSettings = DeviceSettings.builder(deviceInfo).setDefault().build();
            if (MotionSenseManager.getDevice(deviceInfo.getDeviceId()) != null) return;
            String deviceId = deviceInfo.getDeviceId();
            deviceId = deviceId.replace(":", "_");
            String filename = deviceId + "_" + String.valueOf(System.currentTimeMillis()) + ".csv";
            Log.e("abc", "filename=" + filename);

            Device d = MotionSenseManager.addDevice(deviceInfo, deviceSettings);
            setConnectedDevice();
            d.start(new ReceiveCallback() {
                @Override
                public void onReceive(Data d) {
                    results.add(d.toString());
                    synchronized (results) {
                        if (results.size() > 1000) {
                            try {
                                Log.e("abc", "writing");
                                String privateDir = MainActivity.this.getExternalFilesDir(null).getAbsolutePath();
                                File newFile = new File(privateDir, filename);
                                FileWriter fw = new FileWriter(newFile);
                                for (int i = 0; i < results.size(); i++) {
                                    fw.write(results.get(i)+"\n");
                                }
                                results.clear();
                                fw.flush();
                                fw.close();
                            }catch (IOException e) {
                                Log.e("abc", "error writing file exception=" + e.getMessage());
                            }
                        }
                    }

                    Log.d("abc", d.toString());
                }
            });
        } catch (Exception e) {
            Log.e("abc", "error writing file exception:" + e.getMessage());
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
