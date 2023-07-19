package com.example.adsponsor.utils;

import com.example.adcommon.exception.AdException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.time.DateUtils;

import java.util.Date;

public class CommonUtils {
    private static String[] parsePatterns = {"yyyy-MM-dd", "yyyy/MM/dd", "yyyy.MM.dd"};

    public static String md5(String value) {

        return DigestUtils.md5Hex(value).toUpperCase();
    }

    public static Date parseStringDate(String dateString) throws AdException {

        try {
            return DateUtils.parseDate(dateString, parsePatterns);
        } catch (Exception ex) {
            throw new AdException(ex.getMessage());
        }
    }

    public static String getExtension(String fileName) {
        // 获取文件名的后缀
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);

        // 将后缀转换为小写（可选）
        fileExtension = fileExtension.toLowerCase();
        return fileExtension;
    }

    public static boolean isImage(String fileExtension) {
//        String fileExtension = getExtension(filename);
        return fileExtension.equals("jpg") || fileExtension.equals("jpeg") || fileExtension.equals("png");
    }

    public static boolean isVideo(String fileExtension) {
//        String fileExtension = getExtension(filename);
        return fileExtension.equals("mp4") || fileExtension.equals("mov");
    }


}
