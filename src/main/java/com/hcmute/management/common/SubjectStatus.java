package com.hcmute.management.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum SubjectStatus {
    NULL("null"),
    REJECT("0"),
    NEED_CHECKED("1"),
    PENDING("2"),
    UNASSIGNED("3"),
    ASSIGNED("4");
    private String status;


}
