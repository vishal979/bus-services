package com.vishal.busservices;

public class DetailBusClass {
    private String time;
    private int seats;
    private String busId;
    public DetailBusClass(String time,int seats,String busId){
        this.time=time;
        this.seats=seats;
        this.busId=busId;
    }

    public DetailBusClass(String time){
        this.time=time;
    }

    public String getTime(){
        return time;
    }
    public int getSeats(){
        return seats;
    }
    public String getBusId(){
        return busId;
    }
}
