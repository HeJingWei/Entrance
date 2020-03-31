package com.kentrasoft.entrance.base;

import com.squareup.otto.Bus;

/**
 * created date: 2020/2/27 on 17:38
 * des:
 * author: HJW HP
 */
public class BusManager {
    private BusManager(){}
    private static Bus bus = new Bus();
    public static Bus getInstance(){
        if (bus == null){
            bus = new Bus();
        }
        return bus;
    }
}
