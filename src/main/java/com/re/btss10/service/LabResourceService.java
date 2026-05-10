package com.re.btss10.service;

import com.re.btss10.exception.ResourceNotFoundException;
import com.re.btss10.model.LabResource;
import com.re.btss10.repository.LabResourceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabResourceService {

    private final LabResourceRepository repository;

    public LabResourceService(LabResourceRepository repository) {
        this.repository = repository;
    }

    public List<LabResource> findAll() {
        return repository.findAll();
    }

    public LabResource requireById(String id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public LabResource save(LabResource resource) {
        if (resource.isLab()) {
            resource.setAvailableQuantity(1);
        }
        return repository.save(resource);
    }

    public void restoreDefaultStock(String id) {
        repository.restoreDefaultStock(id);
    }
}
