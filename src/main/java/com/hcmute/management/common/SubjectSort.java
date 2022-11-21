package com.hcmute.management.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum SubjectSort {
    LECTURER("lecturer"),
    MAJOR("major");
    private String name;
}
