package com.devstories.anipointcompany.android.base;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devstories.anipointcompany.android.R;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by theclub on 3/8/17.
 */

public class CalendarGridView2 extends GridView {

    private final Context context;

    private int year;
    private int month;

    private String seletedDate = "";

    private OnDateSelectedListener onDateSelectedListener;

    ArrayList<CalendarDate> data = new ArrayList<CalendarDate>();
    ArrayList<CalendarDate> reserved = new ArrayList<CalendarDate>();
    ArrayList<CalendarDate> my = new ArrayList<CalendarDate>();

    private ArrayList<JSONObject> objectData = new ArrayList<JSONObject>();

    public ArrayList<JSONObject> getObjectData() {
        return objectData;
    }

    public void setObjectData(ArrayList<JSONObject> objectData) {
        this.objectData = objectData;
    }

    public String getSeletedDate() {
        return seletedDate;
    }

    public void setSeletedDate(String seletedDate) {
        this.seletedDate = seletedDate;
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

    public OnDateSelectedListener getOnDateSelectedListener() {
        return onDateSelectedListener;
    }

    public void setOnDateSelectedListener(OnDateSelectedListener onDateSelectedListener) {
        this.onDateSelectedListener = onDateSelectedListener;
    }

    public ArrayList<CalendarDate> getReserved() {
        return reserved;
    }

    public ArrayList<CalendarDate> getMy() {
        return my;
    }

    public void setMy(ArrayList<CalendarDate> my) {
        this.my = my;
    }

    public CalendarGridView2(Context context) {
        super(context);
        this.context = context;

        init();
    }

    public CalendarGridView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        init();
    }

