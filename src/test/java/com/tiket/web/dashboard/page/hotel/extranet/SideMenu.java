package com.tiket.web.dashboard.page.hotel.extranet;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Optional;

public class SideMenu<T extends WebDriver> extends BasePage<T> {
    public SideMenu(Session<T> session) {
        super(session);
    }

    private static final Locator HOME_MENU = new Locator("HOME_MENU", new By.ByXPath("//nav//a[contains(@href,'/home')] | //div[@class='menu']//a[contains(@href,'/home')]"));
    private static final Locator PROPERTY_MENU = new Locator("PROPERTY_MENU", new By.ByXPath("//nav//*[@*='tds_ic_edit_property']//ancestor::button | //span[contains(@class,'edit-property')]/parent::div"));
    private static final Locator PROPERTY_DETAILS_MENU = new Locator("PROPERTY_DETAILS_MENU", new By.ByXPath("//nav//a[normalize-space()='Property Details' or normalize-space()='Detail Properti'] | //div[@class='menu']//a[normalize-space()='Property Details' or normalize-space()='Detail Properti']"));
    private static final Locator PROPERTY_GENERAL_INFO_MENU = new Locator("PROPERTY_GENERAL_INFO_MENU", new By.ByXPath("//nav//a[contains(@href,'/property/hotel-general-info')] | //div[@class='menu']//a[contains(@href,'/property/hotel-general-info')]"));
    private static final Locator ROOM_DATA_MENU = new Locator("ROOM_DATA_MENU", new By.ByXPath("//nav//a[contains(@href,'/property/room-data')] | //div[@class='menu']//a[contains(@href,'/property/room-data')]"));
    private static final Locator PHOTO_GALLERY_MENU = new Locator("PHOTO_GALLERY_MENU", new By.ByXPath("//nav//a[normalize-space()='Photo Gallery' or normalize-space()='Galeri Foto'] | //div[@class='menu']//a[normalize-space()='Photo Gallery' or normalize-space()='Galeri Foto']"));
    private static final Locator FACILITIES_AND_SERVICES_MENU = new Locator("FACILITIES_AND_SERVICES_MENU", new By.ByXPath("//nav//a[normalize-space()='Facilities & Services' or normalize-space()='Fasilitas & Layanan'] | //div[@class='menu']//a[normalize-space()='Facilities & Services' or normalize-space()='Fasilitas & Layanan']"));
    private static final Locator CONTENT_SCORE_MENU = new Locator("CONTENT_SCORE_MENU", new By.ByXPath("//nav//a[normalize-space()='Content Score' or normalize-space()='Skor Konten'] | //div[@class='menu']//a[normalize-space()='Content Score' or normalize-space()='Skor Konten']"));
    private static final Locator PROPERTY_ANNOUNCEMENTS_MENU = new Locator("PROPERTY_ANNOUNCEMENTS_MENU", new By.ByXPath("//nav//a[contains(@href,'/property/hotel-announcement')] | //div[@class='menu']//a[contains(@href,'/property/hotel-announcement')]"));
    private static final Locator RATES_AND_AVAILABILITY_MENU = new Locator("RATES_AND_AVAILABILITY_MENU", new By.ByXPath("//nav//span[normalize-space()='Harga & Ketersediaan' or normalize-space()='Rates & Availability']//ancestor::button | //div[@class='menu']//span[normalize-space()='Harga & Ketersediaan' or normalize-space()='Rates & Availability']"));
    private static final Locator PRICING_MODEL_MENU = new Locator("PRICING_MODEL_MENU", new By.ByXPath("//nav//a[contains(@href,'/rates-and-availability/pricing-model')] | //div[@class='menu']//a[contains(@href,'/rates-and-availability/pricing-model')]"));
    private static final Locator RATE_PLAN_MENU = new Locator("RATE_PLAN_MENU", new By.ByXPath("//nav//a[contains(@href,'/rates-and-availability/rate-plans')] | //div[@class='menu']//a[contains(@href,'/rates-and-availability/rate-plans')]"));
    private static final Locator SURCHARGE_SUBMENU = new Locator("SURCHARGE_SUBMENU", new By.ByXPath("//a[@href='/v3/rates-and-availability/surcharge'] | //a[contains(@href,'/rates-and-availability/surcharge') and not(contains(@href,'create'))] | //a[.//span[normalize-space()='Biaya Tambahan' or normalize-space()='Surcharge']]"));
    private static final Locator CHILDREN_RATE_PLAN_MENU = new Locator("CHILDREN_RATE_PLAN_MENU", new By.ByXPath("//nav//a[contains(@href,'/rates-and-availability/children-rate-plans')] | //div[@class='menu']//a[contains(@href,'/rates-and-availability/children-rate-plans')]"));
    private static final Locator BULK_UPDATE_MENU = new Locator("BULK_UPDATE_MENU", new By.ByXPath("//nav//a[contains(@href,'/rates-and-availability/bulk-updates')] | //div[@class='menu']//a[contains(@href,'/rates-and-availability/bulk-updates')]"));
    private static final Locator MANAGE_RATES_AND_AVAILABILITY = new Locator("MANAGE_RATES_AND_AVAILABILITY", new By.ByXPath("//nav//a[contains(@href,'/rates-and-availability/rates')] | //div[@class='menu']//a[contains(@href,'/rates-and-availability/rates')]"));
    private static final Locator PROMO_AND_CAMPAIGN_MENU = new Locator("PROMO_AND_CAMPAIGN_MENU", new By.ByXPath("//nav//*[contains(@*,'discount-tag')]//ancestor::button | //div[@class='menu']//span[normalize-space()='Promo & Campaign']"));
    private static final Locator ADD_NEW_PROMO_AND_CAMPAIGN_MENU = new Locator("ADD_NEW_PROMO_AND_CAMPAIGN_MENU", new By.ByXPath("//nav//a[contains(@href,'/promotions-and-campaigns/add')] | //div[@class='menu']//a[contains(@href,'/promotions-and-campaigns/add')]"));
    private static final Locator YOUR_PROMOTIONS_MENU = new Locator("YOUR_PROMOTIONS_MENU", new By.ByXPath("//nav//a[contains(@href,'/promotions-and-campaigns/promo-list')] | //div[@class='menu']//a[contains(@href,'/promotions-and-campaigns/promo-list')]"));
    private static final Locator EXCLUSIVE_PRIVATE_DEALS_MENU = new Locator("EXCLUSIVE_PRIVATE_DEALS_MENU", new By.ByXPath("//a[contains(@href,'/promotions-and-campaigns/exclusive-member-deals')]"));
    private static final Locator ADDONS_MENU = new Locator("ADDONS_MENU", new By.ByXPath("//nav//span[normalize-space()='Fasilitas Ekstra' or normalize-space()='Add-ons']//ancestor::button | //span[contains(@class,'add-ons')]/parent::div"));
    private static final Locator ADDONS_LIST_MENU = new Locator("ADDONS_LIST_MENU", new By.ByXPath("//nav//a[contains(@href,'/add-ons/list')] | //div[@class='menu']//a[contains(@href,'/add-ons/list')]"));
    private static final Locator BOOKINGS_MENU = new Locator("BOOKINGS_MENU", new By.ByXPath("//nav//*[contains(@*,'booking-list')]//ancestor::button | //div[@class='menu']//span[normalize-space()='Pemesanan' or normalize-space()='Bookings']"));
    private static final Locator GROWTH_AND_OPPORTUNITIES_MENU = new Locator("GROWTH_AND_OPPORTUNITIES_MENU", new By.ByXPath("//nav//button[.//span[normalize-space()='Growth & Opportunities' or normalize-space()='Pertumbuhan & Peluang']] | //div[contains(@class,'nav-toggle')][.//span[normalize-space()='Growth & Opportunities' or normalize-space()='Pertumbuhan & Peluang']]"));
    private static final Locator CHAT_MENU = new Locator("CHAT_MENU", new By.ByXPath("//nav//a[contains(@href,'/chat')] | //div[@class='menu']//a[contains(@href,'/chat')]"));
    private static final Locator ANALYTICS_MENU = new Locator("ANALYTICS_MENU", new By.ByXPath("//button[.//span[normalize-space()='Analytics' or normalize-space()='Analitik']]"));
    private static final Locator PREFERRED_PARTNER_MENU = new Locator("PREFERRED_PARTNER_MENU", new By.ByXPath("//nav//a[contains(@href,'preferred-partner/join')] | //div[@class='menu']//a[contains(@href,'preferred-partner/join')]"));
    private static final Locator GUEST_REVIEWS_MENU = new Locator("GUEST_REVIEWS_MENU", new By.ByXPath("//nav//a[contains(@href,'/report-analytics/guest-review')] | //div[@class='menu']//a[contains(@href,'/report-analytics/guest-review')]"));
    private static final Locator FINANCE_MENU = new Locator("FINANCE_MENU", new By.ByXPath("//nav//*[contains(@*,'money')]//ancestor::button | //div[@class='menu']//span[normalize-space()='Finance' or normalize-space()='Keuangan']"));
    private static final Locator COMMISSION_MENU = new Locator("COMMISSION_MENU", new By.ByXPath("//a[@href='/v3/finance/commission']"));
    private static final Locator ACTIVITY_LOG_MENU = new Locator("ACTIVITY_LOG_MENU", new By.ByXPath("//nav//a[contains(@href,'/account/activity-log')] | //div[@class='menu']//a[contains(@href,'/account/activity-log')]"));
    private static final Locator MANAGE_USERS_MENU = new Locator("MANAGE_USERS_MENU", new By.ByXPath("//nav//a[contains(@href,'/account/user-manage')] | //div[@class='menu']//a[contains(@href,'/account/user-manage')]"));
    private static final Locator PROPERTY_CONTRACT_MENU = new Locator("PROPERTY_CONTRACT_MENU", new By.ByXPath("//nav//a[contains(@href,'/hotel-contract')] | //div[@class='menu']//a[contains(@href,'/hotel-contract')]"));
    private static final Locator CONNECTIVITY_MENU = new Locator("CONNECTIVITY_MENU", new By.ByXPath("//nav//a[contains(@href,'/connectivity')] | //div[@class='menu']//a[contains(@href,'/connectivity')]"));
    private static final Locator FAQ_MENU = new Locator("FAQ_MENU", new By.ByXPath("//nav//a[contains(@href,'/help-center')] | //div[@class='menu']//a[contains(@href,'/help-center')]"));
    private static final Locator TAX_MENU = new Locator("TAX_MENU", new By.ByXPath("//a[contains(@class,'nav-sub-list') and contains(@href,'/finance/tax')]"));
    private static final Locator SEARCH_BOOKINGS_MENU = new Locator("SEARCH_BOOKINGS_MENU", new By.ByXPath("//span[normalize-space()='Search Bookings' or normalize-space()='Cari Pemesanan']"));
    private static final Locator BLIBLI_TIKET_REWARDS_BTN = new Locator("BLIBLI_TIKET_REWARDS_BTN", By.xpath("//aside[contains(@class,'sidebar')]//ul[.//a[contains(@class,'nav-sub-list')]] /li[5]//div[contains(@class,'submenu-label')]/span | //a[contains(@href,'blibli-tiket-rewards')]//button"));

