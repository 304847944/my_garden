package com.liuchenxi.foundation.breakpoint;

import com.orhanobut.logger.Logger;

import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownLoadTask implements Runnable {
    private static final String TAG = "DownloadTask";
    private DownloadEntity dEntity;
    private String configFPath;

    public DownLoadTask(DownloadEntity downloadInfo) {
        dEntity = downloadInfo;
        configFPath = dEntity.context.getFilesDir().getPath() + "/temp/" + dEntity.tempFile.getName() + ".properties";
    }

    @Override
    public void run() {
        try {
            Logger.d(TAG, "线程_" + dEntity.threadId + "_正在下载【" + "开始位置 : " + dEntity.startLocation + "，结束位置：" + dEntity.endLocation + "】");
            URL url = new URL(dEntity.downloadUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //在头里面请求下载开始位置和结束位置
            conn.setRequestProperty("Range", "bytes=" + dEntity.startLocation + "-" + dEntity.endLocation);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setConnectTimeout(3000);
            conn.setRequestProperty("User-Agent", "Mozilla/4 .0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3. 0.4506.2152; .NET CLR 3.5.30729)");
            conn.setRequestProperty("Accept", "image/gif, im age/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flas h, application/xaml+xml, application/vnd.ms-xpsdocument, applica tion/x-ms-xbap, application/x-ms-application, application/vnd.ms -excel, application/vnd.ms-powerpoint, application/msword, */*");
            conn.setReadTimeout(3000);
            InputStream is = conn.getInputStream();
            //创建可设置位置的文件
            RandomAccessFile file = new RandomAccessFile(dEntity.tempFile, "rwd");
            file.seek(dEntity.startLocation);
            byte[] buffer = new byte[1024];
            int len;
            long currentLocation = dEntity.startLocation;
            while ((len = is.read(buffer)) != -1) {
//                if () {
//                    Logger.d(TAG, "++++++++++ thread_" + dEntity. threadId + "_cancel ++++++++++"); break;
//                    break;
//                }
//                if ()
            }
        } catch (Exception e) {
            Logger.e(e.toString());
        }
    }
}
