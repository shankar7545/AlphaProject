package com.example.alpha.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.request.RequestOptions;
import com.example.alpha.Activity.ChainActivity;
import com.example.alpha.Course.CourseActivity;
import com.example.alpha.Plan.OnBoardActivity;
import com.example.alpha.R;
import com.example.alpha.Tutorials.TutorialActivity;
import com.example.alpha.Wallet.walletActivity;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.animations.DescriptionAnimation;
import com.glide.slider.library.slidertypes.BaseSliderView;
import com.glide.slider.library.slidertypes.TextSliderView;
import com.glide.slider.library.tricks.ViewPagerEx;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;


public class HomeFragment extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {


    private String selfUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private View mView;

    //Functions

    private LinearLayout walletLayout, SecurityLayout, ReferLayout, courseLayout;


    private LinearLayout layoutOne, tutorialsLayout;
    private SliderLayout mDemoSlider;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);


        //CheckingLevelUpgrades();
        //loadTransactions();
        initComponent();
        FunctionOnclick();
        levelOnCLick();
        Slider();
        return mView;

    }

    private void levelOnCLick() {

        mView.findViewById(R.id.beginnerPlanLayout).setOnClickListener(v -> startActivity(new Intent(getContext(), OnBoardActivity.class)));
    }

    private void initComponent() {

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Users");
        tutorialsLayout = mView.findViewById(R.id.tutorialsLayout);
        tutorialsLayout.setOnClickListener(v -> startActivity(new Intent(getContext(), TutorialActivity.class)));




    }

    private void FunctionOnclick() {
        walletLayout = mView.findViewById(R.id.walletLayout);
        walletLayout.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), walletActivity.class);
            startActivity(intent);
        });
        SecurityLayout = mView.findViewById(R.id.securityLayout);
        SecurityLayout.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), ChainActivity.class));
        });

        mView.findViewById(R.id.referLayout).setOnClickListener(v -> {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Alpha");
                String shareMessage = "\nInterested to earn Money? Want to make some cash out of it?? Try out DreamWinner, an eSports Platform. Join Daily PUBG Matches & Get Rewards on Each Kill you Score. Get Huge Prize on Getting Chicken Dinner. Just Download the DreamWinner Android App & Register and Prove your Skills \n\n";
                shareMessage = shareMessage + "Download Link:\n" + "\n https://dreamwinner.in";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch (Exception e) {
                //e.toString();
            }
        });


        mView.findViewById(R.id.courseLayout).setOnClickListener(v -> startActivity(new Intent(getActivity(), CourseActivity.class)));
    }


    private void Slider() {
        mDemoSlider = mView.findViewById(R.id.slider);

        ArrayList<String> listUrl = new ArrayList<>();
        //ArrayList<String> listName = new ArrayList<>();



        //image2
        listUrl.add("https://www.khelaghorbd.in/imagesTesting/crowdfunding.jpg");
        // listName.add("");

        //image4
        listUrl.add("https://www.khelaghorbd.in/imagesTesting/crowdfunding_two.jpg");
        //listName.add("");

        //image3
        listUrl.add("https://www.khelaghorbd.in/imagesTesting/crowdfunding_three.jpg");
        //listName.add("");

        //image1
        listUrl.add("https://firebasestorage.googleapis.com/v0/b/jobtrackingsystem-83bad.appspot.com/o/image4.jpg?alt=media&token=e8788eef-f8fb-4c86-be13-2d0258d43527");
        //listName.add("");


        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop();

        for (int i = 0; i < listUrl.size(); i++) {
            TextSliderView sliderView = new TextSliderView(getContext());
            // if you want show image only / without description text use DefaultSliderView instead

            // initialize SliderLayout
            BaseSliderView baseSliderView = sliderView
                    .image(listUrl.get(i))
                    //.description(listName.get(i))
                    .setRequestOption(requestOptions)
                    .setProgressBarVisible(true)
                    .setOnSliderClickListener(this);

            //add your extra information
            sliderView.bundle(new Bundle());
            // sliderView.getBundle().putString("extra", listName.get(i));
            mDemoSlider.addSlider(sliderView);
        }

        // set Slider Transition Animation
        //mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);
        mDemoSlider.stopCyclingWhenTouch(false);
    }


    @Override
    public void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

        //Toast.makeText(getContext(), slider.getBundle().getString("extra") + "", Toast.LENGTH_SHORT).show();
        //Intent intent = new Intent(getContext(), JobsActivity.class);
        //startActivity(intent);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}