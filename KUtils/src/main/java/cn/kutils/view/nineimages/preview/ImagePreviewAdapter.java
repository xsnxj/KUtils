package cn.kutils.view.nineimages.preview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import cn.kutils.R;
import cn.kutils.klog.KLog;
import cn.kutils.utils.FileIOUtils;
import cn.kutils.utils.FileUtils;
import cn.kutils.utils.SDCardUtils;
import cn.kutils.view.nineimages.ImageInfo;
import cn.kutils.view.nineimages.NineGridView;
import uk.co.senab.photoview.PhotoViewAttacher;

import static android.R.attr.bitmap;


/**
 * ================================================
 * 作    者：廖子尧
 * 版    本：1.0
 * 创建日期：2016/3/21
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class ImagePreviewAdapter extends PagerAdapter implements OnPhotoTapListener {

    private List<ImageInfo> imageInfo;
    private Context context;
    private View currentView;

    public ImagePreviewAdapter(Context context, @NonNull List<ImageInfo> imageInfo) {
        super();
        this.imageInfo = imageInfo;
        this.context = context;
    }

    @Override
    public int getCount() {
        return imageInfo.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        currentView = (View) object;
    }

    public View getPrimaryItem() {
        return currentView;
    }

    public ImageView getPrimaryImageView() {
        return (ImageView) currentView.findViewById(R.id.pv);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final View view = LayoutInflater.from(context).inflate(R.layout.item_photoview, container, false);
        final ProgressBar pb = (ProgressBar) view.findViewById(R.id.pb);
        final PhotoView imageView = (PhotoView) view.findViewById(R.id.pv);

        final ImageInfo info = this.imageInfo.get(position);
//        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setOnPhotoTapListener(this);
        showExcessPic(info, imageView);

        //如果需要加载的loading,需要自己改写,不能使用这个方法
        NineGridView.getImageLoader().onDisplayImage(view.getContext(), imageView, info.bigImageUrl);

//        pb.setVisibility(View.VISIBLE);
//        Glide.with(context).load(info.bigImageUrl)//
//                .placeholder(R.drawable.ic_default_image)//
//                .error(R.drawable.ic_default_image)//
//                .diskCacheStrategy(DiskCacheStrategy.ALL)//
//                .listener(new RequestListener<String, GlideDrawable>() {
//                    @Override
//                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                        pb.setVisibility(View.GONE);
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        pb.setVisibility(View.GONE);
//                        return false;
//                    }
//                }).into(imageView);

        container.addView(view);

        view.findViewById(R.id.xz).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KLog.json("旋转第" + position + "个图片");
                imageView.setRotationBy(90);
            }
        });
        view.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Bitmap myBitmap = Glide.with(context)
                                    .load(info.bigImageUrl)
                                    .asBitmap() //必须
                                    .centerCrop()
                                    .into(500, 500)
                                    .get();
                            KLog.json("myBitmap:" + myBitmap);
                            save(myBitmap);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            t("保存失败,请稍后再试");
                        } catch (ExecutionException e) {
                            t("保存失败,请稍后再试");
                            e.printStackTrace();
                        }
                    }
                }).start();
//                NineGridView.getImageLoader().onSave(view.getContext(), imageView, info.bigImageUrl);
//                KLog.json("保存第" + position + "个图片");
//                if (SDCardUtils.isSDCardEnable()) {
//                    String _file = SDCardUtils.getSDCardPath()+"/zstimages/"+String.valueOf(new Date().getTime())+".jpg";
//                    File file = new File(_file);
//                    BufferedOutputStream os = null;
//                    try {
//
//                        // String _filePath_file.replace(File.separatorChar +
//                        // file.getName(), "");
//                        int end = _file.lastIndexOf(File.separator);
//                        String _filePath = _file.substring(0, end);
//                        File filePath = new File(_filePath);
//                        if (!filePath.exists()) {
//                            filePath.mkdirs();
//                        }
//                        file.createNewFile();
//                        os = new BufferedOutputStream(new FileOutputStream(file));
//                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } finally {
//                        if (os != null) {
//                            try {
//                                os.close();
//                            } catch (IOException e) {
//                                Log.e("SavaImageView", e.getMessage(), e);
//                            }
//                        }
//                    }
//                    if (file.exists()){
//                        Toast.makeText(context, "图片保存成功", Toast.LENGTH_SHORT).show();
//                    }else {
//                        Toast.makeText(context, "图片保存失败,请重试", Toast.LENGTH_SHORT).show();
//                    }
//                }else {
////                    context.get
//                }
            }
        });
        return view;
    }

    private void save(Bitmap myBitmap) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        KLog.json("path:" + path);
        File appDir = new File(path + "/掌上通图片存储文件夹/");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            t("图片保存成功");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            t("写入文件失败");
        } catch (IOException e) {
            e.printStackTrace();
            t("写入文件失败");
        }
    }

    private void t(final String msg) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 展示过度图片
     */
    private void showExcessPic(ImageInfo imageInfo, PhotoView imageView) {
        //先获取大图的缓存图片
        Bitmap cacheImage = NineGridView.getImageLoader().getCacheImage(imageInfo.bigImageUrl);
        //如果大图的缓存不存在,在获取小图的缓存
        if (cacheImage == null)
            cacheImage = NineGridView.getImageLoader().getCacheImage(imageInfo.thumbnailUrl);
        //如果没有任何缓存,使用默认图片,否者使用缓存
        if (cacheImage == null) {
            imageView.setImageResource(R.drawable.ic_default_color);
        } else {
            imageView.setImageBitmap(cacheImage);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

//    @Override
//    public void onPhotoTap(ImageView view, float x, float y) {
//        try {
//            ((ImagePreviewActivity) context).finishActivityAnim();
//        } catch (Exception mE) {
//            mE.printStackTrace();
//        }
//    }

    /**
     * 单击屏幕关闭
     */
    @Override
    public void onPhotoTap(ImageView view, float x, float y) {
        try {
            ((ImagePreviewActivity) context).finishActivityAnim();
        } catch (Exception mE) {
            mE.printStackTrace();
        }
    }

//    /**
//     * 单击屏幕关闭
//     */
//    @Override
//    public void onPhotoTap(View view, float x, float y) {
//        try {
//            ((ImagePreviewActivity) context).finishActivityAnim();
//        } catch (Exception mE) {
//            mE.printStackTrace();
//        }
//    }

//    @Override
//    public void onPhotoTap(View view, float x, float y) {
//        try {
//            ((ImagePreviewActivity) context).finishActivityAnim();
//        } catch (Exception mE) {
//            mE.printStackTrace();
//        }
//    }
}