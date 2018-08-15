package com.nino.engineer.service;

import com.nino.engineer.dao.JurisDictionDao;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Reader;

/**
 * @author ss
 * @date 2018/8/14 16:28
 */
@Service
public class JurisDictionService {

    @Autowired
    JurisDictionDao dictionDao;
    /**
     * 匹配用户权限
     * @param u_id 用户id
     * @param j_id 权限代号
     * @return
     */
    public boolean matchingUserRights(int u_id,int j_id){
        int row = dictionDao.matchingUserRights(u_id);
        return j_id >= row && row > -1;
    }
}
