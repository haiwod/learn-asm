package org.learn.asm.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * 文件操作工具类
 *
 * @author : zhangzy
 * @date : 2022/02/13
 */
public class FileUtil {

    public static String getAbsolutePath(String relativePath) {
        String dir = FileUtil.class.getResource("/").getPath();
        return dir + relativePath;
    }

    public static void writeByteToFile(byte[] bytes, String filePath) {
        File file = new File(filePath);
        File dirFile = file.getParentFile();
        mkdir(dirFile);

        try (
                BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))
        ) {
            outputStream.write(bytes);
            outputStream.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void mkdir(File dirFile) {
        boolean fileExists = dirFile.exists();

        if (fileExists && dirFile.isDirectory()) {
            return;
        }

        if (fileExists && dirFile.isFile()) {
            throw new RuntimeException("Not A Directory: " + dirFile);
        }

        if (!fileExists) {
            dirFile.mkdirs();
        }
    }
}
