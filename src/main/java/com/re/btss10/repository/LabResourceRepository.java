package com.re.btss10.repository;

import com.re.btss10.model.LabResource;
import com.re.btss10.model.ResourceType;
import org.springframework.stereotype.Repository;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Repository
public class LabResourceRepository {

    private final Map<String, LabResource> resources = new LinkedHashMap<>();

    public LabResourceRepository() {
        seed();
    }

    public synchronized List<LabResource> findAll() {
        return new ArrayList<>(resources.values());
    }

    public synchronized Optional<LabResource> findById(String id) {
        return Optional.ofNullable(resources.get(id));
    }

    public synchronized LabResource save(LabResource resource) {
        if (resource.getId() == null || resource.getId().isBlank()) {
            resource.setId(uniqueSlug(resource.getName()));
        }
        resource.setDefaultQuantity(resource.getAvailableQuantity());
        resources.put(resource.getId(), resource);
        return resource;
    }

    public synchronized void decreaseStock(String id, int quantity) {
        LabResource resource = resources.get(id);
        if (resource != null && !resource.isLab()) {
            resource.setAvailableQuantity(Math.max(0, resource.getAvailableQuantity() - quantity));
        }
    }

    public synchronized void restoreDefaultStock(String id) {
        LabResource resource = resources.get(id);
        if (resource != null) {
            resource.setAvailableQuantity(resource.getDefaultQuantity());
        }
    }

    private void seed() {
        save(new LabResource(
                "monitor-24",
                "Màn hình rời 24 inch",
                ResourceType.DEVICE,
                "/assets/img/monitor.svg",
                8,
                "Kho A - Tủ 01",
                "Màn hình Full HD phục vụ học lập trình, kiểm thử giao diện và thuyết trình nhóm."
        ));
        save(new LabResource(
                "usb-c-hub",
                "Hub USB-C đa cổng",
                ResourceType.DEVICE,
                "/assets/img/usb-hub.svg",
                12,
                "Kho A - Tủ 02",
                "Hub chuyển đổi HDMI, USB-A, LAN cho laptop khi học tại phòng thực hành."
        ));
        save(new LabResource(
                "arduino-kit",
                "Bộ kit Arduino IoT",
                ResourceType.DEVICE,
                "/assets/img/arduino.svg",
                6,
                "Kho B - Kệ 03",
                "Board mạch, cảm biến và dây nối dùng cho bài tập nhúng và IoT căn bản."
        ));
        save(new LabResource(
                "lab-a101",
                "Phòng Lab A101",
                ResourceType.LAB,
                "/assets/img/lab-room.svg",
                1,
                "Tầng 1 - Khu A",
                "Phòng 30 máy, máy chiếu và bảng viết, phù hợp làm bài nhóm ngoài giờ."
        ));
        save(new LabResource(
                "lab-iot",
                "Phòng Lab IoT",
                ResourceType.LAB,
                "/assets/img/iot-lab.svg",
                1,
                "Tầng 3 - Khu B",
                "Không gian thực hành thiết bị nhúng, robot mini và cảm biến thông minh."
        ));
    }

    private String uniqueSlug(String value) {
        String base = Normalizer.normalize(value == null ? "resource" : value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
        if (base.isBlank()) {
            base = "resource";
        }

        String candidate = base;
        int index = 2;
        while (resources.containsKey(candidate)) {
            candidate = base + "-" + index;
            index++;
        }
        return candidate;
    }
}
