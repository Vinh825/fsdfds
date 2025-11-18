package com.example.IAM.dto.request;

import com.example.IAM.entity.Privilege;

public record PrivilegeEntry(Privilege privilege, Boolean permitted) {}