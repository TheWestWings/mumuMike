package com.mumuwest.mumumike.utils;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtil {

    /**
     * 将List<T>导出到Excel并写入HttpServletResponse
     * @param dataList 数据列表
     * @param response HttpServletResponse
     * @param fileName 下载文件名（不含路径，仅文件名，如 "data.xlsx"）
     * @param <T> 泛型类型
     * @throws IOException
     * @throws IllegalAccessException
     */
    public static <T> void exportListToExcel(List<T> dataList, HttpServletResponse response, String fileName)
            throws IOException, IllegalAccessException {
        if (dataList == null || dataList.isEmpty()) {
            throw new IllegalArgumentException("Data list cannot be null or empty");
        }
        if (fileName == null || fileName.trim().isEmpty()) {
            fileName = "export.xlsx";
        }

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" +
                new String(fileName.getBytes("UTF-8"), "ISO-8859-1")); // 处理中文文件名

        // 创建Excel工作簿和工作表
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");

        // 获取泛型类的字段
        Class<?> clazz = dataList.get(0).getClass();
        Field[] fields = clazz.getDeclaredFields();

        // 创建表头
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < fields.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(fields[i].getName());

            // 设置表头样式
            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);
            cell.setCellStyle(headerStyle);
        }

        // 填充数据
        int rowNum = 1;
        for (T item : dataList) {
            Row row = sheet.createRow(rowNum++);
            for (int i = 0; i < fields.length; i++) {
                Cell cell = row.createCell(i);
                fields[i].setAccessible(true); // 允许访问私有字段
                Object value = fields[i].get(item);
                if (value != null) {
                    if (value instanceof Number) {
                        cell.setCellValue(((Number) value).doubleValue());
                    } else if (value instanceof Boolean) {
                        cell.setCellValue((Boolean) value);
                    } else if (value instanceof java.util.Date) {
                        // 格式化日期为 YYYY-MM-DD
                        CellStyle dateStyle = workbook.createCellStyle();
                        dateStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("yyyy-MM-dd"));
                        cell.setCellStyle(dateStyle);
                        cell.setCellValue((java.util.Date) value);
                    } else {
                        cell.setCellValue(value.toString());
                    }
                }
                fields[i].setAccessible(false);
            }
        }

        // 自动调整列宽
        for (int i = 0; i < fields.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // 写入响应输出流
        try (OutputStream out = response.getOutputStream()) {
            workbook.write(out);
            out.flush();
        }

        // 关闭工作簿
        workbook.close();
    }

    /**
     * 从Excel读取数据并映射到实体类
     * @param inputStream Excel文件输入流
     * @param clazz 实体类的Class
     * @param <T> 泛型类型
     * @return 实体类列表
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <T> List<T> importExcelToList(InputStream inputStream, Class<T> clazz)
            throws IOException, IllegalAccessException, InstantiationException {
        List<T> resultList = new ArrayList<>();

        // 创建工作簿
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0); // 读取第一个工作表

        // 获取表头
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            throw new IllegalArgumentException("Excel file has no header row");
        }

        // 构建表头字段映射
        Map<String, Integer> headerMap = new HashMap<>();
        for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
            Cell cell = headerRow.getCell(i);
            if (cell != null) {
                headerMap.put(cell.getStringCellValue().trim(), i);
            }
        }

        // 获取实体类字段
        Field[] fields = clazz.getDeclaredFields();

        // 读取数据行
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            // 创建实体类实例
            T instance = clazz.newInstance();

            // 为每个字段赋值
            for (Field field : fields) {
                field.setAccessible(true);
                Integer columnIndex = headerMap.get(field.getName());
                if (columnIndex != null) {
                    Cell cell = row.getCell(columnIndex);
                    if (cell != null) {
                        setFieldValue(field, instance, cell);
                    }
                }
                field.setAccessible(false);
            }

            resultList.add(instance);
        }

        // 关闭工作簿
        workbook.close();
        return resultList;
    }

    /**
     * 根据实体类生成只包含表头的Excel（排除id字段）
     * @param clazz 实体类的Class
     * @param response HttpServletResponse
     * @param fileName 下载文件名（不含路径，仅文件名，如 "template.xlsx"）
     * @param <T> 泛型类型
     * @throws IOException
     */
    public static <T> void generateHeaderExcel(Class<T> clazz, HttpServletResponse response, String fileName)
            throws IOException {
        if (clazz == null) {
            throw new IllegalArgumentException("Class cannot be null");
        }
        if (fileName == null || fileName.trim().isEmpty()) {
            fileName = "template.xlsx";
        }

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" +
                new String(fileName.getBytes("UTF-8"), "ISO-8859-1")); // 处理中文文件名

        // 创建Excel工作簿和工作表
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");

        // 获取实体类字段，排除id字段
        Field[] fields = clazz.getDeclaredFields();
        List<Field> filteredFields = new ArrayList<>();
        for (Field field : fields) {
            if (!field.getName().equals("id") && !field.getName().equals("updateTime")) {
                filteredFields.add(field);
            }

        }

        // 创建表头
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < filteredFields.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(filteredFields.get(i).getName());

            // 设置表头样式
            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);
            cell.setCellStyle(headerStyle);
        }

        // 自动调整列宽
        for (int i = 0; i < filteredFields.size(); i++) {
            sheet.autoSizeColumn(i);
        }

        // 写入响应输出流
        try (OutputStream out = response.getOutputStream()) {
            workbook.write(out);
            out.flush();
        }

        // 关闭工作簿
        workbook.close();
    }

    /**
     * 根据字段类型设置实体类字段值
     * @param field 字段
     * @param instance 实体类实例
     * @param cell Excel单元格
     * @throws IllegalAccessException
     */
    private static void setFieldValue(Field field, Object instance, Cell cell)
            throws IllegalAccessException {
        Class<?> fieldType = field.getType();

        // 处理日期类型
        if (fieldType == java.util.Date.class) {
            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                field.set(instance, cell.getDateCellValue());
            } else if (cell.getCellType() == CellType.STRING) {
                String value = cell.getStringCellValue();
                if (!value.isEmpty()) {
                    try {
                        // 尝试多种日期格式
                        String[] dateFormats = {
                                "yyyy-MM-dd",
                                "yyyy/MM/dd",
                                "yyyy-MM-dd HH:mm:ss",
                                "yyyy/MM/dd HH:mm:ss"
                        };
                        for (String format : dateFormats) {
                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat(format);
                                field.set(instance, sdf.parse(value));
                                return;
                            } catch (Exception ignored) {
                                // 尝试下一个格式
                            }
                        }
                    } catch (Exception e) {
                        // 日期解析失败，跳过
                    }
                }
            }
            return;
        }

        // 处理其他类型
        switch (cell.getCellType()) {
            case STRING:
                String value = cell.getStringCellValue();
                if (fieldType == String.class) {
                    field.set(instance, value);
                } else if (fieldType == Integer.class || fieldType == int.class) {
                    field.set(instance, value.isEmpty() ? 0 : Integer.parseInt(value));
                } else if (fieldType == Double.class || fieldType == double.class) {
                    field.set(instance, value.isEmpty() ? 0.0 : Double.parseDouble(value));
                } else if (fieldType == Boolean.class || fieldType == boolean.class) {
                    field.set(instance, Boolean.parseBoolean(value));
                }
                break;
            case NUMERIC:
                double numericValue = cell.getNumericCellValue();
                if (fieldType == Integer.class || fieldType == int.class) {
                    field.set(instance, (int) numericValue);
                } else if (fieldType == Double.class || fieldType == double.class) {
                    field.set(instance, numericValue);
                } else if (fieldType == String.class) {
                    field.set(instance, String.valueOf(numericValue));
                }
                break;
            case BOOLEAN:
                if (fieldType == Boolean.class || fieldType == boolean.class) {
                    field.set(instance, cell.getBooleanCellValue());
                } else if (fieldType == String.class) {
                    field.set(instance, String.valueOf(cell.getBooleanCellValue()));
                }
                break;
            default:
                break;
        }
    }
}