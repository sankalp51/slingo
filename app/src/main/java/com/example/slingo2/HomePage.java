package com.example.slingo2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {
    private ImageSlider imageSlider;
    AppCompatButton start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        imageSlider=findViewById(R.id.imageSlider);
        start=findViewById(R.id.startML);

        //create a list for images
        ArrayList<SlideModel> slideModels=new ArrayList();
        slideModels.add(new SlideModel("https://storage.googleapis.com/kagglesdsdata/datasets/3258/5337/amer_sign2.png?X-Goog-Algorithm=GOOG4-RSA-SHA256&X-Goog-Credential=databundle-worker-v2%40kaggle-161607.iam.gserviceaccount.com%2F20230315%2Fauto%2Fstorage%2Fgoog4_request&X-Goog-Date=20230315T084030Z&X-Goog-Expires=345600&X-Goog-SignedHeaders=host&X-Goog-Signature=0e2b8213e16cf8c54ab75534bb4385fdf8ce9f02ac8133e39f71f86ac23706ef51f621cf065a4b87bc202c7e806b5dfddf8c952509455f116509c35e25d80ed0410a93048e207211559aaf1df074e58cba4962a001418a05610b271eeeb121088fe18df2aa19afdb7e791770ec0412ff759898b705a604e4fc7cb275e4de59cc6a172e77f603c1a6685281590cde6d98b182bec9f5b99eac2032f66d74d881ff1a151495a722a5631801ba4af2d4f1342ca4dd229f7b09a60758c0c31b96d9e857066bea598217d215c72fd67e42bed0b92e6a3d91e86e4998282e48426f7acf2776d82c3ab4bd8b8cb26b08d3a9921ba868b00958344364472dcbc87c67ea7c", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://media.istockphoto.com/id/1056692270/photo/two-young-woman-speak-in-sign-language.jpg?s=612x612&w=0&k=20&c=-lTCGlavZnMbI24hIOugy92zr_0SvZ9qVSWIPR_QLZw=", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://media.istockphoto.com/id/1125750539/photo/learning-sign-language.jpg?s=612x612&w=0&k=20&c=e5yA9pBjv4KPNuAts1JUtIlSHIZTc1N3u4tSRJWctTs=", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://media.istockphoto.com/id/1135080982/photo/teenage-boy-and-girl-having-conversation-using-sign-language.jpg?s=612x612&w=0&k=20&c=6L8_aB6isPOohJicWcHGwJOLgrVvmJOwq_ny5Hl1ig0=", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://media.istockphoto.com/id/1349418242/photo/woman-talking-on-sign-language.jpg?s=612x612&w=0&k=20&c=2YJhJgKrLJqJGn6Tzk0zKz1cYEmq_WbgJe10Ii-BB0s=", ScaleTypes.FIT));

        imageSlider.setImageList(slideModels,ScaleTypes.FIT);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomePage.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }
}