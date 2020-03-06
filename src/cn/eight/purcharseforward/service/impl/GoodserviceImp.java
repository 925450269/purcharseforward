package cn.eight.purcharseforward.service.impl;

import cn.eight.purcharseforward.dao.GoodDao;
import cn.eight.purcharseforward.pojo.Good;
import cn.eight.purcharseforward.service.Goodservice;

import java.util.List;

/**
 * @author wu
 * @create 2020-03-01
 */
public class GoodserviceImp implements Goodservice {
    GoodDao goodDao = new GoodDao();

    @Override
    public boolean addgood(Good good) {
        return goodDao.insertGood(good);
    }

    @Override
    public List<Good> findGoodCritria(Good good) {
        return goodDao.QueryGoodCritria(good);
    }


    @Override
    public List<Good> findGoodAll(int pageNow, int pageSize) {
        return goodDao.QueryGoodAll(pageNow, pageSize);
    }

    @Override
    public int findTotalRecord() {
        return goodDao.QueryTotalRecord();
    }

    @Override
    public boolean modifyGood(Good good) {
        return goodDao.UpdateGood(good);
    }

    @Override
    public boolean removeGood(int id) {
        return goodDao.deleteGood(id);
    }


}
