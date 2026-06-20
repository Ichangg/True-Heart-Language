package com.englishcenter.backend.modules.admin.dto;

import com.englishcenter.backend.core.entity.User;
import com.englishcenter.backend.core.entity.Classes;
import java.util.List;

public class StudentProfileDTO {
    private User studentInfo;
    private List<Classes> enrolledClasses;

    public StudentProfileDTO(User studentInfo, List<Classes> enrolledClasses) {
        this.studentInfo = studentInfo;
        this.enrolledClasses = enrolledClasses;
    }

    public User getStudentInfo() {
        return studentInfo;
    }

    public void setStudentInfo(User studentInfo) {
        this.studentInfo = studentInfo;
    }

    public List<Classes> getEnrolledClasses() {
        return enrolledClasses;
    }

    public void setEnrolledClasses(List<Classes> enrolledClasses) {
        this.enrolledClasses = enrolledClasses;
    }
}
