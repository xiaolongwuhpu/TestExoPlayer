package com.example.testexoplayer.base;

public interface BasePlayer {
     void play();

     void resume();

     void pause();

     void stop();

     void release();

     void setUrl();

     void setLooping(boolean loop);

     void seek(long position);

     long getDuration(long position);

     long getVolume();

     void setVolume(long position);


}
