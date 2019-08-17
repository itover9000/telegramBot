package com.util;

import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Component
public class DrawVillageOnMap {

    private int x;
    private int y;
    private int width;
    private int height;
    private String message;

    public DrawVillageOnMap() {
        this.x = 191;
        this.y = 188;
        this.width = 5;
        this.height = 5;
        this.message = "Мозоли";
    }

    public DrawVillageOnMap(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void mapWithVillage(String pathToMap) throws IOException {
        // Open picture file, load into a BufferedImage.
        BufferedImage img = ImageIO.read(new File(pathToMap));

        // Obtain the Graphics2D context associated with the BufferedImage.
        Graphics2D g = img.createGraphics();

        //set the mark on the map village Мозоли
        Font font = new Font("TimesRoman", Font.PLAIN, 9);
        g.setFont(font);
        g.setPaint(Color.BLACK);
        g.drawOval(x, y, width, height);

        g.drawString(message, x - 13, y - 1);
        ImageIO.write(img, "gif", new File(pathToMap));

        // Clean up -- dispose the graphics context that was created
        g.dispose();
    }
}
