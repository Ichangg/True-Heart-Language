package com.englishcenter.backend.service;

import com.englishcenter.backend.entity.Classes;
import java.util.List;

public interface ClassesService {
    List<Classes> getAllClasses();
    List<Classes> getClassesByStatus(String status);
    Classes createClass(Classes classes);
    Classes createClassWithAllocations(com.englishcenter.backend.dto.ClassCreationDTO dto);
}