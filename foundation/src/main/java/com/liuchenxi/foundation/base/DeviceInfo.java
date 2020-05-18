package com.liuchenxi.foundation.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellLocation;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.DisplayMetrics;

import androidx.core.app.ActivityCompat;

import com.liuchenxi.foundation.BaseApplication;
import com.orhanobut.logger.Logger;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;


import static com.liuchenxi.foundation.BaseApplication.mApplicationContext;

/**
 * 终端设备信息 分辨率 系统版本号等
 *
 * @author cailiming
 */
public class DeviceInfo {

    /**
     * android SDK version
     */
    public final int sdk;

    /**
     * android SDK version string
     */
    public final String release;

    /**
     * 手机品牌
     */
    public final String brand;

    /**
     * 手机型号
     */
    public String model;
    /**
     * imei号
     */
    public String imei;
    /**
     * imsi号
     */
    public String imsi;
    /**
     * 手机号(基本上得不到)
     */
    public String cellphoneNum;
    public final float scale;
    /**
     * 屏幕高度(px)
     */
    public final int heightPixels;
    /**
     * 屏幕宽度(px)
     */
    public final int widthPixels;
    public final int heightDips;
    public final int widthDips;
    public final float xdpi;
    public final float ydpi;
    /**
     * mac地址
     */
    public String mac = "";

    public DeviceInfo(Context mContext) {

        TelephonyManager telephonyManager = (TelephonyManager) mContext
                .getSystemService(Context.TELEPHONY_SERVICE);
        // ConnectivityManager connectivityManager = (ConnectivityManager)
        // context
        // .getSystemService(Context.CONNECTIVITY_SERVICE);
        this.sdk = Build.VERSION.SDK_INT;
        this.release = Build.VERSION.RELEASE;
        this.brand = Build.BRAND;
        this.model = Build.MODEL;

        try {
            WifiManager wifi =
                    (WifiManager) mApplicationContext
                            .getApplicationContext()
                            .getSystemService(Context.WIFI_SERVICE);
            if (wifi.isWifiEnabled()) {
                WifiInfo info = wifi.getConnectionInfo();
                if (info != null) {
                    if (!TextUtils.isEmpty(info.getMacAddress())) {
                        this.mac = info.getMacAddress();
                    }
                }
            }
        } catch (Exception e) {
            Logger.e("WifiManager no root");
            //Crashlytics.logException(e);
        }
        try {
            if (ActivityCompat.checkSelfPermission(mApplicationContext,
                    Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_GRANTED) {
                this.imei = telephonyManager.getDeviceId();
                if (!TextUtils.isEmpty(telephonyManager.getSubscriberId())) {
                    this.imsi = telephonyManager.getSubscriberId();
                } else {
                    this.imsi = "";
                }
                if (!TextUtils.isEmpty(telephonyManager.getLine1Number())) {
                    this.cellphoneNum = telephonyManager.getLine1Number();
                } else {
                    this.cellphoneNum = "";
                }
            }
        } catch (Exception e) {
            Logger.e("telephonyManager no root");
            imsi = "";
            cellphoneNum = "";
            imei = "";
        }
        DisplayMetrics dm = BaseApplication.mApplicationContext.getResources().getDisplayMetrics();
        this.scale = dm.density;
        // this.scale = context.getResources().getDisplayMetrics().density;
        this.widthPixels = dm.widthPixels;
        this.heightPixels = dm.heightPixels;
        this.xdpi = dm.xdpi;
        this.ydpi = dm.ydpi;
        this.widthDips = (int) (this.widthPixels / this.scale + 0.5f);
        this.heightDips = (int) (this.heightPixels / this.scale + 0.5f);
    }

    public static String getIpAddress() {
        String userIpAddress;
        // String venusCalculateIp = ApplicationSetting.getInstance().getClientIp();
        //优先使用venus检测出来的IP
        //if (!TextUtils.isEmpty(venusCalculateIp)) {
        // userIpAddress = venusCalculateIp;
        //} else {
        //其次使用本地检测出来的IP
        userIpAddress = getLocalIpAddress();
        //}

        return userIpAddress;
    }

    public static boolean bIsPortrait(Activity activity) {
        int orientation = activity.getResources().getConfiguration().orientation;
        boolean isPortrait = orientation == Configuration.ORIENTATION_PORTRAIT;
        return isPortrait;
    }

    private static String getLocalIpAddress() {
        String fixedLocalIpAddress = "192.168.0.2";

        //下面的代码是从本地获取IP，在wifi下，该IP是局域网IP，没有参考意义。
        NetworkInfo info = ((ConnectivityManager) mApplicationContext.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                         en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                             enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress()
                                    && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }
            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager =
                        (WifiManager) mApplicationContext
                                .getApplicationContext()
                                .getSystemService(
                                        Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return fixedLocalIpAddress;
    }

    public static String[] getPhoneInfo() {
        // 1. 网络类型   2. 基站id  3.dns  4 信号强度
        String[] netWorkInfo = new String[4];

        netWorkInfo[0] = getNetWorkType();
        netWorkInfo[1] = getCellIdentity(netWorkInfo[0]);
        netWorkInfo[2] = getLocalIpAddress();

        TelephonyManager tm =
                (TelephonyManager) mApplicationContext.getSystemService(
                        Context.TELEPHONY_SERVICE);

        if (ActivityCompat.checkSelfPermission(mApplicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Logger.d("loss ACCESS_COARSE_LOCATION pression");
            return netWorkInfo;
        }

        if (null == tm) {
            return netWorkInfo;
        }
        @SuppressLint("MissingPermission") List<CellInfo> allCellInfo = tm.getAllCellInfo();
        if (allCellInfo == null) {
            return netWorkInfo;
        }
        int dbm = -1;
        for (CellInfo cellInfo : allCellInfo) {
            if (cellInfo.isRegistered()) {
                if (cellInfo instanceof CellInfoGsm) {
                    CellSignalStrengthGsm cellSignalStrengthGsm =
                            ((CellInfoGsm) cellInfo).getCellSignalStrength();
                    dbm = cellSignalStrengthGsm.getLevel();
                } else if (cellInfo instanceof CellInfoCdma) {
                    CellSignalStrengthCdma cellSignalStrengthCdma =
                            ((CellInfoCdma) cellInfo).getCellSignalStrength();
                    dbm = cellSignalStrengthCdma.getLevel();
                } else if (cellInfo instanceof CellInfoWcdma) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        CellSignalStrengthWcdma cellSignalStrengthWcdma =
                                ((CellInfoWcdma) cellInfo).getCellSignalStrength();
                        dbm = cellSignalStrengthWcdma.getLevel();
                    }
                } else if (cellInfo instanceof CellInfoLte) {
                    CellSignalStrengthLte cellSignalStrengthLte =
                            ((CellInfoLte) cellInfo).getCellSignalStrength();
                    dbm = cellSignalStrengthLte.getLevel();
                }
            }
            netWorkInfo[3] = (dbm + 1) + "";
        }
        return netWorkInfo;
    }

    /**
     * 获取网络类型 WIFI，4G
     */
    public static String getNetWorkType() {
        //4G,WIFI
        String currentNetwork;
        currentNetwork = "";
        ConnectivityManager mConnectivityManager =
                (ConnectivityManager)mApplicationContext.getSystemService(
                        Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();

        if (networkInfo != null
                && networkInfo.isAvailable()
                && networkInfo.getState() == NetworkInfo.State.CONNECTED) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                currentNetwork = "4G";
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                currentNetwork = "WIFI";
            }
        }
        return currentNetwork;
    }

