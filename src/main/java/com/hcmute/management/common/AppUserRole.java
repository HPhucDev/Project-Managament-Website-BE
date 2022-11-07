package com.hcmute.management.common;

import com.google.common.collect.Sets;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.hcmute.management.common.UserPermission.*;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum AppUserRole {
    ROLE_STUDENT("ROLE_STUDENT"), ROLE_LECTURER("ROLE_LECTURER"), ROLE_ADMIN("ROLE_ADMIN");
    private final String name;
}