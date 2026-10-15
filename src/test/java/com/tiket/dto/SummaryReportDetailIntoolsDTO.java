package com.tiket.dto;

import lombok.Data;

@Data
public class SummaryReportDetailIntoolsDTO {
    String Vendor_Name;
    String Currency;
    String Total_Quantity_Sold;
    Double Revenue_From_Orders;
    Double Additional_Margin;
    Double Additional_Commission;
    Double VAT;
    Double WHT;
    Double Top_Up_Amount_From_Vendor;
    Double Total_Commission_Earned_For_The_Month;
    Double Vendor_Share;
    Double Direct_Costs;
    Double Profit;
    String Settlement_Type;
    Double Start_Deposit_Balance;
    Double End_Deposit_Balance;
}