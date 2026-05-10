package com.re.btss10.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final LabResourceService resourceService;
    private final BorrowRequestService requestService;

    public AdminController(LabResourceService resourceService, BorrowRequestService requestService) {
        this.resourceService = resourceService;
        this.requestService = requestService;
    }

    @GetMapping
    public String dashboard() {
        return "redirect:/admin/resources";
    }

    @GetMapping("/resources")
    public String resources(Model model) {
        if (!model.containsAttribute("resourceForm")) {
            model.addAttribute("resourceForm", new LabResource());
        }
        model.addAttribute("resources", resourceService.findAll());
        return "admin/resources";
    }

    @PostMapping("/resources")
    public String createResource(@Valid @ModelAttribute("resourceForm") LabResource resource,
                                 BindingResult bindingResult,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("resources", resourceService.findAll());
            return "admin/resources";
        }

        resourceService.save(resource);
        redirectAttributes.addFlashAttribute("successMessage", "Đã thêm danh mục thành công.");
        return "redirect:/admin/resources";
    }

    @PostMapping("/resources/{id}/restore")
    public String restoreStock(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
        resourceService.restoreDefaultStock(id);
        redirectAttributes.addFlashAttribute("successMessage", "Đã khôi phục số lượng mặc định.");
        return "redirect:/admin/resources";
    }

    @GetMapping("/requests")
    public String requests(Model model) {
        model.addAttribute("requests", requestService.findAll());
        return "admin/requests";
    }
}
