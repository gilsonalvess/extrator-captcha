import net.sourceforge.tess4j.Tesseract

import javax.imageio.ImageIO
import java.awt.*
import java.awt.image.BufferedImage

class CaptchaUtils {

    private static final int THRESHOLD = 35
    private static final int DELTA = 3
    private static final String INPUT_IMAGE_PATH = 'src/main/resources/inputImage/captcha_original.png'
    private static final String OUTPUT_IMAGE_PATH = 'src/main/resources/outputImage/captcha.png'

    static String extractTextFromImage(byte[] imageBytes, Color backgroundColor) throws IOException {
        File file = new File(INPUT_IMAGE_PATH)
        FileOutputStream fos = new FileOutputStream(file)
        fos.write(imageBytes)

        BufferedImage imageOriginal = ImageIO.read(new File(INPUT_IMAGE_PATH))
        replaceBackground(imageOriginal, backgroundColor)
        BufferedImage imageClean = cleanImage(ImageIO.read(new File(OUTPUT_IMAGE_PATH)))
        //ImageIO.write(imageClean, "png", new FileOutputStream("src/main/resources/outputImage/clean.png"))

        Tesseract tesseract = new Tesseract()
        tesseract.setDatapath("tessdata")
        String result = tesseract.doOCR(imageClean)

        return cleanResult(result)

    }

    private static void replaceBackground(BufferedImage initImage, Color backColor) {
        Integer width = initImage.getWidth()
        Integer height = initImage.getHeight()

        BufferedImage image = new BufferedImage(initImage.getWidth(), initImage.getHeight(), BufferedImage.TYPE_INT_ARGB)
        Graphics g = image.getGraphics()
        g.setColor(Color.WHITE)
        g.fillRect(0, 0, image.getWidth(), image.getHeight())
        g.drawImage(initImage, 0, 0, null)

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = image.getRGB(x, y)
                Color color = new Color(pixel)

                int dr = Math.abs(color.getRed() - backColor.getRed()),
                    dg = Math.abs(color.getGreen() - backColor.getGreen()),
                    db = Math.abs(color.getBlue() - backColor.getBlue())

                if (dr < THRESHOLD && dg < THRESHOLD && db < THRESHOLD) {
                    image.setRGB(x, y, Color.WHITE.getRGB())
                }
            }
        }
        File file = new File(OUTPUT_IMAGE_PATH)
        ImageIO.write(image, "png", file)
    }

    static String cleanResult(String textResult) {
        return textResult.find('\\w+').toString().toUpperCase()
    }

    static BufferedImage cleanImage(BufferedImage source) {
        BufferedImage imageClone = new BufferedImage(source.getWidth(), source.getHeight(), source.getType())
        Graphics2D g2d = imageClone.createGraphics()
        g2d.drawImage(source, 0, 0, null)
        g2d.dispose()

        for (int i = 0; i < imageClone.getWidth(); i++) {
            for (int j = 0; j < imageClone.getHeight(); j++) {
                int rgb = imageClone.getRGB(i, j)
                if (rgb == Color.WHITE.getRGB()) {
                    continue
                }
                if (isEligible(imageClone, i, j)) {
                    continue
                } else {
                    imageClone.setRGB(i, j, Color.WHITE.getRGB())
                }
            }
        }
        return imageClone
    }

    static Boolean isEligible(BufferedImage image, int x, int y) {

        int left = x - 1
        while (left < 0 && x - left < 2 * DELTA) {
            if (image.getRGB(left, y) == Color.WHITE.getRGB()) {
                break
            }
            left--
        }
        if (left < 0) {
            return false
        }
        int right = x + 1

        while (right < image.getWidth() && right - left < 2 * DELTA) {
            if (image.getRGB(right, y) == Color.WHITE.getRGB()) {
                break
            }
            right++
        }
        if (right > image.getWidth()) {
            return false
        }
        int top = y - 1
        while (top > 0 && y - top < 2 * DELTA) {
            if (image.getRGB(x, top) == Color.WHITE.getRGB()) {
                break
            }
            top--
        }
        if (top < 0) {
            return false
        }
        int bottom = y + 1
        while (bottom < image.getHeight() && bottom - top < 2 * DELTA) {
            if (image.getRGB(x, bottom) == Color.WHITE.getRGB()) {
                break
            }
            bottom++
        }
        if (bottom > image.getHeight()) {
            return false
        }

        int width = right - left
        int height = bottom - top
        if (width >= DELTA && height >= DELTA) {
            return true
        }
        return false

    }

    static Boolean convertImageFormat(String inputImagePath, String outputImagePath, String formatName) throws IOException {
        FileInputStream inputStream = new FileInputStream(inputImagePath)
        FileOutputStream outputStream = new FileOutputStream(outputImagePath)
        BufferedImage inputImage = ImageIO.read(inputStream)

        Boolean result = ImageIO.write(inputImage, formatName, outputStream)

        outputStream.close()
        inputStream.close()

        return result
    }
}
