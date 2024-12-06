package uz.duol.akfadealerbot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.duol.akfadealerbot.constants.FilePath;
import uz.duol.akfadealerbot.service.ImageWriterService;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageWriterServiceImpl implements ImageWriterService {


    @Override
    public File writeImage(String fileName,String amount){
        File outputFile = null;

        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File(new FilePath().FONT_PATH + "ComicNeue-Bold.ttf"));
            customFont = customFont.deriveFont(85f); // Set the font size

            log.info("Maxsus font muvaffaqiyatli yuklandi.");
            // Load the image
            File inputFile = new File(new FilePath().IMAGE_PATH + fileName);

            if (!inputFile.exists()) {
                log.warn("Kiritilgan fayl topilmadi: {}", inputFile.getAbsolutePath());
                return null;
            }

            BufferedImage image = ImageIO.read(inputFile);

            Graphics2D g2d = image.createGraphics();

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw percentage text
            g2d.setColor(Color.BLACK); // Text color
            g2d.setFont(customFont); // Font settings
            g2d.drawString(amount, 700, 750); // Adjust position as needed
            log.info("Rasmga matn chizildi: {} ", amount);

            // Dispose of the Graphics2D object
            g2d.dispose();

            // Save the output image
            outputFile = new File(new FilePath().IMAGE_PATH  + "output_image_" + fileName + ".png");

            ImageIO.write(image, "png", outputFile);

            log.info("Rasm muvaffaqiyatli ishlov berildi va saqlandi: {}", outputFile.getAbsolutePath());

        }catch (IOException | FontFormatException e) {
            log.error("Rasmga ishlov berishda xatolik yuz berdi: {} ", e.getMessage());
        }
        return outputFile;
    }



}