    /**
     * 获取dns
     */
    private static String getLocalDNS() {
        String dnsIP = "";
        WifiManager wifi = (WifiManager) mApplicationContext
                .getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);

        DhcpInfo dhcpInfo = wifi.getDhcpInfo();
        dnsIP = Formatter.formatIpAddress(dhcpInfo.dns1);
        return dnsIP;
    }

    /**
     * 获取基站Id  移动网络下
     */
    private static String getCellIdentity(String isMobileConnected) {
        if ("4G".equals(isMobileConnected)) {
            int cellid = 0;
            TelephonyManager tel =
                    (TelephonyManager) mApplicationContext.getSystemService(
                            Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(mApplicationContext,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(mApplicationContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return "";
            }

            @SuppressLint("MissingPermission") CellLocation cel = tel.getCellLocation();
            //移动联通 GsmCellLocation
            if (cel instanceof GsmCellLocation) {
                GsmCellLocation gsmCellLocation = (GsmCellLocation) cel;
                cellid = gsmCellLocation.getCid();
            } else if (cel instanceof CdmaCellLocation) {
                //电信   CdmaCellLocation
                CdmaCellLocation cdmaCellLocation = (CdmaCellLocation) cel;
                cellid = cdmaCellLocation.getBaseStationId();
            }
            return cellid + "";
        }
        return "";
    }

    /**
     * 将得到的int类型的IP转换为String类型
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    public static int getDeviceWidth() {
        Context context = mApplicationContext;
        return Math.min(context.getResources().getDisplayMetrics().widthPixels,
                context.getResources().getDisplayMetrics().heightPixels);
    }

    public static int getDeviceHeight() {
        Context context = mApplicationContext;
        return Math.max(context.getResources().getDisplayMetrics().widthPixels,
                context.getResources().getDisplayMetrics().heightPixels);
    }
}
