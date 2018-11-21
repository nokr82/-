
package com.devstories.anipointcompany.android.base;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


/**
 * @author hooni
 */
public final class StringUtils {

    /**
     * delimeter으로 분리하여 List를 반환
     *
     * @param delimeter 구분자
     * @param str 문자열
     * @return list
     */
    public static List<String> split(String str, String delimeter) {
        List<String> list = new ArrayList<String>();
        if (isEmpty(str)) {
            return list;
        }
        if (delimeter == null) {
            list.add(str);
            return list;
        }
        java.util.StringTokenizer st = new java.util.StringTokenizer(str, delimeter);
        try {
            while (st.hasMoreTokens()) {
                list.add(st.nextToken());
            }
            return list;
        } catch (java.util.NoSuchElementException e) {
            return list;
        }
    }

    /**
     * 꽁수로 만든 왼쪽에서 몇바이트 가져오는 함수<br>
     * 바이트 수는 ASCII 이외의 문자를 2바이트로 가정하고 가져옴<br>
     * 버그 있을 수 있음 (Unicode 문자 쓰다보면..)
     *
     * @param str 문자들
     * @param size size in bytes
     * @return size의 크기만큼 왼쪽에서 바이트 단위로 짤라온 값
     */
    public static String crop(String str, int size) {
        str = trim(str);
        if ("".equals(str))
            return "";
        //
        String tmp = left(str, size, "KSC5601");
        if (isEmpty(tmp) || equals(tmp, str))
            return tmp;
        return left(tmp, size - 3, "KSC5601") + "...";
    }

    public static int strlen(String s) {
        if (s == null)
            return 0;
        return s.length();
    }

    /**
     * 문자열 길이(바이트 단위)
     */
    public static int cstrlen(String s) {
        return cstrlen(s, "KSC5601");
    }

    public static int cstrlen(String s, String enc) {
        byte[] src = null;
        try {
            src = s.getBytes(enc);
            return src.length;
        } catch (java.io.UnsupportedEncodingException e) {
            return 0;
        }
    }

    /**
     * 문자열 함수(바이트 단위): cleft, cright, cmid
     */
    public static String cleft(String s, int size) {
        return cmid(s, 0, size);
    }

    public static String cright(String s, int size) {
        if (s == null || size == 0)
            return "";
        String enc = "KSC5601";
        int len = 0;
        byte[] src = null;
        try {
            src = s.getBytes(enc);
            len = src.length;
            if (size >= len)
                return s;
            int splen = len - size;
            String ret = new String(src, 0, splen, enc);
            if ("".equals(ret)) {
                splen++;
                size--;
            }
            return new String(src, splen, size, enc);
        } catch (java.io.UnsupportedEncodingException e) {
            return "";
        }
    }

    public static String cmid(String s, int start) {
        return cmid(s, start, 0);
    }

    public static String cmid(String s, int start, int size) {
        if (s == null || s.length() < start)
            return "";
        String enc = "KSC5601";
        int len = 0;
        byte[] src = null;
        try {
            src = s.getBytes(enc);
            len = src.length;
            int splen = len - start;
            if (size == 0 || splen < size)
                size = splen;
            //
            String ret = "";
            if (start > 0) {
                ret = new String(src, 0, start, enc);
                if ("".equals(ret)) {
                    start++;
                    size--;
                }
            }
            while (size > 0) {
                ret = new String(src, start, size, enc);
                if (!"".equals(ret))
                    return ret;
                size--;
            }
            return "";
        } catch (java.io.UnsupportedEncodingException e) {
            return "";
        }
    }

    /**
     * 문자열 함수: left, right, mid
     */
    public static String right(String str, int size) {
        if (str == null || size == 0)
            return "";
        if (str.length() <= size)
            return str;
        return str.substring(str.length() - size);
    }

    public static String mid(String str, int start) {
        return mid(str, start, 0);
    }

    public static String mid(String str, int start, int size) {
        if (str == null || str.length() < start)
            return "";
        int ln = str.length();
        String ret = right(str, ln - start);
        if (size < 1)
            return ret;
        return left(ret, size);
    }

    public static String left(String str, int size) {
        if (str == null || size == 0)
            return "";
        if (str.length() <= size)
            return str;
        return str.substring(0, size);
    }

    public static String left(String str, int size, String enc) {
        if (str == null || size == 0)
            return "";
        int len = 0;
        byte[] src = null;
        String result = null;
        //
        try {
            src = str.getBytes(enc);
            len = src.length;
            //
            if (len <= size)
                return str;
            while (size > 0) {
                result = new String(src, 0, size, enc);
                if (!"".equals(result))
                    return result;
                size--;
            }
            return "";
        } catch (java.io.UnsupportedEncodingException e) {
            return "";
        }
    }

