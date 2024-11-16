package co.edu.uptc.helpers;

import java.time.LocalDate;
import java.time.Period;

public class UtilDate {
    public static int calculateAge(LocalDate birthday) {
        LocalDate today = LocalDate.now();
        Period period = Period.between(birthday, today);
        return period.getYears();
    }
}