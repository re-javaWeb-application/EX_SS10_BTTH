package com.re.btss10.controller;

import com.re.btss10.form.BorrowRequestForm;
import com.re.btss10.model.LabResource;
import com.re.btss10.service.BorrowRequestService;
import com.re.btss10.service.LabResourceService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CatalogController {

    private final LabResourceService resourceService;
    private final BorrowRequestService requestService;

    public CatalogController(LabResourceService resourceService, BorrowRequestService requestService) {
        this.resourceService = resourceService;
        this.requestService = requestService;
    }

    @GetMapping({"/", "/resources"})
    public String index(Model model) {
        model.addAttribute("resources", resourceService.findAll());
        return "catalog/list";
    }

    @GetMapping("/resources/{id}/borrow")
    public String borrowForm(@PathVariable("id") String id, Model model) {
        LabResource resource = resourceService.requireById(id);
        if (!model.containsAttribute("borrowRequest")) {
            model.addAttribute("borrowRequest", BorrowRequestForm.forResource(resource));
        }
        model.addAttribute("resource", resource);
        return "catalog/borrow-form";
    }

    @PostMapping("/resources/{id}/borrow")
    public String submitBorrowRequest(@PathVariable("id") String id,
                                      @Valid @ModelAttribute("borrowRequest") BorrowRequestForm form,
                                      BindingResult bindingResult,
                                      Model model,
                                      RedirectAttributes redirectAttributes) {
        LabResource resource = resourceService.requireById(id);
        form.setItemId(id);

        if (bindingResult.hasErrors()) {
            model.addAttribute("resource", resource);
            return "catalog/borrow-form";
        }

        requestService.create(form);
        redirectAttributes.addFlashAttribute("successMessage", "Đăng ký thành công! Quản lý Lab sẽ chuẩn bị thiết bị/phòng cho bạn.");
        return "redirect:/resources";
    }
}
