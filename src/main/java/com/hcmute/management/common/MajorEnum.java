package com.hcmute.management.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum MajorEnum {
    CNPM("Công nghệ phần mềm"),
    HTTT("Hệ thống thông tin"),
    ANM("An ninh mạng"),
    NULL(null);
    private String name;
}
