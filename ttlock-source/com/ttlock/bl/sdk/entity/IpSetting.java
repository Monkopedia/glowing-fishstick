package com.ttlock.bl.sdk.entity;

import android.text.TextUtils;

import com.ttlock.bl.sdk.util.LogUtil;

import java.util.Arrays;

public class IpSetting {

    public static final int STATIC_IP = 0;
    public static final int DHCP = 1;

    /**
     * 0 -固定ip
     * 1 -DHCP 自动获取ip地址
     */
    private int type;

    /**
     * 静态ip
     */
    private String ipAddress;
    /**
     * 子网掩码
     */
    private String subnetMask;
    /**
     * 默认网关
     */
    private String router;

    /**
     * 首选dns
     */
    private String preferredDns;

    /**
     * 备用dns
     */
    private String alternateDns;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getSubnetMask() {
        return subnetMask;
    }

    public void setSubnetMask(String subnetMask) {
        this.subnetMask = subnetMask;
    }

    public String getRouter() {
        return router;
    }

    public void setRouter(String router) {
        this.router = router;
    }

    public String getPreferredDns() {
        return preferredDns;
    }

    public void setPreferredDns(String preferredDns) {
        this.preferredDns = preferredDns;
    }

    public String getAlternateDns() {
        return alternateDns;
    }

    public void setAlternateDns(String alternateDns) {
        this.alternateDns = alternateDns;
    }

    public boolean isValidData() {
        if (type == STATIC_IP) {
            if (!checkIpFormat(ipAddress)) {
                return false;
            }
            if (!checkIpFormat(subnetMask)) {
                return false;
            }
            if (!checkIpFormat(router)) {
                return false;
            }
            if (!TextUtils.isEmpty(preferredDns) && !checkIpFormat(preferredDns)) {//dns可以为空 不设置
                return false;
            }
            if (!TextUtils.isEmpty(alternateDns) && !checkIpFormat(alternateDns)) {//dns可以为空 不设置
                return false;
            }
        }
        return true;
    }

    public boolean checkIpFormat(String ip) {
        if (TextUtils.isEmpty(ip)) {
            return false;
        }
        try {
            LogUtil.d("ip:" + ip);
            String[] dividerList = ip.split("\\.");
            LogUtil.d("dividerList:" + Arrays.toString(dividerList));
            if (dividerList.length != 4) {
                return false;
            }
            for (String str : dividerList) {
                int temp = Integer.valueOf(str);
                if (temp < 0 || temp > 255) {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
