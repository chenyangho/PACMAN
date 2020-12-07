package se1a_2200432.CatchTheItem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    //テキストビュー
    private TextView scoreTextView;
    private TextView startTextView;
    //イメージビュー
    private ImageView pacmanImageView;
    private ImageView orangeImageView;
    private ImageView cherryImageView;
    private ImageView enemyImageView;
    //パックマンの横幅
    private int pacmanWidth;
    //パックマンの高さ
    private int pacmanHeight;
    //各オブジェクトの座標
    private float pacmanY;
    private float orangeX;
    private float orangeY;
    private float cherryX;
    private float cherryY;
    private float enemyX;
    private float enemyY;
    //画面タッチフラグ（true:タッチ時, false：離す）
    private boolean touchFlag = false;
    //ゲーム開始フラグ（true:開始, false:未開始）
    private boolean startFlag = false;
    //ゲーム動作用ハンドラー&タイマー
    private Handler handler = new Handler();
    private Timer timer = new Timer();
    //スコア
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //各オブジェクト関連付け
        scoreTextView = findViewById(R.id.score_text);
        startTextView = findViewById(R.id.start_text);
        pacmanImageView = findViewById(R.id.pacman);
        orangeImageView = findViewById(R.id.orange);
        cherryImageView = findViewById(R.id.cherry);
        enemyImageView = findViewById(R.id.enemy);

    }

    private void changePosition(){
        //以下移動処理記述
        int pacmanSpeed = 20;
        int orangeSpeed = 12;
        int cherrySpeed = 16;
        int enemySpeed = 8;

        int orangeFreq = 500;
        int cherryFreq = 2000;
        int enemyFreq = 100;

        FrameLayout frameLayout = findViewById(R.id.main_layout);
        int frameWidth = frameLayout.getWidth();
        int frameHeight = frameLayout.getHeight();

        if(touchFlag == true){
            pacmanY -= pacmanSpeed; //上へ移動
        }else{
            pacmanY += pacmanSpeed; //下へ移動
        }
        if(pacmanY < 0){
            pacmanY = 0;
        }
        if(pacmanY > frameHeight - pacmanHeight){
            pacmanY = frameHeight - pacmanHeight;
        }
        pacmanImageView.setY(pacmanY);

        orangeX -= orangeSpeed; //左へ移動
        if(orangeX + orangeImageView.getWidth() < 0) {
            orangeX = frameWidth + orangeFreq;
            orangeY = (float)Math.floor(Math.random() * (frameHeight - orangeImageView.getHeight()));
        }
        orangeImageView.setX(orangeX);
        orangeImageView.setY(orangeY);


        cherryX -= cherrySpeed; //左へ移動
        if(cherryX + cherryImageView.getWidth() < 0) {
            cherryX = frameWidth + cherryFreq;
            cherryY = (float)Math.floor(Math.random() * (frameHeight - cherryImageView.getHeight()));
        }
        cherryImageView.setX(cherryX);
        cherryImageView.setY(cherryY);


        enemyX -= enemySpeed; //左へ移動
        if(enemyX + enemyImageView.getWidth() < 0) {
            enemyX = frameWidth + enemyFreq;
            enemyY = (float)Math.floor(Math.random() * (frameHeight - enemyImageView.getHeight()));
        }
        enemyImageView.setX(enemyX);
        enemyImageView.setY(enemyY);

    }

    private void hitCheck(){
        //以下判定処理記述
        int orangePoint = 10;
        int cherryPoint = 50;

        float orangeCenterX = orangeX + orangeImageView.getWidth() / 2;
        float orangeCenterY = orangeY + orangeImageView.getHeight() / 2;

        if(0 <= orangeCenterX && pacmanWidth >= orangeCenterX &&
        pacmanY <= orangeCenterY && (pacmanHeight + pacmanY) >= orangeCenterY){
            orangeX = -orangeImageView.getWidth(); //当たったので左画面外へ移動
            score += orangePoint; //スコアにオレンジの得点を加算
        }

        float cherryCenterX = cherryX + cherryImageView.getWidth() / 2;
        float cherryCenterY = cherryY + cherryImageView.getHeight() / 2;

        if(0 <= cherryCenterX && pacmanWidth >= cherryCenterX &&
                pacmanY <= cherryCenterY && (pacmanHeight + pacmanY) >= cherryCenterY){
            cherryX = -cherryImageView.getWidth(); //当たったので左画面外へ移動
            score += cherryPoint; //スコアにオレンジの得点を加算
        }

        float enemyCenterX = enemyX + enemyImageView.getWidth() / 2;
        float enemyCenterY = enemyY + enemyImageView.getHeight() / 2;

        if(0 <= enemyCenterX && pacmanWidth >= enemyCenterX &&
                pacmanY <= enemyCenterY && (pacmanHeight + pacmanY) >= enemyCenterY){
            enemyX = -enemyImageView.getWidth(); //当たったので左画面外へ移動

            if(timer != null) {
                timer.cancel();
            }
            Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
            intent.putExtra("SCORE", score);
            startActivity(intent);
        }
        scoreTextView.setText(getString(R.string.game_score) + score);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //ここから記述していく
        if(startFlag == true){
            //タッチフラグ切り替え処理
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                touchFlag = true; //タッチした
            }else if(event.getAction() == MotionEvent.ACTION_UP){
                touchFlag = false; //離した
            }
        }else{
            startFlag = true;
            pacmanWidth = pacmanImageView.getWidth();
            pacmanHeight = pacmanImageView.getHeight();
            pacmanY = pacmanImageView.getY();
            orangeX = orangeImageView.getX();
            orangeY = orangeImageView.getY();
            cherryX = cherryImageView.getX();
            cherryY = cherryImageView.getY();
            enemyX = enemyImageView.getX();
            enemyY = enemyImageView.getY();
            startTextView.setVisibility(View.GONE);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePosition(); //各オブジェクト移動
                            hitCheck(); //当たり判定
                        }
                    });
                }
            }, 0, 10);
        }
        return true;

    }


}