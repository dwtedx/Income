package com.dwtedx.income.entity;

import java.util.List;

public class OcrResultInfo {


    /**
     * log_id : 2661573626
     * words_result : [{"location":{"left":10,"top":3,"width":121,"height":24},"words":"姓名:小明明","chars":[{"location":{"left":16,"top":6,"width":17,"height":20},"char":"姓"},{"location":{"left":35,"top":6,"width":17,"height":20},"char":"名"},{"location":{"left":55,"top":6,"width":11,"height":20},"char":":"},{"location":{"left":68,"top":6,"width":17,"height":20},"char":"小"},{"location":{"left":87,"top":6,"width":17,"height":20},"char":"明"},{"location":{"left":107,"top":6,"width":17,"height":20},"char":"明"}]}]
     * words_result_num : 2
     */

    private long log_id;
    private int words_result_num;
    private List<OcrWordsResultInfo> words_result;

    public long getLog_id() {
        return log_id;
    }

    public void setLog_id(long log_id) {
        this.log_id = log_id;
    }

    public int getWords_result_num() {
        return words_result_num;
    }

    public void setWords_result_num(int words_result_num) {
        this.words_result_num = words_result_num;
    }

    public List<OcrWordsResultInfo> getWords_result() {
        return words_result;
    }

    public void setWords_result(List<OcrWordsResultInfo> words_result) {
        this.words_result = words_result;
    }
}