    /**
     * 문자열 치환
     *
     * @param oldstr 대상 문자열
     * @param newstr 바꿀 문자열
     * @param str
     * @return
     */
    public static String replace(String oldstr, String newstr, String str) {
        if (str == null)
            return "";
        if (oldstr == null)
            return str;
        if (newstr == null)
            newstr = "";
        for (int i = 0; (i = str.indexOf(oldstr, i)) >= 0; i += newstr.length()) {
            str = str.substring(0, i) + newstr
                  + str.substring(i + oldstr.length());
        }
        return str;
    }

    /**
     * 문자열 비교.
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean equals(String a, String b) {
        if (a == null && b == null)
            return true;
        if (a == null || b == null)
            return false;
        return a.equals(b);
    }

    public static boolean equalsIgnoreCase(String a, String b) {
        if (a == null && b == null)
            return true;
        if (a == null || b == null)
            return false;
        return a.equalsIgnoreCase(b);
    }

    public static boolean equalsNotNull(String a, String b) {
        if (a == null || b == null)
            return false;
        return a.equals(b);
    }

    /**
     * Remove special white space from both ends of this string.
     * <p>
     * All characters that have codes less than or equal to <code>'&#92;u0020'</code> (the space character) are considered to be white space.
     * <p>
     * java.lang.String의 trim()과 차이점은 일반적인 white space만 짜르는 것이 아니라 위에서와 같은 특수한 blank도 짤라 준다.<br>
     * 이 소스는 IBM HOST와 데이타를 주고 받을 때 유용하게 사용했었다. 일반적으로 많이 쓰이지는 않을 것이다.
     *
     * @return trimed string with white space removed from the front and end.
     * @author WonYoung Lee, wyounglee@lgeds.lg.co.kr
     */
    public static String trim(Object s) {
        if (s == null)
            return "";

        return trim((String) s);
    }

    /**
     * Remove special white space from both ends of this string.
     * <p>
     * All characters that have codes less than or equal to <code>'&#92;u0020'</code> (the space character) are considered to be white space.
     * <p>
     * java.lang.String의 trim()과 차이점은 일반적인 white space만 짜르는 것이 아니라 위에서와 같은 특수한 blank도 짤라 준다.<br>
     * 이 소스는 IBM HOST와 데이타를 주고 받을 때 유용하게 사용했었다. 일반적으로 많이 쓰이지는 않을 것이다.
     *
     * @return trimed string with white space removed from the front and end.
     * @author WonYoung Lee, wyounglee@lgeds.lg.co.kr
     */
    public static String trim(String s) {
        if (s == null)
            return "";
        //
        int st = 0;
        char[] val = s.toCharArray();
        int count = val.length;
        int len = count;
        //
        while ((st < len) && ((val[st] <= ' ') || (val[st] == '　')))
            st++;
        while ((st < len) && ((val[len - 1] <= ' ') || (val[len - 1] == '　')))
            len--;
        //
        return ((st > 0) || (len < count)) ? s.substring(st, len) : s;
    }

    public static String rtrim(String s) {
        if (s == null)
            return "";
        //
        int st = 0;
        char[] val = s.toCharArray();
        int count = val.length;
        int len = count;
        //
        while ((st < len) && ((val[len - 1] <= ' ') || (val[len - 1] == '　')))
            len--;
        //
        return ((st > 0) || (len < count)) ? s.substring(st, len) : s;
    }

    public static String ltrim(String s) {
        if (s == null)
            return "";
        //
        int st = 0;
        char[] val = s.toCharArray();
        int count = val.length;
        int len = count;
        //
        while ((st < len) && ((val[st] <= ' ') || (val[st] == '　')))
            st++;
        return ((st > 0) || (len < count)) ? s.substring(st, len) : s;
    }

    /**
     * str이 빈문자열이면 sdef를 리튼한다.
     *
     * @param str
     * @param sdef
     * @return str or sdef
     */
    public static String def(String str, String sdef) {
        if (isEmpty(str))
            return sdef;
        return str;
    }

    /**
     * 문자열 Null 체크
     *
     * @param s
     * @return
     */
    public static String nchk(String s) {
        if (s == null)
            return "";
        return s;
    }

    /**
     * data 가 null 이거나 길이가 0 이면 true를 리턴한다.
     *
     * @param data 문자열
     * @return boolean
     */
    public static boolean isEmpty(String data) {
        return data == null || data.trim().length() == 0;
    }

    public static boolean isStr(String data) {
        return !isEmpty(data);
    }

