package com.hcmute.management.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum StudentSort {
    EDUCATION_PROGRAM("education_program"),
    MAJOR("major"),
    CLASS("classes"),
    SCHOOL_YEAR("school_year");
    private String name;
}
