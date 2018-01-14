package io.smallant.sunorrain.helpers;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * Created by Jonathan on 12/06/2016.
 */
public class JsonController {
    Context context;

    public JsonController(Context context) {
        this.context = context;
    }

    private String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = context.getAssets().open("timezone.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public String getTimeZone(String countryCode) {
        String valueTimeZone = "";
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONObject m_jArry = obj.getJSONObject("countries");
            Iterator<String> iter = m_jArry.keys();

            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    JSONObject value = m_jArry.getJSONObject(key);
                    if (value.getString("abbr").equals(countryCode)) {
                        valueTimeZone = value.getJSONArray("zones").get(0).toString();
                        break;
                    }
                } catch (JSONException e) {
                    // Something went wrong!
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return valueTimeZone;
    }
}
