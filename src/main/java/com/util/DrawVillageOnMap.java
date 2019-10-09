package com.util;

import com.exception.NoDataOnSiteException;
import com.settings.DrawVillageSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Component
public class DrawVillageOnMap {
    private static final Font FONT = new Font("TimesRoman", Font.PLAIN, 9);
    private final DrawVillageSetting villageSetting;

    @Autowired
    public DrawVillageOnMap(DrawVillageSetting villageSetting) {
        this.villageSetting = villageSetting;
    }

    // set the location of the village relative to the upper left corner
    void mapWithVillage(String pathToMap) throws IOException, NoDataOnSiteException {
        // Open picture file, load into a BufferedImage.
        BufferedImage img = ImageIO.read(new File(pathToMap));

        // Obtain the Graphics2D context associated with the BufferedImage.
        Graphics2D g;
        if (img != null) {
            g = img.createGraphics();
        } else throw new NoDataOnSiteException("Missing picture link");

        //set the mark on the map village
        g.setFont(FONT);
        g.setPaint(Color.BLACK);
        g.drawOval(villageSetting.getX(), villageSetting.getY(), villageSetting.getWidth(), villageSetting.getHeight());

        g.drawString(villageSetting.getMessage(), villageSetting.getX() - 13, villageSetting.getY() - 1);
        ImageIO.write(img, "gif", new File(pathToMap));

        // Clean up -- dispose the graphics context that was created
        g.dispose();
    }
}
