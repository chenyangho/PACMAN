package se1a_2200432.CatchTheItem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        //ここから記述していく
        TextView scoreTextView = findViewById(R.id.score_text);
        TextView highScoreLabel = findViewById(R.id.highScore_text);
        int score = getIntent().getIntExtra("SCORE", 0);

        scoreTextView.setText(String.valueOf(score));
        SharedPreferences sharedPreferences = getSharedPreferences("GAME_DATA", MODE_PRIVATE);
        int highScore = sharedPreferences.getInt("HIGH_SCORE", 0);

        if (score > highScore) {
            highScoreLabel.setText(getString(R.string.high_score) + score);
            //今回のスコアをハイスコアとして更新
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("HIGH_SCORE", score); //ハイスコア更新
            editor.apply(); //データ反映
        } else {
            //今のハイスコアを表示
            highScoreLabel.setText(getString(R.string.high_score) + highScore);
        }

        Button retryButton = findViewById(R.id.retry_button);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ゲーム画面起動
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });



    }
}