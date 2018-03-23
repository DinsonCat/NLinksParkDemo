package com.nlinks.parkdemo.entity.other;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dinson
 * @date 2017/6/28
 */
public class BrandOfCar {


    private List<DatasBean> datas;

    public List<DatasBean> getDatas() {
        return datas;
    }

    public void setDatas(List<DatasBean> datas) {
        this.datas = datas;
    }

    public class DatasBean {
        /**
         * brandname : 奥迪
         * brandimg : http://x.autoimg.cn/app/image/brands/33.png
         * letter : A
         * seriesclub : ["奥迪A3","奥迪A4L","奥迪A6L","奥迪Q3","奥迪Q5","奥迪Q4","奥迪A1","奥迪A5","奥迪S","奥迪A7","奥迪A8","奥迪Q7","奥迪TT","奥迪R8","奥迪e-tron","奥迪A9","allroad","奥迪Q1","奥迪Q2","奥迪Q9","quattro","奥迪RS"]
         */

        private String brandname;
        private String brandimg;
        private String letter;
        private ArrayList<String> seriesclub;


        public String getBrandname() {
            return brandname;
        }

        public void setBrandname(String brandname) {
            this.brandname = brandname;
        }

        public String getBrandimg() {
            return brandimg;
        }

        public void setBrandimg(String brandimg) {
            this.brandimg = brandimg;
        }

        public String getLetter() {
            return letter;
        }

        public void setLetter(String letter) {
            this.letter = letter;
        }

        public ArrayList<String> getSeriesclub() {
            return seriesclub;
        }

        public void setSeriesclub(ArrayList<String> seriesclub) {
            this.seriesclub = seriesclub;
        }
    }
}
