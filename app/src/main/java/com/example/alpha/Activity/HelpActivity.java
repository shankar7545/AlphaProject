package com.example.alpha.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.alpha.R;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class HelpActivity extends AppCompatActivity {

    LinearLayout expandOne, expandTwo;
    ImageButton img_btn_one, img_btn_two;
    private List<View> view_list = new ArrayList<>();
    private List<View> image_list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        img_btn_one = findViewById(R.id.img_btn_one);
        img_btn_two = findViewById(R.id.img_btn_two);


        expandOne = findViewById(R.id.expandOne);
        expandTwo = findViewById(R.id.expandTwo);

        view_list.add(findViewById(R.id.expandOne));
        view_list.add(findViewById(R.id.expandTwo));

        image_list.add(findViewById(R.id.img_btn_one));
        image_list.add(findViewById(R.id.img_btn_two));

        findViewById(R.id.contactLayout).setOnClickListener(v -> startActivity(new Intent(this, SupportActivity.class)));

        visibilityOff();
        initToolbar();

    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Help");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.onClickOne:
                //ViewAnimation.expand(expandOne);
                if (expandOne.getVisibility() == View.VISIBLE) {
                    expandOne.setVisibility(View.GONE);
                    img_btn_one.setImageResource(R.drawable.ic_arrow_right);
                    visibilityOff();

                } else {
                    visibilityOff();
                    expandOne.setVisibility(View.VISIBLE);
                    img_btn_one.setImageResource(R.drawable.ic_expand_arrow);


                }
                break;
            case R.id.onClickTwo:

                if (expandTwo.getVisibility() == View.VISIBLE) {
                    expandTwo.setVisibility(View.GONE);
                    img_btn_two.setImageResource(R.drawable.ic_arrow_right);
                    visibilityOff();

                } else {
                    visibilityOff();
                    expandTwo.setVisibility(View.VISIBLE);
                    img_btn_two.setImageResource(R.drawable.ic_expand_arrow);
                }
                break;

        }
    }

    private void visibilityOff() {

        for (View v : view_list) {
            v.setVisibility(View.GONE);
            img_btn_one.setImageResource(R.drawable.ic_arrow_right);
            img_btn_two.setImageResource(R.drawable.ic_arrow_right);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
