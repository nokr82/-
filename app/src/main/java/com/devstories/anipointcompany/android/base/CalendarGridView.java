package com.devstories.anipointcompany.android.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devstories.anipointcompany.android.R;
import com.devstories.anipointcompany.android.activities.DlgReserveSaveActivity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by theclub on 3/8/17.
 */

public class CalendarGridView extends GridView {

    private final Context context;

    ArrayList<CalendarDate> data = new ArrayList<CalendarDate>();

    private int year = -1;
    private int month = -1;

    private ArrayList<JSONObject> objectData = new ArrayList<JSONObject>();
    private String orderType = "";
    private ArrayList<String> greenDays = new ArrayList<String>();
    private ArrayList<String> yellowDays = new ArrayList<String>();
    private ArrayList<String> organgeDays = new ArrayList<String>();
    private ArrayList<String> attendanceDays = new ArrayList<String>();
    private ArrayList<String> useDays = new ArrayList<String>();

    private List<ReplaceableDayText> replaceableDayTexts = new ArrayList<ReplaceableDayText>();

    private int greenDateTextColor = Color.parseColor("#FFFFFF");
    private int yellowDateTextColor = Color.parseColor("#FFFFFF");
    private int organgeDateTextColor = Color.parseColor("#FFFFFF");
    private int attendanceDateTextColor = Color.parseColor("#898989");


    private int prevMonthDateTextColor = Color.parseColor("#888889");
    private int nextMonthDateTextColor = Color.parseColor("#888889");

    public int getGreenDateTextColor() {
        return greenDateTextColor;
    }

    public void setGreenDateTextColor(int greenDateTextColor) {
        this.greenDateTextColor = greenDateTextColor;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public int getYellowDateTextColor() {
        return yellowDateTextColor;
    }

    public void setYellowDateTextColor(int yellowDateTextColor) {
        this.yellowDateTextColor = yellowDateTextColor;
    }

    public int getOrgangeDateTextColor() {
        return organgeDateTextColor;
    }

    public void setOrgangeDateTextColor(int organgeDateTextColor) {
        this.organgeDateTextColor = organgeDateTextColor;
    }

    public int getPrevMonthDateTextColor() {
        return prevMonthDateTextColor;
    }

    public void setPrevMonthDateTextColor(int prevMonthDateTextColor) {
        this.prevMonthDateTextColor = prevMonthDateTextColor;
    }

    public int getNextMonthDateTextColor() {
        return nextMonthDateTextColor;
    }

    public void setNextMonthDateTextColor(int nextMonthDateTextColor) {
        this.nextMonthDateTextColor = nextMonthDateTextColor;
    }

    public List<ReplaceableDayText> getReplaceableDayTexts() {
        return replaceableDayTexts;
    }

    public void setReplaceableDayTexts(List<ReplaceableDayText> replaceableDayTexts) {
        this.replaceableDayTexts = replaceableDayTexts;
    }

    public ArrayList<JSONObject> getObjectData() {
        return objectData;
    }

    public void setObjectData(ArrayList<JSONObject> objectData) {
        this.objectData = objectData;
    }

    public ArrayList<String> getGreenDays() {
        return greenDays;
    }

    public void setGreenDays(ArrayList<String> greenDays) {
        this.greenDays = greenDays;
    }

    public ArrayList<String> getYellowDays() {
        return yellowDays;
    }

    public void setYellowDays(ArrayList<String> yellowDays) {
        this.yellowDays = yellowDays;
    }

    public ArrayList<String> getOrgangeDays() {
        return organgeDays;
    }

    public void setOrgangeDays(ArrayList<String> organgeDays) {
        this.organgeDays = organgeDays;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public ArrayList<String> getAttendanceDays() {
        return attendanceDays;
    }

    public void setAttendanceDays(ArrayList<String> attendanceDays) {
        this.attendanceDays = attendanceDays;
    }

    public int getAttendanceDateTextColor() {
        return attendanceDateTextColor;
    }

    public void setAttendanceDateTextColor(int attendanceDateTextColor) {
        this.attendanceDateTextColor = attendanceDateTextColor;
    }

    public ArrayList<String> getUseDays() {
        return useDays;
    }

    public void setUseDays(ArrayList<String> useDays) {
        this.useDays = useDays;
    }

    public CalendarGridView(Context context) {
        super(context);
        this.context = context;

        init();
    }

    public CalendarGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        init();
    }

    public CalendarGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        init();
    }

