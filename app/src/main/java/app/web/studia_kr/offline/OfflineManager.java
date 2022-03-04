//Studia Offline Manager

package app.web.studia_kr.offline;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class OfflineManager {
    public SharedPreferences offlineDatabase;
    public ArrayList<String> jsonDateList = new ArrayList<String>();
    public DatabaseReference userReference;

    public void setOfflineDatabase(SharedPreferences offlineDatabase) {
        this.offlineDatabase = offlineDatabase;
    }

    public SharedPreferences getOfflineDatabase() {
        return offlineDatabase;
    }

    public DatabaseReference getUserReference() {
        return userReference;
    }

    public void setUserReference(DatabaseReference userReference) {
        this.userReference = userReference;
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
            boolean isTargetDBNull;

            if (getTargetDB != null) {
                isTargetDBNull = false;
            } else {
                isTargetDBNull = true;
            }

            SharedPreferences.Editor editor = offlineDatabase.edit();

            if (TYPE.equals("note")) { //TYPE equals note
                JSONObject offlineDBObject = new JSONObject(); //subject와 content를 넣을 JSONObject
                offlineDBObject.put("subject", subject);
                offlineDBObject.put("content", content);

                JSONObject noteObject = getTargetDB.getJSONObject("note"); //Target된 날짜의 note를 불러오는 JSONObject
                boolean isNoteObjectNull;

                if (noteObject != null) { //isNoteObjectNull 결정
                    isNoteObjectNull = false;
                } else {
                    isNoteObjectNull = true;
                }

                if (isNoteObjectNull) {
                    noteObject.put("0", offlineDBObject);
                    getTargetDB.put("note", noteObject);

                    offlineDBJson.put(targetDate, getTargetDB);
                } else {
                    int i = 0;
                    while (noteObject.get(Integer.toString(i)) != null) {
                        i++;
                    }

                    noteObject.put(Integer.toString(i), offlineDBObject);
                    getTargetDB.put("note", noteObject);

                    offlineDBJson.remove(targetDate);
                    offlineDBJson.put(targetDate, getTargetDB);
                }

                editor.remove("json");
                editor.putString("json", offlineDBJson.toString());
                editor.commit();
            } else if (TYPE.equals("memo")) {
                if (isTargetDBNull) {
                    getTargetDB.put("memo", content);
                } else {
                    getTargetDB.remove("memo");
                    getTargetDB.put("memo", content);

                    offlineDBJson.remove(targetDate);
                }

                offlineDBJson.put(targetDate, getTargetDB);

                editor.remove("json");
                editor.putString("json", offlineDBJson.toString());
                editor.commit();
            } else if (TYPE.equals("reminder")) {
                JSONObject reminderObject = getTargetDB.getJSONObject("reminder");
                boolean isReminderObjectNull;

                if (reminderObject != null) {
                    isReminderObjectNull = false;
                } else {
                    isReminderObjectNull = true;
                }

                if (isReminderObjectNull) {
                    reminderObject.put("0", content);
                    getTargetDB.put("reminder", reminderObject);

                    offlineDBJson.put(targetDate, getTargetDB);
                } else {
                    int i = 0;
                    while (reminderObject.get(Integer.toString(i)) != null) {
                        i++;
                    }

                    reminderObject.put(Integer.toString(i), content);
                    getTargetDB.put("reminder", reminderObject);

                    offlineDBJson.remove(targetDate);
                    offlineDBJson.put(targetDate, getTargetDB);
                }

                editor.remove("json");
                editor.putString("json", offlineDBJson.toString());
                editor.commit();
            } else {
                throw new Exception("TYPENotFoundException");
            }

            Log.d("OfflineManager", "Successfully finished void addToOfflineDatabase");
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

    public void removeFromOfflineDatabase(Calendar calendar, final String TYPE, final int targetNumber) {
        try {
            String offlineDBString = offlineDatabase.getString("json", null);
            JSONObject offlineDBJson = new JSONObject(offlineDBString);

            SimpleDateFormat databaseFormat = new SimpleDateFormat("yyyyMMdd");
            String targetDate = databaseFormat.format(calendar);

            JSONObject getTargetDB = offlineDBJson.getJSONObject(targetDate);
            boolean isTargetDBNull;

            if (getTargetDB != null) {
                isTargetDBNull = false;
            } else {
                isTargetDBNull = true;
                return;
            }

            SharedPreferences.Editor editor = offlineDatabase.edit();

            if (TYPE.equals("note")) {
                JSONObject noteObject = getTargetDB.getJSONObject("note");
                boolean isNoteObjectNull;

                if (noteObject != null) {
                    isNoteObjectNull = false;
                } else {
                    isNoteObjectNull = true;
                    return;
                }

                JSONObject target = noteObject.getJSONObject(Integer.toString(targetNumber));
                boolean isTargetNull;

                if (target != null) {
                    isTargetNull = false;
                } else {
                    isTargetNull = true;
                    return;
                }

                int i = 0;
                while (noteObject.get(Integer.toString(i)) != null) {
                    i++;
                }

                if (i == targetNumber) {
                    noteObject.remove(Integer.toString(targetNumber));

                    getTargetDB.remove("note");
                    getTargetDB.put("note", noteObject);

                    offlineDBJson.remove(targetDate);
                    offlineDBJson.put(targetDate, getTargetDB);
                } else if (i > targetNumber) {
                    noteObject.remove(Integer.toString(targetNumber));

                    for (int k = targetNumber + 1; k<i; k++) {
                        String value = noteObject.get(Integer.toString(k)).toString();
                        noteObject.put(Integer.toString(i - 1), value);
                    }

                    getTargetDB.remove("note");
                    getTargetDB.put("note", noteObject);

                    offlineDBJson.remove(targetDate);
                    offlineDBJson.put(targetDate, getTargetDB);

                    editor.remove("json");
                    editor.putString("json", offlineDBJson.toString());
                    editor.commit();
                } else {
                    throw new Exception("TargetNumberNotExistsException");
                }
            } else if (TYPE.equals("memo")) {
                if (getTargetDB.getJSONObject("memo") != null) {
                    getTargetDB.remove("memo");

                    offlineDBJson.remove(targetDate);
                    offlineDBJson.put(targetDate, getTargetDB);

                    editor.remove("json");
                    editor.putString("json", offlineDBJson.toString());
                    editor.commit();
                }
            } else if (TYPE.equals("reminder")) {
                if (getTargetDB.getJSONObject("reminder").getJSONObject(targetDate) != null) {
                    int i = 0;
                    while (getTargetDB.getJSONObject("reminder").getJSONObject(Integer.toString(i)) != null) {
                        i++;
                    }

                    if (targetNumber == i) {
                        getTargetDB.getJSONObject("reminder").remove(Integer.toString(i));
                    } else {
                        for (int k = targetNumber + 1; k<i; k++) {
                            String value = getTargetDB.getJSONObject("reminder").get(Integer.toString(k)).toString();
                            getTargetDB.getJSONObject("reminder").remove(Integer.toString(k));
                            getTargetDB.getJSONObject("reminder").put(Integer.toString(k - 1), value);
                        }
                    }

                    offlineDBJson.remove(targetDate);
                    offlineDBJson.put(targetDate, getTargetDB);

                    editor.remove("json");
                    editor.putString("json", offlineDBJson.toString());
                    editor.commit();
                }
            } else {
                throw new Exception("TYPENotFoundException");
            }
        } catch (NullPointerException e) {
            Log.e("OfflineManager", "Error occurred while executing void removeFromOfflineDatabase because one of the objects in OfflineManager was null");
            e.printStackTrace();
        } catch (JSONException e) {
            Log.e("OfflineManager", "Error occurred while executing void removeFromOfflineDatabase because of the unknown JSONException");
            e.printStackTrace();
        } catch (Exception e) {
            Log.e("OfflineManager", "Error occurred while executing void removeFromOfflineDatabase because it failed to make Exception");
            e.printStackTrace();
        }
    }

    public void resetOfflineDatabase() {
        SharedPreferences.Editor editor = offlineDatabase.edit();
        editor.remove("json");
        editor.commit();
    }

    public void uploadOfflineDatabase() {
        try {
            String offlineDatabaseString = offlineDatabase.getString("json", null);
            JSONObject offlineDBObject = new JSONObject(offlineDatabaseString);
            JSONArray dateArray = new JSONArray(offlineDBObject);

            DatabaseReference userReference = getUserReference();

            for (int i = 0; i<dateArray.length(); i++) {
                JSONObject target = dateArray.getJSONObject(i);
                String targetDate = dateArray.get(i).toString();

                if (target.getJSONObject("note") != null) {
                    int k = 0;
                    while (target.getJSONObject("note").getJSONObject(Integer.toString(k)) != null) {
                        userReference.child(targetDate).child("note").child(Integer.toString(k)).setValue(target.getJSONObject("note").get(Integer.toString(k)).toString());
                        k++;
                    }
                }
                if (target.getJSONObject("memo") != null) {
                    userReference.child(targetDate).child("memo").setValue(target.getJSONObject("memo").toString());
                }
                if (target.getJSONObject("reminder") != null) {
                    int l = 0;
                    while (target.getJSONObject("reminder").getJSONObject(Integer.toString(l)) != null) {
                        userReference.child(targetDate).child("reminder").child(Integer.toString(l)).setValue(target.getJSONObject("reminder").get(Integer.toString(l)).toString());
                        l++;
                    }
                }
            }

            Log.d("OfflineManager", "Finished offline database upload");
        } catch (JSONException e) {
            Log.e("OfflineManager", "Error occurred while executing void uploadOfflineDatabase because of the unknown JSONException");
            e.printStackTrace();
        } catch (NullPointerException e) {
            Log.e("OfflineManager", "Error occurred while executing void uploadOfflineDatabase because one of the objects in OfflineManager was null");
            e.printStackTrace();
        }
    }
}