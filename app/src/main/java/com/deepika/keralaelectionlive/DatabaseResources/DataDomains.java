package com.deepika.keralaelectionlive.DatabaseResources;

public class DataDomains {
    int domain_id;
    String domain_name;
    int domain_status;

    public DataDomains(int domain_id, String domain_name, int domain_status) {
        this.domain_id = domain_id;
        this.domain_name = domain_name;
        this.domain_status = domain_status;
    }

    public DataDomains() {
    }

    public int getDomain_id() {
        return domain_id;
    }

    public void setDomain_id(int domain_id) {
        this.domain_id = domain_id;
    }

    public String getDomain_name() {
        return domain_name;
    }

    public void setDomain_name(String domain_name) {
        this.domain_name = domain_name;
    }

    public int getDomain_status() {
        return domain_status;
    }

    public void setDomain_status(int domain_status) {
        this.domain_status = domain_status;
    }
}
