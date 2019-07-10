package org.md2k.motionsenselibrary.device.v2.motion_sense;


import com.polidea.rxandroidble2.RxBleConnection;

import org.md2k.motionsenselibrary.device.CharacteristicConfig;
import org.md2k.motionsenselibrary.device.DeviceSettings;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.Function;

/*
 * Copyright (c) 2016, The University of Memphis, MD2K Center
 * - Syed Monowar Hossain <monowar.hossain@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
class CharacteristicConfigMotionSense extends CharacteristicConfig {

    @Override
    protected Observable<RxBleConnection> setConfiguration(final RxBleConnection rxBleConnection, final DeviceSettings deviceSettings) {
        MotionSenseSettingsV2 s = (MotionSenseSettingsV2) deviceSettings;
        return setSensorEnableObservable(rxBleConnection, s.accelerometerEnable, s.gyroscopeEnable, false, false)
                .flatMap((Function<byte[], Single<byte[]>>) bytes -> {
                    if (s.accelerometerEnable || s.gyroscopeEnable)
                        return setSamplingRateObservable(rxBleConnection, s.motionRawFrequency, 0);
                    else return Single.just(new byte[0]);
                })
                .flatMap((Function<byte[], Single<byte[]>>) bytes -> {
                    if (s.gyroscopeEnable)
                        return setSensitivityObservable(rxBleConnection, s.accelerometerSensitivity,s.gyroscopeSensitivity);
                    else return Single.just(new byte[0]);
                })
                .flatMap((Function<byte[], Single<byte[]>>) bytes -> setMinimumConnectionInterval(rxBleConnection, s.minimumConnectionInterval)).flatMapObservable((Function<byte[], ObservableSource<RxBleConnection>>) bytes -> Observable.just(rxBleConnection));
    }
}
