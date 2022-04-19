####高仿相册可拖拽图片
###### 正常我们在使用缩放图片跟显示长图的使用都是用到PhotoView跟SubsamplingScaleImageView这2个控件，我在github逛了一圈，都存在2种问题；
其一：都是基于上面2个控件进行开发，会导致出现新的缩放控件或者更新控件会导致代码开发不动
其二：虽然他们都能拖拽，但是在拖拽返回的时候，由于显示是全屏，列表展示是正方形，导致图片返回会变形或者大小不一致，导致会闪一下。
看了下iOS的相册，跟小米的相册，他们在拖拽返回不存在这个问题，体验非常好；

所以自己开发一个，先开效果图
![1650361076188_1650361092587.gif](https://upload-images.jianshu.io/upload_images/6835615-63b44adaa32c4d8f.gif?imageMogr2/auto-orient/strip)


## 优点：
1、不对任何缩放图片控件进行封装，我们只封装拖拽功能，也就是说PhotoView跟SubsamplingScaleImageView需要自己写在自己项目上（不会使用的可以参考demo）。
2、拖拽返回的时候，图片不会变形，也不存在大小不一致等问题。
3、支持ViewPage跟ViewPage2随意切换，2者的不同大家都知道就是Fragment生命周期不一样
## 依赖：
```
 implementation 'com.github.yanxuwen:DragViewLayout:1.0.3'       
```     
### 使用方法
```
List<PictureData> listdata = new ArrayList<>();
List<Class<? extends Fragment>> listfragemnt = new ArrayList<>();
views.add(v1);
listfragemnt.add(MyFragment3.class);
PictureData pictureData = new PictureData(R.mipmap.longphoto, "长图");
listdata.add(pictureData);
//启动
 new DragViewDialog.Builder(this)
                .setData(listdata, listfragemnt)//设置数据
                .setViewPage2(true)//是否支持ViewPage2
                .setTransparentView(true)//是否挖空视图
                .setBackgroundColor(Color.parseColor("#333333"))//背景图
                .setListener(new Listener<PictureData>() {//监听器
                    /**
                     * 联动View,默认为null，则不能随意拖拽效果，只能上下滑动关闭（类似 
                     * 与今日头条效果）
                     * 因为找不可以联动的View,
                     */
                    @Override
                    public View getCurView(int position, PictureData pictureData) {
                        return views.get(position);
                    }
                })
                .show();
```

### 请前往github  [点击跳转](https://www.jianshu.com/p/bf2e6e5a3ba0)
### 喜欢就在 github star下,非常感谢o(∩_∩)o~~~








