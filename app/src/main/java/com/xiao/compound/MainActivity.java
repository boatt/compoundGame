package com.xiao.compound;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.xiao.compound.utils.MockData;
import com.xiao.compound.utils.SoundPlayUtils;

public class MainActivity extends AppCompatActivity {
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        SoundPlayUtils.init(this);
        final GameLayout gameLayout = new GameLayout(context);
        setContentView(gameLayout);
        getLifecycle().addObserver(gameLayout);
        Button addButton = new Button(context);
        addButton.setText("添加");
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 120, Gravity.BOTTOM);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean success = MockData.getInstance().addData();
                if (!success) {
                    Toast.makeText(context, "没有空位了", Toast.LENGTH_SHORT).show();
                } else {
                    gameLayout.requestLayoutCatView();
                    SoundPlayUtils.play(2);
                }
            }
        });
        gameLayout.addView(addButton, params);
    }
}
