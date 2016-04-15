package com.sf.test;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ForwardingControllerListener;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.sf.R;
import com.sf.fresco.CustomProgressBar;
import com.sf.fresco.FrescoConfigHelper;
import com.sf.okhttp.CallBackResult;
import com.sf.okhttp.ConnFormat;
import com.sf.okhttp.HttpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 测试Fresco 图片加载
 */
public class TestFrescoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_fresco_iamge);

        //initView();
        //init();
        testHttp();
    }

    private void testHttp() {
        Button bt = (Button) findViewById(R.id.bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("remind", "remember-me");
                map.put("name", "umu052501@163.com");
                map.put("passwd", "umu123");

                HttpUtil.httpPost("http://tapp.umu.cn/ajax/loginVsPass", map, new CallBackResult() {
                    @Override
                    public void sendSuccess(ConnFormat connFormat) {
                        Toast.makeText(TestFrescoActivity.this, connFormat.code + "-" + connFormat.result, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void sendFailure(ConnFormat connFormat) {
                        Toast.makeText(TestFrescoActivity.this, connFormat.code + "-" + connFormat.result, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


    }

    private void init() {
        String s = "https://raw.githubusercontent.com/facebook/fresco/gh-pages/static/fresco-logo.png";
        List<String> images = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            images.add(s);
        }

        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(new LAdapter(this, images));
    }


    private void initView() {
        Uri uri = Uri.parse("https://raw.githubusercontent.com/facebook/fresco/gh-pages/static/fresco-logo.png");
        SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.my_image_view);
        Button button = (Button) findViewById(R.id.bt);

        final GenericDraweeHierarchy genericDraweeHierarchy = FrescoConfigHelper.initGenericDrawHierarchy();
   /*     RoundingParams roundingParams = new RoundingParams();
        roundingParams.setCornersRadius(10);
        genericDraweeHierarchy.setRoundingParams(roundingParams);*/
        genericDraweeHierarchy.setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);
        genericDraweeHierarchy.setProgressBarImage(new CustomProgressBar());

        /*draweeView.setHierarchy(genericDraweeHierarchy);
        draweeView.setImageURI(uri);
        draweeView.setBackgroundResource(R.drawable.umu_logo);
        draweeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        /**
         * 渐进式加载
         */
        ImageRequest request = ImageRequestBuilder
                .newBuilderWithResourceId(R.drawable.mm)
                .setProgressiveRenderingEnabled(true)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder().setImageRequest(request).setAutoPlayAnimations(false).build();
        ImagePipelineFactory imagePipelineFactory = Fresco.getImagePipelineFactory();
        draweeView.setController(controller);


/*        final PipelineDraweeControllerBuilder drawBuilder = FrescoConfigHelper.getDrawController();
        drawBuilder.setUri(uri);
        draweeView.setController(drawBuilder.build());
        drawBuilder.setTapToRetryEnabled(true);*/

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    class LAdapter extends BaseAdapter {

        private List<String> images;
        private LayoutInflater inflater;

        public LAdapter(Context context, List<String> images) {
            this.images = images;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.adapter_images_test_item, null);
                viewHolder.textView = (TextView) convertView.findViewById(R.id.item_tv);
                viewHolder.imageView = (SimpleDraweeView) convertView.findViewById(R.id.item_image);
                //FrescoConfigHelper.setImageBaseGenericDrawHierarchy(viewHolder.imageView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.textView.setText(images.get(position));
            //FrescoConfigHelper.setImageRedMeshPostprocessor(viewHolder.imageView,images.get(position));
            viewHolder.imageView.setImageURI(Uri.parse(images.get(position)));
            ;
            return convertView;
        }

        public class ViewHolder {
            public TextView textView;
            public SimpleDraweeView imageView;

        }

    }

}
