package com.deepika.keralaelectionlive;

public class DataParties {
    int party_id;
    String party_name;
    String party_image;
    int party_panel;

    public DataParties(int party_id, String party_name, String party_image, int party_panel) {
        this.party_id = party_id;
        this.party_name = party_name;
        this.party_image = party_image;
        this.party_panel = party_panel;
    }

    public DataParties() {
    }

    public int getParty_id() {
        return party_id;
    }

    public void setParty_id(int party_id) {
        this.party_id = party_id;
    }

    public String getParty_name() {
        return party_name;
    }

    public void setParty_name(String party_name) {
        this.party_name = party_name;
    }

    public String getParty_image() {
        return party_image;
    }

    public void setParty_image(String party_image) {
        this.party_image = party_image;
    }

    public int getParty_panel() {
        return party_panel;
    }

    public void setParty_panel(int party_panel) {
        this.party_panel = party_panel;
    }
}
