## 1.流媒体协议简介

* #### DASH协议

  简述：MPEG-DASH协议是MPEG委员会提出，各大流媒体公司和组织共同制定的标准，全名**Dynamic Adaptive Streaming over HTTP**。目的是为Apple/Adobe/Microsoft等公司的码率自适应协议提供统一标准。大致内容可以看下第一部分中给出的三份白皮书。

  DASH的分片(Segment)中可以包含mp4文件(fMP4)，也可以包含TS文件，但整个标准更加prefer使用分片的mp4文件, 新标准中好像对3GPP协议也有支持，这个等看完再说。对于编码标准无指定。

  MPD文件中的一些关键名词定义（参考白皮书）：Periods（区段）、Adaptation Set（AS，自适应子集）、Representation（表示）、Segment（分片）、Subsegment（子分片）。其中一个MPD中包含一个或多个Periods，每个Periods包含一个或多个AS（每个AS对应一组可供切换的码流），一个AS包含一组Representation（每个Representation对应不同的分辨率或码率，保持不变），一个Representation内的内容被切分为多个Segment（方便在不同Segment间切换，粒度小），Segment可以进一步被切分为Subsegment。

* #### HLS协议

* ###

##  2.Exoplayer 概述

ExoPlayer是构建在Android低水平媒体API之上的一个应用层媒体播放器。和Android内置的媒体播放器相比，ExoPlayer有许多优点。ExoPlayer支持内置的媒体播放器支持的所有格式外加自适应格式DASH和SmoothStreaming。ExoPlayer可以被高度定制和扩展以适应不同的使用场景。

ExoPlayer库的核心是ExoPlayer接口。ExoPlayer公开了传统的高水平媒体播放器的功能，例如媒体缓冲，播放，暂停和快进功能。ExoPlayer实现旨在对正在播放的媒体类型，存储方式和位置以及渲染方式做出一些假设（因此几乎没有限制）。ExoPlayer没有直接实现媒体文件的加载和渲染，而是把这些工作委托给了在创建播放器或者播放器准备好播放的时候注入的组件。所有ExoPlayer实现的通用组件是：

-  `MediaSource`：媒体资源，用于定义要播放的媒体，加载媒体，以及从哪里加载媒体。简单的说，`MediaSource`就是代表我们要播放的媒体文件，可以是本地资源，可以是网络资源。`MediaSource`在播放开始的时候，通过`ExoPlayer.prepare`方法注入。
-  `Renderer`：渲染器，用于渲染媒体文件。当创建播放器的时候，`Renderers`被注入。
-  `TrackSelector`：轨道选择器，用于选择`MediaSource`提供的轨道（tracks），供每个可用的渲染器使用。
-  `LoadControl`：用于控制`MediaSource`何时缓冲更多的媒体资源以及缓冲多少媒体资源。`LoadControl`在创建播放器的时候被注入。

ExoPlayer库提供了在普通使用场景下上述组件的默认实现。ExoPlayer可以使用这些默认的组件，也可以使用自定义组件。例如可以注入一个自定义的`LoadControl`用来改变播放器的缓存策略，或者可以注入一个自定义渲染器以使用Android本身不支持的视频解码器。

## 3.优点和缺点

