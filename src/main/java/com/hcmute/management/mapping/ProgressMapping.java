package com.hcmute.management.mapping;

import com.hcmute.management.model.entity.ProgressEntity;
import com.hcmute.management.model.payload.request.Progress.AddNewProgressRequest;
import com.hcmute.management.model.payload.request.Progress.UpdateProgressRequest;

public class ProgressMapping {
    public static ProgressEntity addProgressToEntity(AddNewProgressRequest addNewProgressRequest) {
        ProgressEntity progress = new ProgressEntity();
        progress.setDescription(addNewProgressRequest.getDescription());
        progress.setStatus(addNewProgressRequest.getStatus());
        progress.setWeek(addNewProgressRequest.getWeek());
        return progress;
    }
    public static ProgressEntity updateProgressToEntity(UpdateProgressRequest updateProgressRequest) {
        ProgressEntity progress = new ProgressEntity();
        progress.setDescription(updateProgressRequest.getDescription());
        progress.setCreateDate(updateProgressRequest.getCreateDate());
        progress.setStatus(updateProgressRequest.getStatus());
        progress.setModiferDate(updateProgressRequest.getModiferDate());
        progress.setTimeSubmit(updateProgressRequest.getTimeSubmit());
        return progress;
    }
}
