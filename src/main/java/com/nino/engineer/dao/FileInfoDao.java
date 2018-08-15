package com.nino.engineer.dao;

import com.nino.engineer.domain.File_Info;

import java.util.List;

/**
 * @author ss
 * @date 2018/8/14 16:02
 */
public interface FileInfoDao {
    String matching(String str);
    List<File_Info> matchingList(List<File_Info> F_Info);
    int addFileInfo(List<File_Info> F_Info);
}
