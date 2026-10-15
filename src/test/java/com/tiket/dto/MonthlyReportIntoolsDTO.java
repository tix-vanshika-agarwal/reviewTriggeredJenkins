package com.tiket.dto;
import lombok.Data;

@Data
public class MonthlyReportIntoolsDTO {
    Double Quantity;
    Double Revenue;
    Double VAT;
    Double WHT;
    Double CommissionAndMarkup;
    Double VendorShare;
    Double DirectCost;
    Double Profit;
}
