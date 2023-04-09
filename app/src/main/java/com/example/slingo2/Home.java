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
        slideModels.add(new SlideModel("https://storage.googleapis.com/kagglesdsdata/datasets/3258/5337/amer_sign2.png?X-Goog-Algorithm=GOOG4-RSA-SHA256&X-Goog-Credential=databundle-worker-v2%40kaggle-161607.iam.gserviceaccount.com%2F20230408%2Fauto%2Fstorage%2Fgoog4_request&X-Goog-Date=20230408T014712Z&X-Goog-Expires=345600&X-Goog-SignedHeaders=host&X-Goog-Signature=567755ff9c858e4f413424f037272411c6a3d98842b7bc24af138ccff2c2eda098e973897745b90f5d418de9226d831f3d4bf34e78ff2954771c6f13f74e78eb3278704e9a04e51c237bd166441302a44ca9a6920acf9595548f5789d43708a9e9331e439d8ca2bcca74b98cb5678086ad86cfa017ddb90a8ff23abe0fa4ecbc77ec09c5b05f651bbe3e21221070af949da384d9b8c9e132a3a12e754551b8949c0fb12f0de8b3fe8486c2bf14ea43a6ffafb56bf580f7bd23ee7de9c6071ddccdee670745418fb7f51759824e1a0b2845a9ca6563c59597679c7de760eea5c1bb2f01d17a542f3d17a99eca0306c6ecfc88c23c2d0647f29b6ee2044c98b558", ScaleTypes.FIT));
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