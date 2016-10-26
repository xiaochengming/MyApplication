package com.example.administrator.myapplication.queryEntity;

import com.example.administrator.myapplication.entity.Housekeeper;

/**
 * Created by luhai on 2016/10/20.
 */
public class QueryHouserkeeperSize {
    private int count;
    private Housekeeper housekeeper;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Housekeeper getHousekeeper() {
        return housekeeper;
    }

    public void setHousekeeper(Housekeeper housekeeper) {
        this.housekeeper = housekeeper;
    }

    public QueryHouserkeeperSize(int count, Housekeeper housekeeper) {
        this.count = count;
        this.housekeeper = housekeeper;
    }
}
