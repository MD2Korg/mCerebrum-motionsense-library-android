package org.md2k.motionsenselibrary.plot;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.components.YAxis;

import org.md2k.motionsenselibrary.device.Data;
import org.md2k.motionsenselibrary.device.Device;
import org.md2k.motionsenselibrary.device.MotionSenseManager;
import org.md2k.motionsenselibrary.device.ReceiveCallback;
import org.md2k.motionsenselibrary.device.SensorInfo;
import org.md2k.motionsenselibrary.device.SensorType;

public class ActivityPlot extends RealtimeLineChartActivity {
    Device device;
    SensorInfo sensorInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

            String deviceId = getIntent().getStringExtra("deviceId");
            SensorType sensorType = SensorType.valueOf(getIntent().getStringExtra("sensorType"));
            device = MotionSenseManager.getDevice(deviceId);
            assert device != null;
            sensorInfo = device.getSensorInfo(sensorType);
            YAxis leftAxis = mChart.getAxisLeft();
            leftAxis.setAxisMinimum(sensorInfo.getMinValue());
            leftAxis.setAxisMaximum(sensorInfo.getMaxValue());
        } catch (Exception e) {
            finish();
        }
    }

    void updatePlot(Data data) {
        getmChart().getDescription().setText(sensorInfo.getTitle());
        getmChart().getDescription().setPosition(1f, 1f);
        getmChart().getDescription().setEnabled(true);
        getmChart().getDescription().setTextColor(Color.WHITE);
        double[] samples = data.getSample();
        float[] sampleTemp = new float[samples.length];
        for (int i = 0; i < samples.length; i++) {
            sampleTemp[i] = (float) samples[i];
        }
        addEntry(sampleTemp, sensorInfo.getFields(), 400);
    }

    @Override
    public void onResume() {

        device.start(receiveCallback);
        super.onResume();
    }

    @Override
    public void onPause() {
        device.stop(receiveCallback);
        super.onPause();

    }

    private ReceiveCallback receiveCallback = new ReceiveCallback() {
        @Override
        public void onReceive(Data d) {
            if (d.getSensorType() == sensorInfo.getSensorType()) {
                updatePlot(d);
            }
        }
    };

}
