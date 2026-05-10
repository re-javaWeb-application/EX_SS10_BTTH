package com.re.btss10.validation;

import com.re.btss10.form.BorrowRequestForm;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BorrowPeriodValidator implements ConstraintValidator<ValidBorrowPeriod, BorrowRequestForm> {

    @Override
    public boolean isValid(BorrowRequestForm form, ConstraintValidatorContext context) {
        if (form == null || form.getReceiveDate() == null || form.getReturnDate() == null) {
            return true;
        }
        if (form.getReturnDate().isAfter(form.getReceiveDate())) {
            return true;
        }

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("{borrow.returnDate.afterReceiveDate}")
                .addPropertyNode("returnDate")
                .addConstraintViolation();
        return false;
    }
}
