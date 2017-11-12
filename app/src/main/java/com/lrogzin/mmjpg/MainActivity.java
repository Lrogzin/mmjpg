package com.lrogzin.mmjpg;

import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Window;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private ArrayList<String> titleList;
    private ArrayList<String> category_url;
    private ArrayList<BaseFragment> fragmentList;
    private FragmentAdapter fragmentAdapter;
    NetData netData;
    private Map<String,String> categoryMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        CategoryTask task = new CategoryTask();
        task.execute();

    }

    private void initView() {
        tabLayout=findViewById(R.id.tlMain);
        viewPager=findViewById(R.id.vpMain);
        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        fragmentList = new ArrayList<BaseFragment>();
        for(int i=0;i<titleList.size();i++){
                    BaseFragment fragment = new BaseFragment().newInstance(titleList.get(i), category_url.get(i));
                    fragmentList.add(fragment);
                }
                fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), titleList, fragmentList);
                viewPager.setAdapter(fragmentAdapter);
                tabLayout.setupWithViewPager(viewPager, true);
                tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    public class CategoryTask extends AsyncTask<Void,Void,Map<String,String>> {
        @Override
        protected Map<String, String> doInBackground(Void... voids) {
            netData = new NetData();
            categoryMap = new LinkedHashMap<>();
            try {
                categoryMap = netData.getCategory();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return categoryMap;
        }

        @Override
        protected void onPostExecute(Map<String,String> categoryMap){
            super.onPostExecute(categoryMap);
            titleList = new ArrayList<>();
            category_url = new ArrayList<>();
            Iterator iter = categoryMap.entrySet().iterator();
            while(iter.hasNext()){
                Map.Entry entry = (Map.Entry) iter.next();
                titleList.add(entry.getKey().toString());
                category_url.add(entry.getValue().toString());
            }
            initView();
        }
    }
}
