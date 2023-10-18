package com.jxm.file.util;


import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.*;

import com.microsoft.schemas.office.office.CTLock;
import com.microsoft.schemas.vml.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.sl.usermodel.PictureData;
import org.apache.poi.wp.usermodel.HeaderFooterType;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFPictureShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import javax.imageio.ImageIO;
import javax.swing.*;
import com.itextpdf.text.Rectangle;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.util.stream.Stream;

@Slf4j
public class WaterMarkUtils {

    private static final String fontName = "PingFang SC"; // word字体
    private static final String fontSize = "0.5pt"; // 字体大小
    private static final int widthPerWord = 10; // 一个字平均长度，单位pt，用于：计算文本占用的长度（文本总个数*单字长度）
    private static final String styleRotation = "-45"; // 文本旋转角度
    private static final String fontColor = "#dfdfdf"; // 字体颜色

    // 水印透明度
    private static float alpha = 0.3f;
    // 水印之间的间隔
    private static final int XMOVE = 200;
    // 水印之间的间隔
    private static final int YMOVE = 200;

    /**
     * @param srcImgPath       源图片路径
     * @param tarImgPath       保存的图片路径
     * @param waterMarkContent 水印内容
     */
    public static void addPictureWaterMark(String srcImgPath, String tarImgPath,
                                           String waterMarkContent) {
        FileOutputStream outImgStream = null;
        try {
            // 读取原图片信息
            File srcImgFile = new File(srcImgPath);// 得到文件
            Image srcImg = ImageIO.read(srcImgFile);// 文件转化为图片
            int srcImgWidth = srcImg.getWidth(null);// 获取图片的宽
            int srcImgHeight = srcImg.getHeight(null);// 获取图片的高
            // 加水印
            BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufImg.createGraphics();
            // 设置对线段的锯齿状边缘处理
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
            // 设置水印旋转
            g.rotate(Math.toRadians(-40),
                    (double) bufImg.getWidth() / 2,
                    (double) bufImg.getHeight() / 2);
            g.setColor(new Color(107, 109, 106)); // 根据图片的背景设置水印颜色
            Font font = new Font("宋体", Font.PLAIN, 20);
            g.setFont(font); // 设置字体
            // 设置水印文字透明度
//			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,alpha));

            //根据不同的像素判断多个水印还是单个水印300*300
            if (srcImgWidth > 300 && srcImgHeight > 300) {
                //多个水印
                // 设置水印的坐标
                int x = -srcImgWidth / 2;
                int y = -srcImgHeight / 2;
                int markWidth = font.getSize() * getTextLength(waterMarkContent);// 字体长度
                int markHeight = font.getSize();// 字体高度
                // 循环添加水印
                while (x < srcImgWidth * 1.5) {
                    y = -srcImgHeight / 2;
                    while (y < srcImgHeight * 1.5) {
                        g.drawString(waterMarkContent, x, y);
                        y += markHeight + YMOVE;
                    }
                    x += markWidth + XMOVE;
                }
            } else {
                //单个水印
                int x = (srcImgWidth - getWatermarkLength(waterMarkContent, g)) / 2;
                int y = srcImgHeight / 2;
                g.drawString(waterMarkContent, x, y);
            }
            g.dispose();
            // 输出图片
            outImgStream = new FileOutputStream(tarImgPath);
            String formatName = srcImgPath.substring(srcImgPath.lastIndexOf(".") + 1, srcImgPath.length());
            ImageIO.write(bufImg, formatName, outImgStream);
            outImgStream.flush();
        } catch (Exception e) {
            log.error("addPictureWatermark fail", e);
            throw new RuntimeException("addPictureWatermark fail");
        } finally {
            if (outImgStream != null) {
                try {
                    outImgStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void setPictureWatermark(FileInputStream fileInputStream, OutputStream outputStream, String markStr) {
        try {
            Image srcImg = ImageIO.read(fileInputStream);
            int srcImgWidth = srcImg.getWidth(null);
            int srcImgHeight = srcImg.getHeight(null);
            // 加水印
            BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufImg.createGraphics();
            g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
            Font font = new Font("宋体", Font.BOLD, srcImgHeight / 6);
            //根据图片的背景设置水印颜色
            g.setColor(Color.black);
            //设置旋转角度
            g.rotate(Math.toRadians(-45), (double) bufImg.getWidth() / 2, (double) bufImg.getHeight() / 2);
            //设置水印透明度
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.2F));

            g.setFont(font);
            int x = (srcImgWidth - getWatermarkLength(markStr, g)) / 2;
            int y = srcImgHeight / 2;
            g.drawString(markStr, x, y);
            g.dispose();
            // 输出图片
            ImageIO.write(bufImg, "jpg", outputStream);
        }catch (Exception e) {
            log.error("setPictureWatermark fail", e);
            throw new RuntimeException("setPictureWatermark fail");
        }
    }

    /**
     * 图片添加水印
     *
     * @param srcImgPath       需要添加水印的图片的路径
     * @param outImgPath       添加水印后图片输出路径
     * @param markContentColor 水印文字的颜色
     * @param waterMarkContent 水印的文字
     */
    public static void setPictureWatermark(String srcImgPath, String outImgPath, Color markContentColor, String waterMarkContent) {
        FileOutputStream outImgStream = null;
        try {
            // 读取原图片信息
            File srcImgFile = new File(srcImgPath);
            Image srcImg = ImageIO.read(srcImgFile);
            int srcImgWidth = srcImg.getWidth(null);
            int srcImgHeight = srcImg.getHeight(null);
            // 加水印
            BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufImg.createGraphics();
            g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
            Font font = new Font("宋体", Font.BOLD, srcImgHeight / 6);
            //根据图片的背景设置水印颜色
            g.setColor(markContentColor);
            //设置旋转角度
            g.rotate(Math.toRadians(-45), (double) bufImg.getWidth() / 2, (double) bufImg.getHeight() / 2);
            //设置水印透明度
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.2F));

            g.setFont(font);
            int x = (srcImgWidth - getWatermarkLength(waterMarkContent, g)) / 2;
            int y = srcImgHeight / 2;
            g.drawString(waterMarkContent, x, y);
            g.dispose();
            // 输出图片
            outImgStream = new FileOutputStream(outImgPath);
            ImageIO.write(bufImg, "jpg", outImgStream);
            outImgStream.flush();
            outImgStream.close();
        } catch (Exception e) {
            log.error("setPictureWatermark fail", e);
            throw new RuntimeException("setPictureWatermark fail");
        } finally {
            if (outImgStream != null) {
                try {
                    outImgStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * word文字水印
     */
    public static void setWordWaterMark(FileInputStream fileInputStream, OutputStream outputStream, String markStr) {
        XWPFDocument doc = null;
        try {
            doc = new XWPFDocument(fileInputStream);
        } catch (FileNotFoundException var24) {
            throw new RuntimeException("源文件不存在");
        } catch (IOException var25) {
            throw new RuntimeException("读取源文件IO异常");
        } catch (Exception var26) {
            throw new RuntimeException("不支持的文档格式");
        }
        makeFullWaterMarkByWordArt(doc, markStr, fontColor, fontSize, styleRotation);
        // 设置文档只读
        doc.enforceReadonlyProtection();
        try {
            doc.write(outputStream);
        } catch (FileNotFoundException var21) {
            throw new RuntimeException("创建输出文件失败");
        } catch (Exception var22) {
            throw new RuntimeException("添加文档水印失败");
        }
    }

    /**
     * word文字水印  打印输出
     */
    public static void setWordWaterMark(String inputPath, String outPath, String markStr) {
        // 读取原始文件
        File inputFile = new File(inputPath);
        XWPFDocument doc = null;
        try {
            doc = new XWPFDocument(new FileInputStream(inputFile));
        } catch (FileNotFoundException var24) {
            throw new RuntimeException("源文件不存在");
        } catch (IOException var25) {
            throw new RuntimeException("读取源文件IO异常");
        } catch (Exception var26) {
            throw new RuntimeException("不支持的文档格式");
        }

        makeFullWaterMarkByWordArt(doc, markStr, fontColor, fontSize, styleRotation);
        // 设置文档只读
        doc.enforceReadonlyProtection();
        // 生成输出文件
        File file = new File(outPath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException var23) {
                throw new RuntimeException("创建输出文件失败");
            }
        }
        // 打开输出流，将doc写入输出文件
        OutputStream os = null;
        try {
            os = new FileOutputStream(outPath);
            doc.write(os);
        } catch (FileNotFoundException var21) {
            throw new RuntimeException("创建输出文件失败");
        } catch (Exception var22) {
            throw new RuntimeException("添加文档水印失败");
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException var20) {
                    var20.printStackTrace();
                }
            }
        }
    }

    /**
     * pdf设置文字水印
     */
    public static void setPdfWatermark(FileInputStream fileInputStream, OutputStream outputStream, String markStr) {
        PdfStamper stamper = null;
        int total = 0;
        PdfContentByte content;
        Rectangle pageSizeWithRotation = null;
        BaseFont base = null;
        PdfReader reader = null;
        try {
            reader = new PdfReader(fileInputStream);
            //解决PdfReader not opened with owner password
            Field f = PdfReader.class.getDeclaredField("ownerPasswordUsed");
            f.setAccessible(true);
            f.set(reader, Boolean.TRUE);
            stamper = new PdfStamper(reader, outputStream);
            total = reader.getNumberOfPages() + 1;
            base = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
        } catch (IOException e) {
            log.error("setPdfWatermark fail:", e);
            throw new RuntimeException("setPdfWatermark fail");
        } catch (DocumentException e) {
            log.error("setPdfWatermark fail:", e);
            throw new RuntimeException("setPdfWatermark fail");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        // 获取水印文字的高度和宽度
        int textH = 0, textW = 0;
        JLabel label = new JLabel();
        label.setText(markStr);
        FontMetrics metrics = label.getFontMetrics(label.getFont());
        textH = metrics.getHeight();
        textW = metrics.stringWidth(label.getText());
        PdfGState gs = new PdfGState();
        for (int i = 1; i < total; i++) {
            //在内容上方加水印
            content = stamper.getOverContent(i);
            gs.setFillOpacity(alpha);
            content.saveState();
            content.setGState(gs);
            content.beginText();
//            content.setRGBColorFill(0, 0, 0);
            content.setFontAndSize(base, 20);
            // 获取每一页的高度、宽度
            pageSizeWithRotation = reader.getPageSizeWithRotation(i);
            float pageHeight = pageSizeWithRotation.getHeight();
            float pageWidth = pageSizeWithRotation.getWidth();

            // 根据纸张大小多次添加， 水印文字成30度角倾斜
            for (int height = -5 + textH; height < pageHeight; height = height + YMOVE) {
                for (int width = -5 + textW; width < pageWidth + textW; width = width + XMOVE) {
                    content.showTextAligned(Element.ALIGN_LEFT, markStr, width - textW, height - textH, 30);
                }
            }
            content.endText();
        }
        try {
            stamper.close();
            reader.close();
        } catch (IOException e) {
            log.error("setPdfWatermark fail:", e);
            throw new RuntimeException("setPdfWatermark fail");
        } catch (DocumentException e) {
            log.error("setPdfWatermark fail:", e);
            throw new RuntimeException("setPdfWatermark fail");
        }
    }
    /**
     * pdf设置文字水印
     */
    public static void setPdfWatermark(String inputPath, String outPath, String markStr) {
        File file = new File(outPath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                log.error("setPdfWatermark fail: 创建输出文件IO异常", e);
                throw new RuntimeException("setPdfWatermark fail: 创建输出文件IO异常");
            }
        }
        BufferedOutputStream bufferOut = null;
        try {
            bufferOut = new BufferedOutputStream(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            log.error("setPdfWatermark fail: 源文件不存在", e);
            throw new RuntimeException("setPdfWatermark fail: 源文件不存在");
        }
        PdfStamper stamper = null;
        int total = 0;
        PdfContentByte content;
        Rectangle pageSizeWithRotation = null;
        BaseFont base = null;
        PdfReader reader = null;
        try {
            reader = new PdfReader(inputPath);
            //解决PdfReader not opened with owner password
            Field f = PdfReader.class.getDeclaredField("ownerPasswordUsed");
            f.setAccessible(true);
            f.set(reader, Boolean.TRUE);
            stamper = new PdfStamper(reader, bufferOut);
            total = reader.getNumberOfPages() + 1;
            base = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
        } catch (IOException e) {
            log.error("setPdfWatermark fail:", e);
            throw new RuntimeException("setPdfWatermark fail");
        } catch (DocumentException e) {
            log.error("setPdfWatermark fail:", e);
            throw new RuntimeException("setPdfWatermark fail");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        // 获取水印文字的高度和宽度
        int textH = 0, textW = 0;
        JLabel label = new JLabel();
        label.setText(markStr);
        FontMetrics metrics = label.getFontMetrics(label.getFont());
        textH = metrics.getHeight();
        textW = metrics.stringWidth(label.getText());
        PdfGState gs = new PdfGState();
        for (int i = 1; i < total; i++) {
            //在内容上方加水印
            content = stamper.getOverContent(i);
            gs.setFillOpacity(alpha);
            content.saveState();
            content.setGState(gs);
            content.beginText();
//            content.setRGBColorFill(0, 0, 0);
            content.setFontAndSize(base, 20);
            // 获取每一页的高度、宽度
            pageSizeWithRotation = reader.getPageSizeWithRotation(i);
            float pageHeight = pageSizeWithRotation.getHeight();
            float pageWidth = pageSizeWithRotation.getWidth();

            // 根据纸张大小多次添加， 水印文字成30度角倾斜
            for (int height = -5 + textH; height < pageHeight; height = height + YMOVE) {
                for (int width = -5 + textW; width < pageWidth + textW; width = width + XMOVE) {
                    content.showTextAligned(Element.ALIGN_LEFT, markStr, width - textW, height - textH, 30);
                }
            }
            content.endText();
        }
        try {
            stamper.close();
            bufferOut.flush();
            bufferOut.close();
            reader.close();
        } catch (IOException e) {
            log.error("setPdfWatermark fail:", e);
            throw new RuntimeException("setPdfWatermark fail");
        } catch (DocumentException e) {
            log.error("setPdfWatermark fail:", e);
            throw new RuntimeException("setPdfWatermark fail");
        }
    }


    /**
     * PPT设置水印
     */
    public static void setPPTWaterMark(FileInputStream fileInputStream, OutputStream outputStream, String markStr) {
        XMLSlideShow slideShow;
        try {
            slideShow = new XMLSlideShow(fileInputStream);
        } catch (IOException e) {
            log.error("setPPTWaterMark fail:", e);
            throw new RuntimeException("setPPTWaterMark fail:获取PPT文件失败");
        }
        ByteArrayOutputStream os = null;
        try {
            //获取水印
            os = getImage(markStr);
            PictureData pictureData1 = slideShow.addPicture(os.toByteArray(), PictureData.PictureType.PNG);
            for (XSLFSlide slide : slideShow.getSlides()) {
                XSLFPictureShape pictureShape = slide.createPicture(pictureData1);
//                pictureShape.setAnchor(new java.awt.Rectangle(250, 0, 500, 500));
                pictureShape.setAnchor(pictureShape.getAnchor());
            }
            slideShow.write(outputStream);
        } catch (IOException e) {
            log.error("setPPTWaterMark fail:" + e);
            throw new RuntimeException("setPPTWaterMark fail：生成ppt文件失败");
        }
        finally {
            if (slideShow != null) {
                try {
                    slideShow.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * PPT设置水印
     */
    public static void setPPTWaterMark(String path, String targetpath, String markStr) {
        XMLSlideShow slideShow;
        try {
            slideShow = new XMLSlideShow(new FileInputStream(path));
        } catch (IOException e) {
            log.error("setPPTWaterMark fail:", e);
            throw new RuntimeException("setPPTWaterMark fail:获取PPT文件失败");
        }
        ByteArrayOutputStream os = null;
        FileOutputStream out = null;
        try {
            //获取水印
            os = getImage(markStr);
            PictureData pictureData1 = slideShow.addPicture(os.toByteArray(), PictureData.PictureType.PNG);
            for (XSLFSlide slide : slideShow.getSlides()) {
                XSLFPictureShape pictureShape = slide.createPicture(pictureData1);
//                pictureShape.setAnchor(new java.awt.Rectangle(250, 0, 500, 500));
                pictureShape.setAnchor(pictureShape.getAnchor());
            }
            out = new FileOutputStream(targetpath);
            slideShow.write(out);
        } catch (IOException e) {
            log.error("setPPTWaterMark fail:" + e);
            throw new RuntimeException("setPPTWaterMark fail：生成ppt文件失败");
        } finally {
            if (slideShow != null) {
                try {
                    slideShow.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取水印文字总长度
     *
     * @param waterMarkContent 水印的文字
     * @param g
     * @return 水印文字总长度
     */
    public static int getWatermarkLength(String waterMarkContent, Graphics2D g) {
        return g.getFontMetrics(g.getFont()).charsWidth(waterMarkContent.toCharArray(), 0, waterMarkContent.length());
    }

    /**
     * 获取文本长度。汉字为1:1，英文和数字为2:1
     */
    private static int getTextLength(String text) {
        int length = text.length();
        for (int i = 0; i < text.length(); i++) {
            String s = String.valueOf(text.charAt(i));
            if (s.getBytes().length > 1) {
                length++;
            }
        }
        length = length % 2 == 0 ? length / 2 : length / 2 + 1;
        return length;
    }

    /**
     * 获取水印文字图片流
     *
     * @param text
     * @return
     */
    private static ByteArrayOutputStream getImage(String text) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            // 导出到字节流B
            BufferedImage image = createWaterMarkImageBig(text);
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            log.error("getImage fail: 创建水印图片IO异常", e);
            throw new RuntimeException("getImage fail: 创建水印图片IO异常");
        }
        return os;
    }

    /**
     * 根据文字生成水印图片（大号 平铺）
     *
     * @param text
     * @return
     */
    public static BufferedImage createWaterMarkImageBig(String text) {
        Integer width = 1000;
        Integer height = 800;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);// 获取bufferedImage对象
        Font font = new Font("宋体", Font.PLAIN, 70);
        Graphics2D g2d = image.createGraphics();
        image = g2d.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        g2d.dispose();
        g2d = image.createGraphics();
        //设置字体颜色和透明度
        g2d.setColor(new Color(0, 0, 0, 60));
        //设置字体
        g2d.setStroke(new BasicStroke(1));
        //设置字体类型  加粗 大小
        g2d.setFont(font);
        //设置倾斜度
        g2d.rotate(Math.toRadians(-30), (double) image.getWidth() / 2, (double) image.getHeight() / 2);
        FontRenderContext context = g2d.getFontRenderContext();
        Rectangle2D bounds = font.getStringBounds(text, context);
        double x = (width - bounds.getWidth()) / 2;
        double y = (height - bounds.getHeight()) / 2;
        double ascent = -bounds.getY();
        double baseY = y + ascent;
        //写入水印文字原定高度过小，所以累计写水印，增加高度
        g2d.drawString(text, (int) x, (int) baseY);
        //设置透明度
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        //释放对象
        g2d.dispose();
        return image;
    }


    public static void makeFullWaterMarkByWordArt(XWPFDocument docx, String customText,String fontColor,String fontSize,String styleRotation) {
        customText = customText + repeatString(" ", 16); // 水印文字之间使用8个空格分隔
        customText = repeatString(customText, 3); // 一行水印重复水印文字次数
        String styleTop = "0pt";  // 与顶部的间距

        if (docx == null) {
            return;
        }
        // 遍历文档，添加水印
        for (int lineIndex = -10; lineIndex < 20; lineIndex++) {
            styleTop = 200 * lineIndex + "pt";
            waterMarkDocXDocument(docx, customText, styleTop, 1,fontColor, fontSize ,styleRotation);
        }
    }


    private static void waterMarkDocXDocument(XWPFDocument doc, String customText, String styleTop, int type,String fontColor,String fontSize,String rotation) {
        XWPFHeader header = doc.createHeader(HeaderFooterType.DEFAULT); // 如果之前已经创建过 DEFAULT 的Header，将会复用之
        int size = header.getParagraphs().size();
        if (size == 0) {
            header.createParagraph();
        }
        CTP ctp = header.getParagraphArray(0).getCTP();
        byte[] rsidr = doc.getDocument().getBody().getPArray(0).getRsidR();
        byte[] rsidrdefault = doc.getDocument().getBody().getPArray(0).getRsidRDefault();
        ctp.setRsidP(rsidr);
        ctp.setRsidRDefault(rsidrdefault);
        CTPPr ppr = ctp.addNewPPr();
        ppr.addNewPStyle().setVal("Header");
        // 开始加水印
        CTR ctr = ctp.addNewR();
        CTRPr ctrpr = ctr.addNewRPr();
        ctrpr.addNewNoProof();
        CTGroup group = CTGroup.Factory.newInstance();
        CTShapetype shapetype = group.addNewShapetype();
        CTTextPath shapeTypeTextPath = shapetype.addNewTextpath();
        shapeTypeTextPath.setOn(STTrueFalse.T);
        shapeTypeTextPath.setFitshape(STTrueFalse.T);
        CTLock lock = shapetype.addNewLock();
        lock.setExt(STExt.VIEW);
        CTShape shape = group.addNewShape();
        shape.setId("PowerPlusWaterMarkObject");
        shape.setSpid("_x0000_s102");
        shape.setType("#_x0000_t136");
        if(type != 2){
            shape.setStyle(getShapeStyle(customText, styleTop,rotation)); // 设置形状样式（旋转，位置，相对路径等参数）
        }else{
            shape.setStyle(getShapeStyle()); // 设置形状样式（旋转，位置，相对路径等参数）
        }
        shape.setFillcolor(fontColor);
        shape.setStroked(STTrueFalse.FALSE); // 字体设置为实心
        CTTextPath shapeTextPath = shape.addNewTextpath(); // 绘制文本的路径
        shapeTextPath.setStyle("font-family:" + fontName + ";font-size:" + fontSize); // 设置文本字体与大小
        shapeTextPath.setString(customText);
        CTPicture pict = ctr.addNewPict();
        pict.set(group);
    }


    /**
     * 将指定的字符串重复repeats次.
     * @param pattern 字符串
     * @param repeats 重复次数
     * @return 生成的字符串
     */
    private static String repeatString(String pattern, int repeats) {
        StringBuilder buffer = new StringBuilder(pattern.length() * repeats);
        Stream.generate(() -> pattern).limit(repeats).forEach(buffer::append);
        return new String(buffer);
    }

    /**
     * 构建Shape的样式参数
     * @param customText 水印文本
     * @return
     */
    private static String getShapeStyle(String customText, String styleTop,String styleRotation) {
        StringBuilder sb = new StringBuilder();
        sb.append("position: ").append("absolute"); // 文本path绘制的定位方式
        sb.append(";width: ").append(customText.length() * widthPerWord).append("pt"); // 计算文本占用的长度（文本总个数*单字长度）
        sb.append(";height: ").append("20pt"); // 字体高度
        sb.append(";z-index: ").append("-251654144");
        sb.append(";mso-wrap-edited: ").append("f");
        sb.append(";margin-top: ").append(styleTop);
        sb.append(";mso-position-horizontal-relative: ").append("margin");
        sb.append(";mso-position-vertical-relative: ").append("margin");
        sb.append(";mso-position-vertical: ").append("left");
        sb.append(";mso-position-horizontal: ").append("center");
        sb.append(";rotation: ").append(styleRotation);
        return sb.toString();
    }
    /**
     * 构建Shape的样式参数
     * @return
     */
    private static String getShapeStyle() {
        StringBuilder sb = new StringBuilder();
        sb.append("position: ").append("absolute"); // 文本path绘制的定位方式
        sb.append(";left: ").append("opt");
        sb.append(";width: ").append("500pt"); // 计算文本占用的长度（文本总个数*单字长度）
        sb.append(";height: ").append("150pt"); // 字体高度
        sb.append(";z-index: ").append("-251654144");
        sb.append(";mso-wrap-edited: ").append("f");
        sb.append(";margin-left: ").append("-50pt");
        sb.append(";margin-top: ").append("270pt");
        sb.append(";mso-position-horizontal-relative: ").append("margin");
        sb.append(";mso-position-vertical-relative: ").append("margin");
        sb.append(";mso-width-relative: ").append("page");
        sb.append(";mso-height-relative: ").append("page");
        sb.append(";rotation: ").append("-2949120f");
        return sb.toString();
    }

}
