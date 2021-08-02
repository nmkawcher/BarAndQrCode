package kawcher.websheba.barandqrcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class GenerateBarCodeActivity extends AppCompatActivity {

    private EditText inputET;
    private ImageView qrIV, barIV;
    private Button generateBtn;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_bar_code);

        inputET=findViewById(R.id.et_input);
        generateBtn=findViewById(R.id.generate_btn);
        qrIV =findViewById(R.id.qr_iv);
        barIV =findViewById(R.id.bar_iv);

        generateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input=inputET.getText().toString();
                if(TextUtils.isEmpty(input)){
                    inputET.setError("required");
                    inputET.requestFocus();
                    return;
                }
                generateBarCode(input);
                generateQrCode(input);
            }
        });
    }

    private void generateBarCode(String input){
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(input, BarcodeFormat.CODE_128, barIV.getWidth(), barIV.getHeight());
            Bitmap bitmap = Bitmap.createBitmap(barIV.getWidth(), barIV.getHeight(), Bitmap.Config.RGB_565);
            for (int i = 0; i< barIV.getWidth(); i++){
                for (int j = 0; j< barIV.getHeight(); j++){
                    bitmap.setPixel(i,j,bitMatrix.get(i,j)? Color.BLACK:Color.WHITE);
                }
            }
            barIV.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void generateQrCode(String input) {
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);

        // initializing a variable for default display.
        Display display = manager.getDefaultDisplay();

        // creating a variable for point which
        // is to be displayed in QR Code.
        Point point = new Point();
        display.getSize(point);

        // getting width and
        // height of a point
        int width = point.x;
        int height = point.y;

        // generating dimension from width and height.
        int dimen = width < height ? width : height;
        dimen = dimen * 3 / 4;

        // setting this dimensions inside our qr code
        // encoder to generate our qr code.
        qrgEncoder = new QRGEncoder(input, null, QRGContents.Type.TEXT, dimen);
        try {
            // getting our qrcode in the form of bitmap.
            bitmap = qrgEncoder.encodeAsBitmap();
            // the bitmap is set inside our image
            // view using .setimagebitmap method.
            qrIV.setImageBitmap(bitmap);
        } catch (WriterException e) {
            // this method is called for
            // exception handling.
            Log.e("Tag", e.toString());
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(GenerateBarCodeActivity.this,MainActivity.class));
    }
}
