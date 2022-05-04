package com.example.jobagapi.resource;

public class SavePostulantJobResource {

    private boolean aceppt;

    public SavePostulantJobResource(boolean aceppt) {
        this.aceppt = aceppt;
    }

    public SavePostulantJobResource() {

    }

    public boolean isAceppt() {
        return aceppt;
    }

    public SavePostulantJobResource setAceppt(boolean aceppt) {
        this.aceppt = aceppt;
        return this;
    }
}
