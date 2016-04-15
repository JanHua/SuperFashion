package com.sf.fresco;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeView;
import com.facebook.drawee.view.GenericDraweeView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.ProgressiveJpegConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;
import com.facebook.imagepipeline.image.ImmutableQualityInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;
import com.sf.R;
import com.sf.base.IApplication;

import java.util.List;

/**
 * 图片属性配置帮助类
 */
public class FrescoConfigHelper {

    private static Context context = IApplication.getIApplication();

    /**
     * 获取图片参数配置者 - 利用classLoader机制实例化可节省资源
     *
     * @return
     */
    public static final GenericDraweeHierarchy initGenericDrawHierarchy() {
        return HierarchyBuilderHolder.INSTANCE;
    }

    /**
     * 单列初始化
     */
    private static class HierarchyBuilderHolder {
        private static final GenericDraweeHierarchy INSTANCE = getGenericDrawHierarchy(context);
    }

    /**
     * 初始化配置
     */
    public static final void initBuild() {
        Fresco.initialize(context, getImagePipelineConfig(getSimpleProgressiveJpegConfig()));
    }

    /**
     * 释放配置
     */
    public static final void shutDownBuild() {
        Fresco.shutDown();
    }

    /**
     * 图片风格构造器
     *
     * @param context
     * @return
     */
    private static final GenericDraweeHierarchy getGenericDrawHierarchy(Context context) {
        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(context.getResources());
        GenericDraweeHierarchy hierarchy = builder
                .setFadeDuration(300)
                .setBackgrounds(null)
                .setOverlays(null)
                .setPressedStateOverlay(context.getResources().getDrawable(R.drawable.umu_logo))
                .build();

        return hierarchy;
    }

    /**
     * 图像默认显示风格
     *
     * @param scaleType       图像显示类型 ScalingUtils.ScaleType.CENTER_INSIDE
     * @param placeholderId   展位图资源
     * @param failureDrawable 失败后默认图
     * @param radius          图片圆角
     * @return
     */
    public static final GenericDraweeHierarchy getGenericDrawHierarchy(ScalingUtils.ScaleType scaleType, int placeholderId, Drawable failureDrawable, int radius) {
        GenericDraweeHierarchy genericDraweeHierarchy = initGenericDrawHierarchy();
        genericDraweeHierarchy.setActualImageScaleType(scaleType);
        genericDraweeHierarchy.setPlaceholderImage(placeholderId);
        genericDraweeHierarchy.setFailureImage(failureDrawable);
        RoundingParams roundingParams = genericDraweeHierarchy.getRoundingParams();
        if (roundingParams == null) {
            roundingParams = new RoundingParams();
        }
        roundingParams.setCornersRadius(radius);
        genericDraweeHierarchy.setRoundingParams(roundingParams);

        return genericDraweeHierarchy;
    }

    /**
     * 默认展示风格
     *
     * @param imageView
     */
    public static final void setImageBaseGenericDrawHierarchy(SimpleDraweeView imageView) {
        if (imageView == null) {
            return;
        }

        GenericDraweeHierarchy genericDraweeHierarchy = getGenericDrawHierarchy(ScalingUtils.ScaleType.CENTER_CROP, R.drawable.umu_logo, context.getResources().getDrawable(R.drawable.umu_logo), 8);
        imageView.setHierarchy(genericDraweeHierarchy);
    }

    /**
     * 展示风格 - 支持图片加载进度
     *
     * @param scaleType
     * @param placeholderId
     * @param failureDrawable
     * @param radius
     * @param drawable
     * @return
     */
    public static final GenericDraweeHierarchy getGenericDrawHierarchy(ScalingUtils.ScaleType scaleType, int placeholderId, Drawable failureDrawable, int radius, Drawable drawable) {
        GenericDraweeHierarchy genericDraweeHierarchy = getGenericDrawHierarchy(scaleType, placeholderId, failureDrawable, radius);
        genericDraweeHierarchy.setProgressBarImage(drawable);
        return genericDraweeHierarchy;
    }

