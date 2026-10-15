package com.tiket.postprocessing;

import com.tiket.model.Platform;

public class HTMLTemplate {
    public static String bodyEmailOld(Platform platform, String startedBy, String buildNumber, String buildUrl, String extentReportUrl, String imgResult, String tableSummary) {
        return "<body style=\"font-family: 'Poppins', Arial, sans-serif;\">\n" +
                "    <table width=\"80%\" align=\"center\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"background:#f3f3f3\">\n" +
                "        <tr>\n" +
                "            <td align=\"center\" style=\"padding: 20px;\">\n" +
                "                <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"border-collapse: collapse; border: 1px solid #cccccc;\">\n" +
                "<tr>\n" +
                "    <td style=\"background-color: #95A6AD; padding: 15px; text-align: center; color: white; font-size: 20px; text-shadow:black 0.1em 0.1em 0.1em;\">\n" +
                "        <img src=\"https://ci3.googleusercontent.com/meips/ADKq_Nb7LytlCF-Buk9T47F6AJDjztOERcCeXrSPq_hxeSRhcRQBad-YDzlzf4BGGhg4pv55UhDQDqxjvig3hStbMFuCCFvjeBAaGBU-fvlXfEuOr28J7ATC-7Y-_xkQe4Irddg-k0Utrf1NryF7KmZy7Nx8lftGNfPoe0ukEjkNjRZbdNmxBGcF3CH8eQcQCbPYQoCqx1IsWgisrOYqF2n2SBNU8giJODl2ZcOZ6AsC0Ws7JZSWFrrS3m1zIoNJqmzlyry60Bv9gOkiwA=s0-d-e1-ft#https://s-light.tiket.photos/t/01E25EBZS3W0FY9GTG6C42E1SE/original/assets/order/2024/08/06/df78c089-7721-4356-9bdf-81afe9f41700-1722931345843-413d3b92f5d3b5cab287806e98f0fb57.png\" alt=\"Company Logo\" style=\"width: 120px; display: block; margin: 0 auto;\">\n" +
                "        <div style=\"margin-top: 10px; font-size: 24px; font-weight: bold;\">\n" +
                "            ("+ String.valueOf(platform).toUpperCase() +") App Automation Test Report - Detailed Summary\n" +
                "        </div>\n" +
                "    </td>\n" +
                "</tr>\n" +
                "\n" +
                "<!-- Introduction and Links in Columns -->\n" +
                "          <tr>\n" +
                "            <td style=\"padding: 5px 40px; text-align: left; font-size: 12px; line-height: 1;\">\n" +
                "              <table width=\"100%\">\n" +
                "                <tr>\n" +
                "                  <!--<td style=\"color: #345C72; vertical-align: top; text-align:left\">Started by: <strong>"+startedBy+"</strong>\n" +
                "                  </td>-->\n" +
                "                  <td style=\"vertical-align: top; text-align:right\">\n" +
                "                    <a style=\"color: #345C72\" href='"+buildUrl+"' target=\"_blank\">Build Number: <strong>#"+buildNumber+"</strong></a>\n" +
                "                  </td>\n" +
                "                </tr>\n" +
                "              </table>\n" +
                "            </td>\n" +
                "          </tr>" +
                "<!-- Greetings Section -->\n" +
                "          <tr>\n" +
                "            <td style=\"padding: 20px 40px 0px; text-align: left; font-size: 16px; line-height: 1.6;\"> Hello Team, <br>\n" +
                "              <br>Here is the test report generated as part of this build process, capturing the current results of our App automation suite. Please see the details below for further insights. <br>\n" +
                "              <br>\n" +
                "            </td>\n" +
                "          </tr>" +
                "<!-- Action Button -->\n" +
                "          <tr>\n" +
                "            <td class=\"button-section\" style=\"text-align: center;\">\n" +
                "              <a href='"+extentReportUrl+"' target=\"_blank\">\n" +
                "                <button style=\"background-color: #345C72; color: white; padding: 10px 20px; border: none; border-radius: 5px; font-weight: bold;\"> View Full Extent Report </button>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>" +
                "<!-- Test Execution Result Image -->\n" +
                "          <tr>\n" +
                "            <td align=\"center\" style=\"padding: 20px;\">\n" +
                "              <p style=\"font-size: 18px; color: #345C72;\">Test Execution Result</p>\n" +
                "              <img src='"+imgResult+"' alt=\"Test Result Image\" style=\"border: 1px solid #cccccc; width: 100%; max-width: 500px;\">\n" +
                "            </td>\n" +
                "          </tr>" +
                "<!-- Summary Title Before Table -->\n" +
                "          <tr>\n" +
                "            <td style=\"padding: 10px 40px 0px; font-size: 18px; color: #345C72;\"><br><center>Tribe Wise Summary</center></td>\n" +
                "          </tr>" +
                "\n" +
                "<!-- Test Result Table -->" +
                tableSummary +
                "\n" +
                "<!-- Footer with Logo -->\n" +
                "         <tr>\n" +
                "            <td style=\"padding: 10px 40px; text-align: left; font-size: 16px; line-height: 1.6;\"> Cheers, <br> SDET Team <br>\n" +
                "              <br>\n" +
                "            </td>\n" +
                "         </tr>" +
                "         <tr>\n" +
                "            <td style=\"background-color: #95A6AD; padding: 20px; text-align: center; color: white; font-size: 14px;\">\n" +
                "              <img src=\"https://ci3.googleusercontent.com/meips/ADKq_Nb7LytlCF-Buk9T47F6AJDjztOERcCeXrSPq_hxeSRhcRQBad-YDzlzf4BGGhg4pv55UhDQDqxjvig3hStbMFuCCFvjeBAaGBU-fvlXfEuOr28J7ATC-7Y-_xkQe4Irddg-k0Utrf1NryF7KmZy7Nx8lftGNfPoe0ukEjkNjRZbdNmxBGcF3CH8eQcQCbPYQoCqx1IsWgisrOYqF2n2SBNU8giJODl2ZcOZ6AsC0Ws7JZSWFrrS3m1zIoNJqmzlyry60Bv9gOkiwA=s0-d-e1-ft#https://s-light.tiket.photos/t/01E25EBZS3W0FY9GTG6C42E1SE/original/assets/order/2024/08/06/df78c089-7721-4356-9bdf-81afe9f41700-1722931345843-413d3b92f5d3b5cab287806e98f0fb57.png\" alt=\"Company Logo\" style=\"width: 100px; display: block; margin: 0 auto;\">\n" +
                "              <div style=\"margin-top: 10px;\">Need help? You can slack our team <b style='color: #003355; text-shadow:white 0.1em 0.1em 0.1em;'>@sdet_app</b> !</div>\n" +
                "            </td>\n" +
                "          </tr>" +
                "        </table>\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </table>\n" +
                "</body>";
    }

