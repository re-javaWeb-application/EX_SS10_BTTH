package com.re.btss10;

import com.re.btss10.config.WebConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CatalogFlowTest {

    private AnnotationConfigWebApplicationContext context;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockServletContext servletContext = new MockServletContext("src/main/webapp", new FileSystemResourceLoader());
        context = new AnnotationConfigWebApplicationContext();
        context.setServletContext(servletContext);
        context.register(WebConfig.class);
        context.refresh();
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @AfterEach
    void tearDown() {
        context.close();
    }

    @Test
    void rendersCatalogAndAdminPages() throws Exception {
        mockMvc.perform(get("/resources"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/admin/resources"))
                .andExpect(status().isOk());
    }

    @Test
    void createsBorrowRequestAndShowsItToAdmin() throws Exception {
        LocalDate receiveDate = LocalDate.now().plusDays(2);
        LocalDate returnDate = receiveDate.plusDays(2);

        mockMvc.perform(post("/resources/monitor-24/borrow")
                        .param("itemId", "monitor-24")
                        .param("studentName", "Nguyen Van A")
                        .param("studentCode", "SV123456")
                        .param("email", "sv123456@school.edu.vn")
                        .param("quantity", "2")
                        .param("receiveDate", receiveDate.toString())
                        .param("returnDate", returnDate.toString())
                        .param("reason", "Muon man hinh de lam bai tap Java Web nhom."))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/resources"));

        String adminHtml = mockMvc.perform(get("/admin/requests"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertTrue(adminHtml.contains("Nguyen Van A"));
        assertTrue(adminHtml.contains("SV123456"));
    }

    @Test
    void invalidBorrowFormKeepsUserOnFormWithVietnameseErrors() throws Exception {
        LocalDate receiveDate = LocalDate.now().minusDays(1);
        LocalDate returnDate = receiveDate.minusDays(1);

        String html = mockMvc.perform(post("/resources/monitor-24/borrow")
                        .param("itemId", "monitor-24")
                        .param("studentName", "")
                        .param("studentCode", "123")
                        .param("email", "khong-phai-email")
                        .param("quantity", "0")
                        .param("receiveDate", receiveDate.toString())
                        .param("returnDate", returnDate.toString())
                        .param("reason", "ngan"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertTrue(html.contains("Vui lòng nhập họ và tên sinh viên."));
        assertTrue(html.contains("Email không đúng định dạng."));
        assertTrue(html.contains("Ngày dự kiến trả phải diễn ra sau ngày dự kiến nhận."));
    }
}
