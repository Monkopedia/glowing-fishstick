package com.ttlock.bl.sdk.util;

/**
 * Created on  2019/4/30 0030 16:25
 *
 * @author theodre
 */
@Deprecated
public class SpecialValueUtil {
    public static boolean isSupportFeature(int specialValue,int feature){
        return (specialValue & feature) != 0;
    }
}
