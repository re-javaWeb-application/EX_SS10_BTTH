package com.re.btss10.model;

public enum RequestStatus {
    PENDING("Chờ chuẩn bị"),
    APPROVED("Đã duyệt"),
    RETURNED("Đã trả");

    private final String label;

    RequestStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
