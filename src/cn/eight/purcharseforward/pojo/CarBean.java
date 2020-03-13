package cn.eight.purcharseforward.pojo;


import cn.eight.purcharseforward.service.Goodservice;
import cn.eight.purcharseforward.service.impl.GoodserviceImp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarBean {
    Map<Integer, Integer> car;

    public Map<Integer, Integer> getCar() {
        return car;
    }

    public void setCar(Map<Integer, Integer> car) {
        this.car = car;
    }

    //增加商品
    public void addGood(Integer id) {
        if (car == null) {
            car = new HashMap<>();
        }
        car.put(id, 1);
    }

    //删除商品
    public void removeGood(Integer id) {
        if (car != null) {
            car.remove(id);
        }
    }

    //修改商品
    public void modGood(Integer[] ids, Integer[] counts) {
        for (int i = 0; i < ids.length; i++) {
            car.put(ids[i], counts[i]);
        }
    }


    //清空商品
    public void clearGood() {
        if (car != null) {
            car.clear();
        }
    }

    //得到商品的总数量
    public Integer gettalCount() {
        int talcount = 0;
        if (car != null) {
            for (Map.Entry<Integer,Integer>entry : car.entrySet()) {
                talcount+=entry.getValue();
            }
        }
        return  talcount;
    }

    //得到商品的总数量
    public Double gettalmoney() {
        Goodservice service=new GoodserviceImp();
        List<Good> cars = service.findCars(this);
        double totalmoney=0;
        //得到总额;
        for (int i = 0; i < car.size(); i++) {
            totalmoney+=cars.get(i).getCount()*cars.get(i).getPrice();
        }


        return  totalmoney;
    }
}
