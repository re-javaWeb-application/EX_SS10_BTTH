package com.re.btss10.model;

public enum ResourceType {
    DEVICE("Thiết bị IT"),
    LAB("Phòng Lab");

    private final String label;

    ResourceType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
