package com.dwtedx.income.report;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseFragment;
import com.dwtedx.income.entity.DiIncome;
import com.dwtedx.income.sqliteservice.DlIncomeService;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.utility.CommonUtility;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by dwtedx(qinyl)(http://dwtedx.com) on 16/5/16.
 * Company 路之遥网络科技有限公司
 * Description TODO(这里用一句话描述这个类的作用)
 */
public class ReportPayingPieFragment extends BaseFragment implements View.OnClickListener {

    private DlIncomeService mDlIncomeService;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat formatMonth = new SimpleDateFormat("yyyy-MM");
    private Typeface mTf;

    private NestedScrollView mReportScrollview;

    //支出圆形图片
    private PieChart mPieChart;
    private TextView mPieStartTimeTextView;
    private TextView mPieEndTimeTextView;
    private String mPieStartTimePie;
    private String mPieEndTimePie;
    private Calendar mPieStartTimeCalendar;
    private Calendar mPieEndTimeCalendar;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_report_paying_pie, container, false);

        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mReportScrollview = (NestedScrollView) view.findViewById(R.id.report_scrollview);

        mPieChart = (PieChart) view.findViewById(R.id.pie_chart);
        mPieStartTimeTextView = (TextView) view.findViewById(R.id.pie_chart_starttime);
        mPieStartTimeTextView.setOnClickListener(this);
        mPieEndTimeTextView = (TextView) view.findViewById(R.id.pie_chart_endtime);
        mPieEndTimeTextView.setOnClickListener(this);

        mDlIncomeService = DlIncomeService.getInstance(getContext());

        mTf = Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Regular.ttf");
    }

    @Override
    public void onResume() {
        super.onResume();

        //==================圆形图=======开始=============================
        //获取当前月第一天：
        mPieStartTimeCalendar = Calendar.getInstance();
        mPieStartTimeCalendar.add(Calendar.DAY_OF_MONTH, -30);
        //设置为1号,当前日期既为本月第一天
        mPieStartTimeTextView.setText(format.format(mPieStartTimeCalendar.getTime()));
        mPieStartTimePie = format.format(mPieStartTimeCalendar.getTime()) + " 00:00:00";
        //获取当前月最后一天
        mPieEndTimeCalendar = Calendar.getInstance();
        mPieEndTimeTextView.setText(format.format(mPieEndTimeCalendar.getTime()));
        mPieEndTimePie = format.format(mPieEndTimeCalendar.getTime()) + " 24:00:00";
        generatePayingDataPie();
        //==================圆形图=======结束=============================
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pie_chart_starttime:
                DatePickerDialog pieStartdd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mPieStartTimeCalendar.set(year, monthOfYear, dayOfMonth);
                        mPieStartTimeTextView.setText(format.format(mPieStartTimeCalendar.getTime()));
                        mPieStartTimePie = format.format(mPieStartTimeCalendar.getTime()) + " 00:00:00";
                        generatePayingDataPie();
                    }
                }, mPieStartTimeCalendar.get(Calendar.YEAR), mPieStartTimeCalendar.get(Calendar.MONTH), mPieStartTimeCalendar.get(Calendar.DAY_OF_MONTH));
                pieStartdd.show();
                break;

            case R.id.pie_chart_endtime:
                DatePickerDialog pieEnddd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mPieEndTimeCalendar.set(year, monthOfYear, dayOfMonth);
                        mPieEndTimeTextView.setText(format.format(mPieEndTimeCalendar.getTime()));
                        mPieEndTimePie = format.format(mPieEndTimeCalendar.getTime()) + " 24:00:00";
                        generatePayingDataPie();
                    }
                }, mPieEndTimeCalendar.get(Calendar.YEAR), mPieEndTimeCalendar.get(Calendar.MONTH), mPieEndTimeCalendar.get(Calendar.DAY_OF_MONTH));
                pieEnddd.show();
                break;
        }
    }

    /**
     * 支出饼图
     *
     * @return
     */
    private void generatePayingDataPie() {

        if (-1 != CommonUtility.compareDate(mPieStartTimePie, mPieEndTimePie)) {
            //Toast.makeText(getContext(), getString(R.string.report_date_error_ago), Toast.LENGTH_SHORT).show();
            Snackbar.make(mReportScrollview, R.string.report_date_error_ago, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return;
        }

        List<DiIncome> mDiIncomeList = mDlIncomeService.getByTypeSumMoneyData(CommonConstants.INCOME_ROLE_PAYING, mPieStartTimePie, mPieEndTimePie);

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        ArrayList<Integer> entriescolors = new ArrayList<>();
        double allPay = 0;
        String showColor;
        int iShowColor = -1;

        for (int i = 0; i < mDiIncomeList.size(); i++) {
            entries.add(new PieEntry((float) mDiIncomeList.get(i).getMoneysum(), mDiIncomeList.get(i).getType() + CommonUtility.doubleTrans(mDiIncomeList.get(i).getMoneysum())));

            //2019-02-13 自定义类型报表颜色更换
            showColor = CommonConstants.PAYTYPE_DIY_COLOR;
            if(!CommonUtility.isEmpty(mDiIncomeList.get(i).getColor())){
                if(CommonConstants.PAYTYPE_DIY_COLOR.equals(mDiIncomeList.get(i).getColor())){
                    if(iShowColor >= 0){
                        showColor = CommonConstants.INCOME_PAYTYPE_DIY_ICON_COLOR[iShowColor % CommonConstants.INCOME_PAYTYPE_DIY_ICON_COLOR.length];
                    }else{
                        showColor = mDiIncomeList.get(i).getColor();
                    }
                    iShowColor++;
                } else {
                    showColor = mDiIncomeList.get(i).getColor();
                }
            }

            String[] colorArr = showColor.split(",");
            entriescolors.add(Color.rgb(Integer.parseInt(colorArr[0]), Integer.parseInt(colorArr[1]), Integer.parseInt(colorArr[2])));
            allPay += mDiIncomeList.get(i).getMoneysum();
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        // space between slices
        dataSet.setSliceSpace(2f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(entriescolors);

        SpannableString allPayStr = new SpannableString(getString(R.string.record_pay_all) + "\n￥" + CommonUtility.doubleFormat(allPay));
        allPayStr.setSpan(new RelativeSizeSpan(1.6f), 0, 3, 0);
        allPayStr.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.common_body_tip_colors)), 0, 3, 0);
        allPayStr.setSpan(new RelativeSizeSpan(1.8f), 3, allPayStr.length(), 0);

        PieData cd = new PieData(dataSet);

        // apply styling
        //mPieChart.setDescription("");
        mPieChart.setDrawEntryLabels(false);
        mPieChart.setHoleRadius(52f);
        mPieChart.setTransparentCircleRadius(57f);
        mPieChart.setCenterText(allPayStr);
        mPieChart.setCenterTextSize(105);
        mPieChart.setCenterTextTypeface(mTf);
        mPieChart.setCenterTextSize(9f);
        mPieChart.setUsePercentValues(true);
        mPieChart.setExtraOffsets(5, 0, 40, 0);
        mPieChart.getDescription().setEnabled(false);

        cd.setValueFormatter(new PercentFormatter());
        cd.setValueTypeface(mTf);
        cd.setValueTextSize(11f);
        cd.setValueTextColor(Color.WHITE);
        // set data
        mPieChart.setData((PieData) cd);

        Legend l = mPieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // do not forget to refresh the chart
        // mPieChart.invalidate();
        mPieChart.animateY(900);
    }

}
