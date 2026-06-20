package com.englishcenter.backend.modules.admin.dto;

import com.englishcenter.backend.core.entity.User;
import java.util.List;

public class ParentProfileDTO {
    private User parentInfo;
    private List<User> children; // List of Students

    public ParentProfileDTO(User parentInfo, List<User> children) {
        this.parentInfo = parentInfo;
        this.children = children;
    }

    public User getParentInfo() {
        return parentInfo;
    }

    public void setParentInfo(User parentInfo) {
        this.parentInfo = parentInfo;
    }

    public List<User> getChildren() {
        return children;
    }

    public void setChildren(List<User> children) {
        this.children = children;
    }
}
