package com.deepika.keralaelectionlive.DatabaseResources;

public class DataCandidates {
    int candidate_id;
    String candidate_name;
    String candidate_image;
    int candidate_domain;
    int candidate_party;

    public DataCandidates(int candidate_id, String candidate_name, String candidate_image, int candidate_domain, int candidate_party) {
        this.candidate_id = candidate_id;
        this.candidate_name = candidate_name;
        this.candidate_image = candidate_image;
        this.candidate_domain = candidate_domain;
        this.candidate_party = candidate_party;
    }

    public DataCandidates() {
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

    public String getCandidate_image() {
        return candidate_image;
    }

    public void setCandidate_image(String candidate_image) {
        this.candidate_image = candidate_image;
    }

    public int getCandidate_domain() {
        return candidate_domain;
    }

    public void setCandidate_domain(int candidate_domain) {
        this.candidate_domain = candidate_domain;
    }

    public int getCandidate_party() {
        return candidate_party;
    }

    public void setCandidate_party(int candidate_party) {
        this.candidate_party = candidate_party;
    }
}
