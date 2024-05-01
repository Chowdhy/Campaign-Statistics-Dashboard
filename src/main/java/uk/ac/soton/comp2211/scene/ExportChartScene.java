package uk.ac.soton.comp2211.scene;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import uk.ac.soton.comp2211.ui.MainWindow;

import javax.imageio.ImageIO;
import java.io.File;
import java.nio.file.Path;

public class ExportChartScene extends MainScene {
    WritableImage image;

    public ExportChartScene(MainWindow window, WritableImage image) {
        super(window);
        this.image = image;
    }

    @Override
    public void initialise() {

    }

    @Override
    public void build() {
        File file = new File("Hello.png");

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (Exception e) {

        }
    }

    @Override
    public void cleanup() {

    }
}