    private static final Locator HOTEL_NOW_AVAILABILITY_MENU = new Locator("HOTEL_NOW_AVAILABILITY_MENU", new By.ByXPath("//nav//a[normalize-space()='Hotel Now Availability' or normalize-space()='Ketersediaan Hotel Now'] | //div[@class='menu']//a[normalize-space()='Hotel Now Availability' or normalize-space()='Ketersediaan Hotel Now']"));
    private static final Locator YOUR_CAMPAIGN_LIST_MENU = new Locator("YOUR_CAMPAIGN_LIST_MENU", new By.ByXPath("//nav//a[normalize-space()='Your Campaign List' or normalize-space()='Campaign Anda'] | //div[@class='menu']//a[normalize-space()='Your Campaign List' or normalize-space()='Campaign Anda']"));
    private static final Locator OPPORTUNITY_CENTER_MENU = new Locator("OPPORTUNITY_CENTER_MENU", new By.ByXPath("//nav//a[@prefix='app' and contains(@href,'opportunity-center')] | //aside[contains(@class,'sidebar')]//a[contains(@href,'opportunity-center')]"));
    private static final Locator PROMOTED_STAYS_MENU = new Locator("PROMOTED_STAYS_MENU", new By.ByXPath("//nav//a[@prefix='app' and contains(@href,'promoted-stays')] | //aside[contains(@class,'sidebar')]//a[contains(@href,'promoted-stays')]"));
    private static final Locator PERFORMANCE_REPORT_MENU = new Locator("PERFORMANCE_REPORT_MENU", new By.ByXPath("//nav//a[normalize-space()='Performance Report' or normalize-space()='Perfomance Report' or normalize-space()='Laporan Performa'] | //div[@class='menu']//a[normalize-space()='Performance Report' or normalize-space()='Perfomance Report' or normalize-space()='Laporan Performa']"));
    private static final Locator COMPETITIVE_SET_MENU = new Locator("COMPETITIVE_SET_MENU", new By.ByXPath("//nav//a[normalize-space()='Competitive Set' or normalize-space()='Set Kompetitif'] | //div[@class='menu']//a[normalize-space()='Competitive Set' or normalize-space()='Set Kompetitif']"));
    private static final Locator AVERAGE_DAILY_RATE_MENU = new Locator("AVERAGE_DAILY_RATE_MENU", new By.ByXPath("//nav//a[normalize-space()='Average Daily Rate' or normalize-space()='Rata-rata Harga Harian'] | //div[@class='menu']//a[normalize-space()='Average Daily Rate' or normalize-space()='Rata-rata Harga Harian']"));
    private static final Locator PAYMENT_LIST_MENU = new Locator("PAYMENT_LIST_MENU", new By.ByXPath("//nav//a[normalize-space()='Payment List' or normalize-space()='Daftar Pembayaran'] | //div[@class='menu']//a[normalize-space()='Payment List' or normalize-space()='Daftar Pembayaran']"));
    private static final Locator PAYMENT_DETAILS_MENU = new Locator("PAYMENT_DETAILS_MENU", new By.ByXPath("//nav//a[normalize-space()='Payment Details' or normalize-space()='Detail Pembayaran'] | //div[@class='menu']//a[normalize-space()='Payment Details' or normalize-space()='Detail Pembayaran']"));
    private static final Locator DEPOSIT_HISTORY_MENU = new Locator("DEPOSIT_HISTORY_MENU", new By.ByXPath("//nav//a[normalize-space()='Deposit History' or normalize-space()='Histori Deposit'] | //div[@class='menu']//a[normalize-space()='Deposit History' or normalize-space()='Histori Deposit']"));
    private static final Locator ADDITIONAL_FEES_CHECKIN_MENU = new Locator("ADDITIONAL_FEES_CHECKIN_MENU", new By.ByXPath("//nav//a[normalize-space()='Additional Fees at Check-in' or normalize-space()='Biaya Tambahan saat Check-in'] | //div[@class='menu']//a[normalize-space()='Additional Fees at Check-in' or normalize-space()='Biaya Tambahan saat Check-in']"));
    private static final Locator NPWP_MENU = new Locator("NPWP_MENU", new By.ByXPath("//nav//a[contains(@href,'/finance/npwp')] | //div[@class='menu']//a[contains(@href,'/finance/npwp')]"));
    private static final Locator CUSTOMER_SERVICE_MENU = new Locator("CUSTOMER_SERVICE_MENU", new By.ByXPath("//nav//span[normalize-space()='Customer Service' or normalize-space()='Layanan Pelanggan']/ancestor::button | //div[@class='menu']//span[normalize-space()='Customer Service' or normalize-space()='Layanan Pelanggan']"));
    private static final Locator CS_BOOKING_LIST_MENU = new Locator("CS_BOOKING_LIST_MENU", new By.ByXPath("//nav//a[normalize-space()='Booking List' or normalize-space()='Daftar Pemesanan'] | //div[@class='menu']//a[normalize-space()='Booking List' or normalize-space()='Daftar Pemesanan']"));

