package seedu.address.bot.qrcode;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.bot.qrcode.exceptions.QRreadException;


/**
 * QRcodeAnalyser takes in a QR code image and unwraps the information encoded within it.
 */
public class QRcodeAnalyserTest {

    private static final String UNREADABLE_FILE_PATH = "./src/test/data/qrcode/BETSY_CROWE_UNREADABLE.jpg";
    private static final String READABLE_FILE_PATH = "./src/test/data/qrcode/BETSY_CROWE_READABLE.jpg";
    private static final String BETSY_DESIRED_DATA = "#/RR000000000SG n/Betsy Crowe t/frozen d/02-02-2002 "
            + "e/betsycrowe@example.com a/22 Crowe road S123123 p/1234567 t/fragile";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private QRcodeAnalyser qrCodeAnalyser;

    @Test
    public void acceptsReadableAndClear() throws QRreadException {
        File readable = new File(READABLE_FILE_PATH);
        qrCodeAnalyser = new QRcodeAnalyser(readable);
        assertEquals(BETSY_DESIRED_DATA, qrCodeAnalyser.getDecodedText());
    }

    @Test
    public void rejectsUnreadable() throws QRreadException {
        thrown.expect(QRreadException.class);
        File unreadable = new File(UNREADABLE_FILE_PATH);
        qrCodeAnalyser = new QRcodeAnalyser(unreadable);
    }

}
