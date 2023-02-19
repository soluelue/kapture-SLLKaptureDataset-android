package com.sll.arviewer.ar.common.samplerender.arcore;

import com.google.ar.core.Anchor;
import com.google.ar.core.InstantPlacementPoint;
import com.google.ar.core.Trackable;

/**
 * Associates an Anchor with the trackable it was attached to. This is used to be able to check
 * whether or not an Anchor originally was attached to an {@link InstantPlacementPoint}.
 */
public class WrappedAnchor {

    private Anchor anchor;
    private Trackable trackable;

    public WrappedAnchor(Anchor anchor, Trackable trackable) {
        this.anchor = anchor;
        this.trackable = trackable;
    }

    public Anchor getAnchor() {
        return anchor;
    }

    public Trackable getTrackable() {
        return trackable;
    }
}