    private void init() {

        Calendar cal = Calendar.getInstance();

        this.year = cal.get(Calendar.YEAR);
        this.month = cal.get(Calendar.MONTH);

        draw();
    }

    public void setDate(int year, int month) {

        this.year = year;
        this.month = month;

        draw();
    }

    public void draw() {

        data.clear();

        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, getMonth());
        cal.set(Calendar.DATE, 1);
        int week = cal.get(Calendar.DAY_OF_WEEK);

        int min = cal.getActualMinimum(Calendar.DATE);
        int max = cal.getActualMaximum(Calendar.DATE);

        /*
        data.add(new CalendarDate("sun"));
        data.add(new CalendarDate("mon"));
        data.add(new CalendarDate("tue"));
        data.add(new CalendarDate("wed"));
        data.add(new CalendarDate("thur"));
        data.add(new CalendarDate("fri"));
        data.add(new CalendarDate("sat"));
        */

        data.add(new CalendarDate("일"));
        data.add(new CalendarDate("월"));
        data.add(new CalendarDate("화"));
        data.add(new CalendarDate("수"));
        data.add(new CalendarDate("목"));
        data.add(new CalendarDate("금"));
        data.add(new CalendarDate("토"));

        // System.out.println("min : " + min);
        // System.out.println("max : " + max);
        // System.out.println("week : " + week);

        // 전월
        cal.add(Calendar.DATE, -week);
        for (int i = 1; i < week; i++) {
            cal.add(Calendar.DATE, 1);

            // System.out.println("cal : " + cal);

            // SimpleDateFormat sdf = new SimpleDateFormat("M/d");
            SimpleDateFormat sdf = new SimpleDateFormat("d");
            String day = sdf.format(cal.getTime());

            CalendarDate cd = new CalendarDate(day);
            cd.setDate(cal.get(Calendar.DATE));
            cd.setMonth(cal.get(Calendar.MONTH) + 1);
            cd.setYear(cal.get(Calendar.YEAR));
            cd.setPrevMonth(true);

            // System.out.println("cd : " + cd);

            data.add(cd);
        }

        Calendar today = Calendar.getInstance();
        int todayMonth = today.get(Calendar.MONTH);
        int todayDate = today.get(Calendar.DATE);

        // 이번 달
        for (int i = 1; i <= max; i++) {
            cal.add(Calendar.DATE, 1);

            int month = cal.get(Calendar.MONTH);
            int date = cal.get(Calendar.DATE);

            SimpleDateFormat sdf = new SimpleDateFormat("d");
            String day = sdf.format(cal.getTime());

            CalendarDate cd = new CalendarDate(day);
            cd.setDate(date);
            cd.setMonth(month + 1);
            cd.setYear(cal.get(Calendar.YEAR));
            cd.setPrevMonth(false);
            cd.setNextMonth(false);

            if(todayMonth == month && todayDate == date) {
                cd.setToday(true);
            }

            // System.out.println("cd : " + cd);

            data.add(cd);
        }

        // 다음달
        week = cal.get(Calendar.DAY_OF_WEEK);
        while (week < 7) {
            cal.add(Calendar.DATE, 1);

            // SimpleDateFormat sdf = new SimpleDateFormat("M/d");
            SimpleDateFormat sdf = new SimpleDateFormat("d");
            String day = sdf.format(cal.getTime());

            CalendarDate cd = new CalendarDate(day);
            cd.setDate(cal.get(Calendar.DATE));
            cd.setMonth(cal.get(Calendar.MONTH) + 1);
            cd.setYear(cal.get(Calendar.YEAR));
            cd.setPrevMonth(false);
            cd.setNextMonth(true);

            // System.out.println("cd : " + cd);

            data.add(cd);

            week = cal.get(Calendar.DAY_OF_WEEK);
        }


