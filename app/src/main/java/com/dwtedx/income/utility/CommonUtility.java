package com.dwtedx.income.utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.widget.NestedScrollView;
import android.telephony.TelephonyManager;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.dwtedx.income.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtility {

    private final static String TAG = "CommonUtility";

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    /**
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: getCurrentTimeHms
     * @Description: 当前时间
     * @author qinyl http://dwtedx.com
     */
    public static String getCurrentDate() {

        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd", new Locale("zh", "CN"));
        return formatDate.format(new Date());

		/*Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);// 获取年份
		int month = ca.get(Calendar.MONTH);// 获取月份
		int day = ca.get(Calendar.DATE);// 获取日
		int minute = ca.get(Calendar.MINUTE);// 分
		int hour = ca.get(Calendar.HOUR);// 小时
		int second = ca.get(Calendar.SECOND);// 秒

		return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;*/
    }

    /**
     * 时间戳转化成时间
     *
     * @param time
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String longToDate(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date(time));

    }

    /**
     * 时间戳转化成时间
     * 精确到分
     *
     * @param time
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String longToDateS(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(new Date(time));

    }

    /**
     * Date转String
     *
     * @param date
     * @return
     */
    public static String stringDateFormart(Date date) {
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("zh", "CN"));
        return formatDate.format(date);
    }

    /**
     * Date转String MM-dd
     *
     * @param date
     * @return
     */
    public static String stringDateFormartMMdd(Date date) {
        SimpleDateFormat formatDate = new SimpleDateFormat("MM月dd日", new Locale("zh", "CN"));
        String value = formatDate.format(date);
        return value;
    }

    /**
     * Date转String yyy-MM-dd
     *
     * @param date
     * @return
     */
    public static String stringDateFormartYYYYMMdd(Date date) {
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd", new Locale("zh", "CN"));
        String value = formatDate.format(date);
        return value;
    }

    /**
     * Date转String
     *
     * @param date
     * @return
     */
    public static String stringDateFormart(String date, String formart) {
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("zh", "CN"));
        SimpleDateFormat formatDate1 = new SimpleDateFormat(formart, new Locale("zh", "CN"));
        Date time = null;
        try {
            time = formatDate.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatDate1.format(time);
    }

    @SuppressLint("SimpleDateFormat")
    public static int compareDate(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                //System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                //System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return 0;
        }
    }

    /**
     * @param @param  day
     * @param @return 设定文件
     * @return Date    返回类型
     * @throws
     * @Title: getDateByCondition
     * @Description: 当前时间相对的某一天
     * @author qinyl http://dwtedx.com
     */
    public static Date getDateByCondition(int day) {
        Calendar calendar = Calendar.getInstance();//此时打印它获取的是系统当前时间
        calendar.add(Calendar.DATE, day);    //得到前一天
        return calendar.getTime();
    }

    /**
     * @param @param  str_input
     * @param @param  rDateFormat
     * @param @return 设定文件
     * @return boolean    返回类型
     * @throws
     * @Title: isDate
     * @Description: 判断是否为合法的日期时间字符串
     * @author qinyl http://dwtedx.com
     */
    public static boolean isDate(String str_input, String rDateFormat) {
        if (!isNull(str_input)) {
            SimpleDateFormat formatter = new SimpleDateFormat(rDateFormat, new Locale("zh", "CN"));
            formatter.setLenient(false);
            try {
                formatter.format(formatter.parse(str_input));
            } catch (Exception e) {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: getTempFileName
     * @Description: 当前时间的jpg名称
     * @author qinyl http://dwtedx.com
     */
    public static String getTempImageName() {
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);// 获取年份
        int month = ca.get(Calendar.MONTH) + 1;// 获取月份
        int day = ca.get(Calendar.DATE);// 获取日
        int minute = ca.get(Calendar.MINUTE);// 分
        int hour = ca.get(Calendar.HOUR);// 小时
        int second = ca.get(Calendar.SECOND);// 秒
        return "IMG_" + year + month + day + "_" + hour + minute + second + ".jpg";
    }

    /**
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: getTempFileName
     * @Description: 当前时间的mp4名称
     * @author qinyl http://dwtedx.com
     */
    public static String getTempVideoName() {
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);// 获取年份
        int month = ca.get(Calendar.MONTH) + 1;// 获取月份
        int day = ca.get(Calendar.DATE);// 获取日
        int minute = ca.get(Calendar.MINUTE);// 分
        int hour = ca.get(Calendar.HOUR);// 小时
        int second = ca.get(Calendar.SECOND);// 秒
        return "VODEO_" + year + month + day + "_" + hour + minute + second + ".mp4";
    }

    /**
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: getTempFileName
     * @Description: 当前时间的amr名称
     * @author qinyl http://dwtedx.com
     */
    public static String getTempVoiceName() {
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);// 获取年份
        int month = ca.get(Calendar.MONTH) + 1;// 获取月份
        int day = ca.get(Calendar.DATE);// 获取日
        int minute = ca.get(Calendar.MINUTE);// 分
        int hour = ca.get(Calendar.HOUR);// 小时
        int second = ca.get(Calendar.SECOND);// 秒
        return "VODEO_" + year + month + day + "_" + hour + minute + second + ".amr";
    }

    /**
     * 将指定byte数组转换成16进制字符串
     *
     * @param b
     * @return
     */
    @SuppressLint("DefaultLocale")
    public static String byteToHexString(byte[] b) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            hexString.append(hex.toUpperCase());
        }
        return hexString.toString();
    }

    /**
     * 生成0-9的随机数
     *
     * @param count 生成几位数（6位）
     * @return random 随机数
     */

    public static String getRandomNumber(int count) {
        String random = "";
        for (int i = 0; i < count; i++) {
            String str = String.valueOf((int) (Math.random() * 10 - 1));
            random = random + str;
        }
        return random;
    }

    /**
     * @param @param  phoneNumber
     * @param @return 设定文件
     * @return boolean 返回类型
     * @throws
     * @Title: isPhoneNumberValid
     * @Description: 验证号码 手机号 固话均可
     * @author qinyl
     * @date 2014年6月20日 下午3:16:03
     */
    public static boolean isPhoneNumberValid(String phoneNumber) {
        if (null == phoneNumber) {
            return false;
        }
        String expression = "((^(13|15|18|17)[0-9]{9}$)|(^0[1,2]{1}\\d{1}-?\\d{8}$)|"
                + "(^0[3-9] {1}\\d{2}-?\\d{7,8}$)|(^0[1,2]{1}\\d{1}-?\\d{8}-(\\d{1,4})$)|"
                + "(^0[3-9]{1}\\d{2}-? \\d{7,8}-(\\d{1,4})$))";
        CharSequence inputStr = phoneNumber;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    /**
     * @param @param phoneNumber
     * @return boolean 返回类型
     * @throws
     * @Title: isHttpURLValid
     * @Description: 验证 url
     * @author qinyl
     * @date 2015年10月20日 下午3:16:03
     */
    public static boolean isHttpURLValid(String httpUrl) {
        if (null == httpUrl) {
            return false;
        }
        String expression = "(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.," +
                "@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?";
        CharSequence inputStr = httpUrl;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 校验银行卡卡号
     *
     * @param cardId
     * @return
     */
    public static boolean checkBankCard(String cardId) {
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;
    }

    /**
     * 验证身份证号是否符合规则
     *
     * @param text 身份证号
     * @return
     */
    public static boolean personIdValidation(String text) {
        String regx = "[0-9]{17}x";
        String reg1 = "[0-9]{15}";
        String regex = "[0-9]{18}";
        return text.matches(regx) || text.matches(reg1) || text.matches(regex);
    }

    /**
     * 判断输入是否是中文
     *
     * @param input
     * @return
     */
    public static boolean isInputChinese(String input) {
        Pattern pattern = Pattern.compile("^[\u4e00-\u9fa5]*$");
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();//true全部为汉字，否则是false
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    /**
     * 判断邮箱是否合法
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (null == email || "".equals(email))
            return false;
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }


    /**
     * @param @param  src
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: bytesToHexString
     * @Description: byte 转 hexString
     * @author qinyl http://dwtedx.com
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }


    /**
     * @param @param  context
     * @param @return 设定文件
     * @return boolean    返回类型
     * @throws
     * @Title: isOPenGPS
     * @Description: 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     * @author qinyl http://dwtedx.com
     */
    public static final boolean isOPenGPS(final Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context
                .LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }

    /**
     * @param @param  value
     * @param @param  formart
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: floatToStr
     * @Description: format as "##0.00"
     * @author qinyl http://dwtedx.com
     */
    public static String floatToStr(float value, String formart) {
        DecimalFormat fnum = new DecimalFormat(formart);
        return fnum.format(value);
    }

    /**
     * @param @param  value
     * @param @param  bit
     * @param @return 设定文件
     * @return float    返回类型
     * @throws
     * @Title: floatFormart
     * @Description: floatFormart
     * @author qinyl http://dwtedx.com
     */
    public static float floatFormart(float value, int bit) {
        BigDecimal b = new BigDecimal(value);
        return b.setScale(bit, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     * @param @param  value
     * @param @param  bit
     * @param @return 设定文件
     * @return float    返回类型
     * @throws
     * @Title: floatFormart
     * @Description: floatFormart
     * @author qinyl http://dwtedx.com
     */
    public static Float StrToFloat(String floatStr) {
        float value = 0.0f;

        if (null != floatStr && floatStr.length() > 0) {
            try {
                value = Float.parseFloat(floatStr);
            } catch (NumberFormatException e) {
                value = 0.0f;
            }
        }

        return value;
    }

    /**
     * @param @param  date
     * @param @return 设定文件
     * @return Date    返回类型
     * @throws
     * @Title: stringToDate
     * @Description: Date转String
     * @author qinyl http://dwtedx.com
     */
    public static Date stringToDate(String date) {
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("zh", "CN"));
        Date time = null;
        try {
            time = formatDate.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * @param @param  str
     * @param @return 设定文件
     * @return boolean    返回类型
     * @throws
     * @Title: isNull
     * @Description: 是否为null
     * @author qinyl http://dwtedx.com
     */
    public static boolean isNull(String str) {
        if (str == null)
            return true;
        else
            return false;
    }

    /**
     * @param @param  str
     * @param @return 设定文件
     * @return boolean    返回类型
     * @throws
     * @Title: isNull
     * @Description: 是否为空字符串
     * @author qinyl http://dwtedx.com
     */
    public static boolean isEmpty(String str) {
        if (!isNull(str) && !"".equals(str))
            return false;
        else
            return true;
    }

    /**
     * @param @param  userName
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: hideUserName
     * @Description: 隐藏用户名
     * @author qinyl http://dwtedx.com
     */
    public static String hideUserName(String userName) {
        String result = "";

        if (userName.length() > 1) {
            result = userName.substring(0, 1);
            for (int i = 0; i < userName.length(); i++) {
                result += "*";
            }
        } else {
            result = userName;
        }

        return result;
    }

    /**
     * @param @param  userName
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: hideUserName
     * @Description: 隐藏手机号
     * @author qinyl http://dwtedx.com
     */
    public static String hidePhone(String phone) {
        String result = "";

        if (phone.length() > 3) {
            result = phone.substring(0, 3);
            for (int i = 3; i < phone.length() && i < 7; i++) {
                result += "*";
            }

            if (phone.length() > 7) {
                result += phone.substring(7, phone.length());
            }

        } else {
            result = phone;
        }

        return result;
    }

    /**
     * @param @param filePath
     * @param @param deleteThisPath    设定文件
     * @return void    返回类型
     * @throws
     * @Title: deleteFolderFile
     * @Description: 删除指定目录下文件及目录
     * @author qinyl http://dwtedx.com
     */
    public static void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if (file.isDirectory()) {// 处理目录
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFolderFile(files[i].getAbsolutePath(), true);
                }
            }
            if (deleteThisPath) {
                if (!file.isDirectory()) {// 如果是文件，删除
                    file.delete();
                } else {// 目录
                    if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
                        file.delete();
                    }
                }
            }
        }
    }

    @SuppressLint("DefaultLocale")
    public static String twoPlaces(float mfloat) {
        //构造方法的字符格式这里如果小数不足2位,会以0补足
        //DecimalFormat decimalFormat = new DecimalFormat("#.00");
        //format 返回的是字符串
        return String.format("%.2f", mfloat);
    }

    @SuppressLint("DefaultLocale")
    public static String twoPlaces(double mdouble) {

        return String.format("%.2f", mdouble);
    }

    //是否为数字
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 是否为数字和小数点
     * @param str
     * @return
     */
    public static boolean isNumerics(String str) {
        Pattern pattern = Pattern.compile("[\\d\\.]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 过滤非数字和小数点
     * @param str
     * @return
     */
    public static String getNumericPoints(String str)
    {
        char[] charArr = str.toCharArray();
        String result = "";
        for (int i = 0; i < charArr.length; i++)
        {
            if (("0123456789.").indexOf(charArr[i] + "") != -1)
            {
                result += charArr[i];
            }
        }
        return result;
    }

    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 某一个月第一天和最后一天
     *
     * @param date
     * @return
     */
    public static Map<String, String> getFirstday_Lastday_Month(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);
        Date theDate = calendar.getTime();

        //上个月第一天
        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(theDate);
        gcLast.set(Calendar.DAY_OF_MONTH, 1);
        String day_first = df.format(gcLast.getTime());
        StringBuffer str = new StringBuffer().append(day_first).append(" 00:00:00");
        day_first = str.toString();

        //上个月最后一天
        calendar.add(Calendar.MONTH, 1);    //加一个月
        calendar.set(Calendar.DATE, 1);        //设置为该月第一天
        calendar.add(Calendar.DATE, -1);    //再减一天即为上个月最后一天
        String day_last = df.format(calendar.getTime());
        StringBuffer endStr = new StringBuffer().append(day_last).append(" 23:59:59");
        day_last = endStr.toString();

        Map<String, String> map = new HashMap<String, String>();
        map.put("first", day_first);
        map.put("last", day_last);
        return map;
    }

    /**
     * 当月第一天
     *
     * @return
     */
    public static String getFirstDay() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date theDate = calendar.getTime();

        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(theDate);
        gcLast.set(Calendar.DAY_OF_MONTH, 1);
        String day_first = df.format(gcLast.getTime());
        StringBuffer str = new StringBuffer().append(day_first).append(" 00:00:00");
        return str.toString();

    }

    /**
     * 当月最后一天
     *
     * @return
     */
    public static String getLastDay() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date theDate = calendar.getTime();
        String s = df.format(theDate);
        StringBuffer str = new StringBuffer().append(s).append(" 23:59:59");
        return str.toString();

    }

    //千分位方法
    public static String fmtMicrometer(String text) {
        DecimalFormat df = null;
        if (text.indexOf(".") > 0) {
            if (text.length() - text.indexOf(".") - 1 == 0) {
                df = new DecimalFormat("###,##0.");
            } else if (text.length() - text.indexOf(".") - 1 == 1) {
                df = new DecimalFormat("###,##0.0");
            } else {
                df = new DecimalFormat("###,##0.00");
            }
        } else {
            df = new DecimalFormat("###,##0");
        }
        double number = 0.0;
        try {
            number = Double.parseDouble(text);
        } catch (Exception e) {
            number = 0.0;
        }
        return df.format(number);
    }

    public static double doubleFormat(String nomber) {
        double val = Double.parseDouble(nomber);
        BigDecimal b = new BigDecimal(val);
        val = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return val;
    }

    public static String doubleFormat(double nomber) {
        NumberFormat nf = new DecimalFormat("#0.00");
        return nf.format(nomber);
    }

    //方法二
    //java中double类型如果小数点后为零显示整数，否则保留
    public static String doubleTrans(double num)
    {
        BigDecimal b = new BigDecimal(num);
        double dNum = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        //后面为0社区
        if(Math.round(dNum) - dNum == 0)
        {
            return String.valueOf((long)dNum);
        }
        return String.valueOf(dNum);
    }

    public static String tenThousand(String val) {
        double price = Double.parseDouble(val) / 10000;
        return CommonUtility.twoPlaces(price);
    }

    public static String tenThousand(float val) {
        double price = val / 10000;
        return CommonUtility.twoPlaces(price);
    }

    public static String hundred(String val) {
        double prices = Double.parseDouble(val) / 100;
        return CommonUtility.twoPlaces(prices);
    }

    /**
     * 限定输入框内的文字不能含有特殊字符
     *
     * @param str
     * @return
     */
    public static boolean hasSpecialCharacter(String str) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**********以下方法需要Context对象*********************************以下方法需要Context
     * 对象***********************************以下方法需要Context对象**********************************/
    /**
     * 获得手机唯一IMEI
     */
    @SuppressLint("MissingPermission")
    public static String getPhoneIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String IMEI = telephonyManager.getDeviceId();
        return IMEI;
    }

    /**
     * 获取手机屏幕宽
     *
     * @param activity
     */
    public static int getScreenWidth(Context activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) activity).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        // float density = metrics.density; // 屏幕密度（0.75 / 1.0 / 1.5）
        // int densityDpi = metrics.densityDpi; // 屏幕密度DPI（120 / 160 / 240）
        return metrics.widthPixels;
    }

    /**
     * @param activity
     */
    public static int getPhoneDensityDpi(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int densityDpi = metrics.densityDpi; // 屏幕密度DPI（120 / 160 / 240）
        return densityDpi;
    }

    /**
     * 获取手机屏幕高
     *
     * @param activity
     */
    public static int getScreenHeight(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        return metrics.heightPixels;

    }

    /**
     * 获取手机状态栏的高度
     *
     * @param
     */
    public static int getStatusHeight(Context context) {
        // Rect frame = new Rect();
        // activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        // return frame.top;

        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return 0;
    }

    /**
     * 获取当前屏幕旋转角度
     *
     * @param activity
     * @return 0表示是竖屏; 90表示是左横屏; 180表示是反向竖屏; 270表示是右横屏
     */
    public static int getScreenRotationOnPhone(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        // final Display display = ((WindowManager)
        // context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        switch (display.getRotation()) {
            case Surface.ROTATION_0:
                return 0;

            case Surface.ROTATION_90:
                return 90;

            case Surface.ROTATION_180:
                return 180;

            case Surface.ROTATION_270:
                return -90;
        }
        return 0;
    }

    /**
     * 获取版本
     *
     * @param context
     * @return
     */
    public static int getAppVer(Context context) {
        int version = 1;
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            version = info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return version;

    }

    /**
     * 获取当前版本号
     *
     * @return
     */
    public static String getVersion(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    /**
     * 判断当前是否有可用的网络以及网络类型 0：无网络 1：WIFI 2：CMWAP 3：CMNET
     *
     * @param context
     * @return
     */
    public static int isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return 0;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        NetworkInfo netWorkInfo = info[i];
                        if (netWorkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                            return 1;
                        } else if (netWorkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                            String extraInfo = netWorkInfo.getExtraInfo();
                            if ("cmwap".equalsIgnoreCase(extraInfo)
                                    || "cmwap:gsm".equalsIgnoreCase(extraInfo)) {
                                return 2;
                            }
                            return 3;
                        }
                    }
                }
            }
        }
        return 0;
    }

    /**
     * @param @param  context
     * @param @param  pxValue
     * @param @return 设定文件
     * @return int    返回类型
     * @throws
     * @Title: px2dip
     * @Description: 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     * @author qinyl http://dwtedx.com
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取屏幕密度 (单位 px)
     */
    public static Point getScreenMeture(Context context) {
        Display mDisplay = ((Activity) context).getWindowManager().getDefaultDisplay();
        return new Point(mDisplay.getWidth(), mDisplay.getHeight());
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param context 上下文
     * @param dpValue dp值
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /***
     * 获取MAC地址
     *
     * @return
     */
    public static String getMacAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo.getMacAddress() != null) {
            return wifiInfo.getMacAddress();
        } else {
            return "";
        }
    }

    /**
     * 获取运行时间
     *
     * @return 运行时间(单位/s)
     */
    public static long getRunTimes() {
        long ut = SystemClock.elapsedRealtime() / 1000;
        if (ut == 0) {
            ut = 1;
        }
        return ut;
    }

    /**
     * sdcard是否可读写
     */
    public static boolean IsCanUseSdCard() {
        try {
            return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * sim卡是否可读
     *
     * @param context
     * @return
     */
    public static boolean isCanUseSim(Context context) {
        try {
            TelephonyManager mgr = (TelephonyManager) context.getSystemService(Context
                    .TELEPHONY_SERVICE);

            return TelephonyManager.SIM_STATE_READY == mgr.getSimState();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 作用：获取当前设备的内核数目
     *
     * @return
     */
    public static int getAvailableProcessorsNum() {
        return Runtime.getRuntime().availableProcessors();
    }

    public static DisplayMetrics getWindowDisplay(Context context) {

        DisplayMetrics dm = context.getResources().getDisplayMetrics();

        // float density = dm.density; // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
        // int densityDPI = dm.densityDpi; // 屏幕密度（每寸像素：120/160/240/320）
        // float xdpi = dm.xdpi;
        // float ydpi = dm.ydpi;

        return dm;
    }


    /**
     * 获取手机串号，需要权限：<uses-permission android:name="android.permission.READ_PHONE_STATE"/>
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    public static String getImei(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    /**
     * 获取手机系统的版本号
     *
     * @return
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取应用的版本号,默认值为1.0
     *
     * @return 当前应用的版本号
     */
    public static String getAppVersion(Context context) {
        String version = "1.0";
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            version = info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return version;

    }

    /**
     * 调用系统的分享控件
     *
     * @param activity
     * @param content
     */
    public static void shareContent(Context activity, String content) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, content);
        activity.startActivity(intent);
    }

    /**
     * 判断应用是否安装
     *
     * @param context
     * @param packName
     * @return
     */
    public static boolean appInstalled(Context context, String packName) {
        PackageInfo packageInfo;

        try {
            packageInfo = context.getPackageManager().getPackageInfo(packName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }

        return packageInfo == null ? false : true;
    }

    /**
     * 判断某个服务是不是活着
     *
     * @param mContext
     * @param serviceName
     * @return
     */
    public static boolean isServiceWork(Context mContext, String serviceName) {

        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext.getSystemService(Context
                .ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(50);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

    /**
     * @param context 上下文
     * @param actName 要校验Activity的名称
     * @return true Activity还运行，false
     */
    public static boolean isRunningActivity(Context context, String actName) {
        // ActivityManager
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = am.getRunningTasks(1);
        if (runningTasks != null && runningTasks.size() > 0) {
            for (ActivityManager.RunningTaskInfo runningTaskInfo : runningTasks) {
                String name = runningTaskInfo.topActivity.getClassName();
                if (actName.equals(name)) {
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * 打开输入法
     *
     * @param context
     * @param editText
     */
    public static void openIM(Context context, View editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService
                (Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText, 0);
    }

    /**
     * 关闭输入法
     *
     * @param context
     * @param editText
     */
    public static void closeIM(Context context, View editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService
                (Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }


    /*********以下是图片相关的方法******************以下是图片相关的方法************************以下是图片相关的方法********************以下是图片相关的方法*****************************************/

    /**
     * @param @param  drawable
     * @param @return 设定文件
     * @return Bitmap    返回类型
     * @throws
     * @Title: drawableToBitmap
     * @Description: drawable To Bitmap
     * @author qinyl http://dwtedx.com
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);

        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    /**
     * @param @param  image
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: encodeTobase64
     * @Description: Bitmap To base64 String
     * @author qinyl http://dwtedx.com
     */
    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        return imageEncoded;
    }

    /**
     * @param @param  filePath
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: getTakeCarImageBase64
     * @Description: 把文件夹里面的图片转 base64 String
     * @author qinyl http://dwtedx.com
     */
    public static String getTakeCarImageBase64(String filePath) {
        String result = null;
        File file = new File(filePath);
        if (file.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;// ARGB_8888
            Bitmap bitmap = null;
            bitmap = BitmapFactory.decodeFile(filePath, options);
            if (bitmap != null) {
                result = encodeTobase64(bitmap);
                bitmap.recycle();
                bitmap = null;
            }
        }
        return result;
    }

    /**
     * @param @param  input
     * @param @return 设定文件
     * @return Bitmap    返回类型
     * @throws
     * @Title: decodeBase64
     * @Description: Base64 to Bitmap
     * @author qinyl http://dwtedx.com
     */
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);

        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    /**
     * @param @param  uri
     * @param @param  mContext
     * @param @return 设定文件
     * @return Bitmap    返回类型
     * @throws
     * @Title: getScalingBitmap
     * @Description: 压缩图片
     * @author qinyl http://dwtedx.com
     */
    public static Bitmap getScalingBitmap(Uri uri, Context mContext) {
        try {
            InputStream in = mContext.getContentResolver().openInputStream(uri);

            // 第一次 decode,只取得图片长宽,还未载入记忆体
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, opts);
            in.close();

            // 计算放缩值
            int sampleSize = calculateInSampleSize(opts, 840, 400);

            // 第二次 decode,指定取样数后,产生缩图
            in = mContext.getContentResolver().openInputStream(uri);
            opts.inJustDecodeBounds = false;
            opts.inSampleSize = sampleSize;
            Bitmap bitmap = BitmapFactory.decodeStream(in, null, opts);
            in.close();
            return bitmap;
        } catch (Exception err) {
            Log.e(TAG, err.toString());
            return null;
        }
    }

    /**
     * 压缩图片
     *
     * @param image
     * @return
     */
    public static Bitmap getCompressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > 100) {
            // 重置baos
            baos.reset();
            // 这里压缩options%，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            // 每次都减少10
            options -= 10;
        }
        // 把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        // 把ByteArrayInputStream数据生成图片
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        return bitmap;
    }

    /**
     * @param @param  options
     * @param @param  reqWidth
     * @param @param  reqHeight
     * @param @return 设定文件
     * @return int    返回类型
     * @throws
     * @Title: calculateInSampleSize
     * @Description: 计算图片的缩放值
     * @author qinyl http://dwtedx.com
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int
            reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;

        // 匹配照片的横向和纵向比例
        if (((reqWidth > reqHeight) && (width < height)) || ((reqWidth < reqHeight) && (width >
                height))) {
            int tmp = reqWidth;
            reqWidth = reqHeight;
            reqHeight = tmp;
        }

        if (height > reqHeight || width > reqWidth) {
            int heightRatio = Math.round((float) height / (float) reqHeight);
            int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    /***
     * 图片的缩放方法
     *
     * @param bgimage   源图片资源
     * @param newWidth  缩放后宽度
     * @param newHeight 缩放后高度
     * @return
     */
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth, double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }

    /**
     * @param @param  photo
     * @param @param  watermark
     * @param @param  mark_x
     * @param @param  mark_y
     * @param @return 设定文件
     * @return Bitmap    返回类型
     * @throws
     * @Title: createBitmap
     * @Description: 图片添加图标水印
     */
    public static Bitmap createBitmap(Bitmap photo, Bitmap watermark, int mark_x, int mark_y) {
        //左上角 mark_x = 0；mark_y=0;
        //右上角 mark_x = photo.getWidth() - watermark.getWidth()；mark_y=0;
        //左下角 mark_x = 0；mark_y=photo.getHeight() - watermark.getHeight();
        /*左上角 mark_x = photo.getWidth() - watermark.getWidth()；
    /     mark_y = photo.getHeight() - watermark.getHeight();*/

        //Log.d( tag, "create a new bitmap" );
        if (photo == null) {
            return null;
        }
        int photoWidth = photo.getWidth();
        int photoHeight = photo.getHeight();
        //int markWidth = watermark.getWidth();
        //int markHeight = watermark.getHeight();

        // create the new blank bitmap
        Bitmap newb = Bitmap.createBitmap(photoWidth, photoHeight, Bitmap.Config.ARGB_8888);
        // 创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(newb);

        // draw src into
        cv.drawBitmap(photo, 0, 0, null);// 在 0，0坐标开始画入src
        // draw watermark into
        cv.drawBitmap(watermark, mark_x, mark_y, null);// 在src的右下角画入水印
        // save all clip
        //2019-3-28 升级android9报错
        //cv.save(Canvas.ALL_SAVE_FLAG);// 保存
        cv.save();
        // store
        cv.restore();// 存储
        return newb;

    }

    /**
     * @param @param  photo
     * @param @param  str
     * @param @param  mark_x
     * @param @param  mark_y
     * @param @return 设定文件
     * @return Bitmap    返回类型
     * @throws
     * @Title: createBitmap
     * @Description: 图片添加文字水印
     */
    @SuppressLint("NewApi")
    public static Bitmap createBitmap(Bitmap photo, CharSequence str, int mark_x, int mark_y,
                                      float mFontSizeVal, int mFontColourVal) {

        int width = photo.getWidth(), hight = photo.getHeight();
        System.out.println("宽" + width + "高" + hight);
        Bitmap icon = Bitmap.createBitmap(width, hight, Bitmap.Config.ARGB_8888); //建立一个空的BItMap
        Canvas canvas = new Canvas(icon);//初始化画布绘制的图像到icon上

        Paint photoPaint = new Paint(); //建立画笔
        photoPaint.setDither(true); //获取跟清晰的图像采样
        photoPaint.setFilterBitmap(true);//过滤一些

        Rect src = new Rect(0, 0, photo.getWidth(), photo.getHeight());//创建一个指定的新矩形的坐标
        Rect dst = new Rect(0, 0, width, hight);//创建一个指定的新矩形的坐标
        canvas.drawBitmap(photo, src, dst, photoPaint);//将photo 缩放或则扩大到 dst使用的填充区photoPaint

        TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);//设置画笔
        textPaint.setTextSize(mFontSizeVal);//字体大小
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);//采用默认的宽度
        textPaint.setColor(mFontColourVal);//采用的颜色
        //textPaint.setFontFeatureSettings("Microsoft YaHei");
        // 过滤
        photoPaint.setFilterBitmap(true);// setDither()和setFilterBitmap()
        // 的具体含义不是很清楚，但是只要记住：设置上这两个方法，就可以是图像更清晰就行！
        //textPaint.setShadowLayer(3f, 1, 1,this.getResources().getColor(android.R.color
        // .background_dark));//影音的设置

        /**
         * StaticLayout中参数的解释：
         * 1.字符串子资源
         * 2 .画笔对象
         * 3.layout的宽度，字符串超出宽度时自动换行。
         * 4.layout的样式，有ALIGN_CENTER， ALIGN_NORMAL， ALIGN_OPPOSITE 三种。
         * 5.相对行间距，相对字体大小，1.5f表示行间距为1.5倍的字体高度。
         * 6.相对行间距，0表示0个像素。
         * 实际行间距等于这两者的和。
         * 7.还不知道是什么意思，参数名是boolean includepad。
         */
        StaticLayout layout = new StaticLayout(str, textPaint, 2 * (width / 3), Layout.Alignment
                .ALIGN_NORMAL, 1.2F, 0.0F, true);//
        // 这个StaticLayout是让文字在图片中多行显示的关键，android之所以强大就是它已经帮你封装好了，通过对StaticLayout
        // 的设置就可以让EditText中的文字多行显示

        //canvas.drawText(str, mark_x, mark_y, textPaint);//绘制上去字，开始未知x,y采用那只笔绘制
        canvas.translate(mark_x, mark_y);
        layout.draw(canvas);
        //2019-3-28 升级android9报错
        //canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.save();
        canvas.restore();


        return icon;
    }

    /**
     * encodeBase64File:(将文件转成base64 字符串). <br/>
     *
     * @param path 文件路径
     * @return
     * @throws Exception
     * @author guhaizhou@126.com
     * @since JDK 1.6
     */
    public static String encodeBase64File(String path) throws Exception {
        File file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return Base64.encodeToString(buffer, Base64.DEFAULT);
    }

    /**
     * 加载本地小图片
     *
     * @param url
     * @return
     */
    public static Bitmap getLocalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            // return BitmapFactory.decodeStream(fis); //把流转化为Bitmap图片
            return BitmapFactory.decodeStream(fis, null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 根据图片名称获取R.java中对应的id
     *
     * @param name
     * @return
     */
    public static int getImageIdByName(String name) {
        int value = 0;
        if (null != name) {
            if (name.indexOf(".") != -1) {
                name = name.substring(0, name.indexOf("."));
            }
            Class<R.mipmap> cls = R.mipmap.class;
            try {
                value = cls.getDeclaredField(name).getInt(null);
            } catch (Exception e) {

            }
        }
        return value;
    }

    /**
     * 截取scrollview的屏幕
     *
     * @param scrollView
     * @return
     */
    public static Bitmap getBitmapByView(NestedScrollView scrollView) {
        int h = 0;
        Bitmap bitmap = null;
        // 获取scrollview实际高度
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
            scrollView.getChildAt(i).setBackgroundColor(Color.parseColor("#ffffff"));
        }
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h, Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        return bitmap;
    }

    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断该字符串是否为字母和数字
     * @param str
     * @return
     */
    public static boolean isNumericOrABC(String str){
        String regEx="[A-Z,a-z,0-9,-]*";
        Pattern pattern = Pattern.compile(regEx);
        return pattern.matcher(str).matches();
    }

}
