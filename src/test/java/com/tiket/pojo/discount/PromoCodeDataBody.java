package com.tiket.pojo.discount;

import java.util.List;
import java.util.Map;

public class PromoCodeDataBody {
    public boolean hasExpiredBudget;
    public String dynamicPromoRuleName;
    public String uuid;
    public String budgetId;
    public boolean toggleNewConfiguration;
    public boolean isGenerateTnc;
    public String budgetingSystemFlag;
    public AffiliateSettings affiliateSettings;
    public List<ShortDescription> shortDescriptionsPreview;

    public boolean registeredOnWhitelistDashboard;
    public Metadata metadata;
    public List<String> tags;
    public boolean editQuotaAndCampaignPeriods;
    public String remainingQuotaPromoCodeLevel;
    public boolean refreshQuotaPromoCodeLevel;
    public String quotaLevel;
    public String quotaPromoCodeLevel;
    public String id;
    public String description;
    public boolean isExcludedPromoSuggestion;
    public String code;
    public FraudPrevention fraudPrevention;
    public List<RuleSet> ruleSets;
    public List<String> verticals;
    public String division;
    public String currency;
    public String campaignType;
    public List<CampaignPeriod> campaignPeriods;
    public String campaignName;
    public int maxDiscount;
    public String promoCodeStatus;
    public String createdDate;
    public String createdBy;
    public String updatedDate;
    public String updatedBy;
    public List<ShortDescription> shortDescription;
    public boolean promoCodeCombine;
    public String promoCodeDiscountType;

    public static class Metadata {
        public String dropdownMandatory;
    }

    public static class AffiliateSettings {
        public String affiliateAudience;
        public List<Object> affiliatePartnerList;
    }

    public static class FraudPrevention {
        public String phoneNumberVerification;
        public String userLogin;
        public List<String> platforms;
        public String usageCount;
        public String usagePeriod;
        public String validatedBy;
        public List<String> validatedByList;
    }

    public static class RuleSet {
        public List<DiscountRule> discountRules;
        public List<TransactionRule> transactionRules;
        public int quota;
        public int totalQuota;
        public boolean showingTag;
        public String identifier;
        public Map<String, Object> metadata;
    }

    public static class DiscountRule {
        public int quota;
        public boolean refreshQuota;
        public PromoCodePriceRangeObject promoCodePriceRangeObject;
        public PromoCodeCost promoCodeCost;
        public String uuid;
        public Map<String, Object> metadata;
        public String budgetId;
    }

    public static class PromoCodePriceRangeObject {
        public int maxPrice;
        public int minPrice;
        public PromoCodeDiscount promoCodeDiscount;
        public List<PaymentMethodObject> paymentMethodObjectList;
    }

    public static class PromoCodeDiscount {
        public int maxValue;
        public int percent;
        public int value;
    }

    public static class PaymentMethodObject {
        public String paymentId;
        public List<String> binNumbers;
        public List<String> tenor;
        public List<String> binNumberListRaw;
        public String paymentMethodName;
    }

    public static class PromoCodeCost {
        public List<CostDetail> companyCost;
        public List<CostDetail> partnerCost;
        public List<CostDetail> companyCostCashback;
        public List<CostDetail> partnerCostCashback;
    }

    public static class CostDetail {
        public int maxValue;
        public int percent;
        public int value;
        public String name;
    }

    public static class TransactionRule {
        public String param;
        public String productType;
        public PromoCodeRuleValue promoCodeRuleValue;
        public boolean usedForCalculate;
        public boolean dynamicValue;
    }

    public static class PromoCodeRuleValue {
        public String operator;
        public String value;
    }

    public static class CampaignPeriod {
        public String startDate;
        public String endDate;
        public String campaignPeriodType;
    }

    public static class ShortDescription {
        public String lang;
        public String description;
        public String termsCondition;
    }
}
