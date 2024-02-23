package com.heitaox.sql.executor.test;

import com.sun.jndi.toolkit.url.UrlUtil;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yingjie Zheng
 * @date 2023/7/26 18:46
 * @description
 */
public class PDF2PNG {

    public static void main(String[] args) throws IOException {
        File pdfFile = new File("/Users/kiko/IdeaProjects/self/sql-executor-test/src/main/resources/tmp/1687312289157|艾芯5类.pdf");
        String outputFolderPath = "/Users/kiko/IdeaProjects/self/sql-executor-test/src/main/resources/tmp";
        // pdf转为图片
        File imageFile = null;
        String newFileName;
        PDDocument document = null;
        String pdfFileName = pdfFile.getName();
        long length = pdfFile.length();
        double size = (double) length / (1024 * 1024);
        document = PDDocument.load(pdfFile);
        // document.setResourceCache(new CustomerDefaultResourceCache());
        PDFRenderer renderer = new PDFRenderer(document);
        List<BufferedImage> images = new ArrayList<>();
        BufferedImage combinedImage;
        if (document.getNumberOfPages() == 1) {
            combinedImage = renderer.renderImageWithDPI(0, 150); // 设置 DPI（每英寸点数）
        } else {
            for (int i = 0; i < document.getNumberOfPages(); i++) {
                BufferedImage image = renderer.renderImageWithDPI(i, 150); // 设置 DPI（每英寸点数）
                images.add(image);
            }
            combinedImage = combineImages(images);
        }
        newFileName = pdfFileName.replace(".pdf", "_wh" + combinedImage.getWidth() + "x" + combinedImage.getHeight() + ".png").split("\\|")[1];
        imageFile = new File(outputFolderPath + "/" + newFileName);
        if (!imageFile.exists()) {
            imageFile.createNewFile();
        }
        ImageIO.write(combinedImage, "png", imageFile);
    }

    private static BufferedImage combineImages(List<BufferedImage> images) {
        int maxWidth = 0;
        int totalHeight = 0;
        for (BufferedImage image : images) {
            maxWidth = Math.max(maxWidth, image.getWidth());
            totalHeight += image.getHeight();
        }
        BufferedImage combinedImage = new BufferedImage(maxWidth, totalHeight, BufferedImage.TYPE_INT_ARGB);
        int currentHeight = 0;
        for (BufferedImage image : images) {
            combinedImage.getGraphics().drawImage(image, 0, currentHeight, null);
            currentHeight += image.getHeight();
        }
        return combinedImage;
    }


}
