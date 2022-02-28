package app.web.studia_kr.offline;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class OfflineManager {
    public SharedPreferences offlineDatabase;

    public void setOfflineDatabase(SharedPreferences offlineDatabase) {
        this.offlineDatabase = offlineDatabase;
    }

    public SharedPreferences getOfflineDatabase() {
        return offlineDatabase;
    }

    public boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null) {
            return true;
        }
        else {
            return false;
        }
    }

    public void addToOfflineDatabase(Calendar calendar, final String TYPE, String subject, String content) {
        try {
            String offlineDBString = offlineDatabase.getString("json", null);
            JSONObject offlineDBJson = new JSONObject(offlineDBString);

            SimpleDateFormat databaseFormat = new SimpleDateFormat("yyyyMMdd");
            String targetDate = databaseFormat.format(calendar);

            JSONObject getTargetDB = offlineDBJson.getJSONObject(targetDate);

            SharedPreferences.Editor editor = offlineDatabase.edit();

            if (TYPE.equals("NOTE")) {
                JSONObject offlineDBObject = new JSONObject();
                offlineDBObject.put("subject", subject);
                offlineDBObject.put("content", content);
            } else if (TYPE.equals("MEMO")) {
                if (!getTargetDB.equals(null)) {
                    getTargetDB.remove("memo");
                    getTargetDB.put("memo", content);
                }

                //에이 누가 알아서 해주겠지
            } else if (TYPE.equals("REMINDER")) {

            } else {
                throw new Exception("TYPENotFoundException");
            }

        } catch(NullPointerException e) {
            Log.e("OfflineManager", "Error occurred while executing void addToOfflineDatabase because one of the objects in OfflineManager was null");
            e.printStackTrace();
        } catch (JSONException e) {
            Log.e("OfflineManager", "Error occurred while executing void addToOfflineDatabase because of the unknown JSONException");
            e.printStackTrace();
        } catch (Exception e) {
            Log.e("OfflineManager", "Error occurred while executing void addToOfflineDatabase because it failed to make Exception");
            e.printStackTrace();
        }
    }

    public void removeFromOfflineDatabase(Calendar calendar, final int targetNumber) {

    }

    public void resetOfflineDatabase() {

    }

    public void uploadOfflineDatabase() {
        
    }
}
