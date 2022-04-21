package com.example.smellgood;
import android.app.AlertDialog;
import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smellgood.provider.NoteContentProvider;
import com.example.smellgood.provider.Provider;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import static com.example.smellgood.Defaults.NO_CURSOR;
import static com.example.smellgood.Defaults.DISMISS_ACTION;
import static com.example.smellgood.Defaults.NO_COOKIE;
import static com.example.smellgood.Defaults.NO_CURSOR;
import static com.example.smellgood.Defaults.NO_FLAGS;
import static com.example.smellgood.Defaults.NO_SELECTION;
import static com.example.smellgood.Defaults.NO_SELECTION_ARGS;
import com.example.smellgood.provider.NoteContentProvider;
import com.example.smellgood.provider.Provider;



public class Game extends AppCompatActivity {
    private Handler handler = new Handler();
    private Timer timer;
    private SharedPreferences settings;
    private ImageView robo, roboDeadRight, roboStand, roboToRight, mud, powder, bottom, totemObject;
    private Button play, save;
    private TextView score, totem;
    private LinearLayout gamePanel;
    private int speedMud, speedRobo, period, round, body, tBody, totemB, scoreB, media_length;
    private float roboX, mudY, powderY, sirka, vyska, totemObjectY;
    private boolean right = false, boolPowder = false, prvyBod = true, hybeSa = false, firstGen = true;
    private boolean left = false, prvaZmena = true, roboRight;
    private Random rd = new Random();
    private int[] robko = {R.drawable.robostand, R.drawable.robostandl, R.drawable.robodeadright, R.drawable.robodeadleft};
    private static final int NOTES_LOADER_ID = 0;
    private static final int INSERT_NOTE_TOKEN = 0;
    private static final int DELETE_NOTE_TOKEN = 0;
    private MediaPlayer mpEffects;
    private MediaPlayer mp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.game_layout);

        period = 1;
        scoreB = 0;
        totemB = 0;
        media_length = 0;

        robo = findViewById(R.id.robo);
        totem = findViewById(R.id.totem);
        score = findViewById(R.id.score);
        gamePanel = findViewById(R.id.gamePanel);
        play = findViewById(R.id.startButton);
        mud = findViewById(R.id.mud);
        powder = findViewById(R.id.powder);
        bottom = findViewById(R.id.bottom);
        save = findViewById(R.id.saveButton);
        totemObject = findViewById(R.id.totemObject);

        gamePanel.post(new Runnable() {
            public void run() {
                sirka = gamePanel.getWidth();
                vyska = gamePanel.getHeight() - bottom.getHeight() - mud.getHeight();
            }
        });

        right = true;
    }

    public void klikloSa(View view) {
        if (mp != null){
            mp.reset();
        }
        mp = MediaPlayer.create(this, R.raw.game);
        mp.setLooping(true);
        mp.start();
        score.setText("Score : 0");
        if (totemB > 0){
            totemB--;
        }
        totem.setText("Totem : " + totemB);
        save.setVisibility(View.GONE);
        play.setVisibility(View.GONE);
        robo.setImageResource(robko[0]);
        robo.setLayoutParams(new LinearLayout.LayoutParams(dpToPx(85), dpToPx(140)));
        generateMud();
        generatePowder();
        setTimer();
    }

    public void updateText() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                score.setText("Score : " + scoreB);
                totem.setText("Totem : " + totemB);
            }
        });
    }

    public void setTimer() {
        if (hybeSa) {
            return;
        } else {
            hybeSa = true;
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    pohyb();
                    pohybMud();
                    pohybPowder();
                    updateText();
                }
            }, 0, period);
        }
    }


    public void pohyb() {
        //pohyb hracej postavy
        if (hybeSa) {
            if (right) {
                if (prvaZmena) {
                    prvaZmena = false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            robo.setImageResource(robko[0]);
                            roboRight = true;
                        }
                    });
                }
                if (roboX + robo.getWidth() > gamePanel.getWidth()) {
                    roboX = gamePanel.getWidth() - robo.getWidth();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            robo.setX(roboX);
                        }
                    });
                    prvaZmena = true;
                } else {
                    roboX += 1;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            robo.setX(roboX);
                        }
                    });
                }
            } else {
                if (prvaZmena) {
                    prvaZmena = false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            robo.setImageResource(robko[1]);
                            roboRight = false;
                        }
                    });
                }
                if ((roboX - 1) < 0) {
                    roboX = 0;
                    robo.setX(roboX);
                    prvaZmena = true;
                } else {
                    roboX -= 1;
                    robo.setX(roboX);
                }
            }
        }
    }


    public void pohybMud() {
        if (hybeSa) {
            mudY += 1.35;
            mud.setY(mudY);
            dotykajuSaMud();
            if (mudY >= vyska) {
                generateMud();
            }
        }
    }

    public void pohybPowder() {
        if (hybeSa) {
            powderY += 1.35;
            powder.setY(powderY);
            dotykajuSaPowder();
        }
    }

    public void pohybTotem() {
        if (hybeSa) {
            totemObjectY += 1.35;
            totemObject.setY(totemObjectY);
            dotykajuSaTotem();
        }
    }

    public void klik(View view) {
        prvaZmena = true;
        if (right) {
            right = false;
        } else right = true;
    }

    /* generovanie objektov, ktorym sa treba vyhybat */
    public void generateMud() {
        mud.setY(-40);
        mud.setX((float) Math.random() * (sirka - mud.getWidth()));
        mudY = mud.getY();
        mud.setVisibility(View.VISIBLE);
    }

    /* generovanie objektov, ktore treba zbierat */
    public void generatePowder() {
        if (firstGen) {
            powder.setY(-500);
            firstGen = !firstGen;
        } else {
            powder.setY(-40);
        }
        float mudStart = mud.getX(), mudEnd = mudStart + mud.getWidth();
        float powderStart = powder.getX(), powderEnd = powderStart + powder.getWidth();
        do {
            powder.setX((float) Math.random() * (sirka - powder.getWidth()));
            powderStart = powder.getX();
            powderEnd = powderStart + powder.getWidth();
            mudStart = mud.getX();
            mudEnd = mudStart + mud.getWidth();
        } while (inRange(powderStart, mudStart, mudEnd) || inRange(powderEnd, mudStart, mudEnd));
        powderY = powder.getY();
        powder.setVisibility(View.VISIBLE);
    }

    public void generateTotem() {

        float mudStart = mud.getX(), mudEnd = mudStart + mud.getWidth();
        float powderStart = powder.getX(), powderEnd = powderStart + powder.getWidth();
        float totemStart = totemObject.getX(), totemEnd = totemStart + totemObject.getWidth();
        do {
            totemObject.setX((float) Math.random() * (sirka - powder.getWidth()));
            powderStart = powder.getX();
            powderEnd = powderStart + powder.getWidth();
            mudStart = mud.getX();
            mudEnd = mudStart + mud.getWidth();
            totemStart = totemObject.getX();
            totemEnd = totemStart + totemObject.getWidth();
        } while (inRange(totemStart, mudStart, mudEnd) ||
                inRange(totemEnd, mudStart, mudEnd) ||
                inRange(totemStart, powderStart, powderEnd) ||
                inRange(totemEnd, powderStart, powderEnd)
        );

        totemObject.setY(-40);
        totemObject.setVisibility(View.VISIBLE);
        totemObjectY = totemObject.getY();
    }

    private void dotykajuSaMud() {
        Rect myViewRect = new Rect();
        robo.getHitRect(myViewRect);
        Rect otherViewRect1 = new Rect();
        mud.getHitRect(otherViewRect1);
        if (Rect.intersects(myViewRect, otherViewRect1)) {
            stop();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (roboRight) {
                        robo.setImageResource(robko[2]);
                    } else{
                        robo.setImageResource(robko[3]);
                    }
                    if (robo.getX() + robo.getWidth() >= sirka) {
                        roboX = sirka - robo.getWidth();
                        robo.setX(roboX);
                    }
                    robo.setLayoutParams(new LinearLayout.LayoutParams(dpToPx(140), dpToPx(85)));
                    powder.setVisibility(View.GONE);
                    mud.setVisibility(View.GONE);
                    totemObject.setVisibility(View.GONE);
                }
            });
            mpEffects = MediaPlayer.create(Game.this, R.raw.splash);
            mpEffects.start();
        }
    }

    private void dotykajuSaPowder() {
        Rect myViewRect = new Rect();
        robo.getHitRect(myViewRect);
        Rect otherViewRect1 = new Rect();
        powder.getHitRect(otherViewRect1);
        if (Rect.intersects(myViewRect, otherViewRect1)) {
            generatePowder();
            scoreB++;
            mpEffects = MediaPlayer.create(this, R.raw.score);
            mpEffects.start();
        }
        if (powder.getY() >= vyska) {
            generatePowder();
        }
    }

    private void dotykajuSaTotem() {
        Rect myViewRect = new Rect();
        robo.getHitRect(myViewRect);
        Rect otherViewRect1 = new Rect();
        totemObject.getHitRect(otherViewRect1);
        if (Rect.intersects(myViewRect, otherViewRect1)) {
            totemObject.setX(sirka - 1000);
            totemB++;
            mpEffects = MediaPlayer.create(this, R.raw.score);
            mpEffects.start();
        }
        if (totemObject.getY() >= vyska) {
            totemObject.setX(sirka - 1000);
        }
    }

    protected void stop() {
        mp.reset();
        mp = MediaPlayer.create(this, R.raw.dead);
        mp.setLooping(true);
        mp.start();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                save.setVisibility(View.VISIBLE);
                play.setVisibility(View.VISIBLE);
                if (totemB == 0){
                    play.setText("RESTART");
                    save.setVisibility(View.VISIBLE);
                    play.setVisibility(View.VISIBLE);
                    scoreB = 0;
                    totemB = 0;
                    firstGen = true;
                    if (timer != null) {
                        timer.cancel();
                        right = true;
                        hybeSa = false;
                    }
                } else {
                    play.setText("Continue ( " + totemB + " )");
                    save.setVisibility(View.VISIBLE);
                    play.setVisibility(View.VISIBLE);
                    if (timer != null) {
                        timer.cancel();
                        right = true;
                        hybeSa = false;
                    }
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        stop();
        mp.reset();
        mpEffects.reset();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stop();
        mp.reset();
        mpEffects.reset();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void createReport(View view) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayout a = new LinearLayout(Game.this);
                a.setOrientation(LinearLayout.VERTICAL);
                a.setPadding(25, 10, 25, 10);

                final EditText descriptionEditText = new EditText(Game.this);
                descriptionEditText.setHint("Enter nickname");
                descriptionEditText.setMinimumWidth(a.getWidth());

                a.addView(descriptionEditText);

                new AlertDialog.Builder(Game.this)
                        .setTitle("Enter Shall of Fame")
                        .setView(a)

                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String nazov = descriptionEditText.getText().toString();
                                insertIntoContentProvider(nazov, String.valueOf(scoreB));
                            }
                        })
                        .setNegativeButton("Cancel", DISMISS_ACTION)
                        .show();
            }
        });
    }


    private void insertIntoContentProvider(String nickname, String score) {
        Uri uri = NoteContentProvider.CONTENT_URI;
        ContentValues values = new ContentValues();
        values.put(Provider.Note.NICKNAME, nickname);
        values.put(Provider.Note.SCORE, score);
        AsyncQueryHandler insertHandler = new
                AsyncQueryHandler(getContentResolver()) {
                    @Override
                    protected void onInsertComplete(int token, Object cookie, Uri uri) {
                        Toast.makeText(Game.this, "Saved",
                                Toast.LENGTH_SHORT).show();
                    }
                };
        insertHandler.startInsert(INSERT_NOTE_TOKEN, NO_COOKIE, uri, values);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }


    public static boolean inRange(float value, float min, float max) {
        return toRange(value, min, max) == value;
    }

    public static float toRange(float value, float min, float max) {
        return Math.max(Math.min(max, value), min);
    }

    public int dpToPx(int dp) {
        float density = this.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
}