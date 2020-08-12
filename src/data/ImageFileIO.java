package data;

import presentation.LogViewer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageFileIO {
    private File file;
    private int width;
    private int height;
    public ImageFileIO(File file) {
        this.file = file;
    }

    public File getImageFile() {
        return file;
    }

    public int getImageWidth() {
        return width;
    }

    public int getImageHeight() {
        return height;
    }

    public int[] readImage() {
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            width = bufferedImage.getWidth();
            height = bufferedImage.getHeight();
            int[] bytes = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    bytes[width * i + j] = bufferedImage.getRGB(j, i);
                }
            }
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
            LogViewer.logger.log(e.getMessage());
        }
        return null;
    }

    public void writeImage(int[] bytes, int width, int height) {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        bufferedImage.setRGB(0, 0, width, height, bytes, 0, width);
        try {
            File temp = new File(file.getParentFile(), "new_image.bmp");
            ImageIO.write(bufferedImage, "bmp", temp);
            LogViewer.logger.log("\n\n\n\n\n ARCHIVO GUARDADO COOMO:\n\n " + temp.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            LogViewer.logger.log(e.getMessage());
        }
    }
}
