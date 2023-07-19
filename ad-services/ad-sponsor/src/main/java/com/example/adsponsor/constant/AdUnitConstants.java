package com.example.adsponsor.constant;

public class AdUnitConstants {
    public static class POSITION_TYPE {
        // 开屏广告
        public static final int SPLASH_SCREEN = 1;
        // 贴片广告, 一种在媒体内容中间插入的广告形式。
        public static final int INTERSTITIAL_AD = 2;
        // 前贴片广告（pre-roll ad）是在媒体内容播放之前显示的广告。
        public static final int PRE_ROLL_AD = 3;

        // 中贴片广告（mid-roll ad）是在媒体内容播放过程中的某个中间位置显示的广告。
        public static final int MID_ROLL_AD = 4;
        // 暂停贴片广告（pause-roll ad）是在媒体内容播放过程中的某个中间位置显示的广告。
        public static final int PAUSE_ROLL_AD = 8;
        // 后贴片广告（post-roll ad）是在媒体内容播放结束后显示的广告。
        public static final int POST_ROLL_AD = 16;
    }
}
