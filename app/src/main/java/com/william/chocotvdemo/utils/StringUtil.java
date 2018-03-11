package com.william.chocotvdemo.utils;


import com.william.chocotvdemo.common.Constants;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;

public class StringUtil {


    private static final String JRSYS_ACCOUNT_DEFAULT_VALUE = "尚未設定";

    private StringUtil() {
    }

    /**
     * Check if it is JRSYS default string.
     * JRSYS default string "尚未設定" for 會員資料
     *
     * @param text
     * @return
     */
    public static final boolean isJrsysStrValid(String text) {
        if (!isStrNullOrEmpty(text) && !StringUtil.JRSYS_ACCOUNT_DEFAULT_VALUE.equals(text)) {
            return true;
        }
        return false;
    }


    public static String addquote(String name){
        return "'"+ name + "'";
    }

    /**
     * Check String is null or empty string
     *
     * @param str
     * @return
     */
    public static final boolean isStrNullOrEmpty(String str) {
        if (str == null || Constants.EMPTY_STRING.equals(str)) {
            return true;
        }
        return false;
    }

    /**
     * Check String is null or empty string
     *
     * @param str
     * @return
     */
    public static final boolean isStrNullOrEmpty(String... str) {

        if (str != null) {
            for (int index = 0; index < str.length; index++) {
                if (str[index] == null || Constants.EMPTY_STRING.equals(str[index])) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    public static String ShrinkToDotEnding(String textmessage) {
        String value = "";

        if(textmessage.toString().length()>Constants.MAX_CHATROOM_TEXT){
            String dotmessage = textmessage.substring(0, Constants.MAX_CHATROOM_TEXT-1) + "...";
            textmessage = dotmessage;
        }
        value = textmessage;
        return value;
    }

    public static String priceFormat(Double price) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(price);
    }

    public static String priceFormat(Float price) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(price);
    }

    public static String priceWithDecimal(Double price) {
        DecimalFormat formatter = new DecimalFormat("###,###,###.-");
        return "$" + formatter.format(price);
    }

    public static String priceWithDecimalNoSymbol(Double price) {
        DecimalFormat formatter = new DecimalFormat("###,###,###.-");
        return formatter.format(price);
    }

    public static String toJson(Object obj) {
        ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, Boolean.FALSE);
        // 忽略新增未知 回傳參數
        om.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        try {
            return om.writeValueAsString(obj);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    public static String toDebugJson(Object obj) {
        String json = null;
        ObjectMapper om = new ObjectMapper();
        try {
            json = om.defaultPrettyPrintingWriter().writeValueAsString(obj);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static <T> T fromJson(String src, Class<T> t) {
        ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, Boolean.FALSE);
        // 忽略新增未知 回傳參數
        try {
            return ((T) om.readValue(src, t));
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String big52unicode(String strBIG5) {
        String strReturn = "";
        try {
            strReturn = new String(strBIG5.getBytes("big5"), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strReturn;
    }

    public static String unicode2big5(String strUTF8) {
        String strReturn = "";
        try {
            strReturn = new String(strUTF8.getBytes("UTF-8"), "big5");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strReturn;
    }

    public static String UnicodeToStr(String unicodeStr) throws Exception {
        StringBuffer outStrBuf = new StringBuffer();
        String uCodes[] = unicodeStr.trim().split("\\\\u");
        for (String uc : uCodes) {
            if (uc.trim().isEmpty()) continue;
            byte bs[] = HexByteKit.Hex2Byte(uc.trim());
            if (bs != null) {
                String str = new String(bs, "Unicode");
                outStrBuf.append(str);
            } else System.err.printf("Illegal uc=%s\n", uc);
        }

        return outStrBuf.toString();
    }

    public static String getURLEncode(String str) {
        String url = str;
        try {
            url = URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        url = url.replace("+", "%20");
        return url;
    }

    public static String toUtf8(String str) {
        String value = null;

        try {
            value = new String(str.getBytes("UTF-8"),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        };
        return value;
    }

    public static String UnicodeToChinese(String unicodeStr)  {
        StringReader sr = new StringReader(unicodeStr);
        UnicodeUnescapeReader uur = new UnicodeUnescapeReader(sr);

        StringBuffer buf = new StringBuffer();
        try {
            for(int c = uur.read(); c != -1; c = uur.read())
            {
                buf.append((char)c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buf.toString();
    }
}
