package com.englishcenter.backend.service;

import com.englishcenter.backend.entity.ClassGroups;
import java.util.List;
import java.util.Optional;

public interface ClassGroupsService {
    ClassGroups createGroup(ClassGroups group);

    Optional<ClassGroups> getGroupById(Long id);

    ClassGroups patchGroup(Long id, ClassGroups groupDetails);

    List<ClassGroups> getAllGroups();
}
