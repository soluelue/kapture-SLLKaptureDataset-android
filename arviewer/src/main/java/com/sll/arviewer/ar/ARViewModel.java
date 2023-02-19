package com.sll.arviewer.ar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.ar.core.Session;

import java.util.List;

public class ARViewModel extends ViewModel {
    private final MutableLiveData<List<Boolean>> depthSettingData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> instancePlacementSettingData = new MutableLiveData<>();

    private Session session;

    public void setDepthSettingData(List<Boolean> isDepth){
        depthSettingData.setValue(isDepth);
    }

    public LiveData<List<Boolean>> getDepthSettingData() {
        return depthSettingData;
    }

    public void setInstancePlacementSettingData(boolean isInstantPlace){
        instancePlacementSettingData.setValue(isInstantPlace);
    }

    public LiveData<Boolean> getInstancePlacementSettingData() {
        return instancePlacementSettingData;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Session getSession() {
        return session;
    }
}
