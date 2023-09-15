package org.madman.cargo200rus.pojo;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
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

        for (Map.Entry<String, Object> entry : totalEquipmentLosses.entrySet()) {
            if (entry.getValue() instanceof Integer && !entry.getKey().equals("day")) {
                if (entry.getKey().equals("MRL")) {
                    equipmentLosses.append("<b>Multiple rocket launcher:</b> ");
                } else if (entry.getKey().equals("APC")) {
                    equipmentLosses.append("<b>Armoured fighting vehicle:</b> ");
                } else {
                    equipmentLosses.append("<b>").append(entry.getKey().substring(0, 1).toUpperCase(Locale.ROOT)
                            + entry.getKey().substring(1)).append(": ").append("</b>");
                }

                int totalArticleLosses = Integer.parseInt(entry.getValue().toString());

                int yesterdayArticleLosses = 0;

                try {
                    yesterdayArticleLosses = totalArticleLosses
                            - Integer.parseInt(parsedEquipment.get(parsedEquipment.size() - DAY_BEFORE)
                            .get(entry.getKey()).toString());
                } catch (NullPointerException e) {
                    System.out.println("NullPointerException for " + "'" + entry.getKey() + "'. " + " No data for Day-1 losses. Replaced by 0");
                }

                int sevenDaysArticleLosses = 0;
                int thirtyDaysArticleLosses = 0;

                if (detailedReport) {

                    try {
                        sevenDaysArticleLosses = totalArticleLosses
                                - Integer.parseInt(parsedEquipment.get(parsedEquipment.size() - SEVEN_DAYS_BEFORE)
                                .get(entry.getKey()).toString());
                    } catch (NullPointerException e) {
                        System.out.println("NullPointerException for " + "'" + entry.getKey() + "'. " + " No data for Day-7 losses. Replaced by 0");
                    }

                    try {
                        thirtyDaysArticleLosses = totalArticleLosses
                                - Integer.parseInt(parsedEquipment.get(parsedEquipment.size() - THIRTY_DAYS_BEFORE)
                                .get(entry.getKey()).toString());
                    } catch (NullPointerException e) {
                        System.out.println("NullPointerException for " + "'" + entry.getKey() + "'. " + " No data for Day-30 losses. Replaced by 0");
                    }


                }

                equipmentLosses.append(totalArticleLosses);

                if (detailedReport) {
                    equipmentLosses.append("\n")
                            .append("+").append(yesterdayArticleLosses).append(" [1d]   +")
                            .append(sevenDaysArticleLosses).append(" [7d]   +")
                            .append(thirtyDaysArticleLosses).append(" [30d]").append("\n").append("\n");
                } else {
                    if (yesterdayArticleLosses != 0) {
                        equipmentLosses.append(" (+").append(yesterdayArticleLosses).append(")").append("\n");
                    } else {
                        equipmentLosses.append("\n");
                    }
                }


            }
        }

        equipmentLosses.append("\n");

        return equipmentLosses.toString();

    }

}
