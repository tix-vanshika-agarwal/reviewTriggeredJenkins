package com.tiket.web.dashboard.page.payment;

public class InstallmentDurationDetail {

    public String duration;
    public String totalInterest;
    public String finance;
    public String bank;
    public String minPayment;
    public String reconcileRate;

    public InstallmentDurationDetail() {
    }

    public InstallmentDurationDetail(String duration, String totalInterest, String finance, String bank, String minPayment, String reconcileRate) {
        this.duration = duration;
        this.totalInterest = totalInterest;
        this.finance = finance;
        this.bank = bank;
        this.minPayment = minPayment;
        this.reconcileRate = reconcileRate;
    }

    public String normalizeForComparison(String value) {
        if (value == null) {
            return "";
        }
        return value.replaceAll("[%\\s,]", "").trim();
    }

    public boolean matchesMonthDetailsRow(String rowText) {
        if (rowText == null || rowText.isBlank()) {
            return false;
        }
        String normalizedRow = normalizeForComparison(rowText);
        return normalizedRow.contains(normalizeForComparison(duration))
                && normalizedRow.contains(normalizeForComparison(totalInterest))
                && normalizedRow.contains(normalizeForComparison(finance))
                && normalizedRow.contains(normalizeForComparison(bank))
                && normalizedRow.contains(normalizeForComparison(minPayment))
                && normalizedRow.contains(normalizeForComparison(reconcileRate));
    }

    public boolean matchesActual(InstallmentDurationDetail actual) {
        if (actual == null) {
            return false;
        }
        return normalizeForComparison(actual.duration).contains(normalizeForComparison(duration))
                && normalizeForComparison(actual.totalInterest).contains(normalizeForComparison(totalInterest))
                && normalizeForComparison(actual.finance).contains(normalizeForComparison(finance))
                && normalizeForComparison(actual.bank).contains(normalizeForComparison(bank))
                && normalizeForComparison(actual.minPayment).contains(normalizeForComparison(minPayment))
                && normalizeForComparison(actual.reconcileRate).contains(normalizeForComparison(reconcileRate));
    }

    @Override
    public String toString() {
        return "InstallmentDurationDetail{duration='" + duration + "', totalInterest='" + totalInterest
                + "', finance='" + finance + "', bank='" + bank + "', minPayment='" + minPayment
                + "', reconcileRate='" + reconcileRate + "'}";
    }
}