    // Subject locators: element(s) that must be present once a submenu page opens (confirms it rendered & is not blocked).
    private static final Locator PROPERTY_DETAILS_SUBMENU_SUBJECT = new Locator("PROPERTY_DETAILS_SUBMENU_SUBJECT", By.xpath("//div[contains(@class,'Tabs_tab_panels_inner_container')]/div[not(@hidden)]//div[contains(@class,'justify-end')]/button"));
    private static final Locator ROOM_DATA_SUBMENU_SUBJECT = new Locator("ROOM_DATA_SUBMENU_SUBJECT", By.xpath("//main//div[contains(@class,'justify-between')]//span[contains(@class,'text-xl')] | //form[@autocomplete='off']//button[@type='submit']"));
    private static final Locator PROPERTY_GENERAL_INFO_SUBMENU_SUBJECT = new Locator("PROPERTY_GENERAL_INFO_SUBMENU_SUBJECT", By.xpath("//div[contains(@class,'the-tabs')]//div[contains(@class,'the-tab-edit')][1]/div[contains(@class,'the-tab')] | //div[contains(@class,'the-tabs')]//div[contains(@class,'the-tab-edit')][2]/div[contains(@class,'the-tab')]"));
    private static final Locator PHOTO_GALLERY_SUBMENU_SUBJECT = new Locator("PHOTO_GALLERY_SUBMENU_SUBJECT", By.xpath("//div[contains(@class,'page-header')]//label[contains(@class,'title')] | //div[contains(@class,'the-dropdown')]//input[@readonly]"));
    private static final Locator FACILITIES_AND_SERVICES_SUBMENU_SUBJECT = new Locator("FACILITIES_AND_SERVICES_SUBMENU_SUBJECT", By.xpath("//main//form[@autocomplete='off']/preceding-sibling::div[1]//span[1] | //main//form[@autocomplete='off']"));
    private static final Locator CONTENT_SCORE_SUBMENU_SUBJECT = new Locator("CONTENT_SCORE_SUBMENU_SUBJECT", By.xpath("//main//div[@data-testid='content-score-summary']"));
    private static final Locator PROPERTY_ANNOUNCEMENTS_SUBMENU_SUBJECT = new Locator("PROPERTY_ANNOUNCEMENTS_SUBMENU_SUBJECT", By.xpath("//div[contains(@class,'Toggle_container')]"));
    private static final Locator PRICING_MODEL_SUBMENU_SUBJECT = new Locator("PRICING_MODEL_SUBMENU_SUBJECT", By.xpath("//main//button[contains(@class,'Button_variant_secondary')]"));
    private static final Locator RATE_PLAN_SETUP_SUBMENU_SUBJECT = new Locator("RATE_PLAN_SETUP_SUBMENU_SUBJECT", By.xpath("//main//div[contains(@class,'justify-between') and contains(@class,'items-center')]/button[@type='button']"));
    private static final Locator POLICY_AND_PRICE_FOR_CHILDREN_SUBMENU_SUBJECT = new Locator("POLICY_AND_PRICE_FOR_CHILDREN_SUBMENU_SUBJECT", By.xpath("//main//span[contains(@class,'Text_variant_highEmphasis')] | //div[contains(@class,'Dropdown_dropdown_container')]//div[contains(@class,'input_inner_wrapper')]"));
    private static final Locator MANAGE_RATE_AND_AVAILABILITY_SUBMENU_SUBJECT = new Locator("MANAGE_RATE_AND_AVAILABILITY_SUBMENU_SUBJECT", By.xpath("//div[contains(@class,'page-header')]//label[contains(@class,'manage-rates-header-title')] | //div[contains(@class,'subtitle') and contains(@class,'manage-rates-header-subtitle')]//button[contains(@class,'tix-button')]"));
    private static final Locator BULK_UPDATES_DATE_AND_DAYS_SUBMENU_SUBJECT = new Locator("BULK_UPDATES_DATE_AND_DAYS_SUBMENU_SUBJECT", By.xpath("//div[contains(@class, 'calendar-section')]/preceding::label[contains(@class, 'title')][1] | //span[contains(@class, 'tixicon-calendar-depart')]/following-sibling::input | //span[contains(@class, 'tixicon-calendar-return')]/following-sibling::input"));
    private static final Locator SURCHARGE_SETUP_SUBMENU_SUBJECT = new Locator("SURCHARGE_SETUP_SUBMENU_SUBJECT", By.xpath("//a[contains(@href, '/surcharge/create')]/preceding::label[contains(@class, 'title')][1] | //a[contains(@href, '/surcharge/create')]"));
    private static final Locator HOTEL_NOW_AVAILABILITY_SUBMENU_SUBJECT = new Locator("HOTEL_NOW_AVAILABILITY_SUBMENU_SUBJECT", By.xpath("//div[contains(@class,'Card_card_container')]/preceding-sibling::div[1]//span[1]"));
    private static final Locator PROMOTIONS_AND_CAMPAIGNS_SUBMENU_SUBJECT = new Locator("PROMOTIONS_AND_CAMPAIGNS_SUBMENU_SUBJECT", By.xpath("//main//span[contains(@class,'Text_variant_highEmphasis')] | //div[contains(@class,'Collapse_collapse_with_transition')]//div[contains(@class,'flex') and contains(@class,'gap-6')]"));
    private static final Locator PROMO_LIST_SUBMENU_SUBJECT = new Locator("PROMO_LIST_SUBMENU_SUBJECT", By.xpath("//main//div[contains(@class,'justify-between') and contains(@class,'items-center')]/descendant::*[self::span[contains(@class,'Text_variant_highEmphasis')] or self::button[contains(@class,'Button_variant_primary')]]"));
    private static final Locator CAMPAIGN_LIST_SUBMENU_SUBJECT = new Locator("CAMPAIGN_LIST_SUBMENU_SUBJECT", By.xpath("//main//div[contains(@class,'justify-between') and contains(@class,'items-center')]/descendant::*[self::span[contains(@class,'Text_variant_highEmphasis')] or self::button[contains(@class,'Button_variant_primary')]]"));
    private static final Locator EXCLUSIVE_PRIVATE_DEALS_SUBMENU_SUBJECT = new Locator("EXCLUSIVE_PRIVATE_DEALS_SUBMENU_SUBJECT", By.xpath("//div[contains(@class,'page-header')]/label[contains(@class,'title')] | //div[contains(@class,'the-row')]//button[contains(@class,'yellow') and contains(@class,'the-button')]"));
    private static final Locator BLIBLI_TIKET_REWARDS_SUBMENU_SUBJECT = new Locator("BLIBLI_TIKET_REWARDS_SUBMENU_SUBJECT", By.xpath("//main//h2[contains(@class,'Text_size_h2')] | //main//button[contains(@class,'Button_variant_primary')]"));
    private static final Locator ADD_ONS_SUBMENU_SUBJECT = new Locator("ADD_ONS_SUBMENU_SUBJECT", By.xpath("//main//div[contains(@class,'justify-between') and contains(@class,'items-center')]/descendant::*[self::span[contains(@class,'Text_variant_highEmphasis')] or self::button[contains(@class,'Button_variant_primary')]]"));
    private static final Locator BOOKING_LIST_SUBMENU_SUBJECT = new Locator("BOOKING_LIST_SUBMENU_SUBJECT", By.xpath("//div[contains(@class,'page-header')]//label[contains(@class,'title')] | //div[contains(@class,'the-row-wrap')]//button[contains(@class,'primary') and contains(@class,'lower-case')]"));
    private static final Locator OPPORTUNITY_CENTER_SUBMENU_SUBJECT = new Locator("OPPORTUNITY_CENTER_SUBMENU_SUBJECT", By.xpath("//main//section[contains(@class,'items-center') and contains(@class,'gap-1')]//button[contains(@class,'Button_variant_tertiary')] | //main//aside//div[contains(@class,'Card_card_container')]"));
    private static final Locator PREFERRED_PARTNER_SUBMENU_SUBJECT = new Locator("PREFERRED_PARTNER_SUBMENU_SUBJECT", By.xpath("//main//span[contains(@class,'Text_size_h2')] | //main//span[contains(@class,'Text_size_h2')]"));
    private static final Locator PROMOTED_STAYS_SUBMENU_SUBJECT = new Locator("PROMOTED_STAYS_SUBMENU_SUBJECT", By.xpath("//main//h2[contains(@class,'Text_size_h2')]"));
    private static final Locator PERFORMANCE_REPORT_SUBMENU_SUBJECT = new Locator("PERFORMANCE_REPORT_SUBMENU_SUBJECT", By.xpath("//div[contains(@class,'top-container')]//div[contains(@class,'title')] | //div[contains(@class,'search-header')]//button[contains(@class,'primary')]"));
    private static final Locator COMPETITIVE_SET_SUBMENU_SUBJECT = new Locator("COMPETITIVE_SET_SUBMENU_SUBJECT", By.xpath("//main//span[contains(@class,'Text_variant_highEmphasis')] | //main//div[@data-testid='collapse-container']/div[1]"));
    private static final Locator AVERAGE_DAILY_RATE_SUBMENU_SUBJECT = new Locator("AVERAGE_DAILY_RATE_SUBMENU_SUBJECT", By.xpath("//main//span[contains(@class,'Text_variant_highEmphasis')] | //main//button[contains(@class,'Button_variant_secondary') and not(@disabled)]"));
    private static final Locator PAYMENT_LIST_SUBMENU_SUBJECT = new Locator("PAYMENT_LIST_SUBMENU_SUBJECT", By.xpath("//div[contains(@class,'the-list-payment')]//div[contains(@class,'page-header')]//label[contains(@class,'title')] | //div[contains(@class,'the-list-payment')]//button[contains(@class,'search-payment')]"));
    private static final Locator PAYMENT_DETAILS_SUBMENU_SUBJECT = new Locator("PAYMENT_DETAILS_SUBMENU_SUBJECT", By.xpath("//div[contains(@class,'the-payment-details-head')]//label[contains(@class,'title')] | //div[contains(@class,'the-payment-details-body')]/following-sibling::div[contains(@class,'the-row')]//button[contains(@class,'tix-button')]"));
    private static final Locator DEPOSIT_HISTORY_SUBMENU_SUBJECT = new Locator("DEPOSIT_HISTORY_SUBMENU_SUBJECT", By.xpath("//div[contains(@class,'page-header')]//label[contains(@class,'title')] | //div[contains(@class,'the-deposit-search')]//button[contains(@class,'btn-search-list')]"));
    private static final Locator COMMISSION_SUBMENU_SUBJECT = new Locator("COMMISSION_SUBMENU_SUBJECT", By.xpath("//div[contains(@class,'commission-container')]//div[contains(@class,'page-header')]//label[contains(@class,'title')] | //div[contains(@class,'commission-container')]//div[contains(@class,'btn-right')]//button[contains(@class,'btn-footer')]"));
    private static final Locator TAXES_AND_OTHER_FEES_SUBMENU_SUBJECT = new Locator("TAXES_AND_OTHER_FEES_SUBMENU_SUBJECT", By.xpath("//main//div[contains(@class,'justify-between') and contains(@class,'items-center')][.//div[contains(@class,'Tooltip_tooltip')]]//*[self::span[contains(@class,'Text_variant_highEmphasis')] or self::button[contains(@class,'Button_variant_primary')]]"));
    private static final Locator ADDITIONAL_FEES_AT_CHECKIN_SUBMENU_SUBJECT = new Locator("ADDITIONAL_FEES_AT_CHECKIN_SUBMENU_SUBJECT", By.xpath("//main//div[contains(@class,'justify-between') and contains(@class,'items-center')][.//div[contains(@class,'Tooltip_tooltip')]]//*[self::span[contains(@class,'Text_variant_highEmphasis')] or self::button[contains(@class,'Button_variant_primary')]]"));
    private static final Locator CUSTOMER_SERVICE_BOOKING_LIST_SUBMENU_SUBJECT = new Locator("CUSTOMER_SERVICE_BOOKING_LIST_SUBMENU_SUBJECT", By.xpath("//div[contains(@class,'the-list-payment')]//h1 | //div[contains(@class,'the-list-payment')]//button[contains(@class,'search-payment')]"));
    private static final Locator TAXPAYER_IDENTIFICATION_NUMBER_NPWP_DETAILS_SUBMENU_SUBJECT = new Locator("TAXPAYER_IDENTIFICATION_NUMBER_NPWP_DETAILS_SUBMENU_SUBJECT", By.xpath("//main//span[contains(@class,'Text_variant_highEmphasis')] | //main//button[@data-testid='taxpayer-edit-button']"));

