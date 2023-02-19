package com.sll.arviewer.ar.common.samplerender;

import com.google.ar.core.Frame;

public interface OnUpdateListener {
    /**
     * Called once per frame right before the Scene is updated.
     *
     * @param frame provides time information for the current frame
     */
    void onUpdate(Frame frame);

    void onLog(LogType logType, String message);
}
