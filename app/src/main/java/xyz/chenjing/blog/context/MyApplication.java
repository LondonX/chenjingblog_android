package xyz.chenjing.blog.context;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.londonx.lutil.Lutil;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * Created by london on 16/2/19.
 * override for {@link Application}
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Lutil.init(this);
        //init ImageView
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(this);
        DisplayImageOptions.Builder imageOptions = new DisplayImageOptions.Builder();
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inSampleSize = 2;

        imageOptions.bitmapConfig(Bitmap.Config.RGB_565)
                .decodingOptions(options)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .resetViewBeforeLoading(true)
//                .showImageOnLoading(ContextCompat.getDrawable(this, R.mipmap.bg_admin))
//                .showImageOnFail(R.mipmap.bg_admin)
                .imageScaleType(ImageScaleType.EXACTLY)
                .showImageOnLoading(0);

        builder.diskCacheFileCount(1000)
                .threadPoolSize(10)
                .memoryCache(new WeakMemoryCache())
                .defaultDisplayImageOptions(imageOptions.build());
        ImageLoader.getInstance().init(builder.build());
    }
}
