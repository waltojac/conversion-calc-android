package waltojac.conversioncalc;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

public class ConversionApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
    }
}