    /**
     * 展示图片 - uri
     * 支持1.网络http 2.sdcard目录file 3.工程目录assets
     *
     * @param imageView
     * @param imageUri
     */
    public static final void displayImageUri(SimpleDraweeView imageView, String imageUri) {
        if (imageView == null || TextUtils.isEmpty(imageUri)) {
            return;
        }

        imageView.setImageURI(Uri.parse(imageUri));
    }

    /**
     * 展示图片 - resourceId
     *
     * @param imageView
     * @param resourceId
     */
    public static final void displayImageId(SimpleDraweeView imageView, int resourceId) {
        if (imageView == null || resourceId == 0) {
            return;
        }

        imageView.setBackgroundResource(resourceId);
    }

    /**
     * 展示一个图片 - 监听下载事件
     *
     * @param simpleDraweeView
     * @param imageUri
     * @param controllerListener
     */
    public static final void displayImageListener(SimpleDraweeView simpleDraweeView, String imageUri, ControllerListener controllerListener) {
        if (simpleDraweeView == null || TextUtils.isEmpty(imageUri)) {
            return;
        }

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setControllerListener(controllerListener)
                .setUri(Uri.parse(imageUri)).build();
        simpleDraweeView.setController(controller);
    }

    /**
     * 图片控制器
     *
     * @param autoPlay 支持gif状态
     * @return
     */
    public static final PipelineDraweeControllerBuilder getDrawController(boolean autoPlay) {
        return Fresco.newDraweeControllerBuilder().setAutoPlayAnimations(autoPlay);
    }

    /**
     * 图片管道配置器
     *
     * @param progressiveJpegConfig 渐进式配置
     * @return
     */
    private static final ImagePipelineConfig getImagePipelineConfig(ProgressiveJpegConfig progressiveJpegConfig) {
        return ImagePipelineConfig.newBuilder(context)
                .setProgressiveJpegConfig(progressiveJpegConfig)
                .build();
    }

    /**
     * 自定义渐进式加载器 - 实现节省CPU
     * 模糊到清晰渐渐呈现，你可以设置一个清晰度标准，</b>
     * 在未达到这个清晰度之前，会一直显示占位图，渐进式JPEG图仅仅支持网络图。</b>
     * <p/>
     * 假设，随着下载的进行，下载完的扫描序列如下: 1, 4, 5, 10。那么：
     * 1.首次调用getNextScanNumberToDecode返回为2， 因为初始时，解码的扫描数为0。
     * 2.那么1将不会解码，下载完成4个扫描时，解码一次。下个解码为扫描数为6
     * 3.5不会解码，10才会解码
     *
     * @return
     */
    private static final ProgressiveJpegConfig getSimpleProgressiveJpegConfig() {
        return new ProgressiveJpegConfig() {
            @Override
            public int getNextScanNumberToDecode(int scanNumber) {
                return scanNumber + 2;
            }

            public QualityInfo getQualityInfo(int scanNumber) {
                boolean isGoodEnough = (scanNumber >= 5);
                return ImmutableQualityInfo.of(scanNumber, isGoodEnough, false);
            }
        };
    }

    /**
     * 默认渐进式加载器
     *
     * @param listDecode
     * @param number
     * @return
     */
    private static final ProgressiveJpegConfig getDefaultProgressiveJpegConfig(final List<Integer> listDecode, final int number) {
        if (listDecode == null || listDecode.isEmpty()) {
            return null;
        }

        return new SimpleProgressiveJpegConfig(new SimpleProgressiveJpegConfig.DynamicValueConfig() {
            @Override
            public List<Integer> getScansToDecode() {
                return listDecode;
            }

            @Override
            public int getGoodEnoughScanNumber() {
                return number;
            }
        });
    }

