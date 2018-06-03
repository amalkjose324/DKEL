package com.deepika.keralaelectionlive;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {
    private SharedPreferences prefs;

    public Session(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setSelectedDomain(Integer domain_id) {
        prefs.edit().putInt("domain_id", domain_id).apply();
    }

    public Integer getSelectedDomain() {
        int domain_id = prefs.getInt("domain_id",0);
        return domain_id;
    }
}
