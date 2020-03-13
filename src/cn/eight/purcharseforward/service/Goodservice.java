package cn.eight.purcharseforward.service;


import cn.eight.purcharseforward.pojo.CarBean;
import cn.eight.purcharseforward.pojo.Good;

import java.util.List;


public interface Goodservice {
    List<String> findGoodType();
    List<Good> findAllGoods(String goodType);
    List<Good> findCars(CarBean carBean);

}
