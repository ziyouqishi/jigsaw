package tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

/**
 * Created by 张佳亮 on 2016/7/13.
 */

public class PictureDeal {

    public Bitmap readBitmap(Context context, int resId) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        opts.inPurgeable = true;
        opts.inInputShareable = true;
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opts);
    }
}