- 对于Android内置的MediaPlayer来说，ExoPlayer有以下几个优点：

  1. 支持DASH和SmoothStreaming这两种数据格式的资源，而MediaPlayer对这两种数据格式都不支持。它还支持其它格式的数据资源，比如MP4, M4A, FMP4, WebM, MKV, MP3, Ogg, WAV, MPEG-TS, MPEG-PS, FLV and ADTS (AAC)等
  2. 支持高级的HLS特性，比如能正确的处理#EXT-X-DISCONTINUITY标签
  3. 无缝连接，合并和循环播放多媒体的能力
  4. 和应用一起更新播放器（ExoPlayer）,因为ExoPlayer是一个集成到应用APK里面的库，你可以决定你所想使用的ExoPlayer版本，并且可以随着应用的更新把ExoPlayer更新到一个最新的版本。
  5. 较少的关于设备的特殊问题，并且在不同的Android版本和设备上很少会有不同的表现。
  6. 在Android4.4(API level 19)以及更高的版本上支持Widevine通用加密
  7. 为了符合你的开发需求，播放器支持自定义和扩展。其实ExoPlayer为此专门做了设计，并且允许很多组件可以被自定义的实现类替换。
  8. 使用官方的扩展功能可以很快的集成一些第三方的库，比如IMA扩展功能通过使用互动媒体广告SDK可以很容易地将视频内容货币化（变现）

  同样很重要的是ExoPlayer也有一些不足之处

  1. 比如音频在Android设备上的播放，ExoPlayer会比MediaPlayer消耗更多的电量。更多细节请参考[Battery consumption page](https://exoplayer.dev/battery-consumption.html)

  ## 4.使用

  ```
  implementation 'com.google.android.exoplayer:exoplayer:2.X.X'
  ```

  或者单独引用每个子库

  

  ```
  implementation 'com.google.android.exoplayer:exoplayer-core:2.X.X'
  implementation 'com.google.android.exoplayer:exoplayer-dash:2.X.X'
  implementation 'com.google.android.exoplayer:exoplayer-ui:2.X.X'
  
  exoplayer-core: 核心功能（必须）.
  exoplayer-dash: 支持DASH内容.
  exoplayer-hls: 支持HLS内容.
  exoplayer-smoothstreaming: 支持SmoothStreaming内容.
  exoplayer-ui: ExoPlayer所使用的UI组件和资源.
  ```

#### 1.创建播放器SimpleExoPlayer实例

```java
// 得到默认合适的带宽
BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
// 创建跟踪的工厂
TrackSelection.Factory factory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
// 创建跟踪器
DefaultTrackSelector trackSelection = new DefaultTrackSelector(factory);

// 创建播放器
SimpleExoPlayer exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelection);

// 第二种方式 传入了默认的渲染工厂（DefaultRenderersFactory），默认的轨道选择器（DefaultTrackSelector）和默认的加载控制器（DefaultLoadControl），然后把返回的播放器实例
SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(
         new DefaultRenderersFactory(context),
         new DefaultTrackSelector(), 
         new DefaultLoadControl());
```

#### 2.准备并开始播放

```java
// 生成数据媒体实例，通过该实例加载媒体数据
DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context, Util.getUserAgent(context, "exoplayerdemo"));
// 创建资源
Uri uri = Uri.parse(url);
MediaSource mediaSources = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
// 将播放器附加到view
mPlayerView.setPlayer(exoPlayer);
// 准备播放
exoPlayer.prepare(mediaSource);
// 准备好了之后自动播放，如果已经准备好了，调用该方法实现暂停、开始功能
exoPlayer.setPlayWhenReady(true);
```

#### **3、MediaSource**

- **LoopingMediaSource实现循环播放。**
- **ConcatenatingMediaSource**和**DynamicConcatenatingMediaSource**可以用来无缝的合并播放多个媒体资源。
- **MergingMediaSource**类似ConcatenatingMediaSource, 合并多个音频，多用于视频，可以合并视频+字幕等信息 需要在同一个timeline上，
  如果音频播放使用该类，也相当于添加到一个list中,使用player.prepare(MergingMediaSource) 也将循环播放，不能动态修改播放列表
- **ClippingMediaSource** 剪切功能，能够裁剪一个一段音频的区间
-  **HlsMediaSource**     主要是播放.m3u8格式
-  **DashMediaSource**  主要是播放.mpd格式
-  **SsMediaSource**       主要播放.ism/Manifest格式
-  **ProgressiveMediaSource**    2.10.0版本以后代替ExtractorMediaSource

```java

// 单个资源循环播放 可指定loopCount循环次数
MediaSource source = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
MediaSource mediaSource = new LoopingMediaSource(source, loopCount);

// 多个资源循环播放
MediaSource source = new ConcatenatingMediaSource(mediaSources);
MediaSource mediaSource = new LoopingMediaSource(source, loopCount);
```

```


ExoPlayer库提供了
```

