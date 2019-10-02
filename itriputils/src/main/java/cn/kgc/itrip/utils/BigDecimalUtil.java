package cn.kgc.itrip.utils;

import java.math.BigDecimal;

public class BigDecimalUtil
{
    private BigDecimalUtil(){

    }

    /**
     * 两数相加
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal add(double v1 , double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2);
    }

    /**
     * 两数相减
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal sub(double v1 , double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2);
    }

    /**
     * 两数相乘
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal mul(double v1 , double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2);
    }

    /**
     * 两数相除 在做除法运算的时候可能会出现除不尽的问题
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal div(double v1 , double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        //四舍五入 ，保留两位小数
        return b1.divide(b2,2,BigDecimal.ROUND_HALF_UP);
    }

}