    public static String bodyEmail(Platform platform, String startedBy, String buildNumber, String buildUrl, String extentReportUrl, String imgResult, String tableSummary) {
        return buildEmailBody(platform, startedBy, buildNumber, buildUrl, extentReportUrl,
                buildImageSection(imgResult), tableSummary);
    }

    /**
     * Fallback variant of {@link #bodyEmail}. Used when the dashboard screenshot
     * could not be rendered. The {@code resultBlockHtml} parameter is embedded
     * directly into the "Test Execution Result" section instead of being treated
     * as an image source. All other sections are identical to {@link #bodyEmail}.
     */
    public static String bodyEmailFallback(Platform platform, String startedBy, String buildNumber, String buildUrl, String extentReportUrl, String resultBlockHtml, String tableSummary) {
        return buildEmailBody(platform, startedBy, buildNumber, buildUrl, extentReportUrl,
                buildResultBlockSection(resultBlockHtml), tableSummary);
    }

    /**
     * Shared scaffold for {@link #bodyEmail} and {@link #bodyEmailFallback}. Renders
     * the common header, build info, greeting, action button, summary, and footer.
     * The caller supplies a fully-rendered {@code resultSectionHtml} (e.g. an image
     * row or a fallback HTML block) that is dropped into the "Test Execution Result"
     * slot so the two callers don't drift out of sync on future template edits.
     */
    private static String buildEmailBody(Platform platform, String startedBy, String buildNumber, String buildUrl, String extentReportUrl, String resultSectionHtml, String tableSummary) {
        return "<body style=\"font-family: 'Poppins', Arial, sans-serif;\">\n" +
                "    <table width=\"80%\" align=\"center\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"background:#f3f3f3\">\n" +
                "        <tr>\n" +
                "            <td align=\"center\" style=\"padding: 20px;\">\n" +
                "                <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"border-collapse: collapse; border: 1px solid #cccccc;\">\n" +
                "<tr>\n" +
                "    <td style=\"background-color: #95A6AD; padding: 15px; text-align: center; color: white; font-size: 20px; text-shadow:black 0.1em 0.1em 0.1em;\">\n" +
                "        <img src=\"https://ci3.googleusercontent.com/meips/ADKq_Nb7LytlCF-Buk9T47F6AJDjztOERcCeXrSPq_hxeSRhcRQBad-YDzlzf4BGGhg4pv55UhDQDqxjvig3hStbMFuCCFvjeBAaGBU-fvlXfEuOr28J7ATC-7Y-_xkQe4Irddg-k0Utrf1NryF7KmZy7Nx8lftGNfPoe0ukEjkNjRZbdNmxBGcF3CH8eQcQCbPYQoCqx1IsWgisrOYqF2n2SBNU8giJODl2ZcOZ6AsC0Ws7JZSWFrrS3m1zIoNJqmzlyry60Bv9gOkiwA=s0-d-e1-ft#https://s-light.tiket.photos/t/01E25EBZS3W0FY9GTG6C42E1SE/original/assets/order/2024/08/06/df78c089-7721-4356-9bdf-81afe9f41700-1722931345843-413d3b92f5d3b5cab287806e98f0fb57.png\" alt=\"Company Logo\" style=\"width: 120px; display: block; margin: 0 auto;\">\n" +
                "        <div style=\"margin-top: 10px; font-size: 24px; font-weight: bold;\">\n" +
                "            ("+ String.valueOf(platform).toUpperCase() +") App Automation Test Report - Detailed Summary\n" +
                "        </div>\n" +
                "    </td>\n" +
                "</tr>\n" +
                "\n" +
                "<!-- Introduction and Links in Columns -->\n" +
                "          <tr>\n" +
                "            <td style=\"padding: 5px 40px; text-align: left; font-size: 12px; line-height: 1;\">\n" +
                "              <table width=\"100%\">\n" +
                "                <tr>\n" +
                "                  <!--<td style=\"color: #345C72; vertical-align: top; text-align:left\">Started by: <strong>"+startedBy+"</strong>\n" +
                "                  </td>-->\n" +
                "                  <td style=\"vertical-align: top; text-align:right\">\n" +
                "                    <a style=\"color: #345C72\" href='"+buildUrl+"' target=\"_blank\">Build Number: <strong>#"+buildNumber+"</strong></a>\n" +
                "                  </td>\n" +
                "                </tr>\n" +
                "              </table>\n" +
                "            </td>\n" +
                "          </tr>" +
                "<!-- Greetings Section -->\n" +
                "          <tr>\n" +
                "            <td style=\"padding: 20px 40px 0px; text-align: left; font-size: 16px; line-height: 1.6;\"> Hello Team, <br>\n" +
                "              <br>Here is the test report generated as part of this build process, capturing the current results of our App automation suite. Please see the details below for further insights. <br>\n" +
                "              <br>\n" +
                "            </td>\n" +
                "          </tr>" +
                "<!-- Action Button -->\n" +
                "          <tr>\n" +
                "            <td class=\"button-section\" style=\"text-align: center;\">\n" +
                "              <a href='"+extentReportUrl+"' target=\"_blank\">\n" +
                "                <button style=\"background-color: #345C72; color: white; padding: 10px 20px; border: none; border-radius: 5px; font-weight: bold;\"> View Full Extent Report </button>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>" +
                "<!-- Test Execution Result -->\n" +
                resultSectionHtml +
                "<!-- Summary Section -->\n" +
                buildSummarySection(tableSummary) +
                "\n" +
                "<!-- Footer with Logo -->\n" +
                "         <tr>\n" +
                "            <td style=\"padding: 10px 40px; text-align: left; font-size: 16px; line-height: 1.6;\"> Cheers, <br> SDET Team <br>\n" +
                "              <br>\n" +
                "            </td>\n" +
                "         </tr>" +
                "         <tr>\n" +
                "            <td style=\"background-color: #95A6AD; padding: 20px; text-align: center; color: white; font-size: 14px;\">\n" +
                "              <img src=\"https://ci3.googleusercontent.com/meips/ADKq_Nb7LytlCF-Buk9T47F6AJDjztOERcCeXrSPq_hxeSRhcRQBad-YDzlzf4BGGhg4pv55UhDQDqxjvig3hStbMFuCCFvjeBAaGBU-fvlXfEuOr28J7ATC-7Y-_xkQe4Irddg-k0Utrf1NryF7KmZy7Nx8lftGNfPoe0ukEjkNjRZbdNmxBGcF3CH8eQcQCbPYQoCqx1IsWgisrOYqF2n2SBNU8giJODl2ZcOZ6AsC0Ws7JZSWFrrS3m1zIoNJqmzlyry60Bv9gOkiwA=s0-d-e1-ft#https://s-light.tiket.photos/t/01E25EBZS3W0FY9GTG6C42E1SE/original/assets/order/2024/08/06/df78c089-7721-4356-9bdf-81afe9f41700-1722931345843-413d3b92f5d3b5cab287806e98f0fb57.png\" alt=\"Company Logo\" style=\"width: 100px; display: block; margin: 0 auto;\">\n" +
                "              <div style=\"margin-top: 10px;\">Need help? You can slack our team <b style='color: #003355; text-shadow:white 0.1em 0.1em 0.1em;'>@sdet_app</b> !</div>\n" +
                "            </td>\n" +
                "          </tr>" +
                "        </table>\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </table>\n" +
                "</body>";
    }

