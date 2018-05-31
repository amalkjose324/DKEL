package com.deepika.keralaelectionlive;

public class CandidateDetails {
    int candidate_id;
    String candidate_name;
    String candidate_domain;
    String candidate_party;
    String candidate_panel;
    String candidate_image;

    public CandidateDetails(int candidate_id, String candidate_name, String candidate_domain, String candidate_party, String candidate_panel, String candidate_image) {
        this.candidate_id = candidate_id;
        this.candidate_name = candidate_name;
        this.candidate_domain = candidate_domain;
        this.candidate_party = candidate_party;
        this.candidate_panel = candidate_panel;
        this.candidate_image = candidate_image;
    }

    public CandidateDetails() {
    }

    public int getCandidate_id() {
        return candidate_id;
    }

    public void setCandidate_id(int candidate_id) {
        this.candidate_id = candidate_id;
    }

    public String getCandidate_name() {
        return candidate_name;
    }

    public void setCandidate_name(String candidate_name) {
        this.candidate_name = candidate_name;
    }

    public String getcandidate_domain() {
        return candidate_domain;
    }

    public void setcandidate_domain(String candidate_domain) {
        this.candidate_domain = candidate_domain;
    }

    public String getCandidate_party() {
        return candidate_party;
    }

    public void setCandidate_party(String candidate_party) {
        this.candidate_party = candidate_party;
    }

    public String getcandidate_panel() {
        return candidate_panel;
    }

    public void setcandidate_panel(String candidate_panel) {
        this.candidate_panel = candidate_panel;
    }

    public String getCandidate_image() {
        return candidate_image;
    }

    public void setCandidate_image(String candidate_image) {
        this.candidate_image = candidate_image;
    }
}
