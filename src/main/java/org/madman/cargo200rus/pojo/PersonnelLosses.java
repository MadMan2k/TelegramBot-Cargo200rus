package org.madman.cargo200rus.pojo;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PersonnelLosses {

    private static final String PERSONNEL_URL = "https://raw.githubusercontent.com/PetroIvaniuk/"
            + "2022-Ukraine-Russia-War-Dataset/main/data/russia_losses_personnel.json";
    private static final int DAY_BEFORE = 2;
    private static final int SEVEN_DAYS_BEFORE = 8;
    private static final int THIRTY_DAYS_BEFORE = 31;

    private List<Map<String, Object>> parsedPersonnel;

    private void parsePersonnel() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.parsedPersonnel = objectMapper.readValue(new URL(PERSONNEL_URL), List.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getDataDate() {
        Date dataDate = new Date();
        try {
            dataDate = new SimpleDateFormat("yyyy-MM-dd").
                    parse(parsedPersonnel.get(parsedPersonnel.size() - 1).get("date").toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(dataDate);
    }

    private int getDayOfWar() {
        return Integer.parseInt(parsedPersonnel.get(parsedPersonnel.size() - 1).get("day").toString());
    }

    /**
     * @param detailedReport selector between simple and detailed report
     * @return personnel losses as String
     */
    public String getPersonnelLosses(boolean detailedReport) {
        parsePersonnel();
        String dataDate = getDataDate();
        int dayOfWar = getDayOfWar();

        int totalPersonnelLosses =
                Integer.parseInt(parsedPersonnel.get(parsedPersonnel.size() - 1).get("personnel").toString());
        int yesterdayPersonnelLosses =
                totalPersonnelLosses - Integer.parseInt(parsedPersonnel.get(parsedPersonnel.size() - DAY_BEFORE)
                        .get("personnel").toString());

        int sevenDaysPersonnelLosses = 0;
        int thirtyDaysPersonnelLosses = 0;


        if (detailedReport) {
            sevenDaysPersonnelLosses =
                    totalPersonnelLosses - Integer.parseInt(parsedPersonnel
                            .get(parsedPersonnel.size() - SEVEN_DAYS_BEFORE).get("personnel").toString());
            thirtyDaysPersonnelLosses =
                    totalPersonnelLosses - Integer.parseInt(parsedPersonnel
                            .get(parsedPersonnel.size() - THIRTY_DAYS_BEFORE).get("personnel").toString());
        }

        Locale ukraineLocale = new Locale("uk", "UA");
        NumberFormat numberFormat = NumberFormat.getNumberInstance(ukraineLocale);

        String formattedTotalPersonnelLosses = numberFormat.format(totalPersonnelLosses);
        String formattedYesterdayPersonnelLosses = numberFormat.format(yesterdayPersonnelLosses);
        String formattedSevenDaysPersonnelLosses = numberFormat.format(sevenDaysPersonnelLosses);
        String formattedThirtyDaysPersonnelLosses = numberFormat.format(thirtyDaysPersonnelLosses);


        StringBuilder personnelLosses = new StringBuilder();
        personnelLosses.append("<b>").append("Date: ").append(dataDate).append("\n")
                .append("Invasion day: ").append(dayOfWar).append("\n").append("\n")
                .append("Russian losses from 24/02/2022 :").append("\n").append("\n")
                .append("Personnel: ").append("</b>").append(formattedTotalPersonnelLosses);

        if (detailedReport) {
            personnelLosses.append("\n").append("+")
                    .append(formattedYesterdayPersonnelLosses).append(" [1d]   +").append(formattedSevenDaysPersonnelLosses)
                    .append(" [7d]   +").append(formattedThirtyDaysPersonnelLosses).append(" [30d]").append("\n").append("\n");
        } else {
            if (yesterdayPersonnelLosses != 0) {
                personnelLosses.append(" (+").append(formattedYesterdayPersonnelLosses).append(")").append("\n");
            } else {
                personnelLosses.append("\n");
            }
        }
        return personnelLosses.toString();
    }
}
