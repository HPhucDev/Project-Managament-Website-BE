package com.hcmute.management.mapping;

import com.hcmute.management.model.entity.ProgressEntity;
import com.hcmute.management.model.payload.request.Progress.AddNewProgressRequest;
import com.hcmute.management.model.payload.request.Progress.UpdateProgressRequest;

public class ProgressMapping {
    public static ProgressEntity addProgressToEntity(AddNewProgressRequest addNewProgressRequest) {
        ProgressEntity progress = new ProgressEntity();
        progress.setDescription(addNewProgressRequest.getDescription());
        progress.setCreatedate(addNewProgressRequest.getCreatedate());
        progress.setStatus(addNewProgressRequest.getStatus());
        progress.setTimesubmit(addNewProgressRequest.getTimesubmit());
        progress.setWeek(addNewProgressRequest.getWeek());
        return progress;
    }
    public static ProgressEntity updateProgressToEntity(UpdateProgressRequest updateProgressRequest) {
        ProgressEntity progress = new ProgressEntity();
        progress.setDescription(updateProgressRequest.getDescription());
        progress.setCreatedate(updateProgressRequest.getCreatedate());
        progress.setStatus(updateProgressRequest.getStatus());
        progress.setModiferdate(updateProgressRequest.getModiferdate());
        progress.setTimesubmit(updateProgressRequest.getTimesubmit());
        return progress;
    }
}
