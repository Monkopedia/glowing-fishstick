package com.ttlock.bl.sdk.util;

import android.text.TextUtils;

import com.ttlock.bl.sdk.api.EncryptionUtil;
import com.ttlock.bl.sdk.entity.LockData;

import java.math.BigInteger;

/**
 * Created on  2019/4/30 0030 16:25
 *
 * @author theodre
 */
public class FeatureValueUtil {

    public static boolean isSupportFeature(String lockData, int newFeature) {
        LockData lockDataObj = EncryptionUtil.parseLockData(lockData);
        return isSupportFeature(lockDataObj, newFeature);
    }

    public static boolean isSupportFeature(LockData lockDataObj, int newFeature) {
        if (lockDataObj == null || lockDataObj.featureValue == null) {
            return false;
        }
        return isSupportFeatureValue(lockDataObj.featureValue, newFeature);
    }

    private static boolean isSupportFeatureValue(String featureValue,int newFeature){
        if (TextUtils.isEmpty(featureValue)) {
            return false;
        }
        try {
            BigInteger bigInteger = new BigInteger(featureValue, 16);
            return bigInteger.testBit(newFeature);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean isSupportFeature(String lockFeatureValue, String keypadFeatureValue, int newFeature) {
        return isSupportFeature(lockFeatureValue, newFeature) || isSupportFeature(keypadFeatureValue, newFeature);
    }

    public static String FeatureValueOr(String featureValue1, String featureValue2) {
        BigInteger bigInteger1 = new BigInteger(featureValue1, 16);
        BigInteger bigInteger2 = new BigInteger(featureValue2, 16);
        BigInteger bigInteger = bigInteger1.or(bigInteger2);
        return bigInteger.toString(16);
    }

}
