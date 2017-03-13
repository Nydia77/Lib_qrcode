package cn.feichao.app.zxing;

import android.graphics.Bitmap;
import android.view.inputmethod.CorrectionInfo;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.aztec.AztecWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.datamatrix.DataMatrixWriter;
import com.google.zxing.datamatrix.encoder.ErrorCorrection;
import com.google.zxing.datamatrix.encoder.SymbolShapeHint;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.QRCode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by feichao on 2017/3/3.
 * 二维码编码
 */
public final class Encoder {

    public static final String QRCODE = "QRCode";
    public static final String DATAMATRIX = "DataMatrix";
    public static final String AZTEC = "Aztec";
    public static final String PDF417 = "PDF417";

    private Writer encoder;

    public Encoder() {
        encoder = new MultiFormatWriter();
    }


    public Bitmap createImage(String text, int width, int height, String type) {
        BarcodeFormat format = getBarcodeFormat(type);
        switch (type) {
            case QRCODE:
                return createQRCodeImage(text, width, height, format);
            case DATAMATRIX:
                return createDataMatrixImage(text, 144, 144, format);  // size 10×10 ~ 144×144
            case AZTEC:
                return createAztecImage(text, 151, 151, format); // size 15 X 15 ~ 151 X 151
            case PDF417:
                return createPDF417Image(text, width, height, format);
        }
        return null;
    }

    private Bitmap createQRCodeImage(String text, int width, int height, BarcodeFormat format) {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.MARGIN, 0);
//        hints.put(EncodeHintType.QR_VERSION, 1);
        BitMatrix bitMatrix = encode(text, width, height, format);
        int[] pixels = getPixels(width, height, bitMatrix);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private Bitmap createDataMatrixImage(String text, int width, int height, BarcodeFormat format) {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.MAX_SIZE, 144);
        hints.put(EncodeHintType.MIN_SIZE, 10);
//        if(width == height) {
//            hints.put(EncodeHintType.DATA_MATRIX_SHAPE, SymbolShapeHint.FORCE_SQUARE);  // 指定形状
//        }else {
//            hints.put(EncodeHintType.DATA_MATRIX_SHAPE, SymbolShapeHint.FORCE_RECTANGLE);
//        }
        BitMatrix bitMatrix = encode(text, width, height, format);
        int w = bitMatrix.getWidth();
        int h = bitMatrix.getHeight();
        int[] pixels = getPixels(w, h, bitMatrix);
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        return bitmap;
    }

    private Bitmap createAztecImage(String text, int width, int height, BarcodeFormat format) {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, 23);   // a minimum of 23% + 3 words is recommended
//        hints.put(EncodeHintType.AZTEC_LAYERS, 3);
        BitMatrix bitMatrix = encode(text, width, height, format, hints);
        int[] pixels = getPixels(width, height, bitMatrix);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private Bitmap createPDF417Image(String text, int width, int height, BarcodeFormat format) {
        return null;
    }

    private int[] getPixels(int w, int h, BitMatrix bitMatrix) {
        int[] pixels = new int[w * h];
        for(int y = 0; y < h; y++) {
            for(int x = 0; x < w; x++) {
                if(bitMatrix.get(x, y)) {
                    pixels[y * w + x] = 0xff000000;
                }else {
                    pixels[y * w + x] = 0xffffffff;
                }
            }
        }
        return pixels;
    }

    private BitMatrix encode(String text, int width, int height, BarcodeFormat format) {
        return encode(text, width, height, format, null);
    }

    private BitMatrix encode(String text, int width, int height, BarcodeFormat format,
                        Map<EncodeHintType, ?> hints) {
        try {
            return encoder.encode(text, format, width, height, hints);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }


    private BarcodeFormat getBarcodeFormat(String type) {
        switch (type) {
            case QRCODE:
                return BarcodeFormat.QR_CODE;
            case DATAMATRIX:
                return BarcodeFormat.DATA_MATRIX;
            case AZTEC:
                return BarcodeFormat.AZTEC;
            case PDF417:
                return BarcodeFormat.PDF_417;
        }
        return null;
    }

}
