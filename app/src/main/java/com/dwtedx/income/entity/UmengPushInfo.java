package com.dwtedx.income.entity;

public class UmengPushInfo {


    /**
     * display_type : notification
     * msg_id : umc5ghf155850422997810
     * body : {"after_open":"go_url","play_lights":"false","ticker":"umeng测试推送","play_vibrate":"false","text":"umeng测试推送umeng测试推送umeng测试推送","title":"umeng测试推送","play_sound":"true","url":"https://www.baidu.com/"}
     * random_min : 0
     */

    private String display_type;
    private String msg_id;
    private BodyBean body;
    private int random_min;

    public String getDisplay_type() {
        return display_type;
    }

    public void setDisplay_type(String display_type) {
        this.display_type = display_type;
    }

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public int getRandom_min() {
        return random_min;
    }

    public void setRandom_min(int random_min) {
        this.random_min = random_min;
    }

    public static class BodyBean {
        /**
         * after_open : go_url
         * play_lights : false
         * ticker : umeng测试推送
         * play_vibrate : false
         * text : umeng测试推送umeng测试推送umeng测试推送
         * title : umeng测试推送
         * play_sound : true
         * url : https://www.baidu.com/
         */

        private String after_open;
        private String play_lights;
        private String ticker;
        private String play_vibrate;
        private String text;
        private String title;
        private String play_sound;
        private String url;
        private String activity;
        private String custom;

        public String getAfter_open() {
            return after_open;
        }

        public void setAfter_open(String after_open) {
            this.after_open = after_open;
        }

        public String getPlay_lights() {
            return play_lights;
        }

        public void setPlay_lights(String play_lights) {
            this.play_lights = play_lights;
        }

        public String getTicker() {
            return ticker;
        }

        public void setTicker(String ticker) {
            this.ticker = ticker;
        }

        public String getPlay_vibrate() {
            return play_vibrate;
        }

        public void setPlay_vibrate(String play_vibrate) {
            this.play_vibrate = play_vibrate;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPlay_sound() {
            return play_sound;
        }

        public void setPlay_sound(String play_sound) {
            this.play_sound = play_sound;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getActivity() {
            return activity;
        }

        public void setActivity(String activity) {
            this.activity = activity;
        }

        public String getCustom() {
            return custom;
        }

        public void setCustom(String custom) {
            this.custom = custom;
        }
    }
}
