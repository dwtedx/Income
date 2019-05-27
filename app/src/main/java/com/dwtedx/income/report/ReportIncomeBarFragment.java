package com.dwtedx.income.report;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseFragment;
import com.dwtedx.income.sqliteservice.DlIncomeService;
import com.dwtedx.income.utility.CommonConstants;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by dwtedx(qinyl)(http://dwtedx.com) on 16/5/16.
 * Company 路之遥网络科技有限公司
 * Description TODO(这里用一句话描述这个类的作用)
 */
public class ReportIncomeBarFragment extends BaseFragment implements View.OnClickListener {

    private DlIncomeService mDlIncomeService;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat formatMonth = new SimpleDateFormat("yyyy-MM");
    private SimpleDateFormat formatMonthDesplay = new SimpleDateFormat("MM月");
    private Typeface mTf;

    private NestedScrollView mReportScrollview;

    //纯收趋势图片
    private BarChart mBarChart;
    private TextView mBarStartTimeTextView;
    private TextView mBarEndTimeTextView;
    private String mBarStartTimeBar;
    private String mBarEndTimeBar;
    private Calendar mBarStartTimeCalendar;
    private Calendar mBarEndTimeCalendar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_report_income_bar, container, false);

        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mReportScrollview = (NestedScrollView) view.findViewById(R.id.report_scrollview);

        mBarChart = (BarChart) view.findViewById(R.id.bar_chart);
        mBarStartTimeTextView = (TextView) view.findViewById(R.id.bar_chart_starttime);
        mBarStartTimeTextView.setOnClickListener(this);
        mBarEndTimeTextView = (TextView) view.findViewById(R.id.bar_chart_endtime);
        mBarEndTimeTextView.setOnClickListener(this);

        mDlIncomeService = DlIncomeService.getInstance(getContext());

        mTf = Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Regular.ttf");
    }

    @Override
    public void onResume() {
        super.onResume();

        //==================纯收入线性图=======开始=============================
        //获取开始月：
        mBarStartTimeCalendar = Calendar.getInstance();
        mBarStartTimeCalendar.add(Calendar.MONTH, -5);
        //mBarStartTimeCalendar.set(CommonConstants.INCOME_START_YEAR, CommonConstants.INCOME_START_MONTH, CommonConstants.INCOME_START_DAY);
        mBarStartTimeTextView.setText(formatMonth.format(mBarStartTimeCalendar.getTime()));
        mBarStartTimeBar = formatMonth.format(mBarStartTimeCalendar.getTime()) + "-01 00:00:00";
        //获取当前月最后一天
        mBarEndTimeCalendar = Calendar.getInstance();
        mBarEndTimeCalendar.set(Calendar.DAY_OF_MONTH, mBarEndTimeCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        mBarEndTimeTextView.setText(formatMonth.format(mBarEndTimeCalendar.getTime()));
        mBarEndTimeBar = format.format(mBarEndTimeCalendar.getTime()) + " 24:00:00";

        generateDataBar();
        //==================纯收入线性图=======结束=============================


    }

    @Override
    public void onClick(View v) {
        Calendar today = Calendar.getInstance();
        switch (v.getId()) {
            case R.id.bar_chart_starttime:
                MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(getContext(),
                        new MonthPickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(int selectedMonth, int selectedYear) {
                                mBarStartTimeCalendar.set(selectedYear, selectedMonth, 1);
                                String yearMonth = formatMonth.format(mBarStartTimeCalendar.getTime());
                                mBarStartTimeTextView.setText(yearMonth);
                                mBarStartTimeBar = yearMonth + "-01 00:00:00";
                                generateDataBar();
                            }
                        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));

                builder.setMinYear(1991)
                        .setTitle(getString(R.string.report_date_select))
                        .setActivatedMonth(mBarStartTimeCalendar.get(Calendar.MONTH))
                        .setActivatedYear(mBarStartTimeCalendar.get(Calendar.YEAR))
                        .build()
                        .show();

                break;

            case R.id.bar_chart_endtime:
                MonthPickerDialog.Builder builderEnd = new MonthPickerDialog.Builder(getContext(),
                        new MonthPickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(int selectedMonth, int selectedYear) {
                                mBarEndTimeCalendar.set(selectedYear, selectedMonth, 1);
                                mBarEndTimeCalendar.set(Calendar.DAY_OF_MONTH, mBarEndTimeCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                                String yearMonth = formatMonth.format(mBarEndTimeCalendar.getTime());
                                mBarEndTimeTextView.setText(yearMonth);
                                mBarEndTimeBar = format.format(mBarEndTimeCalendar.getTime()) + " 24:00:00";
                                generateDataBar();
                            }
                        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));

                builderEnd.setMinYear(1991)
                        .setTitle(getString(R.string.report_date_select))
                        .build()
                        .show();


                break;
        }
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private void generateDataBar() {

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        //计算中间月
        final ArrayList<String> mBarMonthArr = new ArrayList<>();
        List<Integer> colors = new ArrayList<Integer>();
        int ii = 0;
        double income, paying;
        int green = Color.rgb(110, 190, 102);
        int red = Color.rgb(211, 74, 88);
        Calendar curr = Calendar.getInstance();
        Calendar currWhile = Calendar.getInstance();
        curr.set(mBarStartTimeCalendar.get(Calendar.YEAR), mBarStartTimeCalendar.get(Calendar.MONTH), mBarStartTimeCalendar.get(Calendar.DAY_OF_MONTH));
        while (curr.before(mBarEndTimeCalendar)) {
            mBarMonthArr.add(formatMonthDesplay.format(curr.getTime()));
            //curr.set(Calendar.DAY_OF_MONTH, curr.getActualMaximum(Calendar.DAY_OF_MONTH));
            currWhile.set(curr.get(Calendar.YEAR), curr.get(Calendar.MONTH), curr.get(Calendar.DAY_OF_MONTH));
            currWhile.set(Calendar.DAY_OF_MONTH, curr.getActualMaximum(Calendar.DAY_OF_MONTH));
            income = mDlIncomeService.getSumMoneyByData(CommonConstants.INCOME_ROLE_INCOME, formatMonth.format(currWhile.getTime()) + "-01 00:00:00", format.format(currWhile.getTime()) + " 24:00:00").getMoneysum();
            paying = mDlIncomeService.getSumMoneyByData(CommonConstants.INCOME_ROLE_PAYING, formatMonth.format(currWhile.getTime()) + "-01 00:00:00", format.format(currWhile.getTime()) + " 24:00:00").getMoneysum();
            float nombers = (float) (income - paying);
            entries.add(new BarEntry(ii, nombers));
            if (nombers >= 0)
                colors.add(red);
            else
                colors.add(green);
            curr.add(Calendar.MONTH, 1);
            ii++;
        }

        mBarChart.setBackgroundColor(Color.WHITE);
        mBarChart.setExtraTopOffset(-30f);
        mBarChart.setExtraBottomOffset(10f);
        mBarChart.setExtraLeftOffset(70f);
        mBarChart.setExtraRightOffset(70f);
        mBarChart.setDrawBarShadow(false);
        mBarChart.setDrawValueAboveBar(true);
        mBarChart.getDescription().setEnabled(false);

        //mBarChart.setDescription("");

        // scaling can now only be done on x- and y-axis separately
        mBarChart.setPinchZoom(false);

        mBarChart.setDrawGridBackground(false);

        XAxis xAxis = mBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTf);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setTextColor(Color.LTGRAY);
        xAxis.setTextSize(13f);
        xAxis.setLabelCount(5);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                try {
                    if(value < 0){
                        return "更早";
                    }
                    return mBarMonthArr.get((int) value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return value + "";
            }
        });

        YAxis left = mBarChart.getAxisLeft();
        left.setDrawLabels(false);
        left.setSpaceTop(25f);
        left.setSpaceBottom(25f);
        left.setDrawAxisLine(false);
        left.setDrawGridLines(false);
        left.setDrawZeroLine(true); // draw a zero line
        left.setZeroLineColor(Color.GRAY);
        left.setZeroLineWidth(0.7f);
        mBarChart.getAxisRight().setEnabled(false);
        mBarChart.getLegend().setEnabled(false);


        BarDataSet set = new BarDataSet(entries, "Values");
        set.setColors(colors);
        set.setValueTextColors(colors);

        BarData data = new BarData(set);
        data.setValueTextSize(13f);
        data.setValueTypeface(mTf);
        data.setBarWidth(0.8f);
        data.setValueFormatter(new ValueFormatter());
        mBarChart.animateY(700);
        mBarChart.setData(data);
        mBarChart.invalidate();

    }

    private class ValueFormatter implements IValueFormatter
    {

        private DecimalFormat mFormat;

        public ValueFormatter() {
            mFormat = new DecimalFormat("######.0");
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return mFormat.format(value);
        }
    }
}