    /**
     * 支持高低分辨率并行
     *
     * @param simpleDraweeView
     * @param lowResUri
     * @param highResUri
     */
    public static final void disPlayImageRes(SimpleDraweeView simpleDraweeView, String lowResUri, String highResUri) {
        if (simpleDraweeView == null) {
            return;
        }

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setLowResImageRequest(ImageRequest.fromUri(lowResUri))
                .setImageRequest(ImageRequest.fromUri(highResUri))
                .setOldController(simpleDraweeView.getController())
                .build();
        simpleDraweeView.setController(controller);
    }

    /**
     * 缩略图预览
     * <p/>
     * 本功能仅支持本地URI，并且是JPEG图片格式
     *
     * @param simpleDraweeView
     * @param imageUri
     */
    public static final void disPlayImageThumbnail(SimpleDraweeView simpleDraweeView, String imageUri) {
        if (simpleDraweeView == null || TextUtils.isEmpty(imageUri)) {
            return;
        }

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(imageUri))
                .setLocalThumbnailPreviewsEnabled(true)
                .build();

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(simpleDraweeView.getController())
                .build();
        simpleDraweeView.setController(controller);
    }

    /**
     * 缩放图片
     *
     * @param simpleDraweeView
     * @param imageUri
     * @param width
     * @param height
     */
    public static final void setImageResize(SimpleDraweeView simpleDraweeView, String imageUri, int width, int height) {
        if (simpleDraweeView == null || TextUtils.isEmpty(imageUri)) {
            return;
        }

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(imageUri))
                .setResizeOptions(new ResizeOptions(width, height))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(simpleDraweeView.getController())
                .setImageRequest(request)
                .build();
        simpleDraweeView.setController(controller);
    }

    /**
     * 自动旋转图片 - 根据照片的方向呈现设备屏幕方向一致
     *
     * @param simpleDraweeView
     * @param imageUri
     */
    public static final void setImageAutoRotate(SimpleDraweeView simpleDraweeView, String imageUri) {
        if (simpleDraweeView == null || TextUtils.isEmpty(imageUri)) {
            return;
        }

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(imageUri))
                .setAutoRotateEnabled(true)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(simpleDraweeView.getController())
                .setImageRequest(request)
                .build();
        simpleDraweeView.setController(controller);
    }

    /**
     * 图片添加红色网格
     *
     * @param simpleDraweeView
     * @param imageUri
     */
    public static final void setImageRedMeshPostprocessor(SimpleDraweeView simpleDraweeView, String imageUri) {
        if (simpleDraweeView == null || TextUtils.isEmpty(imageUri)) {
            return;
        }

        Postprocessor redMeshPostprocessor = new BasePostprocessor() {
            @Override
            public String getName() {
                return "redMeshPostprocessor";
            }

            @Override
            public void process(Bitmap bitmap) {
                for (int x = 0; x < bitmap.getWidth(); x += 2) {
                    for (int y = 0; y < bitmap.getHeight(); y += 2) {
                        bitmap.setPixel(x, y, Color.RED);
                    }
                }
            }
        };
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(imageUri))
                .setPostprocessor(redMeshPostprocessor)
                .build();
        PipelineDraweeController controller = (PipelineDraweeController)
                Fresco.newDraweeControllerBuilder()
                        .setImageRequest(request)
                        .setOldController(simpleDraweeView.getController())
                                // other setters as you need
                        .build();
        simpleDraweeView.setController(controller);
    }

    /**
     * 图片默认加载动态配置
     *
     * @param imageUri
     * @param width
     * @param height
     * @return
     */
    public static final ImageRequest getImageBaseRequest(String imageUri, int width, int height) {
        ImageDecodeOptions decodeOptions = ImageDecodeOptions.newBuilder()
                .setBackgroundColor(Color.GREEN)
                .build();

        return ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(imageUri))
                .setImageDecodeOptions(decodeOptions)
                .setAutoRotateEnabled(true)
                .setLocalThumbnailPreviewsEnabled(true)
                .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
                .setProgressiveRenderingEnabled(false)
                .setResizeOptions(new ResizeOptions(width, height))
                .build();
    }
}
