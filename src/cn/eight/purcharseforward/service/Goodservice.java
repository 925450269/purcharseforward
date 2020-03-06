package cn.eight.purcharseforward.service;

import cn.eight.purcharseforward.pojo.Good;

import java.util.List;

/**
 * @author wu
 * @create 2020-03-01
 */
public interface Goodservice {
    boolean addgood(Good good);

    List<Good> findGoodCritria(Good good);

    List<Good> findGoodAll(int pageNow, int pageSize);

    int findTotalRecord();

    boolean modifyGood(Good good);

    boolean removeGood(int id);

}
