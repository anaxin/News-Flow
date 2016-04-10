package anaxin.newshellas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;


public class LoadingScreen extends Activity {

    private final int WAIT_TIME = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        final String activity = intent.getStringExtra("activity");
        System.out.println("xint activity " + activity);

        setContentView(R.layout.loadingscreen);
        findViewById(R.id.mainSpinner1).setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(LoadingScreen.this, Feed.class);
                intent.putExtra("activity",activity);
                LoadingScreen.this.startActivity(intent);
                LoadingScreen.this.finish();
            }
        }, WAIT_TIME);
    }
}