package com.re.btss10.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class LabResource {

    private String id;

    @NotBlank(message = "{resource.name.required}")
    @Size(max = 100, message = "{resource.name.size}")
    private String name;

    @NotNull(message = "{resource.type.required}")
    private ResourceType type = ResourceType.DEVICE;

    @NotBlank(message = "{resource.image.required}")
    private String imageUrl;

    @NotNull(message = "{resource.quantity.required}")
    @Min(value = 1, message = "{resource.quantity.min}")
    private Integer availableQuantity;

    @NotBlank(message = "{resource.location.required}")
    @Size(max = 80, message = "{resource.location.size}")
    private String location;

    @NotBlank(message = "{resource.description.required}")
    @Size(max = 250, message = "{resource.description.size}")
    private String description;

    private int defaultQuantity;

    public LabResource() {
    }

    public LabResource(String id,
                       String name,
                       ResourceType type,
                       String imageUrl,
                       Integer availableQuantity,
                       String location,
                       String description) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.imageUrl = imageUrl;
        this.availableQuantity = availableQuantity;
        this.defaultQuantity = availableQuantity == null ? 1 : availableQuantity;
        this.location = location;
        this.description = description;
    }

    public boolean isLab() {
        return ResourceType.LAB.equals(type);
    }

    public String getBadgeText() {
        return isLab() ? "Phòng Lab" : "Thiết bị";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ResourceType getType() {
        return type;
    }

    public void setType(ResourceType type) {
        this.type = type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDefaultQuantity() {
        return defaultQuantity;
    }

    public void setDefaultQuantity(int defaultQuantity) {
        this.defaultQuantity = defaultQuantity;
    }
}
