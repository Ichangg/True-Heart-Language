package com.englishcenter.backend.modules.admin.service;
import com.englishcenter.backend.modules.admin.dto.ClassCreationDTO;

import com.englishcenter.backend.core.entity.Classes;
import java.util.List;

public interface ClassesService {
    List<Classes> getAllClasses();
    List<Classes> getClassesByStatus(String status);
    Classes createClass(Classes classes);
    Classes createClassWithAllocations(ClassCreationDTO dto);
}