package com.nino.engineer.service;

import com.nino.engineer.dao.JournalDao;
import com.nino.engineer.domain.Journal;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Reader;
import java.util.List;

/**
 * @author ss
 * @date 2018/8/14 16:24
 */
@Service
public class JournalService {

    @Autowired
    JournalDao dao;
    /* 添加项目日志信息 */
    public boolean addProjectLogInfo (Journal journal) {
        return dao.addProjectLogInfo(journal) > -1;
    }

    /* 查找日志表信息 */
    public List<Journal> findLogTableInfo (String time) {
        List<Journal> journals = dao.findLogTableInfo(time);
        return journals;
    }
}
