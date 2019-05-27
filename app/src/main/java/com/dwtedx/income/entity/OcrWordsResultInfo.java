package com.dwtedx.income.entity;

import java.util.List;

public class OcrWordsResultInfo {
    /**
     * location : {"left":10,"top":3,"width":121,"height":24}
     * words : 姓名:小明明
     * chars : [{"location":{"left":16,"top":6,"width":17,"height":20},"char":"姓"},{"location":{"left":35,"top":6,"width":17,"height":20},"char":"名"},{"location":{"left":55,"top":6,"width":11,"height":20},"char":":"},{"location":{"left":68,"top":6,"width":17,"height":20},"char":"小"},{"location":{"left":87,"top":6,"width":17,"height":20},"char":"明"},{"location":{"left":107,"top":6,"width":17,"height":20},"char":"明"}]
     */

    private OcrLocationInfo location;
    private String words;

    public OcrLocationInfo getLocation() {
        return location;
    }

    public void setLocation(OcrLocationInfo location) {
        this.location = location;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

}
