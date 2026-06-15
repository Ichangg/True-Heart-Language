package com.englishcenter.job;

import com.englishcenter.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReportJob {

    private final PaymentService paymentService;

    /**
     * Tạo báo cáo tài chính tự động vào ngày 1 hàng tháng lúc 8:00 sáng
     */
    @Scheduled(cron = "0 0 8 1 * *")
    public void monthlyReport() {
        log.info("📊 Running monthly finance report job...");

        try {
            LocalDate now = LocalDate.now();
            int lastMonth = now.getMonthValue() == 1 ? 12 : now.getMonthValue() - 1;
            int reportYear = now.getMonthValue() == 1 ? now.getYear() - 1 : now.getYear();

            Map<String, Object> report = paymentService.getFinanceReport("month", lastMonth, null, reportYear, null, null);
            NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));

            @SuppressWarnings("unchecked")
            Map<String, Object> studentRevenue = (Map<String, Object>) report.get("student_revenue");
            @SuppressWarnings("unchecked")
            Map<String, Object> teacherExpenses = (Map<String, Object>) report.get("teacher_expenses");

            log.info("\n📋 BÁO CÁO TÀI CHÍNH THÁNG {}/{}", lastMonth, reportYear);
            log.info("────────────────────────────────────────");
            log.info("Doanh thu dự kiến: {} ₫", formatter.format(studentRevenue.get("total_expected")));
            log.info("Đã thu:            {} ₫", formatter.format(studentRevenue.get("total_collected")));
            log.info("Còn thiếu:         {} ₫", formatter.format(studentRevenue.get("outstanding")));
            log.info("Chi phí GV:        {} ₫", formatter.format(teacherExpenses.get("total_paid")));
            log.info("Lợi nhuận:         {} ₫", formatter.format(report.get("net_income")));
            log.info("────────────────────────────────────────");
            log.info("✅ Monthly report job completed");
        } catch (Exception e) {
            log.error("❌ Report job failed: {}", e.getMessage());
        }
    }
}
