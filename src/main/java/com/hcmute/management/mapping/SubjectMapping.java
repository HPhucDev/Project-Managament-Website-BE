package com.hcmute.management.mapping;

import com.hcmute.management.model.entity.SubjectEntity;
import com.hcmute.management.model.payload.request.Subject.AddNewSubjectRequest;
import com.hcmute.management.model.payload.request.Subject.UpdateSubjectRequest;

public class SubjectMapping {
    public static SubjectEntity addSubjectToEntity(AddNewSubjectRequest addNewSubjectRequest)
    {
        SubjectEntity subject = new SubjectEntity();
        subject.setName(addNewSubjectRequest.getName());
        subject.setTarget(addNewSubjectRequest.getTarget());
        subject.setRequirement(addNewSubjectRequest.getRequirement());
        subject.setProduct(addNewSubjectRequest.getProduct());
        subject.setDescription(addNewSubjectRequest.getDescription());
        subject.setGroupCap(addNewSubjectRequest.getGroupCap());
        subject.setRegFromOtherMajor(addNewSubjectRequest.isRegFromOtherMajor());
        subject.setMajor(addNewSubjectRequest.getMajor());
        subject.setSubjectType(addNewSubjectRequest.getSubjectType());
        subject.setYear(addNewSubjectRequest.getYear());
        subject.setStatus(0);
        return subject;
    }
    public static SubjectEntity updateRequestToEntity(SubjectEntity subject, UpdateSubjectRequest updateSubjectRequest)
    {

        subject.setName(updateSubjectRequest.getName());
        subject.setTarget(updateSubjectRequest.getTarget());
        subject.setRequirement(updateSubjectRequest.getRequirement());
        subject.setProduct(updateSubjectRequest.getProduct());
        subject.setDescription(updateSubjectRequest.getDescription());
        subject.setGroupCap(updateSubjectRequest.getGroupCap());
        subject.setRegFromOtherMajor(updateSubjectRequest.isRegFromOtherMajor());
        subject.setMajor(updateSubjectRequest.getMajorEnum().getName());
        subject.setSubjectType(updateSubjectRequest.getSubjectType());
        subject.setYear(updateSubjectRequest.getYear());
        subject.setStatus(updateSubjectRequest.getStatus());
        return subject;
    }
}