    private static String buildResultBlockSection(String resultBlockHtml) {
        if (isEmpty(resultBlockHtml)) {
            return buildImageSection(null);
        }
        return "<tr>\n" +
                "  <td align=\"center\" style=\"padding: 20px;\">\n" +
                "      <p style=\"font-size: 18px; color: #345C72;\">Test Execution Result</p>\n" +
                resultBlockHtml +
                "  </td>\n" +
                "</tr>";
    }

    private static boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static String buildImageSection(String imgResult) {
        if (isEmpty(imgResult)) {
            return
                    "<tr>" +
                            "  <td align='center' style='padding: 20px;'>" +
                            "    <div style=\"border: 1px solid #ffcccc; background: #ffeeee; padding: 15px; border-radius: 5px; color: #cc0000; font-size: 14px;\">" +
                            "      Unable to load Test Execution Result image.<br>" +
                            "      Please check the full Extent Report in Jenkins for more details." +
                            "    </div>" +
                            "  </td>" +
                            "</tr>";
        }

        return
                "<tr>\n" +
                        "  <td align=\"center\" style=\"padding: 20px;\">\n" +
                        "      <p style=\"font-size: 18px; color: #345C72;\">Test Execution Result</p>\n" +
                        "      <img src='" + imgResult + "' alt=\"Test Result Image\" style=\"border: 1px solid #cccccc; width: 100%; max-width: 500px;\">\n" +
                        "  </td>\n" +
                        "</tr>";
    }

    private static String buildSummarySection(String tableSummary) {
        if (isEmpty(tableSummary)) {
            return
                    "<tr>" +
                            "  <td align='center' style='padding: 20px;'>" +
                            "    <div style=\"border: 1px solid #ffcccc; background: #ffeeee; padding: 15px; border-radius: 5px; color: #cc0000; font-size: 14px;\">" +
                            "      Unable to load Tribe Wise Summary table.<br>" +
                            "      Please check the full Extent Report in Jenkins for more details." +
                            "    </div>" +
                            "  </td>" +
                            "</tr>";
        }

        return
                "<tr>\n" +
                        "  <td style=\"padding: 10px 40px 0px; font-size: 18px; color: #345C72;\"><br><center>Tribe Wise Summary</center></td>\n" +
                        "</tr>\n"
                        +
                        tableSummary;
    }
}
