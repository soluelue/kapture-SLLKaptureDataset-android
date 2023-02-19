package com.sll.arviewer.ar.common.samplerender.arcore;

import android.media.Image;
import android.opengl.Matrix;
import android.util.Pair;

import com.google.ar.core.Anchor;
import com.google.ar.core.CameraIntrinsics;
import com.google.ar.core.Frame;
import com.google.ar.core.exceptions.NotYetAvailableException;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class DepthData {

    public static final int FLOATS_PER_POINT = 4; // X,Y,Z,confidence.

    public static int DEPTH_WIDTH = -1;
    public static int DEPTH_HEIGHT = -1;




    public static Pair<FloatBuffer, ByteBuffer> create(Frame frame, Anchor cameraPoseAnchor) {
        try {
            Image depthImage = frame.acquireRawDepthImage16Bits();
            Image confidenceImage = frame.acquireRawDepthConfidenceImage();

            // Retrieve the intrinsic camera parameters corresponding to the depth image to
            // transform 2D depth pixels into 3D points. See more information about the depth values
            // at
            // https://developers.google.com/ar/develop/java/depth/overview#understand-depth-values.

            final CameraIntrinsics intrinsics = frame.getCamera().getTextureIntrinsics();
            float[] modelMatrix = new float[16];
            cameraPoseAnchor.getPose().toMatrix(modelMatrix, 0);
            final Pair<FloatBuffer, ByteBuffer> pairBuffer = convertRawDepthImagesTo3dPointBuffer(
                    depthImage, confidenceImage, intrinsics, modelMatrix);

            depthImage.close();
            confidenceImage.close();

            return pairBuffer;
        } catch (NotYetAvailableException e) {
            // This normally means that depth data is not available yet.
            // This is normal, so you don't have to spam the logcat with this.
        }
        return null;
    }

    /** Apply camera intrinsics to convert depth image into a 3D pointcloud. */
    private static Pair<FloatBuffer, ByteBuffer> convertRawDepthImagesTo3dPointBuffer(
            Image depth, Image confidence, CameraIntrinsics cameraTextureIntrinsics, float[] modelMatrix) {
        // Java uses big endian so change the endianness to ensure
        // that the depth data is in the correct byte order.
        final Image.Plane depthImagePlane = depth.getPlanes()[0];
        ByteBuffer depthByteBufferOriginal = depthImagePlane.getBuffer();
        ByteBuffer depthByteBuffer = ByteBuffer.allocate(depthByteBufferOriginal.capacity());
        depthByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        while (depthByteBufferOriginal.hasRemaining()) {
            depthByteBuffer.put(depthByteBufferOriginal.get());
        }
        depthByteBuffer.rewind();
        ShortBuffer depthBuffer = depthByteBuffer.asShortBuffer();

        final Image.Plane confidenceImagePlane = confidence.getPlanes()[0];
        ByteBuffer confidenceBufferOriginal = confidenceImagePlane.getBuffer();
        ByteBuffer confidenceBuffer = ByteBuffer.allocate(confidenceBufferOriginal.capacity());
        confidenceBuffer.order(ByteOrder.LITTLE_ENDIAN);
        while (confidenceBufferOriginal.hasRemaining()) {
            confidenceBuffer.put(confidenceBufferOriginal.get());
        }
        confidenceBuffer.rewind();

        // To transform 2D depth pixels into 3D points, retrieve the intrinsic camera parameters
        // corresponding to the depth image. See more information about the depth values at
        // https://developers.google.com/ar/develop/java/depth/overview#understand-depth-values.
        final int[] intrinsicsDimensions = cameraTextureIntrinsics.getImageDimensions();
        final int depthWidth = depth.getWidth();
        final int depthHeight = depth.getHeight();

        ByteBuffer depthMeterArr = ByteBuffer.allocate(depthWidth * depthHeight * 4);
        depthMeterArr.order(ByteOrder.LITTLE_ENDIAN);

        // use sensors.txt
        DEPTH_WIDTH = depthWidth;
        DEPTH_HEIGHT = depthHeight;

        final float fx =
                cameraTextureIntrinsics.getFocalLength()[0] * depthWidth / intrinsicsDimensions[0];
        final float fy =
                cameraTextureIntrinsics.getFocalLength()[1] * depthHeight / intrinsicsDimensions[1];
        final float cx =
                cameraTextureIntrinsics.getPrincipalPoint()[0] * depthWidth / intrinsicsDimensions[0];
        final float cy =
                cameraTextureIntrinsics.getPrincipalPoint()[1] * depthHeight / intrinsicsDimensions[1];

        // Allocate the destination point buffer. If the number of depth pixels is larger than
        // `maxNumberOfPointsToRender` we uniformly subsample. The raw depth image may have
        // different resolutions on different devices.
        final float maxNumberOfPointsToRender = 20000;
        int step = (int) Math.ceil(Math.sqrt(depthWidth * depthHeight / maxNumberOfPointsToRender));

        FloatBuffer points = FloatBuffer.allocate(depthWidth / step * depthHeight / step * FLOATS_PER_POINT);
        float[] pointCamera = new float[4];
        float[] pointWorld = new float[4];

        for (int y = 0; y < depthHeight; y += step) {
            for (int x = 0; x < depthWidth; x += step) {
                // Depth images are tightly packed, so it's OK to not use row and pixel strides.
                int depthMillimeters = depthBuffer.get(y * depthWidth + x); // Depth image pixels are in mm.
                if (depthMillimeters == 0) {
                    // Pixels with value zero are invalid, meaning depth estimates are missing from
                    // this location.
                    continue;
                }

                final float depthMeters = depthMillimeters / 1000.0f; // Depth image pixels are in mm.

                // Retrieve the confidence value for this pixel.
                final byte confidencePixelValue =
                        confidenceBuffer.get(
                                y * confidenceImagePlane.getRowStride()
                                        + x * confidenceImagePlane.getPixelStride());
                final float confidenceNormalized = ((float) (confidencePixelValue & 0xff)) / 255.0f;
//                GLog.d("ARCore", "confidence = " + confidenceNormalized);
                // Unproject the depth into a 3D point in camera coordinates.
                pointCamera[0] = depthMeters * (x - cx) / fx;
                pointCamera[1] = depthMeters * (cy - y) / fy;
                pointCamera[2] = -depthMeters;
                pointCamera[3] = 1;

                depthMeterArr.putFloat(depthMeters);

                // Apply model matrix to transform point into world coordinates.
                Matrix.multiplyMV(pointWorld, 0, modelMatrix, 0, pointCamera, 0);
                points.put(pointWorld[0]); // X.
                points.put(pointWorld[1]); // Y.
                points.put(pointWorld[2]); // Z.
                points.put(confidenceNormalized);
            }
        }

        points.rewind();
        return new Pair<>(points, depthMeterArr);
    }
}
