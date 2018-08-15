package com.nino.engineer.dao;


import com.nino.engineer.domain.Location;
import com.nino.engineer.domain.Project;

import java.util.List;

/**
 * @author ss
 * @date 2018/8/14 16:36
 */
public interface LocationDao {
    List<Location> accordingToaddressID(List<Project> projects);
    int addAddressInfo(Location location);
}
