package com.re.btss10.validation;

import com.re.btss10.form.BorrowRequestForm;
import com.re.btss10.model.LabResource;
import com.re.btss10.service.LabResourceService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class BorrowQuantityValidator implements ConstraintValidator<ValidBorrowQuantity, BorrowRequestForm> {

    private final LabResourceService resourceService;

    public BorrowQuantityValidator(LabResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @Override
    public boolean isValid(BorrowRequestForm form, ConstraintValidatorContext context) {
        if (form == null || form.getItemId() == null || form.getQuantity() == null || form.getQuantity() < 1) {
            return true;
        }

        LabResource resource = resourceService.requireById(form.getItemId());
        if (resource.isLab() && form.getQuantity() != 1) {
            addQuantityViolation(context, "{borrow.quantity.lab}");
            return false;
        }
        if (!resource.isLab() && form.getQuantity() > resource.getAvailableQuantity()) {
            addQuantityViolation(context, "{borrow.quantity.stock}");
            return false;
        }
        return true;
    }

    private void addQuantityViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode("quantity")
                .addConstraintViolation();
    }
}
