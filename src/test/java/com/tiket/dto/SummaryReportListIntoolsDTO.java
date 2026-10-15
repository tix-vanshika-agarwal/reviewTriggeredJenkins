package com.tiket.dto;

import lombok.Data;

@Data
public class SummaryReportListIntoolsDTO {
    String Entity_id;
    String Currency;
    Long Total_Number_of_Vendors;
    Double Total_Revenue;
    Double Number_of_Orders;
    Double Total_Margin;
    Double Base_Price;
    Double Tiket_Point_Value;
    Double Gift_Voucher;
    Double Promo_Value;
    Double Platform_Fee;
    Double Direct_Costs;
    Double Profit;
}