    public enum SidebarMenu {
        HOME(HOME_MENU),
        PROPERTY(PROPERTY_MENU),
        RATES_AND_AVAILABILITY(RATES_AND_AVAILABILITY_MENU),
        PROMO_AND_CAMPAIGN(PROMO_AND_CAMPAIGN_MENU),
        ADDONS(ADDONS_MENU),
        BOOKINGS(BOOKINGS_MENU),
        GROWTH_AND_OPPORTUNITIES(GROWTH_AND_OPPORTUNITIES_MENU),
        CHAT(CHAT_MENU),
        ANALYTICS(ANALYTICS_MENU),
        GUEST_REVIEWS(GUEST_REVIEWS_MENU),
        FINANCE(FINANCE_MENU),
        ACTIVITY_LOG(ACTIVITY_LOG_MENU),
        MANAGE_USERS(MANAGE_USERS_MENU),
        PROPERTY_CONTRACT(PROPERTY_CONTRACT_MENU),
        FAQ(FAQ_MENU),
        CONNECTIVITY(CONNECTIVITY_MENU),
        CUSTOMER_SERVICE(CUSTOMER_SERVICE_MENU),
        ;

        private final Locator locator;

        private SidebarMenu(Locator locator) {
            this.locator = locator;
        }

        public Locator getLocator() {
            return locator;
        }
    }

