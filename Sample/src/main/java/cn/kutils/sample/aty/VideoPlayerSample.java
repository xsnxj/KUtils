package cn.kutils.sample.aty;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.kutils.media.jiecaovideoplayer.JCVideoPlayer;
import cn.kutils.media.jiecaovideoplayer.JCVideoPlayerStandard;
import cn.kutils.sample.R;

/**
 * 创建时间：2017/6/15  上午10:40
 * 创建人：赵文贇
 * 类描述：
 * 包名：cn.kutils.sample.aty
 * 待我代码编好，娶你为妻可好。
 */
public class VideoPlayerSample extends AppCompatActivity {
    @Bind(R.id.jc_01)
    JCVideoPlayerStandard mJc01;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atyvideo);
        ButterKnife.bind(this);
        mJc01.setUp("http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4"
                , JCVideoPlayerStandard.SCREEN_WINDOW_FULLSCREEN, "什么鬼?");
//        JCVideoPlayerStandard.startFullscreen(this, JCVideoPlayerStandard.class, "http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4", "什么鬼?");
        Glide.with(this).load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1497504899719&di=f6955c3c60a962acc65ab14508e26bb2&imgtype=0&src=http%3A%2F%2Fimg.aivu.cn%2F2010%2F11%2F03%2Fd513e8775fff761928596b82a8fefd4b.jpg").into(mJc01.thumbImageView);
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }
}
