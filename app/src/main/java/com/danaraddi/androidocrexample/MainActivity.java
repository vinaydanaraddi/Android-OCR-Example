package com.danaraddi.androidocrexample;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "Text API";
    private static final int PHOTO_REQUEST = 10;
    private TextView scanResults;
    private Uri imageUri;
    private ImageView imageView;
    private TextRecognizer detector;
    private static final int REQUEST_WRITE_PERMISSION = 20;
    private static final String SAVED_INSTANCE_URI = "uri";
    private static final String SAVED_INSTANCE_RESULT = "result";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button);
        imageView = (ImageView) findViewById(R.id.imageView);
        scanResults = (TextView) findViewById(R.id.results);

        detector = new TextRecognizer.Builder(getApplicationContext()).build();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scan();
            }
        });
    }


    private void scan() {
        try {
            // get image from drawable
            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),
                    R.drawable.image);
            imageView.setImageBitmap(bitmap);

            if (detector.isOperational() && bitmap != null) {
                Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                SparseArray<TextBlock> textBlocks = detector.detect(frame);
                String blocks = "";
                String lines = "";
                String words = "";
                for (int index = 0; index < textBlocks.size(); index++) {
                    //extract scanned text blocks here
                    TextBlock tBlock = textBlocks.valueAt(index);
                    blocks = blocks + tBlock.getValue() + "\n" + "\n";
                    for (Text line : tBlock.getComponents()) {
                        //extract scanned text lines here
                        lines = lines + line.getValue() + "\n";
                        for (Text element : line.getComponents()) {
                            //extract scanned text words here
                            words = words + element.getValue() + ", ";
                        }
                    }
                }
                if (textBlocks.size() == 0) {
                    scanResults.setText("Scan Failed: Found nothing to scan");
                } else {
                    scanResults.setText(scanResults.getText() + "Blocks: " + "\n");
                    scanResults.setText(scanResults.getText() + blocks + "\n");
                    scanResults.setText(scanResults.getText() + "---------" + "\n");
                    scanResults.setText(scanResults.getText() + "Lines: " + "\n");
                    scanResults.setText(scanResults.getText() + lines + "\n");
                    scanResults.setText(scanResults.getText() + "---------" + "\n");
                    scanResults.setText(scanResults.getText() + "Words: " + "\n");
                    scanResults.setText(scanResults.getText() + words + "\n");
                    scanResults.setText(scanResults.getText() + "---------" + "\n");
                }
            } else {
                scanResults.setText("Could not set up the detector!");
            }
        } catch (Exception e) {
            Toast.makeText(this, "Failed to load Image", Toast.LENGTH_SHORT)
                    .show();
            Log.e(LOG_TAG, e.toString());
        }
    }


}
