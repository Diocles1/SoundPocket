package com.dynamicdusk.soundpocket;

import android.content.Context;

import java.util.Calendar;

public class LightSaber extends AccelerometerListener {


    public SoundPlayer soundPlayer;
    private long timeStamp =0;
    private long now =0;
    private long sideMove =0;
    private long hitTime =0;
    private long moveStamp;
    private boolean isOn = false;
    private long uppAcc = 0;
    private long downAcc = 0;

    public LightSaber(){
        super.xAccThreshold = 30;
        super.yAccThreshold = 22;
        super.zAccThreshold = 12;
        super.xGyroThreshold = 2f;
        super.yGyroThreshold = 2f;
        super.zGyroThreshold = 2f;

        timeStamp = Calendar.getInstance().getTimeInMillis();
        moveStamp = Calendar.getInstance().getTimeInMillis();
    }

    public void setSoundPlayer(SoundPlayer soundPlayer){
        this.soundPlayer = soundPlayer;
    }

    public void onAccX(float force) {
        now = Calendar.getInstance().getTimeInMillis();
        sideMove = now;
        if(isOn &&soundPlayer.isSoundOn()&& (now - timeStamp) > 500 && now - downAcc > 100) {
            soundPlayer.playSound(SoundPlayer.SOUND_LIGHTSABER_HIT);
            timeStamp = Calendar.getInstance().getTimeInMillis();
            hitTime = Calendar.getInstance().getTimeInMillis();
        }
        //jsHandler.alert("Force: " + force);
    }

    public void onAccY(float force) {
        now = Calendar.getInstance().getTimeInMillis();
        if(soundPlayer.isSoundOn()){

            if (force < 0) {
                downAcc = now;
            } else {
                uppAcc = now;
            }

            if (now - downAcc < 80 && now - uppAcc < 80) {

                if (downAcc - uppAcc < 0) {
                    downMove(force);
                } else {
                    uppMove(force);

                }
            }
        }
    }

    public void onAccZ(float force) {
    }

    public void killLoop(){
        soundPlayer.killLoop();
        isOn = false;
    }

    public void onGyroX(float force){
        now = Calendar.getInstance().getTimeInMillis();
        if(isOn &&soundPlayer.isSoundOn()&& (now - moveStamp) > 600) {
            soundPlayer.playSound(SoundPlayer.SOUND_LIGHTSABER_SWING_ONE);
            moveStamp = Calendar.getInstance().getTimeInMillis();
        }
    }
    public void onGyroY(float force){
        now = Calendar.getInstance().getTimeInMillis();
        if(isOn &&soundPlayer.isSoundOn()&& (now - moveStamp) > 600) {
            soundPlayer.playSound(SoundPlayer.SOUND_LIGHTSABER_SWING_TWO);
            moveStamp = Calendar.getInstance().getTimeInMillis();
        }
    }

    public void onGyroZ(float force){
        now = Calendar.getInstance().getTimeInMillis();
        if(isOn &&soundPlayer.isSoundOn()&& (now - moveStamp) > 600) {
            soundPlayer.playSound(SoundPlayer.SOUND_LIGHTSABER_SWING_TWO);
            moveStamp = Calendar.getInstance().getTimeInMillis();
        }
    }

    private void uppMove(float force){
        if (!isOn) {
            now = Calendar.getInstance().getTimeInMillis();
            soundPlayer.playSound(SoundPlayer.SOUND_LIGHTSABER_OPEN);
            soundPlayer.playLoop(SoundPlayer.SOUND_LIGHTSABER_PULSE);
            timeStamp = now;
            isOn = true;
        }
    }
    private void downMove(float force) {
        now = Calendar.getInstance().getTimeInMillis();
        if (isOn && now - hitTime > 400) {
            isOn = false;

            soundPlayer.playSound(SoundPlayer.SOUND_LIGHTSABER_CLOSE);
            try {
                Thread.sleep(950);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            killLoop();
            timeStamp = now;
        }
    }
    public boolean isOn(){
        return isOn;
    }

}