    public void clickMenu(SidebarMenu sidebarMenu) {
        WebElement menu = driverTools.waitForElementToBeClickable(sidebarMenu.getLocator(), 3).orElseThrow();
        driverTools.click(menu);
    }

    public boolean isMenuDisplayed(SidebarMenu sidebarMenu) {
        return driverTools.waitForElementToAppear(sidebarMenu.getLocator(), 3).isPresent();
    }

    public void clickPropertyMenu(){
        driverTools.waitForElementToAppear(PROPERTY_MENU);
        driverTools.click(PROPERTY_MENU);
    }

    public void clickPropertyGeneralInfoMenu(){
        driverTools.waitForElementToAppear(PROPERTY_GENERAL_INFO_MENU);
        driverTools.click(PROPERTY_GENERAL_INFO_MENU);
    }

    public void clickRoomDataMenu(){
        driverTools.waitForElementToAppear(ROOM_DATA_MENU);
        driverTools.click(ROOM_DATA_MENU);
    }

    public void clickRatesAndAvailabilityMenu(){
        driverTools.waitForElementToAppear(RATES_AND_AVAILABILITY_MENU);
        driverTools.click(RATES_AND_AVAILABILITY_MENU);
    }

    public void clickPricingModelMenu(){
        driverTools.waitForElementToAppear(PRICING_MODEL_MENU);
        driverTools.click(PRICING_MODEL_MENU);
    }

    public void clickRatePlanMenu(){
        driverTools.waitForElementToAppear(RATE_PLAN_MENU);
        driverTools.click(RATE_PLAN_MENU);
    }

    public void clickSurchargeSubmenu(){
        driverTools.waitForElementToAppear(SURCHARGE_SUBMENU);
        driverTools.click(SURCHARGE_SUBMENU);
    }

