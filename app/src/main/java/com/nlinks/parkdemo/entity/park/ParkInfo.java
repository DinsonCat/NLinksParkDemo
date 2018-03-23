package com.nlinks.parkdemo.entity.park;

import java.io.Serializable;
import java.util.List;

/**
 * @author lzhixing@linewell.com
 *         Created at 2016/01/06 17:58
 */
public class ParkInfo implements Serializable {

    private List<ParkMain> parkInfo;
    private List<FavPark> favPark;

    public List<FavPark> getFavPark() {
        return favPark;
    }

    public void setFavPark(List<FavPark> favPark) {
        this.favPark = favPark;
    }

    public List<ParkMain> getParkInfo() {
        return parkInfo;
    }

    public void setParkInfo(List<ParkMain> parkInfo) {
        this.parkInfo = parkInfo;
    }
}
