package com.example.yuekao;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.yuekao.bean.MenuInfo;
import com.example.yuekao.bean.NewWork;
import com.example.yuekao.utils.SeatInfo;
import com.google.gson.Gson;
import com.limxing.xlistview.view.XListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SecondActivity extends AppCompatActivity {

    private static final String TAG = "SecondActivity-->";
    private XListView xlistview;
    private ViewPager viewpager;
    private RadioGroup radiogroup;
    private List<String> imageadd;
    private int index=0;
    private boolean flag=false;
    private int count=1;
Handler handler=new Handler(){
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what){
            case 1:
                viewpager.setCurrentItem(index);
                break;
        }
    }
};
    private myadapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);

        xlistview = (XListView) findViewById(R.id.xlv);
        viewpager = (ViewPager) findViewById(R.id.viewpager2);
        radiogroup = (RadioGroup) findViewById(R.id.radiogroup2);
        imageadd = new ArrayList<String>();
        imageadd.add("http://imgs.juheapi.com/comic_xin/5559b86938f275fd560ad613.jpg");
        imageadd.add("http://imgs.juheapi.com/comic_xin/5559b86938f275fd560ad617.jpg");
        imageadd.add("http://imgs.juheapi.com/comic_xin/5559b86938f275fd560ad709.jpg");

        viewpager.setAdapter(new MyPagerAdapter());
        banner();
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                for (int i=0;i<imageadd.size();i++){
                    RadioButton button = (RadioButton) radiogroup.getChildAt(i);
                    if (i==position%imageadd.size()){
                        button.setChecked(true);
                    }
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
if (NewWork.isConnnent(this)) {
    try {
        read("http://apis.juhe.cn/cook/query.php?key=35f9f65629365ddde8321dce7aaa198f&pn=1&rn=10&menu=" + URLEncoder.encode("秘制红烧肉", "utf-8"));
    } catch (Exception e) {
        e.printStackTrace();
    }
}else {
    showe();
}
    }
    class MyPagerAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(SecondActivity.this);
            ImageLoader.getInstance().displayImage(imageadd.get(position % imageadd.size()),imageView);
            container.addView(imageView);
            return imageView;
        }
    }
    public void read(String path){
        new AsyncTask<String, Void, String>() {
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s!=null){
                    Gson gson=new Gson();
                    MenuInfo menuInfo = gson.fromJson(s, MenuInfo.class);
                    List<MenuInfo.ResultBean.DataBean> list = menuInfo.getResult().getData();
                    if (adapter==null){
                        adapter = new myadapter(list);
                        xlistview.setAdapter(adapter);
                    }else {
                        adapter.loader(list,flag);
                        adapter.notifyDataSetChanged();
                    }

                }
                xlistview.setPullLoadEnable(true);
                xlistview.setXListViewListener(new XListView.IXListViewListener() {
                    @Override
                    public void onRefresh() {
                        ++count;
                        try {
                            read("http://apis.juhe.cn/cook/query.php?key=35f9f65629365ddde8321dce7aaa198f&pn=1&rn=10&menu="+ URLEncoder.encode("秘制红烧肉","utf-8"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        flag=true;
                        xlistview.stopRefresh(true);
                    }
                    @Override
                    public void onLoadMore() {
                        ++count;
                        try {
                            read("http://apis.juhe.cn/cook/query.php?key=35f9f65629365ddde8321dce7aaa198f&pn=1&rn=10&menu="+ URLEncoder.encode("秘制红烧肉","utf-8"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        flag=false;
                        xlistview.stopLoadMore();
                    }
                });

            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    String path=params[0];
                    URL url=new URL(path);
                    HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    int code = connection.getResponseCode();
                    if (code==200){
                        InputStream is = connection.getInputStream();
                        return SeatInfo.add(is);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute(path);
    }

class myadapter extends BaseAdapter{
    DisplayImageOptions options=new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .build();
    List<MenuInfo.ResultBean.DataBean> list;

    public myadapter(List<MenuInfo.ResultBean.DataBean> list) {
        this.list = list;
    }
    public void loader(List<MenuInfo.ResultBean.DataBean> lists,boolean flag){
        for (MenuInfo.ResultBean.DataBean bean:lists
             ) {
            if (flag){
                list.add(0,bean);
            }else {
                list.add(bean);
            }
        }
    }

    @Override
    public int getCount() {

        return list!=null?list.size():0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position%2==0){
            return 0;
        }else {
            return 1;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        viewholder vh1=null;
        viewholder2 vh2=null;
        switch (type){
            case 0:
                if (convertView==null){
                    vh1=new viewholder();
                    convertView=convertView.inflate(SecondActivity.this,R.layout.item,null);
                    vh1.im1= (ImageView) convertView.findViewById(R.id.item1_img1);
                    vh1.im2= (ImageView) convertView.findViewById(R.id.item1_img2);
                    vh1.im3= (ImageView) convertView.findViewById(R.id.item1_img3);
                    convertView.setTag(vh1);
                }else {
                    vh1= (viewholder) convertView.getTag();
                }
                ImageLoader.getInstance().displayImage(list.get(position).getAlbums().get(0),vh1.im1,options);
                ImageLoader.getInstance().displayImage(list.get(position).getAlbums().get(0),vh1.im2,options);
                ImageLoader.getInstance().displayImage(list.get(position).getAlbums().get(0),vh1.im3,options);
                break;
            case 1:
                if (convertView==null){
                    vh2=new viewholder2();
                    convertView=convertView.inflate(SecondActivity.this,R.layout.item2,null);
                    vh2.im4= (ImageView) convertView.findViewById(R.id.ima4);
                    convertView.setTag(vh2);
                }else {
                    vh2= (viewholder2) convertView.getTag();
                }
                ImageLoader.getInstance().displayImage(list.get(position).getAlbums().get(0),vh2.im4,options);
                break;
        }
        return convertView;
    }
}
public  void banner(){
    new Timer(){}.schedule(new TimerTask() {
        @Override
        public void run() {
            ++index;
            handler.sendEmptyMessage(1);
        }
    },0,3000);
}
class viewholder{
    ImageView im1;
    ImageView im2;
    ImageView im3;
}
class viewholder2{
        ImageView im4;
    }

    public void showe(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("没有设置网");
        builder.setNegativeButton("取消",null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent();
                intent.setAction("android.settings.WIRELESS_SETTINGS");
                startActivity(intent);

            }
        });
        builder.create().show();
    }

}
