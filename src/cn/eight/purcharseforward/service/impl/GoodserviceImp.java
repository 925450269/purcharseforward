package cn.eight.purcharseforward.service.impl;

import cn.eight.purcharseforward.dao.GoodDao;
import cn.eight.purcharseforward.pojo.CarBean;
import cn.eight.purcharseforward.pojo.Good;
import cn.eight.purcharseforward.pojo.User;
import cn.eight.purcharseforward.service.Goodservice;

import java.util.List;

/**
 * @author wu
 * @create 2020-03-01
 */
public class GoodserviceImp implements Goodservice {
    GoodDao goodDao = new GoodDao();


    @Override
    public List<String> findGoodType() {
        return goodDao.QueryGoodType();
    }

    @Override
    public List<Good> findAllGoods(String goodType) {
        return goodDao.QueryGoodsByType(goodType);
    }

    @Override
    public List<Good> findCars(CarBean carBean) {
        return goodDao.QueryCarBean(carBean);
    }


}
