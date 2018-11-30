package waltojac.conversioncalc.webservice;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * helper methods.
 */
public class WeatherService extends IntentService {
    private static final String TAG = "WeatherService";
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_WEATHER_AT = "cis.gvsu.edu.webservice.action.WEATHER_AT";
    private static final String BASE_URL = "https://api.darksky.net/forecast/fe5b079b23bf9ed2fef96a8fec1cf87a";
    public static final String BROADCAST_WEATHER = "cis.gvsu.edu.webservice.action.BROADCAST";
    private static final String EXTRA_KEY = "cis.gvsu.edu.webservice.extra.KEY";
    private static final String EXTRA_LAT = "cis.gvsu.edu.webservice.extra.LAT";
    private static final String EXTRA_LNG = "cis.gvsu.edu.webservice.extra.LNG";
    private static final String EXTRA_TIME = "cis.gvsu.edu.webservice.extra.TIME";

    public WeatherService() {
        super("WeatherService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startGetWeather(Context context, String lat, String lng, String key) {
        Intent intent = new Intent(context, WeatherService.class);
        intent.setAction(ACTION_WEATHER_AT);
        intent.putExtra(EXTRA_LAT, lat);
        intent.putExtra(EXTRA_LNG, lng);
        intent.putExtra(EXTRA_KEY, key);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_WEATHER_AT.equals(action)) {
                final String key = intent.getStringExtra(EXTRA_KEY);
                final String lat = intent.getStringExtra(EXTRA_LAT);
                final String lng = intent.getStringExtra(EXTRA_LNG);
                final String when = intent.getStringExtra(EXTRA_TIME);
                fetchWeatherData(key, lat, lng, when);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void fetchWeatherData(String key, String lat, String lon, String time) {
        try {
            // TODO: Format the url based on the input params
            URL url = new URL(BASE_URL+"/" + lat+ "," + lon);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5000 /* milliseconds */);
            conn.setConnectTimeout(10000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096];
                int len;
                while ((len = bis.read(buffer)) > 0) {
                    baos.write(buffer, 0, len);
                }
                JSONObject data = new JSONObject(new String(baos.toByteArray()));
                JSONObject current = data.getJSONObject("currently");

                // TODO: extract the values you need out of current
                double temp = (double) current.get("temperature");
                String sum = (String) current.get("summary");
                String icon = (String) current.get("icon");

                Intent result = new Intent(BROADCAST_WEATHER);

                // TODO: use putExtra to add the extracted values to your broadcast
                result.putExtra("TEMPERATURE", temp);
                result.putExtra("SUMMARY", sum);
                result.putExtra("ICON", icon);
                result.putExtra("KEY", key);
                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}