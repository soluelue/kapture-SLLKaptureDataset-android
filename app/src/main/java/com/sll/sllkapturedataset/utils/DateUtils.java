package com.sll.sllkapturedataset.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    /** @return  mm:ss
     * @param mill unix time
     *  */
    public static String getMMCSS(long mill){
        long durationTime = System.currentTimeMillis() - mill;
        final DateFormat dateFormat = new SimpleDateFormat("mm:ss");
        String result = dateFormat.format(new Date(durationTime));
        return result;
    }

    /** @return MM_dd_HH_mm_ss_SSS */
    public static String getMM_dd_HH_mm_ss_SSS(long mill){
        final DateFormat dateFormat = new SimpleDateFormat("MM_dd_HH_mm_ss_SSS");
        String result = dateFormat.format(mill);
        return result;
    }

    /** @return  HH:mm:ss:SSS */
    public static String getHHCmmCssCSSS(long mill){
        final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS");
        String result = dateFormat.format(mill);
        return result;
    }

    /** @return MM/dd HH:mm:ss SSS */
    public static String getMMSddBHHCmmCssBSSS(long mill){
        final DateFormat dateFormat = new SimpleDateFormat("MM/dd HH:mm:ss SSS");
        String result = dateFormat.format(mill);
        return result;
    }
}
