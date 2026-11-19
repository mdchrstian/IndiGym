package id.indigym.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class Rupiah {

    public static String format(BigDecimal nilai) {
        DecimalFormatSymbols simbol = new DecimalFormatSymbols();
        simbol.setGroupingSeparator('.');
        simbol.setDecimalSeparator(',');
        DecimalFormat pola = new DecimalFormat("#,##0", simbol);
        return "Rp " + pola.format(nilai);
    }
}
