package seedu.address.bot.qrcode;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import seedu.address.bot.qrcode.exceptions.QRreadException;
import seedu.address.commons.core.LogsCenter;

/**
 * QRcodeAnalyser takes in a QR code image and unwraps the information encoded within it.
 */
public class QRcodeAnalyser {

    private static final Logger logger = LogsCenter.getLogger(QRcodeAnalyser.class);

    private String decodedText;

    public QRcodeAnalyser(File file) throws QRreadException {
        try {
            String decodedText = decodeQRcode(file);
            if (decodedText == null) {
                logger.info("No QR Code found in the image");
            } else {
                this.decodedText = decodedText;
                logger.info("Decoded text = " + decodedText);
            }
        } catch (QRreadException | IOException e) {
            logger.info("Could not decode QR Code: " + e.getMessage());
            throw new QRreadException();
        }
    }

    public String getDecodedText() {
        return this.decodedText;
    }

    /**
     * Method to decode the QR code using zxing api.
     */
    private static String decodeQRcode(File qrCodeimage) throws IOException, QRreadException {
        BufferedImage bufferedImage = ImageIO.read(qrCodeimage);
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        try {
            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText();
        } catch (NotFoundException e) {
            logger.info("There is no QR code in the image");
            throw new QRreadException();
        }
    }
}
