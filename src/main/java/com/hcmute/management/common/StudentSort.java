package com.hcmute.management.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum StudentSort {
    EDUCATION_PROGRAM("educationProgram"),
    MAJOR("major"),
    CLASS("classes"),
    SCHOOL_YEAR("schoolYear");
    private String name;
}
