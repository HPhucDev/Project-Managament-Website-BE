package com.hcmute.management.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum QualificationLecturerEnum {
    THACSI("Thạc sĩ"),
    TIENSI("Tiến sĩ");
    private String name;
}
