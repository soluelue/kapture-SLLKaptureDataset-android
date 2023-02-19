package com.sll.sllkapturedataset.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.Image;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicYuvToRGB;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class ImageUtils {

    public static Bitmap yuv_420_888_toBitmap(Image image, Context context){
        final ByteBuffer yuvBytes = imageToByteBuffer(image);

        // Convert YUV to RGB
        final RenderScript rs = RenderScript.create(context);
        final Bitmap bitmap = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);
        final Allocation allocationRgb = Allocation.createFromBitmap(rs, bitmap);

        final Allocation allocationYuv = Allocation.createSized(rs, Element.U8(rs), yuvBytes.array().length);
        allocationYuv.copyFrom(yuvBytes.array());

        ScriptIntrinsicYuvToRGB scriptYuvToRgb = ScriptIntrinsicYuvToRGB.create(rs, Element.U8_4(rs));
        scriptYuvToRgb.setInput(allocationYuv);
        scriptYuvToRgb.forEach(allocationRgb);

        allocationRgb.copyTo(bitmap);

        // Release
        allocationYuv.destroy();
        allocationRgb.destroy();
        rs.destroy();

        return bitmap; //rotateBitmap(bitmap, 90);
    }

    private static ByteBuffer imageToByteBuffer(final Image image)
    {
        final Rect crop   = image.getCropRect();
        final int  width  = crop.width();
        final int  height = crop.height();

        final Image.Plane[] planes     = image.getPlanes();
        final byte[]        rowData    = new byte[planes[0].getRowStride()];
        final int           bufferSize = width * height * ImageFormat.getBitsPerPixel(ImageFormat.YUV_420_888) / 8;
        final ByteBuffer    output     = ByteBuffer.allocateDirect(bufferSize);

        int channelOffset = 0;
        int outputStride = 0;

        for (int planeIndex = 0; planeIndex < 3; planeIndex++)
        {
            if (planeIndex == 0)
            {
                channelOffset = 0;
                outputStride = 1;
            }
            else if (planeIndex == 1)
            {
                channelOffset = width * height + 1;
                outputStride = 2;
            }
            else if (planeIndex == 2)
            {
                channelOffset = width * height;
                outputStride = 2;
            }

            final ByteBuffer buffer      = planes[planeIndex].getBuffer();
            final int        rowStride   = planes[planeIndex].getRowStride();
            final int        pixelStride = planes[planeIndex].getPixelStride();

            final int shift         = (planeIndex == 0) ? 0 : 1;
            final int widthShifted  = width >> shift;
            final int heightShifted = height >> shift;

            buffer.position(rowStride * (crop.top >> shift) + pixelStride * (crop.left >> shift));

            for (int row = 0; row < heightShifted; row++)
            {
                final int length;

                if (pixelStride == 1 && outputStride == 1)
                {
                    length = widthShifted;
                    buffer.get(output.array(), channelOffset, length);
                    channelOffset += length;
                }
                else
                {
                    length = (widthShifted - 1) * pixelStride + 1;
                    buffer.get(rowData, 0, length);

                    for (int col = 0; col < widthShifted; col++)
                    {
                        output.array()[channelOffset] = rowData[col * pixelStride];
                        channelOffset += outputStride;
                    }
                }

                if (row < heightShifted - 1)
                {
                    buffer.position(buffer.position() + rowStride - length);
                }
            }
        }

        return output;
    }

    public static Bitmap rotateBitmap(Bitmap source, float angle)
    {
        //todo: resize image
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }


    public static void bitmapToSaveJpeg(Context context, Bitmap bitmap, String fileName, File rootDir){
        //todo: save cache folder -> move file to gathering folder -> delete cache image
        File cacheImage = new File(context.getExternalCacheDir(), fileName);
        OutputStream outStream = null;

        try {
            cacheImage.createNewFile();
            outStream = new FileOutputStream(cacheImage);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            File mvImage = new File(rootDir, fileName);

            cacheImage.renameTo(mvImage);
            cacheImage.delete();
        }

    }
}