    public void clickManageRatesAndAvailabilityMenu(){
        driverTools.waitForElementToAppear(MANAGE_RATES_AND_AVAILABILITY);
        driverTools.click(MANAGE_RATES_AND_AVAILABILITY);
    }

    public void clickChildrenRatePlanMenu(){
        driverTools.waitForElementToAppear(CHILDREN_RATE_PLAN_MENU);
        driverTools.click(CHILDREN_RATE_PLAN_MENU);
    }

    public void clickBulkUpdateMenu(){
        driverTools.waitForElementToAppear(BULK_UPDATE_MENU);
        driverTools.click(BULK_UPDATE_MENU);
    }

    public void clickPromoAndCampaignMenu(){
        driverTools.waitForElementToAppear(PROMO_AND_CAMPAIGN_MENU);
        driverTools.click(PROMO_AND_CAMPAIGN_MENU);
    }

    public void clickAddNewPromoAndCampaignMenu(){
        driverTools.waitForElementToAppear(ADD_NEW_PROMO_AND_CAMPAIGN_MENU);
        driverTools.click(ADD_NEW_PROMO_AND_CAMPAIGN_MENU);
    }

    public void clickAddOnsMenu(){
        WebElement button = driverTools.waitForElementToBeClickable(ADDONS_MENU).orElseThrow();
        driverTools.click(button);
    }

    public void clickAddOnsListSubmenu(){
        WebElement button = driverTools.waitForElementToBeClickable(ADDONS_LIST_MENU).orElseThrow();
        driverTools.click(button);
    }

    public void clickYourPromotionsMenu(){
        driverTools.waitForElementToAppear(YOUR_PROMOTIONS_MENU);
        driverTools.click(YOUR_PROMOTIONS_MENU);
    }

    public void clickExclusivePrivateDealsMenu(){
        WebElement button = driverTools.waitForElementToBeClickable(EXCLUSIVE_PRIVATE_DEALS_MENU).orElseThrow();
        driverTools.click(button);
    }

    public void clickFinanceMenu(){
        driverTools.waitForElementToAppear(FINANCE_MENU);
        driverTools.click(FINANCE_MENU);
    }

    public void clickCommissionMenu(){
        driverTools.waitForElementToAppear(COMMISSION_MENU);
        driverTools.click(COMMISSION_MENU);
    }

    public void clickTaxMenu(){
        driverTools.waitForElementToAppear(TAX_MENU);
        driverTools.click(TAX_MENU);
    }

    public void clickManageUsersMenu(){
        driverTools.waitForElementToAppear(MANAGE_USERS_MENU);
        driverTools.click(MANAGE_USERS_MENU);
    }

    public void clickBookingMenu(){
        driverTools.waitForElementToAppear(BOOKINGS_MENU);
        driverTools.click(BOOKINGS_MENU);
    }

    public void clickSearchBookingsMenu() {
        driverTools.waitForElementToAppear(SEARCH_BOOKINGS_MENU);
        driverTools.click(SEARCH_BOOKINGS_MENU);
    }

    public void clickActivityLogsMenu() {
        driverTools.waitForElementToAppear(ACTIVITY_LOG_MENU);
        driverTools.click(ACTIVITY_LOG_MENU);
    }

    public void clickBlibliTiketRewardsMenu() {
        driverTools.waitForElementToAppear(BLIBLI_TIKET_REWARDS_BTN);
        driverTools.click(BLIBLI_TIKET_REWARDS_BTN);
    }

