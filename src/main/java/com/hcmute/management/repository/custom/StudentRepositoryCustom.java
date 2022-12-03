package com.hcmute.management.repository.custom;

import com.hcmute.management.common.OrderByEnum;
import com.hcmute.management.common.StudentSort;
import com.hcmute.management.model.entity.StudentEntity;
import com.hcmute.management.model.entity.UserEntity;

import java.util.List;

public interface StudentRepositoryCustom {

    List<StudentEntity> search(String searchText, String searchTextType, OrderByEnum orderBy, StudentSort order, int pageindex, int pagesize);
}
