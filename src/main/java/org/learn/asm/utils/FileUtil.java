package org.learn.asm.utils;

import java.io.*;

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

    public static byte[] readByteFromFile(String path) {
        File file = new File(path);
        byte[] bytes = new byte[1024];
        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
        ) {
            int length;
            while ((length = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, length);
            }
            return outputStream.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new byte[0];
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