        setAdapter(new CalendarAdapter(context, R.layout.item_calendar_day, data));
        ((BaseAdapter) getAdapter()).notifyDataSetChanged();
    }

    public ReplaceableDayText getReplaceableDayText(int position) {
        CalendarDate cd = data.get(position);

        // 공부한 것
        for(ReplaceableDayText replaceableDayText : replaceableDayTexts) {
            if(cd.getFullDay().equals(replaceableDayText.getOldDayText())) {
                return replaceableDayText;
            }
        }

        return null;
    }

    public boolean isAttendance(int position) {
        CalendarDate cd = data.get(position);

        // 공부한 것
        for(String attendanceDay : attendanceDays) {
            if(cd.getFullDay().equals(attendanceDay)) {
                return true;
            }
        }

        return false;
    }

    public String getGreenOrYellow(int position) {
        CalendarDate cd = data.get(position);

        // payment_yn == 'Y'
        for(String greenDay : greenDays) {
            if(cd.getFullDay().equals(greenDay)) {
                return greenDay;
            }
        }

        // isBooking == 'Y'
        for(String yellowDay : yellowDays) {
            if(cd.getFullDay().equals(yellowDay)) {
                return yellowDay;
            }
        }

        return null;
    }

    private class CalendarDate {

        String day = "";
        int year = -1;
        int month = -1;
        int date = -1;
        boolean isPrevMonth = false;
        boolean isNextMonth = false;
        boolean isToday = false;

        public CalendarDate(String day) {
            this.day = day;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getDate() {
            return date;
        }

        public void setDate(int date) {
            this.date = date;
        }

        public boolean isPrevMonth() {
            return isPrevMonth;
        }

        public void setPrevMonth(boolean prevMonth) {
            isPrevMonth = prevMonth;
        }

        public boolean isNextMonth() {
            return isNextMonth;
        }

        public void setNextMonth(boolean nextMonth) {
            isNextMonth = nextMonth;
        }

        public boolean isToday() {
            return isToday;
        }

        public void setToday(boolean today) {
            isToday = today;
        }

        public int getWeek() {
            Calendar cal = Calendar.getInstance();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = sdf.parse(getFullDay());
                cal.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            int week = cal.get(Calendar.DAY_OF_WEEK);

            return week;
        }

        public String getFullDay() {
            if (month < 10) {
                return year + "-" + "0" + month + "-" + (day.length() == 1 ? "0" + day : day);
            } else {
                return year + "-" + month + "-" + (day.length() == 1 ? "0" + day : day);
            }
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }

    public static class ReplaceableDayText {

        private String oldDayText = null;
        private String newDayText = null;

        public ReplaceableDayText(String oldDayText, String newDayText) {
            this.oldDayText = oldDayText;
            this.newDayText = newDayText;
        }

        public String getOldDayText() {
            return oldDayText;
        }

        public void setOldDayText(String oldDayText) {
            this.oldDayText = oldDayText;
        }

        public String getNewDayText() {
            return newDayText;
        }

        public void setNewDayText(String newDayText) {
            this.newDayText = newDayText;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }

    private class CalendarAdapter extends ArrayAdapter<CalendarDate> {

        private ViewHolder item;
        private ArrayList<CalendarDate> data;

        public CalendarAdapter(Context context, int view, ArrayList<CalendarDate> data) {
            super(context, view, data);

            this.data = data;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_calendar_day, null);
                item = new ViewHolder(convertView);
                convertView.setTag(item);
            } else {
                item = (ViewHolder) convertView.getTag();
                if(item == null) {
                    convertView = View.inflate(context, R.layout.item_calendar_day, null);
                    item = new ViewHolder(convertView);
                    convertView.setTag(item);
                }
            }

            /*
            LayoutParams lps = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lps.height = getHeight() / 7;
            convertView.setLayoutParams(lps);
            */

            final CalendarGridView.CalendarDate cd = data.get(position);
            if (cd != null) {
                item.dayTV.setText(cd.getDay());

                // weekday
                if(position <= 6) {
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                    lp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                    // lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    item.dayTV.setLayoutParams(lp);


                    if(position == 0) {
                        item.dayTV.setTextColor(Color.parseColor("#f00000"));
                    } else if(position == 6) {
                        item.dayTV.setTextColor(Color.parseColor("#00b4e9"));
                    } else {
                        item.dayTV.setTextColor(Color.parseColor("#323232"));
                    }

                } else if(position > 6) {
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                    lp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                    // lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    item.dayTV.setLayoutParams(lp);


                    if(cd.getWeek() == 1) {
                        item.dayTV.setTextColor(Color.parseColor("#f00000"));
                    } else if(cd.getWeek() == 7) {
                        item.dayTV.setTextColor(Color.parseColor("#00b4e9"));
                    } else {
                        item.dayTV.setTextColor(Color.parseColor("#323232"));
                    }
                }




                for (int i = 0; i < objectData.size(); i++) {
                    try {
                        JSONObject salesData = objectData.get(i);

                        JSONObject saleData = salesData.getJSONObject("data");
                        if(Utils.getString(saleData, "date").equals(cd.getFullDay())) {
                            item.totalPriceTV.setText(Utils.comma(Utils.getString(saleData, "total_price")));
                            item.salesPriceV.setText(Utils.comma(Utils.getString(saleData, "sales_price")));
                            item.cancelPriceTV.setText(Utils.comma(Utils.getString(saleData, "cancel_price")));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                item.dayIV.setVisibility(GONE);

                if (greenDays.contains(cd.getFullDay())) {
                    item.backIV.setBackgroundResource(R.drawable.calendar_green);
                    item.dayTV.setTextColor(getGreenDateTextColor());

                    // 공부한 것
                    for(ReplaceableDayText replaceableDayText : replaceableDayTexts) {
                        if(cd.getFullDay().equals(replaceableDayText.getOldDayText())) {
                            item.dayTV.setText(replaceableDayText.getNewDayText());
                            item.dayTV.setTextColor(Color.parseColor("#F6CC51"));
                            break;
                        }
                    }

                } else if (yellowDays.contains(cd.getFullDay())) {
                    item.backIV.setBackgroundResource(R.drawable.calendar_yellow);
                    item.dayTV.setTextColor(getYellowDateTextColor());
                } else if (organgeDays.contains(cd.getFullDay())) {
                    item.backIV.setBackgroundResource(R.drawable.calendar_orange);
                    item.dayTV.setTextColor(getOrgangeDateTextColor());
                } else if (useDays.contains(cd.getFullDay())) {
                    item.backIV.setBackgroundResource(R.drawable.calendar_green);
                    item.dayTV.setText("");
                    //item.dayIV.setImageResource(R.mipmap.cal_used);
                    item.dayIV.setVisibility(VISIBLE);
                } else if (attendanceDays.contains(cd.getFullDay())) {
                    item.backIV.setBackgroundResource(R.drawable.calendar_green);
                    item.dayTV.setTextColor(getAttendanceDateTextColor());
                }

                if(cd.isPrevMonth()) {
                    item.dayTV.setTextSize(11);
                    item.dayTV.setTextColor(getPrevMonthDateTextColor());
                } else if(cd.isNextMonth()) {
                    item.dayTV.setTextSize(11);
                    item.dayTV.setTextColor(getNextMonthDateTextColor());
                }



            }

            return convertView;
        }
    }

    public static class ViewHolder {
        public TextView dayTV;
        public ImageView backIV;
        public ImageView dayIV;
        public TextView totalPriceTV;
        public TextView cancelPriceTV;
        public TextView salesPriceV;

        public ViewHolder(View v) {
            dayTV = (TextView) v.findViewById(R.id.dayTV);
            backIV = (ImageView) v.findViewById(R.id.backIV);
            dayIV = (ImageView) v.findViewById(R.id.dayIV);

        }
    }

    public String getMonthEnglishLong() {
        if(this.month == 0) {
            return "January";
        } else if(this.month == 1) {
            return "Febuary";
        } else if(this.month == 2) {
            return "March";
        } else if(this.month == 3) {
            return "April";
        } else if(this.month == 4) {
            return "May";
        } else if(this.month == 5) {
            return "June";
        } else if(this.month == 6) {
            return "July";
        } else if(this.month == 7) {
            return "August";
        } else if(this.month == 8) {
            return "September";
        } else if(this.month == 9) {
            return "October";
        } else if(this.month == 10) {
            return "November";
        } else if(this.month == 11) {
            return "December";
        }

        return "-";
    }

    public void prevMonth() {
        this.month = this.month - 1;
        if(this.month == -1) {
            this.month = 11;
            this.year = this.year - 1;
        }

        draw();
    }

    public void nextMonth() {
        this.month = this.month + 1;
        if(this.month == 12) {
            this.month = 0;
            this.year = this.year + 1;
        }

        draw();
    }

    public void addReplaceableDayText(ReplaceableDayText replaceableDayText) {
        getReplaceableDayTexts().add(replaceableDayText);
    }

    public void notifyDataSetChanged() {
        ((BaseAdapter) getAdapter()).notifyDataSetChanged();
    }
}
