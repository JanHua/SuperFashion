package com.sf.util;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * 基本类型转化
 * <br/> 代理一部分容错效果
 *
 * @author WJH
 */
public class NumberUtil {

    /**
     * 积分转换，若有小数保留一位，则无小数 1.034 = 1.0 ,12 = 12
     *
     * @param obj
     * @return
     */
    public static String scoreFormat(Object obj) {
        return new DecimalFormat("#.#").format(parseFloat(obj));
    }

    /**
     * 表明四舍五入，保留digits小数（若是整数型不保留） 90.99 = 91
     *
     * @param obj
     * @param digits
     * @return
     */
    public static String floatToFractionDigits(Object obj, int digits) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(digits);
        return nf.format(parseFloat(obj));
    }

    /**
     * 表明四舍五入，保留digits小数(强制性保留)  90.99 = 91.0
     *
     * @param obj
     * @param digits
     * @return
     */
    public static String floatToBigDecimal(Object obj, int digits) {
        BigDecimal b = new BigDecimal(parseFloat(obj));
        return String.valueOf(b.setScale(digits, BigDecimal.ROUND_HALF_UP).floatValue());
    }

    /**
     * object to double
     *
     * @param obj obj
     * @return
     */
    public static double parseDouble(Object obj) {
        double count = 0;
        if (obj != null) {
            if (obj instanceof Integer) {
                count = ((Integer) obj).doubleValue();
            } else if (obj instanceof Long) {
                count = ((Long) obj).doubleValue();
            } else if (obj instanceof BigInteger) {
                count = ((BigInteger) obj).doubleValue();
            } else if (obj instanceof Float) {
                count = ((Float) obj).doubleValue();
            } else if (obj instanceof Double) {
                count = ((Double) obj).doubleValue();
            } else if (obj instanceof BigDecimal) {
                count = ((BigDecimal) obj).doubleValue();
            } else if (obj instanceof Byte) {
                count = ((Byte) obj).doubleValue();
            } else if (obj instanceof String) {
                if (TextUtils.isEmpty((String) obj)) {
                    return count;
                }
                try {
                    count = Double.parseDouble(((String) obj).trim());
                } catch (Exception e) {
                    return count;
                }
            }
        }

        return count;
    }

    /**
     * object to float
     *
     * @param obj obj
     * @return
     */
    public static float parseFloat(Object obj) {
        float count = 0;
        if (obj != null) {
            if (obj instanceof Integer) {
                count = ((Integer) obj).floatValue();
            } else if (obj instanceof Long) {
                count = ((Long) obj).floatValue();
            } else if (obj instanceof BigInteger) {
                count = ((BigInteger) obj).floatValue();
            } else if (obj instanceof Float) {
                count = ((Float) obj).floatValue();
            } else if (obj instanceof Double) {
                count = ((Double) obj).floatValue();
            } else if (obj instanceof BigDecimal) {
                count = ((BigDecimal) obj).floatValue();
            } else if (obj instanceof Byte) {
                count = ((Byte) obj).floatValue();
            } else if (obj instanceof String) {
                if (TextUtils.isEmpty((String) obj)) {
                    return count;
                }
                try {
                    count = Float.parseFloat(((String) obj).trim());
                } catch (Exception e) {
                    return count;
                }
            }
        }

        return count;
    }

    /**
     * object to long
     *
     * @param obj obj
     * @return
     */
    public static long parseLong(Object obj) {
        long count = 0;
        if (obj != null) {
            if (obj instanceof Integer) {
                count = ((Integer) obj).longValue();
            } else if (obj instanceof Long) {
                count = ((Long) obj);
            } else if (obj instanceof BigInteger) {
                count = ((BigInteger) obj).longValue();
            } else if (obj instanceof Float) {
                count = ((Float) obj).longValue();
            } else if (obj instanceof Double) {
                count = ((Double) obj).longValue();
            } else if (obj instanceof BigDecimal) {
                count = ((BigDecimal) obj).longValue();
            } else if (obj instanceof Byte) {
                count = ((Byte) obj).longValue();
            } else if (obj instanceof String) {
                if (TextUtils.isEmpty((String) obj)) {
                    return count;
                }
                try {
                    count = Long.parseLong(((String) obj).trim());
                } catch (Exception e) {
                    return count;
                }
            }
        }

        return count;
    }

    /**
     * object to int
     *
     * @param obj obj
     * @return
     */
    public static int parseInt(Object obj) {
        int count = 0;
        if (obj != null) {
            if (obj instanceof Integer) {
                count = ((Integer) obj).intValue();
            } else if (obj instanceof Long) {
                try {
                    count = ((Long) obj).intValue();
                } catch (Exception e) {
                }
            } else if (obj instanceof BigInteger) {
                count = ((BigInteger) obj).intValue();
            } else if (obj instanceof Float) {
                try {
                    count = ((Float) obj).intValue();
                } catch (Exception e) {
                }
            } else if (obj instanceof Double) {
                try {
                    count = ((Double) obj).intValue();
                } catch (Exception e) {
                }
            } else if (obj instanceof BigDecimal) {
                count = ((BigDecimal) obj).intValue();
            } else if (obj instanceof Byte) {
                count = ((Byte) obj).intValue();
            } else if (obj instanceof String) {
                if (TextUtils.isEmpty((String) obj)) {
                    return count;
                }
                try {
                    count = Integer.parseInt(((String) obj).trim());
                } catch (Exception e) {
                    return count;
                }
            }
        }
        return count;
    }

}
