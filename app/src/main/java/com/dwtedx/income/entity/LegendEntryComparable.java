package com.dwtedx.income.entity;

import com.github.mikephil.charting.components.LegendEntry;

public class LegendEntryComparable extends LegendEntry implements Comparable<LegendEntryComparable> {
    @Override
    public int compareTo(LegendEntryComparable o) {
        // 先按label排序
//        if (this.label > o.label()) {
//            return (this.age - o.getAge());
//        }
//        if (this.age < o.getAge()) {
//            return (this.age - o.getAge());
//        }
        return 0;
    }
}
