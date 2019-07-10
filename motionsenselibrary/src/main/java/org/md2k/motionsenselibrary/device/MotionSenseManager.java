package org.md2k.motionsenselibrary.device;

import android.content.Context;
import android.os.ParcelUuid;
import android.util.Log;

import androidx.annotation.NonNull;

import com.polidea.rxandroidble2.LogConstants;
import com.polidea.rxandroidble2.LogOptions;
import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.RxBleConnection;
import com.polidea.rxandroidble2.scan.ScanFilter;
import com.polidea.rxandroidble2.scan.ScanResult;
import com.polidea.rxandroidble2.scan.ScanSettings;

import org.md2k.motionsenselibrary.MSConstants;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

public class MotionSenseManager {
    private static final String SERVICE_CHARACTERISTIC = "0000180f-0000-1000-8000-00805f9b34fb";
    private static final String VERSION_CHARACTERISTIC = "da39d600-1d81-48e2-9c68-d0ae4bbd351f";
    private static MotionSenseManager instance;
    private RxBleClient rxBleClient;
    private ArrayList<Device> devices;
    private Disposable scanSubscription;

    public static void init(@NonNull Context context) {
        if (instance == null) {
            instance = new MotionSenseManager(context);
        }
    }

    private MotionSenseManager(Context context) {
        devices = new ArrayList<>();
        rxBleClient = RxBleClient.create(context);
        RxBleClient.updateLogOptions(new LogOptions.Builder().setLogLevel(LogConstants.DEBUG).setMacAddressLogSetting(LogConstants.MAC_ADDRESS_FULL).build());
        RxJavaPlugins.setErrorHandler(e -> {
            if (e instanceof UndeliverableException) {
                e = e.getCause();
            }
/*
            if ((e instanceof IOException) || (e instanceof SocketException)) {
                // fine, irrelevant network problem or API that throws on cancellation
                return;
            }
            if (e instanceof InterruptedException) {
                // fine, some blocking code was interrupted by a dispose call
                return;
            }
            if ((e instanceof NullPointerException) || (e instanceof IllegalArgumentException)) {
                // that's likely a bug in the application
                Thread.currentThread().getUncaughtExceptionHandler()
                        .handleException(Thread.currentThread(), e);
                return;
            }
            if (e instanceof IllegalStateException) {
                // that's a bug in RxJava or in a custom operator
                Thread.currentThread().getUncaughtExceptionHandler()
                        .handleException(Thread.currentThread(), e);
                return;
            }
*/
            if(MSConstants.DEBUG) Log.e("error","Undeliverable exception received, not sure what to do"+e.getMessage());
        });
    }

    public static Device addDevice(@NonNull DeviceInfo deviceInfo, @NonNull DeviceSettings deviceSettings) {
        for (int i = 0; i < instance.devices.size(); i++) {
            if (deviceInfo.getDeviceId().equals(instance.devices.get(i).deviceInfo.getDeviceId())) {
                return instance.devices.get(i);
            }
        }
        Device d = Device.create(instance.rxBleClient, deviceInfo, deviceSettings);
        instance.devices.add(d);
        return d;
    }

    public static Device getDevice(@NonNull String deviceId) {
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

    public static void removeDevice(@NonNull String deviceId) {
        int index = -1;
        for (int i = 0; i < instance.devices.size(); i++) {
            if (deviceId.equals(instance.devices.get(i).deviceInfo.getDeviceId())) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            instance.devices.get(index).stopAll();
            instance.devices.remove(index);
        }
    }

    public static void startScan(@NonNull ScanCallback scanCallback) {
        HashSet<String> deviceAddresses = new HashSet<>();
        instance.scanSubscription = Observable.timer(1, TimeUnit.SECONDS)
                .flatMap((Function<Long, ObservableSource<ScanResult>>) aLong -> instance.rxBleClient.scanBleDevices(new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build(), new ScanFilter.Builder().setServiceUuid(ParcelUuid.fromString(SERVICE_CHARACTERISTIC)).build()))
                .filter(scanResult -> !deviceAddresses.contains(scanResult.getBleDevice().getMacAddress()))
                .map(scanResult -> {
                    deviceAddresses.add(scanResult.getBleDevice().getMacAddress());
                    if(MSConstants.DEBUG) Log.d("scan", "deviceId=" + scanResult.getBleDevice().getMacAddress()+" deviceName="+scanResult.getBleDevice().getName());
                    return scanResult;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(scanResult -> scanCallback.onReceive(scanResult.getBleDevice().getName(), scanResult.getBleDevice().getMacAddress()), throwable -> Log.e("abc", "Error: " + throwable.getMessage())
                );
    }
    public static void getDeviceVersion(String deviceName, String deviceId, VersionCallback versionCallback){
        Disposable d = instance.getVersion(instance.rxBleClient, deviceName, deviceId).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Version>() {
            @Override
            public void accept(Version version) {
                versionCallback.onReceive(version);

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                versionCallback.onReceive(null);
            }
        });
    }

    public static void stopScan() {
        if (instance.scanSubscription != null && !instance.scanSubscription.isDisposed())
            instance.scanSubscription.dispose();
        instance.scanSubscription = null;
    }

    public static ArrayList<Device> getDevices() {
        return instance.devices;
    }

    private Observable<Version> getVersion(RxBleClient rxBleClient, String deviceName, String deviceId) {
        if(deviceName.equals("MotionSense2")) {
            return rxBleClient.getBleDevice(deviceId).establishConnection(false).observeOn(Schedulers.newThread())
                    .flatMapSingle(rxBleConnection -> rxBleConnection
                    .readCharacteristic(UUID.fromString(VERSION_CHARACTERISTIC)))
                    .map(bytes -> {
                        if (bytes == null || bytes.length == 0) {
                            if(MSConstants.DEBUG) Log.e("error", "version byte = " + bytes);
                            throw new Exception("abc");
                        } else return new Version(bytes);
//                    return new DeviceInfo(version.getType(), deviceId, deviceName, version);
                    }).retry(4).take(1);
        }else{
            return Observable.just(true).map(aBoolean -> {
                Version version;
                    switch (deviceName) {
                        case "EETech_Motion":
                            version = new Version(DeviceType.MOTION_SENSE, 1, 0, 0);
                            break;
                        case "MotionSenseHRV":
                            version = new Version(DeviceType.MOTION_SENSE_HRV, 1, 0, 0);
                            break;
                        case "MotionSenseHRV+":
                            version = new Version(DeviceType.MOTION_SENSE_HRV_PLUS, 1, 0, 0);
                            break;
                        default:
                            version = new Version(null, 1, 0, 0);
                }
                return version;
            });

        }

    }

}
