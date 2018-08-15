package com.nino.engineer.dao;

import com.nino.engineer.domain.Journal;

import java.util.List;

/**
 * @author ss
 * @date 2018/8/14 16:21
 */
public interface JournalDao {
    int addProjectLogInfo(Journal journal);
    List<Journal> findLogTableInfo(String str);
}
