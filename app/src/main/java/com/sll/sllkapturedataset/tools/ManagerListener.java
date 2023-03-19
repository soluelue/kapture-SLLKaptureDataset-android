package com.sll.sllkapturedataset.tools;

import com.sll.sllkapturedataset.kapture.Kapture;

public class ManagerListener {
    public interface OnResultListener{
        void onResult(Kapture kapture, Object object);
    }
}
