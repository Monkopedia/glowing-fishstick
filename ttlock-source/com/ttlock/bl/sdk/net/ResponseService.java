package com.ttlock.bl.sdk.net;

import java.util.HashMap;

/**
 * Created by TTLock on 2016/9/8 0008.
 */
public class ResponseService {
    private static final String TAG = "ResponseService";
    private static String actionUrl = "https://api.ttlock.com.cn";
//    private static String actionUrl = "http://120.26.119.23:8085";
    private static String actionUrlV3 = actionUrl + "/v3";


    /**
     * 解绑管理员
     * @param lockId
     * @return
     */
    public static String getRecoverData(String clientId, String accessToken, int lockId) {
        String url = actionUrlV3 + "/lock/getRecoverData";
        HashMap params = new HashMap();
        params.put("clientId", clientId);
        params.put("accessToken", accessToken);
        params.put("lockId", String.valueOf(lockId));
        params.put("date", String.valueOf(System.currentTimeMillis()));
        return OkHttpRequest.sendPost(url, params);
    }

    /**
     * 解绑管理员
     * @param lockId
     * @return
     */
    public static String getUpgradePackage(String clientId, String accessToken, int lockId) {
        String url = actionUrlV3 + "/lock/getUpgradePackage";
        HashMap params = new HashMap();
        params.put("clientId", clientId);
        params.put("accessToken", accessToken);
        params.put("lockId", String.valueOf(lockId));
        params.put("date", String.valueOf(System.currentTimeMillis()));
        return OkHttpRequest.sendPost(url, params);
    }

    public static String lockUpgradeSuccess(String clientId, String accessToken, int lockId, String lockData) {
        String url = actionUrlV3 + "/lock/upgradeSuccess";
        HashMap params = new HashMap();
        params.put("clientId", clientId);
        params.put("accessToken", accessToken);
        params.put("lockId", String.valueOf(lockId));
        params.put("lockData", lockData);
//        params.put("specialValue", String.valueOf(specialValue));
        params.put("date", String.valueOf(System.currentTimeMillis()));
        return OkHttpRequest.sendPost(url, params);
    }

    /**
     * 上传操作日志
     * @param clientId
     * @param accessToken
     * @param lockId
     * @param records
     * @return
     */
    public static String uploadOperateLog(String clientId, String accessToken, int lockId, String records) {
        String url = actionUrlV3 + "/lockRecord/upload";
        HashMap params = new HashMap();
        params.put("clientId", clientId);
        params.put("accessToken", accessToken);
        params.put("lockId", String.valueOf(lockId));
        params.put("records", records);
        params.put("date", String.valueOf(System.currentTimeMillis()));
        return OkHttpRequest.sendPost(url, params);
    }

    public static String enterDfuMode(String clientId, String accessToken, int gatewayId) {
        String url = actionUrlV3 + "/gateway/setUpgradeMode";
        HashMap params = new HashMap();
        params.put("clientId", clientId);
        params.put("accessToken", accessToken);
        params.put("gatewayId", String.valueOf(gatewayId));
        params.put("date", String.valueOf(System.currentTimeMillis()));
        return OkHttpRequest.sendPost(url, params);
    }

    public static String getPlugUpgradePackage(String clientId, String accessToken, int gatewayId) {
        String url = actionUrlV3 + "/gateway/getUpgradePackage";
        HashMap params = new HashMap();
        params.put("clientId", clientId);
        params.put("accessToken", accessToken);
        params.put("gatewayId", String.valueOf(gatewayId));
        params.put("date", String.valueOf(System.currentTimeMillis()));
        return OkHttpRequest.sendPost(url, params);
    }

    public static String plugUpgradeSuccess(String clientId, String accessToken, int gatewayId) {
        String url = actionUrlV3 + "/gateway/upgradeSuccess";
        HashMap params = new HashMap();
        params.put("clientId", clientId);
        params.put("accessToken", accessToken);
        params.put("gatewayId", String.valueOf(gatewayId));
        params.put("date", String.valueOf(System.currentTimeMillis()));
        return OkHttpRequest.sendPost(url, params);
    }

}
