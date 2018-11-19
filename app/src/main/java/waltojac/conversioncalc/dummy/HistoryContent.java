package waltojac.conversioncalc.dummy;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.*;

public class HistoryContent {
    public static final List<HistoryItem> ITEMS = new ArrayList<HistoryItem>();

    public static void addItem(HistoryItem item) {
        ITEMS.add(item);
    }

    public static class HistoryItem {
        public final Double fromVal;
        public final Double toVal;
        public final String mode;
        public final String fromUnits;
        public final String toUnits;
        public String _key;



        public final String timestamp;

        public HistoryItem() {
            this.fromVal = 1.0;
            this.toVal = 0.0;
            this.mode = "Length";
            this.fromUnits = "Yards";
            this.toUnits = "Meters";
            this.timestamp = "1-0-1970";
        }

        public HistoryItem(Double fromVal, Double toVal, String mode,
                           String fromUnits, String toUnits, DateTime timestamp) {
            this.fromVal = fromVal;
            this.toVal = toVal;
            this.mode = mode;
            this.fromUnits = fromUnits;
            this.toUnits = toUnits;
            DateTimeFormatter fmt = ISODateTimeFormat.dateTime();

            this.timestamp = fmt.print(timestamp);
        }


        @Override
        public String toString() {
            return this.fromVal + " " + this.fromUnits + " = " + this.toVal + " " + this.toUnits;
        }
    }
}