    public static boolean isWhitespace(String s) {
        if (s == null)
            return true;
        for (int i = 0; i < s.length(); i++)
            if (!Character.isWhitespace(s.charAt(i)))
                return false;
        return true;
    }

    /**
     * null 체크하여 null이면 빈문자열을 return한다. 작성 날짜: (2005-07-06 오후 9:24:07)
     *
     * @return java.lang.String
     * @param value java.lang.String
     */
    public static String isNull(String value) {

        if (value == null)
            return "";

        return value.trim();
    }

    /**
     * 원하는 길이만큼 "0" 을 붙여줌. <br>
     * Usage : StrUtil.addZero("A",5) <br>
     * return value : A0000
     *
     * @param paramInt "0"을 붙이고자 하는 String 값
     * @param paramLeng 완성되어진 전체 길이
     * @return String "0"을 뒤에 붙인 String
     */
    public static String addZero(String paramInt, int paramLeng) {
        int len = paramLeng - paramInt.length();

        if (paramInt.length() < paramLeng) {
            for (int i = 0; i < len; i++) {
                paramInt = paramInt + "0";
            }
        }

        return paramInt;
    }

    /**
     * <p>
     * 지정 문자열에서 특정 문자를 변경함.
     * </p>
     *
     * @param 찾으려고하는 단어.
     * @param 바꾸고자하는 단어.
     * @param 대상 문자열.
     * @return 변경된 문자열.
     */
    public static String sReplace(String search, String replace, String source) {

        int spot;
        String returnString;
        String origSource = new String(source);

        spot = source.indexOf(search);
        if (spot > -1)
            returnString = "";
        else
            returnString = source;
        while (spot > -1) {
            if (spot == source.length() + 1) {
                returnString = returnString.concat(source.substring(0, source.length() - 1).concat(replace));
                source = "";
            } else if (spot > 0) {
                returnString = returnString.concat(source.substring(0, spot).concat(replace));
                source = source.substring(spot + search.length(), source.length());
            } else {
                returnString = returnString.concat(replace);
                source = source.substring(spot + search.length(), source.length());
            }
            spot = source.indexOf(search);
        }
        if (!source.equals(origSource)) {
            return returnString.concat(source);
        }

        return returnString;
    }

    /**
     * 문자열1과 문자열2를 결합한다. 결합할 때 결합되는 부분에 "/"이 2개가 발생하지 않게 결합해 준다. "//" 이렇게 되는 것을 방지한다.
     *
     * @param str1 문자열1
     * @param str2 문자열2
     * @return 문자열1 + 문자열2
     */
    public static String concatenate(String str1, String str2) {

        if (str1 == null && str2 == null)
            return "";

        if (str1 == null)
            return str2;

        if (str2 == null)
            return str1;

        if (str1.endsWith(File.separator) && str2.startsWith(File.separator)) {
            return str1 + str2.substring(1);
        }

        else if (str1.endsWith(File.separator) && !str2.startsWith(File.separator)) {
            return str1 + str2;
        }

        else if (!str1.endsWith(File.separator) && str2.startsWith(File.separator)) {
            return str1 + str2;
        }

        else if (!str1.endsWith(File.separator) && !str2.startsWith(File.separator)) {
            return str1 + File.separator + str2;
        }

        return "";
    }

    /**
     * <p>
     * 특정문자로 둘러 알맹이 문자 얻기
     * </p>
     * <p>
     * ex) getLapOutText("<!Text!>", "<!", "!>") --> Text
     * </p>
     *
     * @param str 대상 문자열
     * @param startTag 제거될 시작문자열
     * @param endTag 제거될 끝문자열
     * @return 알맹이 문자열
     */
    public static List<String> getLapOutText(String str,
                                             String startTag,
                                             String endTag) {

        List<String> lapOuts = new ArrayList<String>();

        int posStartTag = 0;
        int lookUpPoint = 0;

        /*
         * 시작 Tag 위치 알아 내기
         */
        while(true) {
            /*
             * 시작 Tag의 위치 알아 내기
             */
            posStartTag = str.indexOf(startTag, lookUpPoint);
            if(posStartTag == -1) {
                break;
            }

            /*
             * 종료 Tag의 위치 알아 내기
             */
            int posEndTag = str.indexOf(endTag, posStartTag);
            if (posEndTag < 0) {
                break;
            }

            /*
             * 값을 가져온다.
             */
            lapOuts.add(str.substring(posStartTag + startTag.length(), posEndTag).trim());

            /*
             * 다음 검색 위치조정
             */
            lookUpPoint = posStartTag + 1;

        }

        return lapOuts;
    }

    public static boolean hasText(String content) {
        return content != null && content.trim().length() > 0;
    }

