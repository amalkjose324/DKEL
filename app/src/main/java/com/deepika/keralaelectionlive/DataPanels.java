package com.deepika.keralaelectionlive;

public class DataPanels {
    int panel_id;
    String panel_name;

    public DataPanels(int panel_id, String panel_name) {
        this.panel_id = panel_id;
        this.panel_name = panel_name;
    }

    public DataPanels() {
    }

    public int getPanel_id() {
        return panel_id;
    }

    public void setPanel_id(int panel_id) {
        this.panel_id = panel_id;
    }

    public String getPanel_name() {
        return panel_name;
    }

    public void setPanel_name(String panel_name) {
        this.panel_name = panel_name;
    }
}
