package com.re.btss10.validation;

import com.re.btss10.form.BorrowRequestForm;
import com.re.btss10.model.LabResource;
import com.re.btss10.service.BorrowRequestService;
import com.re.btss10.service.LabResourceService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class DateCollisionValidator implements ConstraintValidator<CheckDateCollision, BorrowRequestForm> {

    private final LabResourceService resourceService;
    private final BorrowRequestService requestService;

    public DateCollisionValidator(LabResourceService resourceService, BorrowRequestService requestService) {
        this.resourceService = resourceService;
        this.requestService = requestService;
    }

    @Override
    public boolean isValid(BorrowRequestForm form, ConstraintValidatorContext context) {
        if (form == null
                || form.getItemId() == null
                || form.getReceiveDate() == null
                || form.getReturnDate() == null
                || form.getReturnDate().isBefore(form.getReceiveDate())) {
            return true;
        }

        LabResource resource = resourceService.requireById(form.getItemId());
        if (!resource.isLab() || !requestService.hasDateCollision(form.getItemId(), form.getReceiveDate(), form.getReturnDate())) {
            return true;
        }

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("{borrow.date.collision}")
                .addPropertyNode("receiveDate")
                .addConstraintViolation();
        return false;
    }
}
