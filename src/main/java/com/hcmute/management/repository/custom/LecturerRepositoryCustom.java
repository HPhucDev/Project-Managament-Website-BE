package com.hcmute.management.repository.custom;

import com.hcmute.management.common.LecturerSort;
import com.hcmute.management.common.OrderByEnum;
import com.hcmute.management.model.entity.LecturerEntity;
import com.hcmute.management.model.payload.response.PagingResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface LecturerRepositoryCustom {
    PagingResponse search(String searchText, OrderByEnum orderBy, LecturerSort order, int pageindex, int pagesize);
}
