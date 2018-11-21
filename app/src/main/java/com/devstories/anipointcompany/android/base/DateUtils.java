package com.devstories.anipointcompany.android.base;

import android.content.Context;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {
	private static SimpleDateFormat oFor = new SimpleDateFormat(
			"yyyy.MM.dd HH:mm");
	private static SimpleDateFormat nFor = new SimpleDateFormat(
			"yy.MM.dd HH:mm");
	private static SimpleDateFormat webForm = new SimpleDateFormat(
			"M월 d일 a h:mm");
	private static SimpleDateFormat localForm = new SimpleDateFormat("a h:mm");
	private static SimpleDateFormat pjtForm = new SimpleDateFormat(
			"yyyy년 MM월 dd일 a h:mm");
	private static SimpleDateFormat serverForm = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat responseForm = new SimpleDateFormat(
			"yyyy.MM.dd HH:mm");
	private static SimpleDateFormat clientForm = new SimpleDateFormat("HH:mm");
	private static Calendar cal = Calendar.getInstance();

	private static long sec = 1000;
	private static long minite = sec * 60;
	private static long hour = minite * 60;
	private static long day = hour * 24;
	private static long interval = minite * 30;

	/**
	 * UTC시간을 local시간으로 변경
	 * 
	 * @param str
	 * @return
	 */
	public static String convertUTCtoLocal(String str) {
		Date date = null;
		try {
			date = oFor.parse(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cal.setTimeInMillis(convertUTCtoLocal(date.getTime()));
		Date nDate = cal.getTime();
		String result = nFor.format(nDate);

		return result;
	}

	private static long convertUTCtoLocal(long UTC) {
		TimeZone time = TimeZone.getDefault();
		int offset = time.getOffset(UTC);
		long local = UTC + offset;

		return local;
	}

	private static Date getCurrentDate() {
		Calendar _cal = Calendar.getInstance();
		return cal.getTime();
	}

	public static String getCurrentTime() {
		Calendar _cal = Calendar.getInstance();
		return String.format("%04d-%02d-%02d", _cal.get(Calendar.YEAR),
				_cal.get(Calendar.MONTH) + 1, _cal.get(Calendar.DAY_OF_MONTH));
	}

	public static String getServerDate(String strDate) {
		Date date = null;
		try {
			date = responseForm.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cal.setTimeInMillis(convertUTCtoLocal(date.getTime()));
		Date nDate = cal.getTime();
		return webForm.format(nDate);
	}

	public static String getPjtDate(String strDate) {
		Date date = null;
		try {
			date = serverForm.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cal.setTimeInMillis(convertUTCtoLocal(date.getTime()));
		Date nDate = cal.getTime();
		return pjtForm.format(nDate);
	}

	public static String getWebDate(String strDate) {
		Date date = null;
		try {
			date = responseForm.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cal.setTimeInMillis(convertUTCtoLocal(date.getTime()));
		Date nDate = cal.getTime();
		return webForm.format(nDate);
	}

	public static String getDate(Context context, String param) {
		Date date = null;
		try {
			date = serverForm.parse(param);
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
		cal.setTimeInMillis(convertUTCtoLocal(date.getTime()));

		long server = convertUTCtoLocal(date.getTime());
		long client = System.currentTimeMillis();
		long interval = client - server;

		String result = null;
//		if (interval < minite) {// in spot
//			result = context.getString(R.string.interval_spot);
//		} else if (interval >= minite && interval < hour) {// in minite
//			result = String.format(context.getString(R.string.interval_minite), (int) (interval / minite));
//		} else if (interval >= hour && interval < day) {// in hour
//			result = String.format(context.getString(R.string.interval_hour), (int) (interval / hour));
//		} else if (interval >= day && interval < (2 * day)) {// in yesterday
//			result = context.getString(R.string.interval_yesterday);
//		} else if (interval >= (2 * day) && interval < (4 * day)) {// in 3day
//			result = String.format(context.getString(R.string.interval_day), (int) (interval / day));
//		} else {
//			cal.setTimeInMillis(convertUTCtoLocal(date.getTime()));
//			result = webForm.format(cal.getTime());
//		}
		return result;
	}

	public static long getTimeStampOnTimeZone(String serverDate) {
		try {
			return convertUTCtoLocal(serverForm.parse(serverDate).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
	}

    public static String getDate(long systemTime) {
        cal.setTimeInMillis(systemTime);
        Date date = cal.getTime();

        return localForm.format(date);
    }

	public static String getCreatedDateTime(String created ) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("MM월 dd일 HH:mm");
		try {
			return sdf2.format(sdf.parse(created));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return created;
	}

	public static String getCreatedDate(String created ) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy년 MM월 dd일");
		try {
			return sdf2.format(sdf.parse(created));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return created;
	}

	public static String getCreatedShortDateTime(String created ) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yy.MM.dd HH:mm");
		try {
			return sdf2.format(sdf.parse(created));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return created;
	}

	public static String getCreatedShortDate(String created ) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yy.MM.dd");
		try {
			return sdf2.format(sdf.parse(created));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return created;
	}

	/**
	 * Check for New icon.
	 * 
	 * @param time
	 * @return
	 */
	public static boolean checkTimeInterval(String time) {
		if (Utils.isStrEmpty(time))
			return false;

		Date date = null;
		try {
			date = serverForm.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		Date nDate = getCurrentDate();

		long server = convertUTCtoLocal(date.getTime());
		long client = nDate.getTime();

		return ((client - server) <= interval);
	}

	/**
	 * Check for New icon.
	 *
	 * @return
	 */
	public static boolean compareTime(String eventTime, String viewTime) {
		if (Utils.isStrEmpty(eventTime) || Utils.isStrEmpty(viewTime))
			return false;

		Date event = null;
		Date view = null;
		try {
			event = serverForm.parse(eventTime);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		try {
			view = serverForm.parse(viewTime);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}

		long eventT = convertUTCtoLocal(event.getTime());
		long viewT = convertUTCtoLocal(view.getTime());

		return ((eventT - viewT) > 0);
	}

	public static String getToday(String fmt) {
		SimpleDateFormat sfmt = new SimpleDateFormat(fmt);
		return sfmt.format(new Date());
	}

	/**
	 * <p>
	 * 특정 포맷의 문자열을 스트링으로 타입으로 변환.
	 * </p>
	 */
	public static String stringTostring(String date, String fmt1, String fmt2) {

		if (date != null && fmt1 != null) {
			SimpleDateFormat sfmt = new SimpleDateFormat(fmt1);
			try {
				return dateToString(sfmt.parse(date), fmt2);
			} catch (ParseException pe) {
				return null;
			}
		}
		return null;
	}

	/**
	 * <p>
	 * Date형을 원하는 포맷으로 변환하여 스트링으로 전환.
	 * </p>
	 */
	public static String dateToString(Date date, String fmt) {
		if (date != null && fmt != null) {
			SimpleDateFormat sfmt = new SimpleDateFormat(fmt);
			return sfmt.format(date);
		}
		return "";
	}

	/**
	 * 원하는 시간에 몇시간후를 구하기
	 * @param t
	 * @param after
	 * @return
	 */
	public static String afterTime(String t, int after){
		String dateStr = "";
		try {
			Date date = new SimpleDateFormat("HH:mm").parse(t);
			SimpleDateFormat sformat = new SimpleDateFormat("HH:mm");
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.HOUR,  after);
			dateStr = sformat.format(cal.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dateStr;

	}

	/**
	 * 원하는 날짜에 몇일후를 구하기
	 * @param dt
	 * @param after
	 * @return
	 */
	public static String afterDate(String dt, int after, String fmt){
		String dateStr = "";
		try {
			Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dt);
			SimpleDateFormat sformat = new SimpleDateFormat(fmt);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DATE,  after);
			dateStr = sformat.format(cal.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dateStr;

	}

	public static boolean checkTime(String str) {
		boolean dateValidity = true;

		SimpleDateFormat df = new SimpleDateFormat("HH:mm"); // 20041102101244
		df.setLenient(false); // false 로 설정해야 엄밀한 해석을 함.
		try {
			Date dt = df.parse(str);

			String[] s = str.split(":");

			if(!Utils.isNumeric(s[0])){
				dateValidity = false;
			}

			if(!Utils.isNumeric(s[1])){
				dateValidity = false;
			}
		} catch (ParseException pe) {
			dateValidity = false;
		} catch (IllegalArgumentException ae) {
			dateValidity = false;
		}

		return dateValidity;
	}

	public static int getDiffDayCount(String fromDate, String toDate, String fmt) {
		SimpleDateFormat sdf = new SimpleDateFormat(fmt);

		try {
			return (int) ((sdf.parse(toDate).getTime() - sdf.parse(fromDate).getTime()) / 1000 / 60 / 60 / 24);
		} catch (Exception e) {
			return 0;
		}
	}

	public static int getDiffTimeCount() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH");

		try {
			Calendar cal = Calendar.getInstance();

			String timeStr = sdf.format(cal.getTime());
			int time = Integer.parseInt(timeStr);


			return 23 - time;
		} catch (Exception e) {
			return 0;
		}
	}

	public static long minuteDiff(String dt){
		//시간 설정
		Calendar tempcal=Calendar.getInstance();
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startday=sf.parse(dt, new ParsePosition(0));

		long startTime = startday.getTime();

		//현재의 시간 설정
		Calendar cal = Calendar.getInstance();
		Date endDate = cal.getTime();
		long endTime = endDate.getTime();

		long mills=endTime-startTime;

		//분으로 변환
		long min = mills/60000;

		return min;
	}
}
