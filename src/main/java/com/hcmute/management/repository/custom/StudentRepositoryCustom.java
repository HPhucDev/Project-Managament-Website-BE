package com.hcmute.management.repository.custom;

import com.hcmute.management.common.OrderByEnum;
import com.hcmute.management.common.StudentSort;
import com.hcmute.management.model.entity.StudentEntity;
import com.hcmute.management.model.entity.UserEntity;
import com.hcmute.management.model.payload.response.PagingResponse;

import java.util.List;
import java.util.Map;

public interface StudentRepositoryCustom {

    PagingResponse search(String searchText, OrderByEnum orderBy, StudentSort order, int pageindex, int pagesize);
}
