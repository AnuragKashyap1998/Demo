package anurag.myappdemo;

/**
 * Created by anurag on 18-03-2018.
 */

public class Guide {
    String name;
    String area;
    String package_price;
    String mobile_no;
    Float rating;
    String regno;

    public Guide()
    {}

    public Guide( String regno,String name,String mobile_no,String area,String package_price,Float rating) {
        this.name = name;
        this.area = area;
        this.package_price = package_price;
        this.mobile_no = mobile_no;
        this.rating = rating;
        this.regno = regno;
    }

    public String getRegno() {
        return regno;
    }

    public void setRegno(String regno) {
        this.regno = regno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPackage_price() {
        return package_price;
    }

    public void setPackage_price(String package_price) {
        this.package_price = package_price;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }


}
