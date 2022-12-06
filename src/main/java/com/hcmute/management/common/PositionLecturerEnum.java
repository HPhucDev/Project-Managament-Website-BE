package com.hcmute.management.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum PositionLecturerEnum {
    GIANGVIEN("Giảng viên"),
    TRUONGKHOA("Trưởng khoa");
    private String name;
}
