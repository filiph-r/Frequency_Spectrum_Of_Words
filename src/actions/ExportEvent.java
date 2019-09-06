package actions;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class ExportEvent implements EventHandler<KeyEvent> {
	private Node node;
	private String filepath;
	private Stage stage;

	public ExportEvent(Node node, Stage stage) {
		this.node = node;
		this.stage = stage;
	}

	@Override
	public void handle(KeyEvent event) {
		if (event.isControlDown() && (event.getCode() == KeyCode.S)) {
			DirectoryChooser chooser = new DirectoryChooser();
			chooser.setTitle("Directory");
			File defaultDir = new File("C:/");
			chooser.setInitialDirectory(defaultDir);
			File selectedDir = chooser.showDialog(stage);
			if(selectedDir == null)
				return;
			
			filepath = selectedDir.toString();
			
			BufferedImage img = generate_png_from_container(node);

			PDDocument doc = null;
			PDPage page = null;
			PDXObjectImage ximg = null;

			doc = new PDDocument();
			page = new PDPage(new PDRectangle(img.getWidth(), img.getHeight()));

			doc.addPage(page);

			try {
				PDPageContentStream content = new PDPageContentStream(doc, page);
				ximg = new PDJpeg(doc, img);
				content.drawImage(ximg, 0, 0);

				content.close();
				doc.save(filepath + "/Diagram.pdf");
				doc.close();

			} catch (Exception e) {
				System.out.println("ERROR");
				return;
			}

			System.out.println("Exported..");
		}

	}

	public static BufferedImage generate_png_from_container(Node node) {
		SnapshotParameters param = new SnapshotParameters();
		param.setDepthBuffer(true);
		WritableImage snapshot = node.snapshot(param, null);
		BufferedImage tempImg = SwingFXUtils.fromFXImage(snapshot, null);
		BufferedImage img = null;
		byte[] imageInByte;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(tempImg, "png", baos);
			baos.flush();
			imageInByte = baos.toByteArray();
			baos.close();
			InputStream in = new ByteArrayInputStream(imageInByte);
			img = ImageIO.read(in);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return img;
	}

}
