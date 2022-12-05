package com.hcmute.management.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum LecturerSort {
    MSSV("id"),
    POSITION("position"),
    QUALIFICATION("qualification");
    private String name;
}