    /** Submenu items grouped by parent menu, with the route to open and the subject locator(s) to verify. */
    public enum SidebarSubmenu {
        // Property (7)
        PROPERTY_DETAILS(SidebarMenu.PROPERTY, PROPERTY_DETAILS_MENU, "/property/property-detail", PROPERTY_DETAILS_SUBMENU_SUBJECT),
        ROOM_DATA(SidebarMenu.PROPERTY, ROOM_DATA_MENU, "/property/room-data", ROOM_DATA_SUBMENU_SUBJECT),
        PROPERTY_GENERAL_INFO(SidebarMenu.PROPERTY, PROPERTY_GENERAL_INFO_MENU, "/property/hotel-general-info", PROPERTY_GENERAL_INFO_SUBMENU_SUBJECT),
        PHOTO_GALLERY(SidebarMenu.PROPERTY, PHOTO_GALLERY_MENU, "/property/photo-gallery", PHOTO_GALLERY_SUBMENU_SUBJECT),
        FACILITIES_AND_SERVICES(SidebarMenu.PROPERTY, FACILITIES_AND_SERVICES_MENU, "/property/facilities", FACILITIES_AND_SERVICES_SUBMENU_SUBJECT),
        CONTENT_SCORE(SidebarMenu.PROPERTY, CONTENT_SCORE_MENU, "/property/content-score", CONTENT_SCORE_SUBMENU_SUBJECT),
        PROPERTY_ANNOUNCEMENTS(SidebarMenu.PROPERTY, PROPERTY_ANNOUNCEMENTS_MENU, "/property/hotel-announcement", PROPERTY_ANNOUNCEMENTS_SUBMENU_SUBJECT),
        // Rates & Availability (7)
        PRICING_MODEL(SidebarMenu.RATES_AND_AVAILABILITY, PRICING_MODEL_MENU, "/rates-and-availability/pricing-model", PRICING_MODEL_SUBMENU_SUBJECT),
        RATE_PLAN(SidebarMenu.RATES_AND_AVAILABILITY, RATE_PLAN_MENU, "/rates-and-availability/rate-plans", RATE_PLAN_SETUP_SUBMENU_SUBJECT),
        CHILDREN_RATE_PLAN(SidebarMenu.RATES_AND_AVAILABILITY, CHILDREN_RATE_PLAN_MENU, "/rates-and-availability/children-rate-plans", POLICY_AND_PRICE_FOR_CHILDREN_SUBMENU_SUBJECT),
        MANAGE_RATES_AND_AVAILABILITY(SidebarMenu.RATES_AND_AVAILABILITY, SideMenu.MANAGE_RATES_AND_AVAILABILITY, "/rates-and-availability/rates", MANAGE_RATE_AND_AVAILABILITY_SUBMENU_SUBJECT),
        BULK_UPDATE(SidebarMenu.RATES_AND_AVAILABILITY, BULK_UPDATE_MENU, "/rates-and-availability/bulk-updates", BULK_UPDATES_DATE_AND_DAYS_SUBMENU_SUBJECT),
        SURCHARGE(SidebarMenu.RATES_AND_AVAILABILITY, SURCHARGE_SUBMENU, "/rates-and-availability/surcharge", SURCHARGE_SETUP_SUBMENU_SUBJECT),
        HOTEL_NOW_AVAILABILITY(SidebarMenu.RATES_AND_AVAILABILITY, HOTEL_NOW_AVAILABILITY_MENU, "/rates-and-availability/hotel-now-availability", HOTEL_NOW_AVAILABILITY_SUBMENU_SUBJECT),
        // Promo & Campaign (5)
        ADD_NEW_PROMO(SidebarMenu.PROMO_AND_CAMPAIGN, ADD_NEW_PROMO_AND_CAMPAIGN_MENU, "/promotions-and-campaigns/add", PROMOTIONS_AND_CAMPAIGNS_SUBMENU_SUBJECT),
        YOUR_PROMOTIONS(SidebarMenu.PROMO_AND_CAMPAIGN, YOUR_PROMOTIONS_MENU, "/promotions-and-campaigns/promo-list", PROMO_LIST_SUBMENU_SUBJECT),
        YOUR_CAMPAIGN_LIST(SidebarMenu.PROMO_AND_CAMPAIGN, YOUR_CAMPAIGN_LIST_MENU, "/promotions-and-campaigns/campaign-list", CAMPAIGN_LIST_SUBMENU_SUBJECT),
        EXCLUSIVE_PRIVATE_DEALS(SidebarMenu.PROMO_AND_CAMPAIGN, EXCLUSIVE_PRIVATE_DEALS_MENU, "/promotions-and-campaigns/exclusive-member-deals", EXCLUSIVE_PRIVATE_DEALS_SUBMENU_SUBJECT),
        BLIBLI_TIKET_REWARDS(SidebarMenu.PROMO_AND_CAMPAIGN, BLIBLI_TIKET_REWARDS_BTN, "/promotions-and-campaigns/blibli-tiket-rewards", BLIBLI_TIKET_REWARDS_SUBMENU_SUBJECT),
        // Add-ons (1)
        ADDONS_LIST(SidebarMenu.ADDONS, ADDONS_LIST_MENU, "/add-ons/list", ADD_ONS_SUBMENU_SUBJECT),
        // Bookings (1)
        SEARCH_BOOKINGS(SidebarMenu.BOOKINGS, SEARCH_BOOKINGS_MENU, "/bookings/search-bookings", BOOKING_LIST_SUBMENU_SUBJECT),
        // Growth & Opportunities (3)
        OPPORTUNITY_CENTER(SidebarMenu.GROWTH_AND_OPPORTUNITIES, OPPORTUNITY_CENTER_MENU, "/growth/opportunity-center", OPPORTUNITY_CENTER_SUBMENU_SUBJECT),
        PREFERRED_PARTNER(SidebarMenu.GROWTH_AND_OPPORTUNITIES, PREFERRED_PARTNER_MENU, "/preferred-partner/join", PREFERRED_PARTNER_SUBMENU_SUBJECT),
        PROMOTED_STAYS(SidebarMenu.GROWTH_AND_OPPORTUNITIES, PROMOTED_STAYS_MENU, "/growth/promoted-stays", PROMOTED_STAYS_SUBMENU_SUBJECT),
        // Analytics (3)
        PERFORMANCE_REPORT(SidebarMenu.ANALYTICS, PERFORMANCE_REPORT_MENU, "/report-analytics/performance-report", PERFORMANCE_REPORT_SUBMENU_SUBJECT),
        COMPETITIVE_SET(SidebarMenu.ANALYTICS, COMPETITIVE_SET_MENU, "/report-analytics/competitive-set", COMPETITIVE_SET_SUBMENU_SUBJECT   ),
        AVERAGE_DAILY_RATE(SidebarMenu.ANALYTICS, AVERAGE_DAILY_RATE_MENU, "/report-analytics/average-daily-rate", AVERAGE_DAILY_RATE_SUBMENU_SUBJECT),
        // Finance (6)
        PAYMENT_LIST(SidebarMenu.FINANCE, PAYMENT_LIST_MENU, "/finance/payment-list", PAYMENT_LIST_SUBMENU_SUBJECT),
        PAYMENT_DETAILS(SidebarMenu.FINANCE, PAYMENT_DETAILS_MENU, "/finance/payment-details", PAYMENT_DETAILS_SUBMENU_SUBJECT),
        DEPOSIT_HISTORY(SidebarMenu.FINANCE, DEPOSIT_HISTORY_MENU, "/finance/deposit-history", DEPOSIT_HISTORY_SUBMENU_SUBJECT),
        COMMISSION(SidebarMenu.FINANCE, COMMISSION_MENU, "/finance/commission", COMMISSION_SUBMENU_SUBJECT),
        TAX(SidebarMenu.FINANCE, TAX_MENU, "/finance/tax", TAXES_AND_OTHER_FEES_SUBMENU_SUBJECT),
        ADDITIONAL_FEES_AT_CHECKIN(SidebarMenu.FINANCE, ADDITIONAL_FEES_CHECKIN_MENU, "/finance/additional-fees", ADDITIONAL_FEES_AT_CHECKIN_SUBMENU_SUBJECT),
        NPWP(SidebarMenu.FINANCE, NPWP_MENU, "/finance/npwp", true, TAXPAYER_IDENTIFICATION_NUMBER_NPWP_DETAILS_SUBMENU_SUBJECT),
        // Customer Service (1)
        BOOKING_LIST(SidebarMenu.CUSTOMER_SERVICE, CS_BOOKING_LIST_MENU, "/customer-service/booking-list", CUSTOMER_SERVICE_BOOKING_LIST_SUBMENU_SUBJECT),
        ;

        private final SidebarMenu parent;
        private final Locator locator;
        private final String urlFragment;
        private final boolean domesticOnly;
        private final Locator[] subjectLocators;

        SidebarSubmenu(SidebarMenu parent, Locator locator, String urlFragment, Locator... subjectLocators) {
            this(parent, locator, urlFragment, false, subjectLocators);
        }

        SidebarSubmenu(SidebarMenu parent, Locator locator, String urlFragment, boolean domesticOnly, Locator... subjectLocators) {
            this.parent = parent;
            this.locator = locator;
            this.urlFragment = urlFragment;
            this.domesticOnly = domesticOnly;
            this.subjectLocators = subjectLocators;
        }

