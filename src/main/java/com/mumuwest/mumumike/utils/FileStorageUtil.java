package com.mumuwest.mumumike.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Component
public class FileStorageUtil {


    private static final String uploadDir = "./uploads/images/";

    private static final String maxSize = "10MB" ;

    // 允许的图片类型
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/jpeg", "image/png", "image/gif", "image/bmp"
    );

    /**
     * 上传文件到指定目录
     * @param file 上传的文件
     * @return 保存的文件路径
     * @throws IOException 如果文件操作失败
     */
    public static String storeFile(MultipartFile file) throws IOException {
        // 验证文件
        validateFile(file);

        // 生成唯一文件名
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path targetPath = Paths.get(uploadDir, fileName).toAbsolutePath().normalize();

        // 确保目录存在
        Files.createDirectories(targetPath.getParent());

        // 保存文件
        Files.copy(file.getInputStream(), targetPath);

        // 返回相对路径（用于数据库存储）
        return uploadDir + fileName;
    }

    /**
     * 验证上传的文件
     * @param file 上传的文件
     * @throws IllegalArgumentException 如果文件无效
     */
    private static void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty or null");
        }

        // 验证文件类型
        if (!ALLOWED_IMAGE_TYPES.contains(file.getContentType())) {
            throw new IllegalArgumentException("Invalid file type. Only images (JPEG, PNG, GIF, BMP) are allowed");
        }

        // 验证文件大小
        long maxSizeBytes = parseMaxSize(maxSize);
        if (file.getSize() > maxSizeBytes) {
            throw new IllegalArgumentException("File size exceeds limit of " + maxSize);
        }
    }

    /**
     * 解析最大文件大小（支持 KB, MB）
     * @param sizeStr 配置的大小字符串（如 "10MB"）
     * @return 字节数
     */
    private static long parseMaxSize(String sizeStr) {
        sizeStr = sizeStr.toUpperCase();
        long multiplier = 1;
        if (sizeStr.endsWith("KB")) {
            multiplier = 1024;
            sizeStr = sizeStr.replace("KB", "");
        } else if (sizeStr.endsWith("MB")) {
            multiplier = 1024 * 1024;
            sizeStr = sizeStr.replace("MB", "");
        }
        return Long.parseLong(sizeStr.trim()) * multiplier;
    }
}