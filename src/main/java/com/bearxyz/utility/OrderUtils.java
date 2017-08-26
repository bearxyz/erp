package com.bearxyz.utility;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bearxyz on 2017/8/23.
 */
public class OrderUtils {

    public static String genSerialnumber(String prefix) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String serialnumber = sdf.format(date);
        return prefix + serialnumber;
    }

}
