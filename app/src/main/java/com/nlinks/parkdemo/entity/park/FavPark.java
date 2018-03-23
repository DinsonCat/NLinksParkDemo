package com.nlinks.parkdemo.entity.park;

import java.io.Serializable;

/**
 *
 *
 * @author lzhixing@linewell.com
 *         Created at 2016/01/06 18:00
 */
public class FavPark implements Serializable {

    private String parkCode;

    public String getParkCode() {
        return parkCode;
    }

    public void setParkCode(String parkCode) {
        this.parkCode = parkCode;
    }
}
