package stu.num;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 字符串转固定小数位金额
 * @author payne
 *
 */
public class BigDecimalTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String amt = "1.021312";
		Double amt1 = 1.1;
		System.out.println(formatAmt(amt, 2).toString());
		System.out.println(formatAmt(amt1.toString(), 2).toString());
				
	}
	public static boolean isNothing(String s) {
		if (s == null || s.trim().length() < 1)
			return true;
		return false;
	}
	public static String formatAmt(String amt, int scale) {
		if (isNothing(amt))
			return "";
		BigDecimal dec = new BigDecimal(amt);
		if (scale < 0)
			scale = 2;
		String format = ".";
		if (scale == 0)
			format = "";
		else
			for (int i = 0; i < scale; i++) {
				format += "0";
			}
		DecimalFormat fmt = new DecimalFormat("0" + format);
		return fmt.format(dec);
	}
	
	/**
     * 将数据转变为给定精度的数字
     * @param x
     * @param scale
     * @return
     */
    public static double round(double x, int scale) {       
        BigDecimal a = new BigDecimal(String.valueOf(x));
        a = a.divide(new BigDecimal("1"), scale, BigDecimal.ROUND_HALF_UP);       
        return a.doubleValue();
    }
}
