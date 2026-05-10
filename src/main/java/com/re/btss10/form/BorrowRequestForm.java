package com.re.btss10.form;

import com.re.btss10.model.LabResource;
import com.re.btss10.validation.CheckDateCollision;
import com.re.btss10.validation.ValidBorrowPeriod;
import com.re.btss10.validation.ValidBorrowQuantity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@ValidBorrowPeriod
@ValidBorrowQuantity
@CheckDateCollision
public class BorrowRequestForm {

    @NotBlank(message = "{borrow.itemId.required}")
    private String itemId;

    @NotBlank(message = "{borrow.studentName.required}")
    @Size(max = 80, message = "{borrow.studentName.size}")
    private String studentName;

    @NotBlank(message = "{borrow.studentCode.required}")
    @Pattern(regexp = "^[A-Za-z]{2}\\d{4,8}$", message = "{borrow.studentCode.pattern}")
    private String studentCode;

    @NotBlank(message = "{borrow.email.required}")
    @Email(message = "{borrow.email.email}")
    private String email;

    @NotNull(message = "{borrow.quantity.required}")
    @Min(value = 1, message = "{borrow.quantity.min}")
    private Integer quantity;

    @NotNull(message = "{borrow.receiveDate.required}")
    @Future(message = "{borrow.receiveDate.future}")
    private LocalDate receiveDate;

    @NotNull(message = "{borrow.returnDate.required}")
    private LocalDate returnDate;

    @NotBlank(message = "{borrow.reason.required}")
    @Size(min = 10, max = 500, message = "{borrow.reason.size}")
    private String reason;

    public static BorrowRequestForm forResource(LabResource resource) {
        BorrowRequestForm form = new BorrowRequestForm();
        form.setItemId(resource.getId());
        form.setQuantity(resource.isLab() ? 1 : null);
        return form;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDate getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(LocalDate receiveDate) {
        this.receiveDate = receiveDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
