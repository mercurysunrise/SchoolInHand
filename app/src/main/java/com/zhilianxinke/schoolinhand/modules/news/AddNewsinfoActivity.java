package com.zhilianxinke.schoolinhand.modules.news;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.view.ViewGroup.LayoutParams;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zhilianxinke.schoolinhand.AppContext;
import com.zhilianxinke.schoolinhand.R;
import com.zhilianxinke.schoolinhand.base.BaseActivity;
import com.zhilianxinke.schoolinhand.domain.AppNews;
import com.zhilianxinke.schoolinhand.domain.SdkHttpResult;
import com.zhilianxinke.schoolinhand.modules.photos.AlbumActivity;
import com.zhilianxinke.schoolinhand.modules.photos.GalleryActivity;
import com.zhilianxinke.schoolinhand.modules.photos.util.Bimp;
import com.zhilianxinke.schoolinhand.modules.photos.util.FileUtils;
import com.zhilianxinke.schoolinhand.modules.photos.util.ImageItem;
import com.zhilianxinke.schoolinhand.modules.photos.util.Res;
import com.zhilianxinke.schoolinhand.rongyun.ui.WinToast;
import com.zhilianxinke.schoolinhand.util.StaticRes;
import com.zhilianxinke.schoolinhand.util.UrlBuilder;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.utils.FileUtil;


public class AddNewsinfoActivity  extends Activity {

    private static final String TAG = "AddNewsinfoActivity";

    private EditText etNewsTitle;
    private EditText tmlNewsContent;
    private Button btnAddNewsSubmit;
    private Spinner spKey;
    private GridView noScrollgridview;
    private GridAdapter adapter;
    private View parentView;
    private PopupWindow pop = null;
    private LinearLayout ll_popup;

    public static Bitmap bimap;

    static final String end = "\r\n";
    static final String Hyphens = "--";

    public RequestQueue requestQueue;

    AppNews appNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Res.init(this);
        bimap = BitmapFactory.decodeResource(
                getResources(),
                R.drawable.icon_addpic_unfocused);

        parentView = getLayoutInflater().inflate(R.layout.activity_add_newsinfo, null);
        setContentView(parentView);

        requestQueue = Volley.newRequestQueue(this);

