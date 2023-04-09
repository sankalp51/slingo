package com.example.slingo2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.slingo2.ml.Slingo;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SignInterpreter extends AppCompatActivity {
    Button camera,gallery;
    ImageView imageView;
    TextView result;
    int imageSize=300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_interpreter);
        camera=findViewById(R.id.button);
        gallery=findViewById(R.id.button2);
        result=findViewById(R.id.result);
        imageView=findViewById(R.id.imageView);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkSelfPermission(android.Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
                    Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent,3);
                }
                else{
                    requestPermissions(new String[]{Manifest.permission.CAMERA},100);
                }
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 1);
            }
        });
    }
    public void classifyImage(Bitmap image){
        try {
            Slingo model = Slingo.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 300, 300, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer=ByteBuffer.allocateDirect(4*imageSize*imageSize*3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues=new int[imageSize*imageSize];
            image.getPixels(intValues,0,image.getWidth(),0,0,image.getWidth(),image.getHeight());
            int pixel=0;
            for(int i=0;i<imageSize;i++){
                for(int j=0;j<imageSize;j++){
                    int val=intValues[pixel++];//RGB
                    byteBuffer.putFloat(((val>>16)&0xFF)*(1.f/1));
                    byteBuffer.putFloat(((val>>8)&0xFF)*(1.f/1));
                    byteBuffer.putFloat((val&0xFF)*(1.f/1));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Slingo.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences=outputFeature0.getFloatArray();
            //find the index of the class with the highest confidence
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }

            }
            String[] classes = {"A", "B", "C"};
            result.setText(classes[maxPos]);

            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //get picture from camera
        if(resultCode == RESULT_OK){
            if(requestCode==3){
                Bitmap image=(Bitmap) data.getExtras().get("data");
                int dimension=Math.min(image.getWidth(),image.getHeight());
                image= ThumbnailUtils.extractThumbnail(image,dimension,dimension);
                imageView.setImageBitmap(image);

                //resize
                image=Bitmap.createScaledBitmap(image,imageSize,imageSize,false);
                classifyImage(image);
            }else{//get picture from gallery
                Uri dat=data.getData();
                Bitmap image=null;
                try{
                    image=MediaStore.Images.Media.getBitmap(this.getContentResolver(),dat);
                }catch(IOException e){
                    e.printStackTrace();
                }
                imageView.setImageBitmap(image);
                image=Bitmap.createScaledBitmap(image,imageSize,imageSize,false);
                classifyImage(image);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}