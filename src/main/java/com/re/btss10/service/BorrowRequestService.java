package com.re.btss10.service;

import com.re.btss10.form.BorrowRequestForm;
import com.re.btss10.model.BorrowRequest;
import com.re.btss10.model.LabResource;
import com.re.btss10.repository.BorrowRequestRepository;
import com.re.btss10.repository.LabResourceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BorrowRequestService {

    private final BorrowRequestRepository requestRepository;
    private final LabResourceRepository resourceRepository;
    private final LabResourceService resourceService;

    public BorrowRequestService(BorrowRequestRepository requestRepository,
                                LabResourceRepository resourceRepository,
                                LabResourceService resourceService) {
        this.requestRepository = requestRepository;
        this.resourceRepository = resourceRepository;
        this.resourceService = resourceService;
    }

    public BorrowRequest create(BorrowRequestForm form) {
        LabResource resource = resourceService.requireById(form.getItemId());
        BorrowRequest request = new BorrowRequest();
        request.setItemId(resource.getId());
        request.setItemName(resource.getName());
        request.setItemType(resource.getType());
        request.setStudentName(form.getStudentName());
        request.setStudentCode(form.getStudentCode().toUpperCase());
        request.setEmail(form.getEmail());
        request.setQuantity(resource.isLab() ? 1 : form.getQuantity());
        request.setReceiveDate(form.getReceiveDate());
        request.setReturnDate(form.getReturnDate());
        request.setReason(form.getReason());

        BorrowRequest saved = requestRepository.save(request);
        resourceRepository.decreaseStock(resource.getId(), saved.getQuantity());
        return saved;
    }

    public List<BorrowRequest> findAll() {
        return requestRepository.findAll();
    }

    public boolean hasDateCollision(String itemId, java.time.LocalDate receiveDate, java.time.LocalDate returnDate) {
        return requestRepository.hasCollision(itemId, receiveDate, returnDate);
    }
}
