package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;

    //para manipular threads
    private Handler handler;
    private Runnable runnable;
    private SeekBar seekBarConteudo;

    //para manipular volume
    private SeekBar seekBarVolume;
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //colocar a musica
        mediaPlayer=MediaPlayer.create(getApplicationContext(),R.raw.mytearsricochet);

        seekBarConteudo=findViewById(R.id.seekBarConteudo);

        handler=new Handler();

        runnable=new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer.isPlaying()){
                    seekBarConteudo.setProgress((int)mediaPlayer.getCurrentPosition() );
                }
                handler.postDelayed(runnable,1000);
            }
        };
        handler.post(runnable);
        //controlar o conteudo de acordo com a progressBar
        seekBarConteudo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean users) {
                //a mudança foi inciada pelo usuario
                if(users){
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.pause();
                        mediaPlayer.seekTo(i);
                        mediaPlayer.start();
                    }else{
                        mediaPlayer.seekTo(i);

                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        inicializarSeekBarVolume();

    }
    public void executarSom(View view){
        if(mediaPlayer!=null){
            mediaPlayer.start();
            seekBarConteudo.setMax((int) mediaPlayer.getDuration());

        }

    }
    public void pausarSom(View view){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
    }
    public void pararSom(View view){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        mediaPlayer=MediaPlayer.create(getApplicationContext(),R.raw.mytearsricochet);

    }
    public void inicializarSeekBarVolume(){
        seekBarVolume=findViewById(R.id.seekBarVolume);

        //configurar audioManager
        audioManager=(AudioManager) getSystemService(Context.AUDIO_SERVICE);

        //recuperarmos o volume atual e o volume maximo
        int volAtual=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int volMaximo=audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        //configurar vol maximo
        seekBarVolume.setMax(volMaximo);
        //configurar vol atual
        seekBarVolume.setProgress(volAtual);

        //deixar o usuario manipular
        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,i,0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


}