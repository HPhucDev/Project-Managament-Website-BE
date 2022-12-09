package com.hcmute.management.model.payload.response;

import com.hcmute.management.model.entity.LecturerEntity;
import com.hcmute.management.model.entity.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SearchLectureResponse {
    private LecturerEntity lecturer;
    private UserEntity user;
}
