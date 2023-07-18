package com.example.adsearch.index.adunit;

import com.example.adsearch.index.adplan.AdPlanObject;
import lombok.*;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdUnitObject {

    private Long unitId;
    private Integer unitStatus;
    private Integer positionType;
    private Long planId;

    private AdPlanObject adPlanObject;

    void update(AdUnitObject object) {
        if (Objects.nonNull(object.getPlanId())) this.unitId = object.getUnitId();
        if (Objects.nonNull(object.getUnitStatus())) this.unitStatus = object.getUnitStatus();
        if (Objects.nonNull(object.getPositionType())) this.positionType = object.getPositionType();
        if (Objects.nonNull(object.getPlanId())) this.planId = object.getPlanId();
        if (Objects.nonNull(object.getAdPlanObject())) this.adPlanObject = object.getAdPlanObject();
    }


    // 判断是否为开屏广告类型
    private static boolean isSplashScreen(int positionType) {
        return (positionType & AdUnitConstants.POSITION_TYPE.SPLASH_SCREEN) > 0;
    }

    // 判断是否为插屏广告类型
    private static boolean isInterstitialAd(int positionType) {
        return (positionType & AdUnitConstants.POSITION_TYPE.INTERSTITIAL_AD) > 0;
    }

    // 判断是否为中贴片广告类型
    private static boolean isMidRollAd(int positionType) {
        return (positionType & AdUnitConstants.POSITION_TYPE.MID_ROLL_AD) > 0;
    }

    // 判断是否为暂停贴片广告类型
    private static boolean isPauseRollAd(int positionType) {
        return (positionType & AdUnitConstants.POSITION_TYPE.PAUSE_ROLL_AD) > 0;
    }

    // 判断是否为后贴片广告类型
    private static boolean isPostRollAd(int positionType) {
        return (positionType & AdUnitConstants.POSITION_TYPE.POST_ROLL_AD) > 0;
    }

    //
    public static boolean isAdSoltTypeOk(int adSlotType, int positionType) {
        return switch (adSlotType) {
            case AdUnitConstants.POSITION_TYPE.SPLASH_SCREEN -> isSplashScreen(positionType);
            case AdUnitConstants.POSITION_TYPE.INTERSTITIAL_AD -> isInterstitialAd(positionType);
            case AdUnitConstants.POSITION_TYPE.MID_ROLL_AD -> isMidRollAd(positionType);
            case AdUnitConstants.POSITION_TYPE.PAUSE_ROLL_AD -> isPauseRollAd(positionType);
            case AdUnitConstants.POSITION_TYPE.POST_ROLL_AD -> isPostRollAd(positionType);
            default -> false;
        };
    }

}
