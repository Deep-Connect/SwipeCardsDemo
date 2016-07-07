package com.swipecards;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.swipecards.R;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.swipecards.lib.SwipeFlingAdapterView;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> al;
    private CardStackAdapter arrayAdapter;
    private int i;

    @InjectView(R.id.frame)
    SwipeFlingAdapterView flingContainer;


    @InjectView(R.id.undo)
    ImageButton mIbUndo;

    @InjectView(R.id.tvCardInfo)
    TextView mTvCardInfo;

    private boolean isAnimating = false, directlyExited = false;
    private LinearLayout.LayoutParams topViewBounds;
    private int maxDistance = 200;
    private String  lastDeleted = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);


        al = new ArrayList<>();
        al.add("php");
        al.add("c");
        al.add("python");
        al.add("java");
        al.add("html");
        al.add("c++");
        al.add("css");
        al.add("javascript");

        arrayAdapter = new CardStackAdapter(this, al);


        flingContainer.setAdapter(arrayAdapter);
        final Animation in = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fadeout_translate);

        final Animation out = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fadein_translate);

        topViewBounds = (LinearLayout.LayoutParams) mTvCardInfo.getLayoutParams();
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                lastDeleted = al.remove(0);

                arrayAdapter.notifyDataSetChanged();
                Log.d("tag", "removeFirstObjectInAdapter --Visible" + al.get(0));
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                directlyExited = true;
                Log.d("tag", "onLeftCardExit");
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                //    makeToast(MainActivity.this, "Left!");

            }

            @Override
            public void onRightCardExit(Object dataObject) {
                directlyExited = true;
                Log.d("tag", "onRightCardExit");
                //makeToast(MainActivity.this, "Right!");
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
                al.add("XML ".concat(String.valueOf(i)));
                arrayAdapter.notifyDataSetChanged();
                Log.d("LIST", "notified");
                i++;
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                final View view = flingContainer.getSelectedView();
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTvCardInfo.getLayoutParams();
                if (scrollProgressPercent > 0) {
                    mTvCardInfo.setAlpha(1 - scrollProgressPercent);
                    lp.topMargin = (int) (maxDistance * scrollProgressPercent); // use topmargin for the y-property, left margin for the x-property of your view
                } else {
                    mTvCardInfo.setAlpha(1 + scrollProgressPercent);
                    lp.topMargin = -(int) (maxDistance * scrollProgressPercent); // use topmargin for the y-property, left margin for the x-property of your view
                }
                mTvCardInfo.setLayoutParams(lp);
            }

            @Override
            public void onTopViewLoaded() {

                Log.d("tag", "onTopViewLoaded ");
                topViewBounds.topMargin = 0;
                mTvCardInfo.setLayoutParams(topViewBounds);
                mTvCardInfo.setText(al.get(0));
                mTvCardInfo.setAlpha(1.0f);
                mTvCardInfo.setVisibility(View.VISIBLE);
            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                makeToast(MainActivity.this, "Clicked!");
            }
        });

    }

    static void makeToast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }


    @OnClick(R.id.right)
    public void right() {
        /**
         * Trigger the right event manually.
         */
        flingContainer.getTopCardListener().selectRight();
    }

    @OnClick(R.id.left)
    public void left() {
        flingContainer.getTopCardListener().selectLeft();
    }


    @OnClick(R.id.undo)
    public void undo() {
        al.add(0,lastDeleted);
        arrayAdapter.notifyDataSetChanged();
        flingContainer.undo();
    }

}