        etNewsTitle = (EditText) findViewById(R.id.etNewsTitle);
        tmlNewsContent = (EditText) findViewById(R.id.tmlNewsContent);
        btnAddNewsSubmit = (Button) findViewById(R.id.btnAddNewsSubmit);
        spKey = (Spinner)findViewById(R.id.spKey);
        List<String> strKeys = new ArrayList<String>();
        strKeys.add("活动");
        strKeys.add("公告");
        strKeys.add("相册");
        strKeys.add("分享");

        ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,strKeys);
        spKey.setAdapter(spAdapter);

        pop = new PopupWindow(AddNewsinfoActivity.this);
        View view = getLayoutInflater().inflate(R.layout.item_popupwindows, null);

        ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);

        pop.setWidth(LayoutParams.MATCH_PARENT);
        pop.setHeight(LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);

        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
        Button bt1 = (Button) view
                .findViewById(R.id.item_popupwindows_camera);
        Button bt2 = (Button) view
                .findViewById(R.id.item_popupwindows_Photo);
        Button bt3 = (Button) view
                .findViewById(R.id.item_popupwindows_cancel);

        parent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                photo();
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AddNewsinfoActivity.this,
                        AlbumActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });

        noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new GridAdapter(this);
        adapter.update();
        noScrollgridview.setAdapter(adapter);
        noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                //得到InputMethodManager的实例
                if (imm.isActive()) {
                    //如果开启
                    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
                    //关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
                }

                if (arg2 == Bimp.tempSelectBitmap.size()) {
                    Log.i("ddddddd", "----------");
                    ll_popup.startAnimation(AnimationUtils.loadAnimation(AddNewsinfoActivity.this, R.anim.activity_translate_in));
                    pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                } else {
                    Intent intent = new Intent(AddNewsinfoActivity.this,
                            GalleryActivity.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", arg2);
                    startActivity(intent);
                }
            }
        });

        btnAddNewsSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        AddNewsinfoActivity.this.sendAppNews();
                    }
                }).start();
            }

            ;
        });
    }

    public void sendAppNews() {
        String boundary = "----=_Part_8_1780217283." + Calendar.getInstance().getTimeInMillis();
        try {
            final String httpUrl = UrlBuilder.baseUrl + UrlBuilder.Api_addNews;
            URL url = new URL(httpUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            /* 允许Input、Output，不使用Cache */
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setRequestMethod("POST");
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            /* 设定DataOutputStream */
            DataOutputStream ds = new DataOutputStream(con.getOutputStream());
            //1 authorId
            writeString(ds, boundary, "authorId", AppContext.getAppUser().getId());
            //2 title
            writeString(ds, boundary, "title", etNewsTitle.getText().toString());
            //3 key
            String key = spKey.getSelectedItem().toString();
            writeString(ds, boundary, "key", key);
            //4 content
            writeString(ds, boundary, "content", tmlNewsContent.getText().toString());

            for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
                ImageItem imageItem = Bimp.tempSelectBitmap.get(i);
                writeFile(ds, boundary, i, imageItem);
            }
           //分隔符
            ds.writeBytes(Hyphens + boundary + Hyphens + end);

            ds.flush();


            Log.i("addAppNews",ds.toString());
            /* 取得Response内容 */
            InputStream is = con.getInputStream();
            int ch;
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            ds.close();
            SdkHttpResult sdkHttpResult = JSON.parseObject(b.toString(),SdkHttpResult.class);
            if(sdkHttpResult.getCode() == 200){
                finish();
            }else{
                WinToast.toast(this,sdkHttpResult.getResult());
            }

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



    private static void writeFile( DataOutputStream ds,String boundary,int index,ImageItem imageItem)
            throws IOException, FileNotFoundException {
        //分隔符
        ds.writeBytes(Hyphens + boundary + Hyphens + end);

        String thumbnail_fileName = "thumbnail_"+ index + ".png";
        ds.writeBytes("Content-Transfer-Encoding: binary"+end);
        ds.writeBytes("Content-Type: application/octet-stream; name="+thumbnail_fileName+end);
        ds.writeBytes("Content-Disposition: form-data; name=\""+thumbnail_fileName+"\"; filename=\""+thumbnail_fileName+"\""+end);
        ds.writeBytes(end);

        ds.write(Bimp.bitmap2Bytes(imageItem.getBitmap()));
        ds.writeBytes(end);

        //分隔符
        ds.writeBytes(Hyphens + boundary + Hyphens + end);

        String fileName = index + ".png";
        ds.writeBytes("Content-Transfer-Encoding: binary"+end);
        ds.writeBytes("Content-Type: application/octet-stream; name="+fileName+end);
        ds.writeBytes("Content-Disposition: form-data; name=\"" + fileName + "\"; filename=\"" + fileName + "\"" + end);
        ds.writeBytes(end);

        FileInputStream fis = new FileInputStream(imageItem.getImagePath());
        byte[] b=new byte[fis.available()];//新建一个字节数组
        fis.read(b);//将文件中的内容读取到字节数组中
        fis.close();

        ds.write(b);
        ds.writeBytes(end);
    }

    private static void writeString( DataOutputStream ds,String boundary,String paramName,String content) throws IOException {
        //分隔符
        ds.writeBytes(Hyphens + boundary + Hyphens + end);

        ds.writeBytes("Content-Transfer-Encoding: 8bit"+end);
        ds.writeBytes("Content-Type: text/plain; charset=UTF-8"+end);
        ds.writeBytes("Content-Disposition: form-data;name=\""+paramName+"\""+end);
        ds.writeBytes(end);

        ds.write(content.getBytes("UTF-8"));
        ds.writeBytes(end);
    }


    @SuppressLint("HandlerLeak")
    public class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private int selectedPosition = -1;
        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void update() {
            loading();
        }

        public int getCount() {
            if (Bimp.tempSelectBitmap.size() == 9) {
                return 9;
            }
            return (Bimp.tempSelectBitmap.size() + 1);
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int arg0) {
            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_published_grida,
                        parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView
                        .findViewById(R.id.item_grida_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position == Bimp.tempSelectBitmap.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(
                        getResources(), R.drawable.icon_addpic_unfocused));
                if (position == 9) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
            }

            return convertView;
        }


        public class ViewHolder {
            public ImageView image;
        }

        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        adapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        public void loading() {
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        if (Bimp.max == Bimp.tempSelectBitmap.size()) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                            break;
                        } else {
                            Bimp.max += 1;
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    }
                }
            }).start();
        }

    }

    public String getString(String s) {
            String path = null;
            if (s == null)
                return "";
            for (int i = s.length() - 1; i > 0; i++) {
                s.charAt(i);
            }
            return path;
        }

        protected void onRestart() {
            adapter.update();
            super.onRestart();
        }

        private static final int TAKE_PICTURE = 0x000001;

        public void photo() {
            Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(openCameraIntent, TAKE_PICTURE);
        }

        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    if (Bimp.tempSelectBitmap.size() < 9 && resultCode == RESULT_OK) {

                        String fileName = String.valueOf(System.currentTimeMillis());
                        Bitmap bm = (Bitmap) data.getExtras().get("data");
                        FileUtils.saveBitmap(bm, fileName);

                        ImageItem takePhoto = new ImageItem();
                        takePhoto.setBitmap(bm);
                        Bimp.tempSelectBitmap.add(takePhoto);
                    }
                    break;
            }
        }


    }



