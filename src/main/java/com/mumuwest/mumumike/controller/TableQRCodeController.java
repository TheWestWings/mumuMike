package com.mumuwest.mumumike.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.mumuwest.mumumike.pojo.AjaxResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * 餐桌二维码生成控制器
 */
@RestController
@RequestMapping("/table")
public class TableQRCodeController {

    // 小程序AppID，用于生成小程序码
    @Value("${wx.appid:}")
    private String appId;

    /**
     * 生成餐桌二维码图片
     * @param tableNo 桌号
     * @return 二维码图片（PNG格式）
     */
    @GetMapping(value = "/qrcode/{tableNo}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] generateTableQRCode(@PathVariable Integer tableNo) throws IOException, WriterException {
        // 二维码内容：小程序页面路径 + 桌号参数
        String content = String.format("pages/index/index?tableNo=%d", tableNo);
        
        // 如果有小程序AppID，使用小程序码格式
        if (appId != null && !appId.isEmpty()) {
            content = String.format("https://servicewechat.com/%s/0/page/pages/index/index?tableNo=%d", appId, tableNo);
        }

        // 生成二维码
        int width = 300;
        int height = 300;
        
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.MARGIN, 2);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);

        // 创建带桌号标识的二维码图片
        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        BufferedImage finalImage = addTableNoLabel(qrImage, tableNo);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(finalImage, "PNG", outputStream);
        
        return outputStream.toByteArray();
    }

    /**
     * 批量生成餐桌二维码信息
     * @param startNo 起始桌号
     * @param endNo 结束桌号
     * @return 二维码下载链接列表
     */
    @GetMapping("/qrcode/batch")
    public AjaxResult batchGenerateQRCode(
            @RequestParam(defaultValue = "1") Integer startNo,
            @RequestParam(defaultValue = "10") Integer endNo) {
        
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (int i = startNo; i <= endNo; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("tableNo", i);
            item.put("qrCodeUrl", "/table/qrcode/" + i);
            item.put("content", String.format("tableNo=%d", i));
            result.add(item);
        }

        return AjaxResult.success(result);
    }

    /**
     * 获取餐桌列表
     * @return 餐桌列表
     */
    @GetMapping("/list")
    public AjaxResult getTableList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        
        // 这里简单返回模拟数据，实际应该从数据库读取
        List<Map<String, Object>> tables = new ArrayList<>();
        
        int start = (pageNum - 1) * pageSize + 1;
        int end = Math.min(start + pageSize - 1, 50); // 假设最多50桌
        
        for (int i = start; i <= end; i++) {
            Map<String, Object> table = new HashMap<>();
            table.put("id", i);
            table.put("tableNo", i);
            table.put("status", 1); // 1-空闲, 2-使用中
            table.put("qrCodeUrl", "/table/qrcode/" + i);
            tables.add(table);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("total", 50);
        result.put("rows", tables);
        
        return AjaxResult.success(result);
    }

    /**
     * 在二维码图片下方添加桌号标识
     */
    private BufferedImage addTableNoLabel(BufferedImage qrImage, Integer tableNo) {
        int qrWidth = qrImage.getWidth();
        int qrHeight = qrImage.getHeight();
        int labelHeight = 50;
        
        // 创建新图片，包含二维码和标签
        BufferedImage finalImage = new BufferedImage(qrWidth, qrHeight + labelHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = finalImage.createGraphics();
        
        // 设置抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // 填充白色背景
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, qrWidth, qrHeight + labelHeight);
        
        // 绘制二维码
        g2d.drawImage(qrImage, 0, 0, null);
        
        // 绘制桌号标签
        g2d.setColor(new Color(93, 64, 55)); // #5d4037 主题色
        g2d.setFont(new Font("微软雅黑", Font.BOLD, 24));
        
        String label = tableNo + " 号桌";
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(label);
        int x = (qrWidth - textWidth) / 2;
        int y = qrHeight + (labelHeight + fm.getAscent() - fm.getDescent()) / 2;
        
        g2d.drawString(label, x, y);
        g2d.dispose();
        
        return finalImage;
    }
}
