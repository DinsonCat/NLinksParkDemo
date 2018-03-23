package com.nlinks.parkdemo.entity.park;

import com.nlinks.parkdemo.entity.appointment.AppointmentRecord;

/**
 * 首页获取最后一条预约记录或者停车记录
 */
public class LastestParkRecord {


    /**
     * appointRecord : {}
     * parkRecord : {"staffCode":"CS002","inTime":"2017-06-19 15:50:49","couponMoney":0,"hasPay":18,"parkRecordId":"ca0aa69876cd4780bfafe92a574cb824","outTime":"2017-06-20 09:30:15","parkStatus":2,"payTime":"2017-06-19 17:36:41","parkSeconds":63566,"recordStatus":4,"consume":22,"stallCode":"35050506","parkName":"法制广场停车场","stallName":null,"payStatus":4,"address":"福建省泉州市丰泽区丰海路","waitPay":4,"longitude":118.624781,"latitude":24.883042,"chargeStandard":"停放1小时内（含1小时）每辆次3元，超过1小时后每30分钟加收2元（不足30分钟的按30分钟计），同一辆车当天停放最高收费30元。","plateNum":"京A01234"}
     */

    private AppointmentRecord appointRecord;
    private ParkRecord parkRecord;

    public AppointmentRecord getAppointRecord() {
        return appointRecord;
    }

    public void setAppointRecord(AppointmentRecord appointRecord) {
        this.appointRecord = appointRecord;
    }

    public ParkRecord getParkRecord() {
        return parkRecord;
    }

    public void setParkRecord(ParkRecord parkRecord) {
        this.parkRecord = parkRecord;
    }



}
