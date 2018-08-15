package com.nino.engineer.service;

import com.nino.engineer.dao.FileInfoDao;
import com.nino.engineer.domain.File_Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ss
 * @date 2018/8/14 16:03
 */
@Service
public class FileInfoService {

    @Autowired
    FileInfoDao dao;

    /* 匹配 单个文件 MD5 */
    public boolean matching (String f_md5) {
        String str = dao.matching(f_md5);
        return !(str == null || str.length() < 1);
    }

    /* 匹配 多个文件 MD5 */
    public boolean matching (List<File_Info> F_Info) {
        List<File_Info> fileInfos = dao.matchingList(F_Info);
        return F_Info.size() == fileInfos.size();
    }

    /* 添加文件信息 */
    public boolean addFileInfo (List<File_Info> file_infos) {
        return dao.addFileInfo(file_infos) > -1;
    }
}
