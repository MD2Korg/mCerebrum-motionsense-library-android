package org.md2k.motionsenselibrary.device;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Timed;

/*
 * Copyright (c) 2016, The University of Memphis, MD2K Center
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
public class DataQualityPPG extends DataQuality {
    private ArrayList<Data> samples1=new ArrayList<>();
public static final int DELAY = 3000;

    private boolean[] isGood3Sec(ArrayList<Data> values) {
        double[] sum = new double[]{0, 0, 0};
        boolean[] res = new boolean[6];
        int[] count = new int[3];
        for (int i = 0; i < values.size(); i++) {
            double[] samples = values.get(i).getSample();
            sum[0] += samples[0];
            sum[1] += samples[1];
            sum[2] += samples[2];
            if (samples[0] < 30000 || samples[0] > 170000) {
                count[0]++;
            }
            if (samples[1] < 140000 || samples[1] > 230000) {
                count[1]++;
            }
            if (samples[2] < 3000 || samples[2] > 20000) {
                count[2]++;
            }
        }
        res[0] = count[0] < (int) (.34 * values.size());
        res[1] = count[1] < (int) (.34 * values.size());
        res[2] = count[2] < (int) (.34 * values.size());
//        Log.d("data_quality_led","last 3 quality="+res[0]+" "+res[1]+" "+res[2]);
        return res;
    }

    private int[] getMean(ArrayList<Data> values) {
        int[] sum = new int[]{0,0,0};
        for (int i = 0; i < values.size(); i++) {
            double[] samples = values.get(i).getSample();
            sum[0] += samples[0];
            sum[1] += samples[1];
            sum[2] += samples[2];
        }
        for (int i = 0; i < 3; i++) {
            sum[i] = sum[i] / values.size();
        }
        return sum;

    }

    private ArrayList<Data> getLast3Sec(ArrayList<Data> samples) {
        long curTime = System.currentTimeMillis();
        ArrayList<Data> l = new ArrayList<>();
        for (int i = 0; i < samples.size(); i++) {
            if (curTime - samples.get(i).getTimestamp() <= 3000)
                l.add(samples.get(i));
        }
        return l;
    }
    @Override
    protected void addSample(Data data){
        if(data.getSensorType()==SensorType.PPG) {
            samples1.add(data);
        }
    }

    private double[] getSample(int index) {
        double[] d = new double[samples1.size()];
        for (int i = 0; i < samples1.size(); i++) {
            d[i] = ((double[]) samples1.get(i).getSample())[index];
        }
        return d;
    }

    @Override
    protected Observable<Data> getObservable(){
        return Observable.interval(DELAY, TimeUnit.MILLISECONDS).timeInterval().map(new Function<Timed<Long>, Data>() {
            @Override
            public Data apply(Timed<Long> longTimed) throws Exception {
                DataQualityType dataQualityType = getStatus();
                return new Data(SensorType.PPG_DATA_QUALITY, System.currentTimeMillis(), new double[]{dataQualityType.getValue()});
            }
        });
    }

    private synchronized DataQualityType getStatus() {
        try {
            long curTime = System.currentTimeMillis();
            Iterator<Data> i = samples1.iterator();
            while (i.hasNext()) {
                if (curTime - i.next().getTimestamp() >= 8000)
                    i.remove();
            }

            ArrayList<Data> last3Sec = getLast3Sec(samples1);
//            Log.d("data_quality_led","last 3="+last3Sec.size());
            if (last3Sec.size() == 0) return DataQualityType.NO_DATA;

            boolean[] sec3mean = isGood3Sec(samples1);
            if (!sec3mean[0] && !sec3mean[1] && !sec3mean[2]) return DataQualityType.NOT_WORN;

            int[] mean = getMean(samples1);

            if (mean[0] < 5000 && mean[1] < 5000 && mean[2] < 5000) return DataQualityType.NOT_WORN;

            boolean check = mean[0] > mean[2] && mean[1] > mean[0] && mean[1] > mean[2];
//            Log.d("data_quality_led_mean1",""+check);
            if (!check) return DataQualityType.LOOSE_ATTACHMENT;

            int diff;
            if (mean[0] > 140000) {
                diff = 15000;
            } else {
                diff = 50000;
            }
            boolean check1 = mean[0] - mean[2] > 50000 && mean[1] - mean[0] > diff;
//            Log.d("data_quality_led_mean2",""+check1);
            if (!check1) return DataQualityType.LOOSE_ATTACHMENT;

            if (sec3mean[0] && new Bandpass(getSample(0)).getResult()) {
                return DataQualityType.GOOD;
            }
            if (sec3mean[1] && new Bandpass(getSample(1)).getResult()) {
                return DataQualityType.GOOD;
            }
            if (sec3mean[2] && new Bandpass(getSample(2)).getResult()) {

                return DataQualityType.GOOD;
            }

            return DataQualityType.NOT_WORN;

        } catch (Exception e) {
            return DataQualityType.GOOD;
        }
    }
}