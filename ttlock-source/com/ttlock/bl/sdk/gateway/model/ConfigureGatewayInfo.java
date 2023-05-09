package com.ttlock.bl.sdk.gateway.model;

import com.ttlock.bl.sdk.util.DigitUtil;
import com.ttlock.bl.sdk.util.LogUtil;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Created by TTLock on 2019/3/12.
 */

public class ConfigureGatewayInfo {
    /**
     * 4g 设置一个默认值
     */
    public String ssid = "ssid";
    public String wifiPwd = "wifipwd";
    public String server = "plug.sciener.cn";
    public int port = 2999;
    public int uid;
    public String userPwd;
    public int companyId;
    public int branchId;
    public String plugName;
    /**
     * 默认G2网关
     */
    public int plugVersion = 2;

    private final static int LINK_NAME_LENGTH = 51;

    public String getMd5UserPwd() {
        if (userPwd != null && userPwd.length() != 32)
            userPwd = DigitUtil.getMD5(userPwd);
        return userPwd;
    }

    public byte[] getCorrentNameBytes() {
        byte[] nameBytes = null;
        StringBuilder linkNameBuilder = new StringBuilder(plugName);
        try {

            nameBytes = linkNameBuilder.toString().getBytes("UTF-8");

            int needLenth = LINK_NAME_LENGTH - nameBytes.length;
            //补充G2的特殊字符2
            if (needLenth  > 1) {
                linkNameBuilder.append("\n");
                linkNameBuilder.append(String.valueOf(plugVersion));
                for(int i = 2 ;i < needLenth ; i ++){
                    linkNameBuilder.append("\n");
                }
                nameBytes =  linkNameBuilder.toString().getBytes("UTF-8");
            } else {//长度超了截取
                LogUtil.w("name is to long");
                nameBytes = Arrays.copyOf(nameBytes, LINK_NAME_LENGTH);
                nameBytes[LINK_NAME_LENGTH - 2] = '\n';
                nameBytes[LINK_NAME_LENGTH - 1] = '2';
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return nameBytes;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getWifiPwd() {
        return wifiPwd;
    }

    public void setWifiPwd(String wifiPwd) {
        this.wifiPwd = wifiPwd;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public String getPlugName() {
        return plugName;
    }

    public void setPlugName(String plugName) {
        this.plugName = plugName;
    }

    public int getPlugVersion() {
        return plugVersion;
    }

    public void setPlugVersion(int plugVersion) {
        this.plugVersion = plugVersion;
    }
}
