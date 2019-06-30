package org.md2k.motionsenselibrary.device;

import android.content.Context;
import android.util.Log;

import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.scan.ScanResult;
import com.polidea.rxandroidble2.scan.ScanSettings;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

public class MotionSenseManager {
    private static MotionSenseManager instance;
    private RxBleClient rxBleClient;
    private ArrayList<Device> devices;
    private Disposable scanSubscription;

    public static void init(Context context) {
        if (instance == null) {
            instance = new MotionSenseManager(context);
        }
    }

    private MotionSenseManager(Context context) {
        devices = new ArrayList<>();
        rxBleClient = RxBleClient.create(context);
    }

    public static Device addDevice(DeviceInfo deviceInfo, DeviceSettings deviceSettings) {
        for (int i = 0; i < instance.devices.size(); i++) {
            if (deviceInfo.getDeviceId().equals(instance.devices.get(i).deviceInfo.getDeviceId())) {
                instance.devices.get(i).stopAll();
                instance.devices.get(i).deviceSettings = deviceSettings;
                return instance.devices.get(i);
            }
        }
        Device d = Device.create(instance.rxBleClient, deviceInfo, deviceSettings);
        instance.devices.add(d);
        return d;
    }

    public static Device getDevice(String deviceId) {
        for (int i = 0; i < instance.devices.size(); i++) {
            if (instance.devices.get(i).deviceInfo.getDeviceId().equals(deviceId))
                return instance.devices.get(i);
        }
        return null;
    }

    public static int getDeviceNo() {
        return instance.devices.size();
    }

    public static void removeDevices() {
        for (int i = 0; i < instance.devices.size(); i++) {
            instance.devices.get(i).stopAll();
        }
        instance.devices.clear();
    }

    public static void removeDevice(String deviceId) {
        int index = -1;
        for (int i = 0; i < instance.devices.size(); i++) {
            if (deviceId.equals(instance.devices.get(i).deviceInfo.getDeviceId())) {
                index = i;
            }
        }
        if (index != -1) {
            instance.devices.get(index).stopAll();
            instance.devices.remove(index);
        }
    }

    public static void startScan(ScanCallback scanCallback) {
        ArrayList<String> deviceAddresses = new ArrayList<>();
        instance.scanSubscription = Observable.timer(1, TimeUnit.SECONDS).flatMap((Function<Long, ObservableSource<ScanResult>>) aLong -> instance.rxBleClient.scanBleDevices(
                new ScanSettings.Builder().build())).filter(scanResult -> {
            Log.d("scan", "deviceId=" + scanResult.getBleDevice().getMacAddress());
            for (int i = 0; i < deviceAddresses.size(); i++) {
                if (deviceAddresses.get(i).equals(scanResult.getBleDevice().getMacAddress()))
                    return false;
            }
            return true;
        }).subscribe(
                scanResult -> {
                    deviceAddresses.add(scanResult.getBleDevice().getMacAddress());
                    DeviceInfo deviceInfo = Device.getDeviceInfo(instance.rxBleClient, scanResult);
                    if (deviceInfo != null) {
                        scanCallback.onReceive(deviceInfo);
                    }
                },
                throwable -> {
                    Log.e("abc", "Error: " + throwable.getMessage());
                    // Handle an error here.
                }
        );
    }

    public static void stopScan() {
        if (instance.scanSubscription != null)
            instance.scanSubscription.dispose();
        instance.scanSubscription = null;
    }

    public static ArrayList<Device> getDevices() {
        return instance.devices;
    }
}
