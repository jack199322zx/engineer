package com.nino.engineer.dao;

import com.nino.engineer.domain.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface JpageDao {

    List<User> matchingLog(@Param("Username") String userName,@Param("Password") String password);
    User isExistenceMailAddress(String email);
    User isExistenceTelephone(String phone);
    User isExistenceUsername(String userName);
    User isExistenceUserID(int id);
    int register(User user);
    List<User> lookupAllUsers();
}
