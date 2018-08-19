package com.nino.engineer.dao;

import com.nino.engineer.domain.Permissions_detailed;
import com.nino.engineer.domain.Project;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ss
 * @date 2018/8/14 16:41
 */
public interface PermissionsDetailedDao {
    List<Permissions_detailed> usersProject(List<Project> projects);
    List<Permissions_detailed> displayAllProjectAccordingToUserID(int str);
    int userProNumber(int str);
    int addProjectPermissionInformation(Permissions_detailed permissions_detailed);
    int isHavePower(@Param("userId") int u_id,@Param("projectId") int p_id);
    int judgePermission(int u_id);
    int findProjectIdByCreator(int u_id);
}
