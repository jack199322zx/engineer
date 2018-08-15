package com.nino.engineer.service;

import com.nino.engineer.dao.LocationDao;
import com.nino.engineer.domain.Location;
import com.nino.engineer.domain.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ss
 * @date 2018/8/14 16:32
 */
@Service
public class LocationService {

    @Autowired
    LocationDao dao;

    /* 根据 地址 ID 查找数据 */
    public List<Location> accordingToaddressID (List<Project> projects) {
        return dao.accordingToaddressID(projects);
    }

    /* 添加地址信息 */
    public boolean addAddressInfo (Location location){
        return dao.addAddressInfo(location) > -1;
    }
}
