package ru.kudryaeva.subscriptions.api.dto;

public class TotalResponse {

    private int total;

    public TotalResponse() {
    }

    public TotalResponse(int total) {
        this.total = total;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
