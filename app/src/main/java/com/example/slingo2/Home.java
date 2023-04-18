package com.example.slingo2;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;


public class Home extends Fragment {

    private ImageSlider imageSlider;
    AppCompatButton start;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        imageSlider = view.findViewById(R.id.imageSlider);
        start = view.findViewById(R.id.startML);
        ArrayList<SlideModel> slideModels=new ArrayList();
        slideModels.add(new SlideModel("https://storage.googleapis.com/kagglesdsdata/datasets/3258/5337/amer_sign2.png?X-Goog-Algorithm=GOOG4-RSA-SHA256&X-Goog-Credential=databundle-worker-v2%40kaggle-161607.iam.gserviceaccount.com%2F20230417%2Fauto%2Fstorage%2Fgoog4_request&X-Goog-Date=20230417T214812Z&X-Goog-Expires=345600&X-Goog-SignedHeaders=host&X-Goog-Signature=1a7202cc657017463ad9287907a4d0a847b0e5965d999148340edaf7699f55d4032dcf11240dcd36f3890e2a0ba4448c48eaa6bd2fad748cf3379895ab445f0f3c0d84d60f6c8e5e73120878f562f47bf25f910aded8a013f35b0142230ba34f79dfc699c779e11f948776dc58042c2ee04609e15e259339d8bf68c1e7473441f83f7ebf16e046411dcfead0778baa78d08f875dd2890ece05f87e2d739de60fe27af3f1eb86b9e0505ef7820abc3c373baca26be07eca78fa6baa8ccb8516bab6d1a9d6af6ba974468a0c888973e6922ac1ec1a68bbd5d688804e0403debc5a55c67ff680d1ec3610d6ed39a298a70ff771d9679e1a90a1986847fd9bdc55fb", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://media.istockphoto.com/id/1056692270/photo/two-young-woman-speak-in-sign-language.jpg?s=612x612&w=0&k=20&c=-lTCGlavZnMbI24hIOugy92zr_0SvZ9qVSWIPR_QLZw=", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://media.istockphoto.com/id/1125750539/photo/learning-sign-language.jpg?s=612x612&w=0&k=20&c=e5yA9pBjv4KPNuAts1JUtIlSHIZTc1N3u4tSRJWctTs=", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://media.istockphoto.com/id/1135080982/photo/teenage-boy-and-girl-having-conversation-using-sign-language.jpg?s=612x612&w=0&k=20&c=6L8_aB6isPOohJicWcHGwJOLgrVvmJOwq_ny5Hl1ig0=", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://media.istockphoto.com/id/1349418242/photo/woman-talking-on-sign-language.jpg?s=612x612&w=0&k=20&c=2YJhJgKrLJqJGn6Tzk0zKz1cYEmq_WbgJe10Ii-BB0s=", ScaleTypes.FIT));
        imageSlider.setImageList(slideModels,ScaleTypes.FIT);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),SignInterpreter.class);
                startActivity(intent);
            }
        });

        return view;
    }
}