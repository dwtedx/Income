package com.dwtedx.income.topic;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.connect.ProgressDialog;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.connect.SaException;
import com.dwtedx.income.entity.ApplicationData;
import com.dwtedx.income.entity.DiTopic;
import com.dwtedx.income.entity.DiTopicimg;
import com.dwtedx.income.entity.DiTopicvote;
import com.dwtedx.income.scan.ChooseLocationActivity;
import com.dwtedx.income.service.TopicService;
import com.dwtedx.income.topic.adapter.AddTopicImgRecyclerAdapter;
import com.dwtedx.income.topic.adapter.AddTopicVoteRecyclerAdapter;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.utility.CommonUtility;
import com.dwtedx.income.utility.ParseJsonToObject;
import com.dwtedx.income.widget.AppTitleBar;
import com.dwtedx.income.widget.RecycleViewDivider;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddTopicActivity extends BaseActivity implements AppTitleBar.OnTitleClickListener, AddTopicImgRecyclerAdapter.OnAddPicClickListener, AddTopicImgRecyclerAdapter.OnItemClickListener, View.OnClickListener
{
    private static final int REQUEST_CODE_CHOOSE_LOCAL = 71;
    private static final int ACCESS_COARSE_LOCALHOST_REQUEST_CODE = 72;

    @BindView(R.id.m_app_title)
    AppTitleBar mAppTitle;
    @BindView(R.id.m_topic_content)
    EditText mTopicContent;
    @BindView(R.id.m_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.m_location_button)
    Button mLocationButton;
    @BindView(R.id.m_vote_button)
    Button mVoteButton;
    @BindView(R.id.m_vote_switch)
    Switch mVoteSwitch;
    @BindView(R.id.m_vote_recycler_view)
    RecyclerView mVoteRecyclerView;

    private ProgressDialog mProgressDialog;
    private DiTopic mDiTopic;

    //百度定位7.6相关
    public LocationClient mLocationClient;
    private MyLocationListener myListener;
    private List<String> mPoiList;

    //图片选择器
    List<LocalMedia> mLocalMediaList;
    //List<DiTopicimg> mDiTopicimgList;
    AddTopicImgRecyclerAdapter mAdapter;

    //投票选项
    private List<DiTopicvote> mDiTopicvoteList;
    AddTopicVoteRecyclerAdapter mAdapterVote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_topic);
        ButterKnife.bind(this);

        mAppTitle.setOnTitleClickListener(this);

        mProgressDialog = getProgressDialog();
        mProgressDialog.setCancelable(false);
        mDiTopic = new DiTopic();

        //定位
        mLocationButton.setOnClickListener(this);
        initLocation();

        mLocalMediaList = new ArrayList<LocalMedia>();
        mRecyclerView.setVisibility(View.VISIBLE);
        RecyclerView.LayoutManager layoutManagerHeader = new GridLayoutManager(this, 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mRecyclerView.setLayoutManager(layoutManagerHeader);
        mAdapter = new AddTopicImgRecyclerAdapter(this, mLocalMediaList, this);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

        //投票
        mVoteButton.setOnClickListener(this);
        mVoteSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mVoteRecyclerView.setVisibility(View.VISIBLE);
                }else{
                    mVoteRecyclerView.setVisibility(View.GONE);
                }
            }
        });
        mDiTopicvoteList = new ArrayList<>();
        mDiTopicvoteList.add(new DiTopicvote());
        mDiTopicvoteList.add(new DiTopicvote(CommonConstants.INCOME_SCAN_ADDBUTTON));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mVoteRecyclerView.setLayoutManager(layoutManager);
        //自定义分割线的样式
        mVoteRecyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL, 0, ContextCompat.getColor(this, R.color.common_division_line)));
        mAdapterVote = new AddTopicVoteRecyclerAdapter(this, mDiTopicvoteList);
        mVoteRecyclerView.setAdapter(mAdapterVote);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    mLocalMediaList.clear();
                    mLocalMediaList.addAll(PictureSelector.obtainMultipleResult(data));
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    mAdapter.notifyDataSetChanged();
                    break;

                case REQUEST_CODE_CHOOSE_LOCAL:
                    String poiLoca = data.getStringExtra("POI");
                    mLocationButton.setText(poiLoca);
                    mDiTopic.setLocation(poiLoca);
                    break;
            }
        }
    }

    @Override
    public void onTitleClick(int type) {
        switch (type){
            case AppTitleBar.OnTitleClickListener.TITLE_CLICK_LEFT:
                finish();
                break;

            case AppTitleBar.OnTitleClickListener.TITLE_CLICK_RIGHT:
                saveTopic();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.m_location_button:
                intent = new Intent(this, ChooseLocationActivity.class);
                intent.putExtra("POI", ParseJsonToObject.getJsonFromObjList(mPoiList).toString());
                startActivityForResult(intent, REQUEST_CODE_CHOOSE_LOCAL);
                break;

            case R.id.m_vote_button:
                mVoteSwitch.setChecked(!mVoteSwitch.isChecked());
                break;

        }
    }

    @Override
    public void onItemClick(int position, View v) {
        // 预览图片 可自定长按保存路径
        //PictureSelector.create(MainActivity.this).themeStyle(themeId).externalPicturePreview(position, "/custom_file", selectList);
        PictureSelector.create(AddTopicActivity.this).themeStyle(R.style.picture_default_style).openExternalPreview(position, mLocalMediaList);
    }


    private void saveTopic(){
        mTopicContent.requestFocus();//获取焦点 光标出现
        String desc = mTopicContent.getText().toString();
        if(CommonUtility.isEmpty(desc)){
            Toast.makeText(this, R.string.topic_add_tip_text_tip, Toast.LENGTH_SHORT).show();
            return;
        }
        //等待框
        mProgressDialog.show();

        mDiTopic.setUserid(ApplicationData.mDiUserInfo.getId());
        mDiTopic.setName(ApplicationData.mDiUserInfo.getName());
        mDiTopic.setDescription(desc);
        //上传图片
        if(null != mLocalMediaList && mLocalMediaList.size() > 0){
            mDiTopic.setTopicimg(new ArrayList<DiTopicimg>());
            LocalMedia localMedia = null;
            for(int i = 0; i < mLocalMediaList.size(); i++) {
                final int counti = i;
                localMedia = mLocalMediaList.get(i);
                // 1.media.getPath(); 为原图path
                // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                Uri uri = null;
                if(localMedia.isCompressed()){
                    uri = Uri.fromFile(new File(localMedia.getCompressPath()));
                }else{
                    uri = Uri.fromFile(new File(localMedia.getPath()));
                }
                Bitmap bitmap = CommonUtility.getScalingBitmap(uri, this);
                String imgData = CommonUtility.encodeTobase64(bitmap);
                SaDataProccessHandler<Void, Void, DiTopicimg> dataVerHandler = new SaDataProccessHandler<Void, Void, DiTopicimg>(this) {
                    @Override
                    public void onSuccess(DiTopicimg data) {
                        DiTopicimg topicimg = new DiTopicimg();
                        topicimg.setPath(data.getPath());
                        topicimg.setWidth(bitmap.getWidth());
                        topicimg.setHeight(bitmap.getHeight());
                        mDiTopic.getTopicimg().add(topicimg);
                        if(counti == (mLocalMediaList.size() - 1)){
                            postTopic();
                        }
                    }

                    @Override
                    public void onPreExecute() { }

                    @Override
                    public void handlerError(SaException e) {
                        super.handlerError(e);
                        mProgressDialog.dismiss();
                    }
                };
                TopicService.getInstance().uploadImg(imgData, dataVerHandler);
            }
        }else{
            postTopic();
        }
    }

    private void postTopic(){
        //投票
        mDiTopic.setTopicvote(mAdapterVote.getTotalVotes());
        //保存
        SaDataProccessHandler<Void, Void, Void> dataVerHandler = new SaDataProccessHandler<Void, Void, Void>(this) {
            @Override
            public void onSuccess(Void data) {
                Toast.makeText(AddTopicActivity.this, R.string.topic_add_send_success_top, Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
                TopicFragment.isRefresh = true;
                AddTopicActivity.this.finish();
            }

            @Override
            public void onPreExecute() { }

            @Override
            public void handlerError(SaException e) {
                super.handlerError(e);
                mProgressDialog.dismiss();
            }
        };
        TopicService.getInstance().seveTopic(mDiTopic, dataVerHandler);
    }

    @Override
    public void onAddPicClick() {
        // 进入相册 以下是例子：用不到的api可以不写
        PictureSelector.create(AddTopicActivity.this)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_income_style)//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .maxSelectNum(9)// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .previewVideo(false)// 是否可预览视频 true or false
                .enablePreviewAudio(false) // 是否可播放音频 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .setOutputCameraPath("/Income/Images")// 自定义拍照保存路径,可不填
                .enableCrop(false)// 是否裁剪 true or false
                .compress(false)// 是否压缩 true or false
                .glideOverride(160, 160)// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示 true or false
                .isGif(true)// 是否显示gif图片 true or false
                //.compressSavePath("/Income/Images")//压缩图片保存地址
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .openClickSound(false)// 是否开启点击声音 true or false
                .selectionMedia(mLocalMediaList)// 是否传入已选图片 List<LocalMedia> list
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .cropCompressQuality(90)// 裁剪压缩质量 默认90 int
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .cropWH(500, 500)// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
                .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                .videoQuality(1)// 视频录制质量 0 or 1 int
                .videoMaxSecond(15)// 显示多少秒以内的视频or音频也可适用 int
                .videoMinSecond(10)// 显示多少秒以内的视频or音频也可适用 int
                .recordVideoSecond(30)//视频秒数录制 默认60s int
                .isDragFrame(false)// 是否可拖动裁剪框(固定)
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    ///////////////定位相关//////////////////////////////定位相关//////////////////////////////定位相关//////////////////////////////定位相关//////////////////////////////定位相关///////////////
    private void initLocation() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, ACCESS_COARSE_LOCALHOST_REQUEST_CODE);
        } else {
            initLocationCode();
        }
    }

    private void initLocationCode() {
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        myListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数
        //第三步，配置定位SDK参数
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        option.setIsNeedLocationPoiList(true);
        //可选，是否需要周边POI信息，默认为不需要，即参数为false
        //如果开发者需要获得周边POI信息，此处必须为true
        mLocationClient.setLocOption(option);
        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
        mLocationClient.start();
    }

    public class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //保存经纬度和
            mDiTopic.setLatitude(String.valueOf(location.getLatitude()));
            mDiTopic.setLongitude(String.valueOf(location.getLongitude()));
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取周边POI信息相关的结果
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
            if(null == location){
                Toast.makeText(AddTopicActivity.this, R.string.scan_location_tip, Toast.LENGTH_SHORT).show();
                return;
            }
            String cityStr = "中国";
            if (!CommonUtility.isEmpty(location.getCity())) {
                cityStr = location.getCity();
            }
            if (null != location.getPoiList() && location.getPoiList().size() > 0) {
                //位置
                mPoiList = new ArrayList<>();
                mPoiList.add(cityStr);
                for (Poi poi : location.getPoiList()) {
                    mPoiList.add(cityStr + "·" + poi.getName());
                }
                cityStr += "·" + location.getPoiList().get(0).getName();
            }
            mDiTopic.setLocation(cityStr);
            mLocationButton.setText(cityStr);
            //获取周边POI信息
            //POI信息包括POI ID、名称等，具体信息请参照类参考中POI类的相关说明
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ACCESS_COARSE_LOCALHOST_REQUEST_CODE) {
            try {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    initLocationCode();
                } else {
                    // Permission Denied
                    Toast.makeText(this, "访问被拒绝！会导致很多功能异常！请到设置里面开启", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "访问被拒绝！会导致很多功能异常！请到设置里面开启", Toast.LENGTH_SHORT).show();
            }
        }
    }

    ///////////////定位相关//////////////////////////////定位相关//////////////////////////////定位相关//////////////////////////////定位相关//////////////////////////////定位相关///////////////

}
