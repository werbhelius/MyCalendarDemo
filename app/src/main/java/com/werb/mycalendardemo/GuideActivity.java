package com.werb.mycalendardemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.werb.mycalendardemo.utils.PrefUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 导航页
 * Created by acer-pc on 2016/3/9.
 */
public class GuideActivity extends AppCompatActivity {

    @Bind(R.id.vp_guide)
    ViewPager vpGuide;
    @Bind(R.id.btn_start)
    Button btnStart;

    private static final int[] mImageId = new int[]{R.mipmap.guide1, R.mipmap.guide2, R.mipmap.guide3};//三个导航页的图片
    private List<ImageView> mImageList;//存放导航图片

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_guide);

        ButterKnife.bind(this);

        initView();

        vpGuide.setAdapter(new GuideAdapter());
        vpGuide.addOnPageChangeListener(new GuidePageChangeListener());


    }

    @OnClick(R.id.btn_start) void start() {
        PrefUtils.setBoolean(GuideActivity.this, "isFirstIn", false);
        startActivity(new Intent(GuideActivity.this,MainActivity.class));
        finish();
    }

    /**
     * 初始化三个viewPager
     */
    private void initView(){
        mImageList =new ArrayList<>();

        for (int i=0;i<mImageId.length;i++){
            ImageView imageView=new ImageView(this);
            Picasso.with(this).load(mImageId[i]).into(imageView);
//            imageView.setBackgroundColor(Color.BLUE);
            mImageList.add(imageView);
        }
    }

    /**
     * ViewPager滑动监听
     */
    class GuidePageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if(position==mImageId.length-1){
                btnStart.setVisibility(View.VISIBLE);
            }else {
                btnStart.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }


    }

    /**
     * ViewPager数据适配器
     */
    class GuideAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mImageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mImageList.get(position));
            return mImageList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mImageList.get(position));
        }
    }
}