    public static String cutTitle(String s, int len, String tail) {
        if (s == null){
            return null;
        }
        int srcLen = realLength(s);
        if (srcLen < len){
            return s;
        }

        String tmpTail = tail;
        if (tail == null){
            tmpTail = "";
        }

        int tailLen = realLength(tmpTail);
        if (tailLen > len){
            return "";
        }

        char a;
        int i = 0;
        int realLen = 0;
        for (i = 0; i < len - tailLen && realLen < len - tailLen; i++) {
            try {
                a = s.charAt(i);

                if ((a & 0xFF00) == 0){
                    realLen += 1;
                }
                else{
                    realLen += 2;
                }
            } catch (StringIndexOutOfBoundsException e) {
                break;
            }
        }
        while (realLength(s.substring(0, i)) > len - tailLen) {
            i--;
        }

        return s.substring(0, i) + tmpTail;
    }
    
    public static int realLength(String s) {
        return cstrlen(s);
    }
    
    public static String nl2br(String comment) {

        StringBuffer sbuf = new StringBuffer();
        char c = ' ';
        String s = comment;

        try {

            for (int i = 0; i < s.length(); i++) {
                c = s.charAt(i);

                switch (c){
                    case '\n' :
                        sbuf.append("<br />");
                    break;

                    case '\t' :
                        sbuf.append("&nbsp;&nbsp;&nbsp;&nbsp;");
                    break;

                    case '<' :
                        sbuf.append("&lt;");
                    break;

                    case '>' :
                        sbuf.append("&gt;");
                    break;

                    default :
                        sbuf.append(c);
                    break;
                }
            }

            return toHttpLink(sbuf.toString());

        } catch (Exception e) {
            return comment;
        }
    }


    public static String htmlToTxt(String comment) {

        StringBuffer sbuf = new StringBuffer();
        char c = ' ';
        String s = comment;

        try {

            for (int i = 0; i < s.length(); i++) {
                c = s.charAt(i);

                switch (c){
                    case '<' :
                        sbuf.append("&lt;");
                    break;

                    case '>' :
                        sbuf.append("&gt;");
                    break;

                    default :
                        sbuf.append(c);
                    break;
                }
            }

            return toHttpLink(sbuf.toString());

        } catch (Exception e) {
            return comment;
        }
    }
    
    public static String toHttpLink(String StrString2) {
        String tmp = "";
        
        int itmp = 0;
        int wend = 0;
        StringBuffer sb = new StringBuffer();
        sb.append("");
        
        try {
            if (StrString2 == null)
                return null;
        
            tmp = StrString2;
        
            while (tmp.indexOf("http://") > -1) {
                itmp = tmp.indexOf("http://");
                wend = tmp.indexOf(" ", itmp);
        
                if ((wend > tmp.indexOf(")", itmp))
                        && (tmp.indexOf(")", itmp) > -1))
                    wend = tmp.indexOf(")", itmp);
        
                if ((wend > tmp.indexOf("<", itmp))
                        && (tmp.indexOf("<", itmp) > -1))
                    wend = tmp.indexOf("<", itmp);
        
                if (wend == -1) {
                    if (tmp.indexOf("<br />", itmp) == -1) {
                        wend = tmp.length();
                    } else {
                        wend = tmp.indexOf("<br />", itmp);
                    }
                } else {
                    if (tmp.indexOf("<br />", itmp) != -1) {
                        if (wend > tmp.indexOf("<br />", itmp)) {
                            wend = tmp.indexOf("<br />", itmp);
                        }
                    }
                }
        
                sb.append(tmp.substring(0, itmp));
        
                if ((itmp > 3)
                        && (tmp.substring(itmp - 3, itmp).indexOf("=") > -1)) {
                    wend = tmp.indexOf("</a>", itmp) + 3;
        
                    if (wend == 2)
                        wend = tmp.indexOf(">", itmp);
        
                    sb.append(tmp.substring(itmp, wend));
                } else {
                    sb.append("<a href=\"" + tmp.substring(itmp, wend)
                            + "\" target=\"_blank\" >");
                    sb.append(tmp.substring(itmp, wend));
                    sb.append("</a>");
                }
                tmp = tmp.substring(wend);
            }
            sb.append(tmp);
        } catch (Exception e) {
            e.toString();
        } finally {
        }
        return sb.toString();
    }
    
    /**
     * @param s
     * @return 숫자여부
     */
    public static boolean isNumeric(String s) {
        Pattern pattern = Pattern.compile("[+-]?\\d+");
        return pattern.matcher(s).matches();
    }  
    
    /**
     * @param s
     * @return 숫자여부
     */
    public static boolean isNumeric(char s) {
        return isNumeric(String.valueOf(s));
    }  
}
