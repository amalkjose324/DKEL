package com.deepika.keralaelectionlive;

public class DataConfig {

    String  config_key;
    String config_value;

    public DataConfig(String config_key, String config_value) {
        this.config_key = config_key;
        this.config_value = config_value;
    }

    public DataConfig() {
    }

    public String getConfig_key() {
        return config_key;
    }

    public void setConfig_key(String config_key) {
        this.config_key = config_key;
    }

    public String getConfig_value() {
        return config_value;
    }

    public void setConfig_value(String config_value) {
        this.config_value = config_value;
    }
}
