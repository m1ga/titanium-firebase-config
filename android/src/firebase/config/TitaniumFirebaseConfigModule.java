/**
 * This file was auto-generated by the Titanium Module SDK helper for Android
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2018 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */
package firebase.config;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.ConfigUpdate;
import com.google.firebase.remoteconfig.ConfigUpdateListener;
import com.google.firebase.remoteconfig.ConfigUpdateListenerRegistration;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollFunction;
import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.util.TiConvert;
import org.appcelerator.titanium.util.TiRHelper;

import java.util.HashMap;
import java.util.Set;

@Kroll.module(name = "TitaniumFirebaseConfig", id = "firebase.config")
public class TitaniumFirebaseConfigModule extends KrollModule {
    public static final String PROPERTY_SUCCESS = "success";
    public static final String PROPERTY_ERROR = "error";
    public static final String PROPERTY_RESULT = "result";
	ConfigUpdateListenerRegistration culr;
    // Methods

    @Kroll.method
    public void fetch(KrollDict params) {
        final KrollFunction callback = (KrollFunction) params.get("callback");
        int expirationDuration = params.optInt("expirationDuration", 43200); //Using the default minimum fetch interval if no duration is informed

        FirebaseRemoteConfig.getInstance().fetch(expirationDuration).addOnCompleteListener(task -> {
            if (callback != null) {
                KrollDict result = new KrollDict();
                if (task.isSuccessful()) {
                    result.put(PROPERTY_SUCCESS, true);
                    result.put(PROPERTY_RESULT, task.getResult());
                } else {
                    result.put(PROPERTY_SUCCESS, false);
                    if (task.getException() != null) {
                        result.put(PROPERTY_ERROR, task.getException().getMessage());
                    }
                }
                callback.callAsync(getKrollObject(), result);
            }
        });
    }

    @Kroll.method
    public void fetchAndActivate(KrollFunction callback) {
        FirebaseRemoteConfig.getInstance().fetchAndActivate().addOnCompleteListener(task -> {
            if (callback != null) {
                KrollDict result = new KrollDict();
                if (task.isSuccessful()) {
                    result.put(PROPERTY_SUCCESS, true);
                    result.put(PROPERTY_RESULT, task.getResult());
                } else {
                    result.put(PROPERTY_SUCCESS, false);
                    if (task.getException() != null) {
                        result.put(PROPERTY_ERROR, task.getException().getMessage());
                    }
                }
                callback.callAsync(getKrollObject(), result);
            }
        });
    }

    @Kroll.method
    public void setMinimumFetchIntervalInSeconds(int minimumFetchInterval) {
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(minimumFetchInterval)
                .build();
        FirebaseRemoteConfig.getInstance().setConfigSettingsAsync(configSettings);
    }

    @Kroll.method
    public void setDefaults(Object params) throws Exception {
        if (params instanceof String) {
            FirebaseRemoteConfig.getInstance().setDefaultsAsync(TiRHelper.getResource("xml." + TiConvert.toString(params)));
        } else if (params instanceof HashMap) {
            FirebaseRemoteConfig.getInstance().setDefaultsAsync((HashMap) params);
        } else {
            throw new Exception("Invalid defaults provided. Please either pass a dictionary or string");
        }
    }

    @Kroll.method
    public void activateFetched(KrollFunction callback) {
        FirebaseRemoteConfig.getInstance().activate().addOnCompleteListener(task -> {
            if (callback != null) {
                KrollDict result = new KrollDict();
                if (task.isSuccessful()) {
                    result.put(PROPERTY_SUCCESS, true);
                    result.put(PROPERTY_RESULT, task.getResult());
                } else {
                    result.put(PROPERTY_SUCCESS, false);
                    if (task.getException() != null) {
                        result.put(PROPERTY_ERROR, task.getException().getMessage());
                    }
                }
                callback.callAsync(getKrollObject(), result);
            }
        });
    }

    @Kroll.method
    public KrollDict configValueForKey(String key) {
        String stringValue = FirebaseRemoteConfig.getInstance().getString(key);
        boolean boolValue = FirebaseRemoteConfig.getInstance().getBoolean(key);
        double numberValue = FirebaseRemoteConfig.getInstance().getDouble(key);

        KrollDict result = new KrollDict();
        result.put("bool", boolValue);
        result.put("number", numberValue);

        if (stringValue != null) {
            result.put("string", stringValue);
        }

        return result;
    }

    @Kroll.setProperty
    public void enableRealtimeUpdates(Boolean value) {
        if (value) {
			culr = FirebaseRemoteConfig.getInstance().addOnConfigUpdateListener(new ConfigUpdateListener() {
                @Override
                public void onUpdate(ConfigUpdate configUpdate) {
                    Set<String> keys = configUpdate.getUpdatedKeys();
                    KrollDict result = new KrollDict();
                    result.put("keys", keys);
                    fireEvent("update", result);
                    FirebaseRemoteConfig.getInstance().activate().addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                        }
                    });
                }

                @Override
                public void onError(FirebaseRemoteConfigException error) {
                    Log.e("FirebaseConfig", error.getMessage());
                }
            });
        } else if (culr != null) {
			culr.remove();
		}
    }

    @Kroll.method
    public String getString(String key) {
        return FirebaseRemoteConfig.getInstance().getString(key);
    }

    @Kroll.method
    public boolean getBool(String key) {
        return FirebaseRemoteConfig.getInstance().getBoolean(key);
    }

    @Kroll.method
    public double getNumber(String key) {
        return FirebaseRemoteConfig.getInstance().getDouble(key);
    }

    @Kroll.method
    public String getData(String key) {
        return null; // TODO: Implement once available
    }
}