        /** Submenus present only for Domestic hotels (e.g. NPWP), skipped for International. */
        public boolean isDomesticOnly() {
            return domesticOnly;
        }

        public SidebarMenu getParent() {
            return parent;
        }

        public Locator getLocator() {
            return locator;
        }

        public String getUrlFragment() {
            return urlFragment;
        }

        public Locator[] getSubjectLocators() {
            return subjectLocators;
        }
    }

    /** Expands the parent (if the submenu is not already visible) and returns whether the submenu becomes visible. */
    public boolean isSubmenuDisplayed(SidebarSubmenu submenu) {
        if (driverTools.waitForElementToAppear(submenu.getLocator(), 3).isEmpty()) {
            clickMenu(submenu.getParent());
        }
        return driverTools.waitForElementToAppear(submenu.getLocator(), 5).isPresent();
    }

    /** Submenus that raise a pop-up on open which must be dismissed before the subject can be verified. */
    private static final java.util.EnumSet<SidebarSubmenu> DISMISS_POPUPS_ON_OPEN = java.util.EnumSet.of(SidebarSubmenu.FACILITIES_AND_SERVICES);

    private static final Locator FACILITIES_COACHMARK_NEXT_BUTTON = new Locator("FACILITIES_COACHMARK_NEXT_BUTTON", new By.ByXPath("//div[@id='coachmark-portal']//div[contains(@class,'Coachmark_coachmark_button_wrapper')]//button[contains(@class,'Button_variant_primary') and not(contains(@class,'Coachmark_coachmark_done_btn'))]"));
    private static final Locator FACILITIES_COACHMARK_DONE_BUTTON = new Locator("FACILITIES_COACHMARK_DONE_BUTTON", new By.ByXPath("//div[@id='coachmark-portal']//div[contains(@class,'Coachmark_coachmark_button_wrapper')]//button[contains(@class,'Coachmark_coachmark_done_btn')]"));

    /** Clicks "Next" on the first two coachmark steps, then "Done" on the last. */
    public void dismissFacilitiesCoachmarks() {
        for (int step = 0; step < 2; step++) {
            driverTools.waitForElementToBeClickable(FACILITIES_COACHMARK_NEXT_BUTTON, 3).ifPresent(driverTools::click);
        }
        driverTools.waitForElementToBeClickable(FACILITIES_COACHMARK_DONE_BUTTON, 3).ifPresent(driverTools::click);
    }

    /** Opens a submenu (expanding its parent if needed) and returns whether its page subject rendered. */
    // The extranet renders its sidebar in two shells (React <nav> vs legacy div.menu) and sometimes leaves it blank
    // (container present, menu items not rendered) after switching to a legacy page. Home is present in both shells,
    // so we wait for it; if it never renders, we refresh the page (same recovery a user does on a blank sidebar) and re-wait.
    public void ensureSidebarLoaded() {
        if (driverTools.waitForElementToAppear(SidebarMenu.HOME.getLocator(), 10).isPresent()) {
            return;
        }
        logger.log("Sidebar appears blank (Home menu not rendered) - refreshing to restore it.");
        driverTools.refreshWebpage();
        driverTools.waitForElementToAppear(SidebarMenu.HOME.getLocator(), 15);
    }

    public boolean openSubmenuAndVerifySubject(SidebarSubmenu submenu) {
        // The expanded Finance menu pushes Customer Service off-screen, so scroll it into view before opening it.
        if (submenu.getParent() == SidebarMenu.CUSTOMER_SERVICE) {
            driverTools.scrollToElement(SidebarMenu.CUSTOMER_SERVICE.getLocator());
            driverTools.waitForElementToBeClickable(SidebarMenu.CUSTOMER_SERVICE.getLocator(), 5);
        }
        int maxClickAttempts = 3;
        for (int attempt = 1; attempt <= maxClickAttempts; attempt++) {
            try {
                // Extranet swaps between a React shell (<nav>) and legacy pages (div.menu); during that route change
                // the sidebar re-renders blank. Wait until it's back before touching any menu.
                ensureSidebarLoaded();
                if (driverTools.waitForElementToAppear(submenu.getLocator(), 3).isEmpty()) {
                    clickMenu(submenu.getParent());
                }
                Optional<WebElement> link = driverTools.waitForElementToBeClickable(submenu.getLocator(), 10);
                if (link.isEmpty()) {
                    continue;
                }
                // Submenu is a hover fly-out that closes when the cursor leaves the parent, so hover the parent and click the child in one gesture.
                Optional<WebElement> parentEl = driverTools.waitForElementToAppear(submenu.getParent().getLocator(), 3);
                if (parentEl.isPresent()) {
                    driverTools.hoverThenClick(parentEl.get(), link.get());
                } else {
                    driverTools.clickWithActions(link.get());
                }
                if (DISMISS_POPUPS_ON_OPEN.contains(submenu)) {
                    dismissFacilitiesCoachmarks();
                }
                boolean lastAttempt = attempt == maxClickAttempts;
                if (isSubmenuSubjectPresent(submenu, lastAttempt ? 10 : 5)) {
                    return true;
                }
            } catch (Exception e) {
                // The SPA re-renders/collapses the sidebar mid-gesture, so the cached element can go stale
                // (StaleElementReferenceException) or the parent menu may not be clickable in time
                // (NoSuchElementException from orElseThrow). Swallow and retry with a fresh re-locate.
                logger.log("Transient error while opening submenu, retrying: " + submenu.getParent() + " > " + submenu
                        + " (attempt " + attempt + "/" + maxClickAttempts + ") - " + e.getClass().getSimpleName());
            }
            logger.log("Submenu did not open on click, retrying: " + submenu.getParent() + " > " + submenu
                    + " (attempt " + attempt + "/" + maxClickAttempts + ")");
        }
        return false;
    }

    /** True only if EVERY subject locator for the submenu is present on the page. */
    public boolean isSubmenuSubjectPresent(SidebarSubmenu submenu) {
        return isSubmenuSubjectPresent(submenu, 10);
    }

    public boolean isSubmenuSubjectPresent(SidebarSubmenu submenu, int timeoutSeconds) {
        Locator[] subjects = submenu.getSubjectLocators();
        if (subjects.length == 0) {
            return false;
        }
        for (Locator subject : subjects) {
            if (driverTools.waitForElementToAppear(subject, timeoutSeconds).isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
