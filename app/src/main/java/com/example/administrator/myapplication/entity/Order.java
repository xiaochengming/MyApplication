package com.example.administrator.myapplication.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Time;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2016/9/21.
 */
public class Order implements Parcelable {
    private int orderId;
    private User user;
    private Address address;
    private Timestamp time;// 可以把 yyyy-MM-dd hh:mm:ss 字符串转化为此类型
    private int state;// 状态 1.提交未付款， 2.已付款服务时间未到 3.过了服务时间未评价 4.已评价订单完成 5、申诉
    private float allprice;
    private Timestamp begdate;
    private int workerTime;
    private Housekeeper housekeeper;
    private Category category;
    private float price;// 单价
    private int number;// 数量
    private String worker;// 工作时间为保姆等用的
    private String subname;// 子标题
    private Time arriveTime;
    private Timestamp Endtime;// 结束时间

    public Order() {
    }

    public Order(User user, Address address, Timestamp time, int state, float allprice, Category category, float price, Time arriveTime) {
        this.orderId = orderId;
        this.user = user;
        this.address = address;
        this.time = time;
        this.state = state;
        this.allprice = allprice;
        this.category = category;
        this.price = price;
        this.arriveTime = arriveTime;
    }

    public Order(int orderId, User user, Address address, Timestamp time,
                 int state, float allprice, Timestamp begdate, int workerTime,
                 Housekeeper housekeeper, Category category, float price,
                 int number, String worker, String subname, Time arriveTime,
                 Timestamp endtime) {
        super();
        this.orderId = orderId;
        this.user = user;
        this.address = address;
        this.time = time;
        this.state = state;
        this.allprice = allprice;
        this.begdate = begdate;
        this.workerTime = workerTime;
        this.housekeeper = housekeeper;
        this.category = category;
        this.price = price;
        this.number = number;
        this.worker = worker;
        this.subname = subname;
        this.arriveTime = arriveTime;
        Endtime = endtime;
    }

    public Timestamp getEndtime() {
        return Endtime;
    }

    public void setEndtime(Timestamp endtime) {
        Endtime = endtime;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public float getAllprice() {
        return allprice;
    }

    public void setAllprice(float allprice) {
        this.allprice = allprice;
    }

    public Timestamp getBegdate() {
        return begdate;
    }

    public void setBegdate(Timestamp begdate) {
        this.begdate = begdate;
    }

    public int getWorkerTime() {
        return workerTime;
    }

    public void setWorkerTime(int workerTime) {
        this.workerTime = workerTime;
    }

    public Housekeeper getHousekeeper() {
        return housekeeper;
    }

    public void setHousekeeper(Housekeeper housekeeper) {
        this.housekeeper = housekeeper;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getWorker() {
        return worker;
    }

    public void setWorker(String worker) {
        this.worker = worker;
    }

    public String getSubname() {
        return subname;
    }

    public void setSubname(String subname) {
        this.subname = subname;
    }

    public Time getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(Time arriveTime) {
        this.arriveTime = arriveTime;
    }

    @Override
    public String toString() {
        return "Order [orderId=" + orderId + ", user=" + user + ", address="
                + address + ", time=" + time + ", state=" + state
                + ", allprice=" + allprice + ", begdate=" + begdate
                + ", workerTime=" + workerTime + ", housekeeper=" + housekeeper
                + ", category=" + category + ", price=" + price + ", number="
                + number + ", worker=" + worker + ", subname=" + subname
                + ", arriveTime=" + arriveTime + "]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.orderId);
        dest.writeParcelable(this.user, flags);
        dest.writeParcelable(this.address, flags);
        dest.writeSerializable(this.time);
        dest.writeInt(this.state);
        dest.writeFloat(this.allprice);
        dest.writeSerializable(this.begdate);
        dest.writeInt(this.workerTime);
        dest.writeParcelable(this.housekeeper, flags);
        dest.writeParcelable(this.category, flags);
        dest.writeFloat(this.price);
        dest.writeInt(this.number);
        dest.writeString(this.worker);
        dest.writeString(this.subname);
        dest.writeSerializable(this.arriveTime);
        dest.writeSerializable(this.Endtime);
    }

    protected Order(Parcel in) {
        this.orderId = in.readInt();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.address = in.readParcelable(Address.class.getClassLoader());
        this.time = (Timestamp) in.readSerializable();
        this.state = in.readInt();
        this.allprice = in.readFloat();
        this.begdate = (Timestamp) in.readSerializable();
        this.workerTime = in.readInt();
        this.housekeeper = in.readParcelable(Housekeeper.class.getClassLoader());
        this.category = in.readParcelable(Category.class.getClassLoader());
        this.price = in.readFloat();
        this.number = in.readInt();
        this.worker = in.readString();
        this.subname = in.readString();
        this.arriveTime = (Time) in.readSerializable();
        this.Endtime = (Timestamp) in.readSerializable();
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel source) {
            return new Order(source);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
}
