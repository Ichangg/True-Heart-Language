package com.englishcenter.backend.modules.admin.dto;

import com.englishcenter.backend.core.entity.User;
import com.englishcenter.backend.core.entity.Classes;
import com.englishcenter.backend.core.entity.TeacherSalaries;

import java.util.List;

public class TeacherProfileDTO {
    private User teacherInfo;
    private List<Classes> classesTaught;
    private List<TeacherSalaries> salaryHistory;

    public TeacherProfileDTO(User teacherInfo, List<Classes> classesTaught, List<TeacherSalaries> salaryHistory) {
        this.teacherInfo = teacherInfo;
        this.classesTaught = classesTaught;
        this.salaryHistory = salaryHistory;
    }

    public User getTeacherInfo() {
        return teacherInfo;
    }

    public void setTeacherInfo(User teacherInfo) {
        this.teacherInfo = teacherInfo;
    }

    public List<Classes> getClassesTaught() {
        return classesTaught;
    }

    public void setClassesTaught(List<Classes> classesTaught) {
        this.classesTaught = classesTaught;
    }

    public List<TeacherSalaries> getSalaryHistory() {
        return salaryHistory;
    }

    public void setSalaryHistory(List<TeacherSalaries> salaryHistory) {
        this.salaryHistory = salaryHistory;
    }
}
