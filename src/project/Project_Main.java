package project;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import project.ASCIIArtGenerator.ASCIIArtFont;

public class Project_Main {

    public static void main(String args[]) throws IOException, Exception {
        do {
            ConsoleApp cons = new ConsoleApp();
            cons.console_init();

            System.out.println("Do you want to login with a different acount?");
        } while (ConsoleUtils.requestConfirmation());

        ASCIIArtGenerator artGen = new ASCIIArtGenerator();

        System.out.println();

        System.out.println();

        artGen.printTextArt("Thank you!", ASCIIArtGenerator.ART_SIZE_LARGE);

    }
}
