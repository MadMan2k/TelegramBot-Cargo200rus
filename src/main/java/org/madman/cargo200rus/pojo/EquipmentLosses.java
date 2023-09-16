package org.madman.cargo200rus.pojo;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EquipmentLosses {

    private static final String EQUIPMENT_URL = "https://raw.githubusercontent.com/PetroIvaniuk/"
            + "2022-Ukraine-Russia-War-Dataset/main/data/russia_losses_equipment.json";
    private static final int DAY_BEFORE = 2;
    private static final int SEVEN_DAYS_BEFORE = 8;
    private static final int THIRTY_DAYS_BEFORE = 31;

    private List<Map<String, Object>> parsedEquipment;

    private void parseEquipment() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.parsedEquipment = objectMapper.readValue(new URL(EQUIPMENT_URL), List.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param detailedReport selector between simple and detailed report
     * @return equipment losses as String
     */
    public String getEquipmentLosses(boolean detailedReport) {
        parseEquipment();
        Map<String, Object> totalEquipmentLosses = parsedEquipment.get(parsedEquipment.size() - 1);

        StringBuilder equipmentLosses = new StringBuilder();
        Locale ukraineLocale = new Locale("uk", "UA");
        NumberFormat numberFormat = NumberFormat.getNumberInstance(ukraineLocale);

        for (Map.Entry<String, Object> entry : totalEquipmentLosses.entrySet()) {
            if (entry.getValue() instanceof Integer && !entry.getKey().equals("day")) {

                int totalArticleLosses = getTotalArticleLosses(entry.getValue());

                appendTotalArticleLosses(equipmentLosses, totalArticleLosses, entry.getKey(), numberFormat);

                int yesterdayArticleLosses = getYesterdayArticleLosses(totalArticleLosses, entry.getKey());

                if (!detailedReport) {

                    appendYesterdayArticleLossesForSimpleReport(equipmentLosses, yesterdayArticleLosses, numberFormat);

                } else {

                    equipmentLosses.append("\n");

                    appendYesterdayArticleLossesForDetailedReport(
                            equipmentLosses, yesterdayArticleLosses, numberFormat);

                    int sevenDaysArticleLosses = getSevenDaysArticleLosses(totalArticleLosses, entry.getKey());

                    appendSevenDaysArticleLosses(equipmentLosses, sevenDaysArticleLosses, numberFormat);

                    int thirtyDaysArticleLosses = getThirtyDaysArticleLosses(totalArticleLosses, entry.getKey());

                    appendThirtyDaysArticleLosses(equipmentLosses, thirtyDaysArticleLosses, numberFormat);

                    equipmentLosses.append("\n").append("\n");

                }
            }
        }

        equipmentLosses.append("\n");

        return equipmentLosses.toString();

    }

    private int getTotalArticleLosses(Object value) {
        return Integer.parseInt(value.toString());
    }

    private void appendTotalArticleLosses(StringBuilder equipmentLosses,
                                          int totalArticleLosses,
                                          String articleName,
                                          NumberFormat numberFormat) {
        if (articleName.equals("MRL")) {
            equipmentLosses.append("<b>Multiple rocket launcher:</b> ");
        } else if (articleName.equals("APC")) {
            equipmentLosses.append("<b>Armoured fighting vehicle:</b> ");
        } else {
            equipmentLosses.append("<b>").append(articleName.substring(0, 1).toUpperCase(Locale.ROOT)
                    + articleName.substring(1)).append(": ").append("</b>");
        }

        String formattedTotalArticleLosses = numberFormat.format(totalArticleLosses);
        equipmentLosses.append(formattedTotalArticleLosses);
    }

    private int getYesterdayArticleLosses(int totalArticleLosses, String articleName) {
        int yesterdayArticleLosses;

        try {
            yesterdayArticleLosses = totalArticleLosses
                    - Integer.parseInt(parsedEquipment.get(parsedEquipment.size() - DAY_BEFORE)
                    .get(articleName).toString());
        } catch (NullPointerException e) {
            yesterdayArticleLosses = -1;
            System.out.println("NullPointerException for " + "'" + articleName
                    + "'. " + " No data for Day-1 losses. Replaced by 0");
        }

        return yesterdayArticleLosses;
    }

    private void appendYesterdayArticleLossesForSimpleReport(StringBuilder equipmentLosses,
                                                             int yesterdayArticleLosses,
                                                             NumberFormat numberFormat) {
        String formattedYesterdayArticleLosses;

        switch (yesterdayArticleLosses) {
            case -1:
                formattedYesterdayArticleLosses = "no data";
                equipmentLosses.append(" (").append(formattedYesterdayArticleLosses).append(")").append("\n");
                break;
            case 0:
                equipmentLosses.append("\n");
                break;
            default:
                formattedYesterdayArticleLosses = numberFormat.format(yesterdayArticleLosses);
                equipmentLosses.append(" (+").append(formattedYesterdayArticleLosses).append(")").append("\n");
        }
    }

    private void appendYesterdayArticleLossesForDetailedReport(StringBuilder equipmentLosses,
                                                               int yesterdayArticleLosses,
                                                               NumberFormat numberFormat) {
        String formattedYesterdayArticleLosses;

        switch (yesterdayArticleLosses) {
            case -1:
                formattedYesterdayArticleLosses = "no data";
                equipmentLosses.append(" ").append(formattedYesterdayArticleLosses).append(" [1d]");
                break;
            default:
                formattedYesterdayArticleLosses = numberFormat.format(yesterdayArticleLosses);
                equipmentLosses.append(" +").append(formattedYesterdayArticleLosses).append(" [1d]");
        }
    }

    private int getSevenDaysArticleLosses(int totalArticleLosses, String articleName) {
        int sevenDaysArticleLosses;

        try {
            sevenDaysArticleLosses = totalArticleLosses
                    - Integer.parseInt(parsedEquipment.get(parsedEquipment.size() - SEVEN_DAYS_BEFORE)
                    .get(articleName).toString());
        } catch (NullPointerException e) {
            sevenDaysArticleLosses = -1;
            System.out.println("NullPointerException for " + "'" + articleName
                    + "'. " + " No data for Day-7 losses. Replaced by 0");
        }

        return sevenDaysArticleLosses;
    }

    private void appendSevenDaysArticleLosses(StringBuilder equipmentLosses,
                                              int sevenDaysArticleLosses,
                                              NumberFormat numberFormat) {
        String formattedSevenDaysArticleLosses;

        switch (sevenDaysArticleLosses) {
            case -1:
                formattedSevenDaysArticleLosses = "no data";
                equipmentLosses.append("    ").append(formattedSevenDaysArticleLosses).append(" [7d]");
                break;
            default:
                formattedSevenDaysArticleLosses = numberFormat.format(sevenDaysArticleLosses);
                equipmentLosses.append("   +").append(formattedSevenDaysArticleLosses).append(" [7d]");
        }
    }

    private int getThirtyDaysArticleLosses(int totalArticleLosses, String articleName) {
        int thirtyDaysArticleLosses;

        try {
            thirtyDaysArticleLosses = totalArticleLosses
                    - Integer.parseInt(parsedEquipment.get(parsedEquipment.size() - THIRTY_DAYS_BEFORE)
                    .get(articleName).toString());
        } catch (NullPointerException e) {
            thirtyDaysArticleLosses = -1;
            System.out.println("NullPointerException for " + "'" + articleName
                    + "'. " + " No data for Day-30 losses. Replaced by 0");
        }

        return thirtyDaysArticleLosses;
    }

    private void appendThirtyDaysArticleLosses(StringBuilder equipmentLosses,
                                               int thirtyDaysArticleLosses,
                                               NumberFormat numberFormat) {
        String formattedSevenDaysArticleLosses;

        switch (thirtyDaysArticleLosses) {
            case -1:
                formattedSevenDaysArticleLosses = "no data";
                equipmentLosses.append("    ").append(formattedSevenDaysArticleLosses).append(" [30d]");
                break;
            default:
                formattedSevenDaysArticleLosses = numberFormat.format(thirtyDaysArticleLosses);
                equipmentLosses.append("   +").append(formattedSevenDaysArticleLosses).append(" [30d]");
        }
    }

}
