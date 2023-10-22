package com.example.v1.fragment1.function;

import android.graphics.Color;
import android.view.View;

import com.example.v1.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.List;

public class MakeForcastChart {

    View rootView;

    private int chart_text_size = 14;

    public MakeForcastChart(View rootView){
        this.rootView = rootView;
    }

    protected LineChart set_forcast_chart(){
        LineChart forcast_chart = (LineChart)rootView.findViewById(R.id.forecast_chart);

        //차트 초기화
        forcast_chart.invalidate();
        forcast_chart.clear();

        // touch gestures (false-비활성화)
        forcast_chart.setTouchEnabled(false);

        // scaling and dragging (false-비활성화)
        forcast_chart.setDragEnabled(false);
        forcast_chart.setScaleEnabled(false);

        //라인만 남김
        //x축
        XAxis xAxis = forcast_chart.getXAxis();
        xAxis.setLabelCount(4);
        xAxis.setDrawGridLines(true);
        xAxis.enableGridDashedLine(15, 10, 0);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawLabels(false);

        //y축
        YAxis yLAxis = forcast_chart.getAxisLeft();
        yLAxis.setDrawLabels(false);
        yLAxis.setDrawAxisLine(false);
        yLAxis.setDrawGridLines(false);
        YAxis yRAxis = forcast_chart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);


        //오른쪽 하단 기본 텍스트 지우기
        Description description = new Description();
        description.setText("");
        forcast_chart.setDescription(description);

        //legend 지움
        Legend legend = forcast_chart.getLegend();
        legend.setEnabled(false);

        //forcast_chart.animateY(1500, Easing.EasingOption.EaseInCubic); //차트 인 애니메이션

        return forcast_chart;
    }

    protected LineDataSet set_line_dataset(List<Entry> temperatures){
        LineDataSet lineDataSet = new LineDataSet(temperatures, "기온");

        //라인 설정
        lineDataSet.setLineWidth(2);
        lineDataSet.setColor(Color.DKGRAY);

        //점 설정
        lineDataSet.setCircleRadius(5);
        lineDataSet.setCircleColor(Color.DKGRAY);
        lineDataSet.setCircleColorHole(Color.DKGRAY);

        //하이라이터 제거
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);

        return lineDataSet;
    }

    protected LineData set_line_data(LineDataSet lineDataSet, String[] y_labels){
        LineData lineData = new LineData(lineDataSet);

        //데이터 레이블 설정
        lineData.setValueTextColor(Color.BLACK);
        lineData.setValueTextSize(chart_text_size);
        lineData.setValueFormatter(new GraphIValueFormatter(y_labels));

        return lineData;
    }

    private class GraphIValueFormatter implements IValueFormatter {
        private String [] mValues;

        GraphIValueFormatter(String[] values){this.mValues = values;}

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return mValues[(int) entry.getX()];
        }
    }
    private class GraphAxisValueFormatter implements IAxisValueFormatter {
        private String[] mValues;

        GraphAxisValueFormatter(String[] values){
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis){
            return mValues[(int) value];
        }

    }
}
