package com.qldt.lottery.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Component
public class FileUtils {

    public String readFile(String filePath) {
        try {
            ClassPathResource resource = new ClassPathResource(filePath);
            InputStream inputStream = resource.getInputStream();
            byte[] fileData = FileCopyUtils.copyToByteArray(inputStream);
            return new String(fileData, StandardCharsets.UTF_8);
        } catch (IOException e) {
            // Xử lý lỗi khi không thể đọc file
            e.printStackTrace();
            return null;
        }
    }

    public void writeFile(String content, String prize) {
        try {
            File file =  new File("src/main/resources/result/" + prize + ".txt");
            // Tạo file mới nếu file chưa tồn tại
            if (!file.exists()) {
                file.createNewFile();
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            writer.write(content);
            writer.newLine(); // Thêm dòng mới
            writer.close();
        } catch (IOException e) {
            // Xử lý lỗi khi không thể ghi file
            e.printStackTrace();
        }
    }
}
