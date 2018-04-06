AVLib sdk demo
============


Examples for [AVLib sdk](https://rtmplib-android.com) usage.


AVLib library is powerful RTMP-streaming solution that provides following features:

* Ability to use with outdated and low-performance devices with Android 4.1+
* Auto-detection device performance and setting an optimal stream configuration
* Handling rotation and size changes with no stream re-initiation
* Ability to customize frame size, video codec quality, video and audio bitrate, and disable audio
* Activity and Fragment support
* Decreasing bitrate when connection is slow
* Switching the main/front cameras
* Scaling types, aspect ratio correction, and full handling lifecycle
* Handling errors
* Simple interface
* Fast performance
* No dependencies
* Armv7a and armv8-64 support
* Action cameras support
* “Tap to focus” feature

<br>

<h3>How to use:</h3>

- Contact with us for [AVLib sdk](https://rtmplib-android.com)

- Add [AVLib sdk](https://rtmplib-android.com) into your project (It's one aar file without any dependencies)

```gradle
dependencies {
	implementation(name: 'AVlib1.0.0', ext: 'aar')
}
```
- For broadcasting from android cam use CameraStreamer class. Create instance and attach 'broadcast view'.

Camera view from layout 

```java
<com.onix.avlib.view.CameraStreamView
    android:id="@+id/camera_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```


```java
@Override
public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mStreamer = new CameraStreamer();
    mStreamer.onCreate(getActivity(), savedInstanceState);
}
```

```java
@Override
public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_camera_streaming, container, false);
    mStreamer.onCreateView(mBinding.cameraView, PreviewScaleType.CENTER_CROP);
    return view;
}
```

- add some required lifecycle method calls. onPause, onResume, onDestroy, onConfigurationChanged, onSaveInstanceState. For example:


```java
@Override
public void onPause() {
    super.onPause();
    mStreamer.onPause();
}

...
```

- Add some customization if need: setVideoBitrate(VideoBitrate bitrate), setAudioBitrate(AudioBitrate bitrate), setPreviewSize(PreviewSize size)


```java
@Override
public void onInitCompleted() {
    mStreamer.setServerUrl(mServerUrl);
}
```

- Start/Stop broadcasting: 

```java
mStreamer.startStreaming();
```

```java
mStreamer.stopStreaming();
```

<h3>Learn More</h3>

[AVLib sdk developer's guide](AVLib-sdk%20developer's%20guide.pdf)

[Privacy Policy](AVLib-sdk-licence.pdf)

[Official website](https://rtmplib-android.com) 

Copyright © 2017 [Onix-Systems](https://onix-systems.com/), LLC, All Rights Reserved. 
