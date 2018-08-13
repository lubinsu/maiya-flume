package com.maiya.monitor.dao;

public class Address_Credit_Score {

    public int score(double ads_label_rate,double district_rate,double town_rate,int fangchan_cnt,int gouwu_cnt,int gsqiye_cnt) {

        double base = 570.14;
        double 	ads_label_rate_score = 0.0;
        double 	district_rate_score = 0.0;
        double 	town_rate_score = 0.0;
        double 	fangchan_cnt_score = 0.0;
        double 	gouwu_cnt_score = 0.0;
        double 	gsqiye_cnt_score = 0.0;

        //地址标签数据分值转化
        if(ads_label_rate>=0 && ads_label_rate<=0.485){
            ads_label_rate_score = -8.562572045;
        }else if(ads_label_rate>0.485 && ads_label_rate<=0.492){
            ads_label_rate_score = -2.867358643;
        }else if(ads_label_rate>0.492 && ads_label_rate<=0.511){
            ads_label_rate_score = 2.796798499;

        }else if(ads_label_rate>0.511 && ads_label_rate<=0.518){
            ads_label_rate_score = 9.921990132;
        }else if(ads_label_rate>0.518){
            ads_label_rate_score = 17.65011283;
        }else if(ads_label_rate == -1){
            ads_label_rate_score = 17.65011283;
        }


        //区县逾期率分值转化
        if(district_rate>=0 && district_rate<=0.407){
            district_rate_score = -42.56672907;
        }else if(district_rate>0.407 && district_rate<=0.492){
            district_rate_score = -8.676035118;
        }else if(district_rate>0.492 && district_rate<=0.623){
            district_rate_score = 6.731519068;
        }else if(district_rate>0.623){
            district_rate_score = 55.54657656;
        }else if(district_rate == -1){
            district_rate_score = 55.54657656;
        }

        //乡镇逾期率分值转化
        if(town_rate>=0 && town_rate<=0.303){
            town_rate_score = -267.3433967;
        }else if(town_rate>0.303 && town_rate<=0.485){
            town_rate_score = -45.27083676;
        }else if(town_rate>0.485 && town_rate<=0.593){
            town_rate_score = 13.39843459;
        }else if(town_rate>0.593){
            town_rate_score = 160.8988129;
        }else if(town_rate == -1){
            town_rate_score = 160.8988129;
        }

        //POI房产个数分值转化
        if(fangchan_cnt>=0 && fangchan_cnt<=3){
            fangchan_cnt_score = -1.752247101;
        }else if(fangchan_cnt>3){
            fangchan_cnt_score = 9.469993539;
        }else if(fangchan_cnt == -1){
            fangchan_cnt_score = 9.469993539;
        }

        //POI购物广场个数分值转化
        if(gouwu_cnt>=0 && gouwu_cnt<=3){
            gouwu_cnt_score = -1.865610123;
        }else if(gouwu_cnt>3){
            gouwu_cnt_score = 19.31004063;
        }else if(gouwu_cnt == -1){
            gouwu_cnt_score = 19.31004063;
        }


        //POI公司企业个数分值转化
        if(gsqiye_cnt>=0 && gsqiye_cnt<=2){
            gsqiye_cnt_score = -1.62448118;
        }else if(gsqiye_cnt>2){
            gsqiye_cnt_score = 16.98557556;
        }else if(gsqiye_cnt == -1){
            gsqiye_cnt_score = 16.98557556;
        }


        double ret = base + ads_label_rate_score + district_rate_score + town_rate_score + fangchan_cnt_score + gouwu_cnt_score + gsqiye_cnt_score;
        int score = (new Double(ret)).intValue();
        //logger.info(toString());
        return score;
    }





    public static void main(String[] args) {

        Address_Credit_Score acs = new Address_Credit_Score();
        int score = acs.score(0.142, 0.315, 0.321, 3, 1, 4);
        System.out.println(score);

    }

}
