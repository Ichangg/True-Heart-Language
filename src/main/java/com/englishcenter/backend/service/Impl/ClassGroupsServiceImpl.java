package com.englishcenter.backend.service.Impl;

import com.englishcenter.backend.entity.ClassGroups;
import com.englishcenter.backend.repository.ClassGroupRepository;
import com.englishcenter.backend.service.ClassGroupsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClassGroupsServiceImpl implements ClassGroupsService {

    @Autowired
    private ClassGroupRepository classGroupsRepository;

    @Override
    public ClassGroups createGroup(ClassGroups group) {
        return classGroupsRepository.save(group);
    }

    @Override
    public Optional<ClassGroups> getGroupById(Long id) {
        return classGroupsRepository.findById(id);
    }

    @Override
    public ClassGroups patchGroup(Long id, ClassGroups groupDetails) {
        return classGroupsRepository.findById(id).map(group -> {
            if (groupDetails.getName() != null) group.setName(groupDetails.getName());
            if (groupDetails.getAgeRange() != null) group.setAgeRange(groupDetails.getAgeRange());
            if (groupDetails.getDescription() != null) group.setDescription(groupDetails.getDescription());
            return classGroupsRepository.save(group);
        }).orElse(null);
    }

    @Override
    public List<ClassGroups> getAllGroups() { return classGroupsRepository.findAll(); }
}