    public CalendarGridView2(Context context, AttributeSet attrs, int defStyleAttr) {
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

    public void draw() {

        data.clear();

        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
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

        // 전월
        cal.add(Calendar.DATE, -week);
        for(int i = 1; i < week; i++) {
            cal.add(Calendar.DATE, 1);

            SimpleDateFormat sdf = new SimpleDateFormat("d");
            String day = sdf.format(cal.getTime());

            CalendarDate cd = new CalendarDate(day);
            cd.setDate(cal.get(Calendar.DATE));
            cd.setMonth(cal.get(Calendar.MONTH) + 1);
            cd.setYear(cal.get(Calendar.YEAR));
            cd.setPrevMonth(true);

            data.add(cd);
        }

        Calendar today = Calendar.getInstance();
        int todayYear = today.get(Calendar.YEAR);
        int todayMonth = today.get(Calendar.MONTH);
        int todayDate = today.get(Calendar.DATE);

        // 이번 달
        for(int i = 1; i <= max; i++) {
            cal.add(Calendar.DATE, 1);

            int year = cal.get(Calendar.YEAR);
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

            if(todayYear == year && todayMonth == month && todayDate == date) {
                cd.setToday(true);
            }

            if (cd.getFullDay().equals(seletedDate)) {
                cd.setSeleted(true);
            }

            data.add(cd);
        }

        // 다음달
        week = cal.get(Calendar.DAY_OF_WEEK);
        while (week < 7) {
            cal.add(Calendar.DATE, 1);

            SimpleDateFormat sdf = new SimpleDateFormat("d");
            String day = sdf.format(cal.getTime());

            CalendarDate cd = new CalendarDate(day);
            cd.setDate(cal.get(Calendar.DATE));
            cd.setMonth(cal.get(Calendar.MONTH) + 1);
            cd.setYear(cal.get(Calendar.YEAR));
            cd.setPrevMonth(false);
            cd.setNextMonth(true);

            data.add(cd);

            week = cal.get(Calendar.DAY_OF_WEEK);
        }

        setAdapter(new CalendarAdapter(context, R.layout.item_calendar_day, data));
        ((BaseAdapter) getAdapter()).notifyDataSetChanged();
    }

    public static class CalendarDate {

        String day = "";
        int month = -1;
        int year = -1;
        int date = -1;
        boolean isPrevMonth = false;
        boolean isNextMonth = false;
        boolean isToday = false;
        boolean seleted = false;
        int count = 0;

        public CalendarDate(String day) {
            this.day = day;
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

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public Calendar getCalendar() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            try {
                c.setTime(sdf.parse(getFullDay()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return c;
        }

        public Date getTime() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date d = null;
            try {
                d = sdf.parse(getFullDay());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return d;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
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

        public boolean isSeleted() {
            return seleted;
        }

        public void setSeleted(boolean seleted) {
            this.seleted = seleted;
        }

        public String getFullDay() {
            if (month < 10) {
                return year + "-" + "0" + month + "-" + (day.length() == 1 ? "0" + day : day);
            } else {
                return year + "-" + month + "-" + (day.length() == 1 ? "0" + day : day);
            }
        }

        @Override
        public String toString() { return ToStringBuilder.reflectionToString(this); }
    }

    private class CalendarAdapter extends ArrayAdapter<CalendarDate> {

        private ViewHolder item;
        private ArrayList<CalendarDate> data;

        public CalendarAdapter(Context context, int view, ArrayList<CalendarDate> data) {
            super(context, view, data);

            this.data = data;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
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

            LayoutParams lps = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            lps.height = getHeight() / 7;

            convertView.setLayoutParams(lps);

            final CalendarGridView2.CalendarDate cd = data.get(position);
            if (cd != null) {

                item.dayTV.setText(cd.getDay());

                if(cd.isSeleted()) {
                    item.dayLL.setBackgroundColor(Color.parseColor("#F2FFED"));
                } else {
                    item.dayLL.setBackgroundColor(Color.parseColor("#FFFFFF"));

                    if (position % 7 == 0) {
                        item.dayTV.setTextColor(Color.parseColor("#ff6464"));
                    } else {
                        item.dayTV.setTextColor(Color.parseColor("#000000"));
                    }

                }

                for(CalendarGridView2.CalendarDate cdd : reserved) {
                    if(cd.getFullDay().equals(cdd.getFullDay()) && cdd.getCount() >= 5) {
                        item.dayTV.setTextColor(Color.parseColor("#ff5c5c"));
                    }
                }

                for(CalendarGridView2.CalendarDate cdd : my) {
                    if(cd.getFullDay().equals(cdd.getFullDay()) && cdd.getCount() == 1) {
                        item.dayTV.setTextColor(Color.parseColor("#ff5c5c"));
                    }
                }

                // day
                item.dayTV.setTextSize(11);
                if(position <= 6) {
                    item.dayTV.setTypeface(null, Typeface.BOLD);
                } else {
                    item.dayLL.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(cd.isPrevMonth || cd.isNextMonth) {
                                return;
                            }

                            boolean isSelected = cd.isSeleted();
                            if(!isSelected) {
                                for(CalendarGridView2.CalendarDate cdddd : data) {
                                    cdddd.setSeleted(false);
                                }
                            }

                            cd.setSeleted(true);

                            notifyDataSetChanged();

                            if(onDateSelectedListener != null) {
                                onDateSelectedListener.dateSelected(cd.getYear(), cd.getMonth(), cd.getDate());
                            }
                        }
                    });
                }

                if(cd.isToday) {
                    // item.backIV.setBackgroundResource(R.drawable.calendar_rectangle);
                    item.todayTV.setVisibility(View.VISIBLE);
                } else {
                    item.todayTV.setVisibility(View.INVISIBLE);
                    if(cd.isPrevMonth || cd.isNextMonth) {
                        item.dayTV.setTextSize(11);
                        item.dayTV.setTextColor(Color.parseColor("#cccccc"));
                    }
                }

                try {

                    item.reservationCntTV.setVisibility(View.INVISIBLE);

                    for (int i = 0; i < objectData.size(); i++) {
                        JSONObject objData = objectData.get(i);
                        JSONObject reserve = objData.getJSONObject("Reserve");
                        String reserve_date = Utils.getString(reserve, "reserve_date");

                        String reserve_date_str = new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyyy.M.d").parse(reserve_date));

                        if (reserve_date_str.equals(cd.getFullDay())) {
                            JSONObject data = objData.getJSONObject("0");
                            int reservationCnt = Utils.getInt(data, "reservationCnt");
                            item.reservationCntTV.setVisibility(View.VISIBLE);
                            item.reservationCntTV.setText(String.valueOf(reservationCnt));
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }


            return convertView;
        }
    }

    public static class ViewHolder {
        public LinearLayout dayLL;
        public TextView todayTV;
        public TextView dayTV;
        public TextView reservationCntTV;

        public ViewHolder(View v) {
            dayLL = (LinearLayout) v.findViewById(R.id.dayLL);
            todayTV = (TextView) v.findViewById(R.id.todayTV);
            dayTV = (TextView) v.findViewById(R.id.dayTV);
            reservationCntTV = (TextView) v.findViewById(R.id.reservationCntTV);
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

    public interface OnDateSelectedListener {
        public void dateSelected(int year, int month, int day);
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


    public void notifyDataSetChanged() {
        ((BaseAdapter) getAdapter()).notifyDataSetChanged();
    }

    public String getSelectDay() {

        String selectedDay = "";

        for (CalendarGridView2.CalendarDate cd : data) {
            if(cd.isSeleted()) {
                selectedDay = cd.getFullDay();
            }
        }

        return selectedDay;
    }

    public String getFirstSelectedDay() {

        for (CalendarGridView2.CalendarDate cd : data) {
            if(cd.isSeleted()) {
                return cd.getFullDay();
            }
        }

        return null;
    }
}
