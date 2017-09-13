package com.example.mfahmii.bakingapp;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.example.mfahmii.bakingapp.model.RecipeCard;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * The configuration screen for the {@link AppWidget AppWidget} AppWidget.
 */
public class AppWidgetConfigureActivity extends Activity {

    private static final String PREFS_NAME = "com.example.mfahmii.bakingapp.AppWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    EditText mAppWidgetText;
    TextView conf;
    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = AppWidgetConfigureActivity.this;

            // When the button is clicked, store the string locally
            String widgetText = mAppWidgetText.getText().toString();
            saveTitlePref(context, mAppWidgetId, widgetText);

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            AppWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    public AppWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.app_widget_configure);
//        mAppWidgetText = (EditText) findViewById(R.id.appwidget_text);
//        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);
        RecyclerView rv = (RecyclerView) findViewById(R.id.rv_conf_widget);
        conf=(TextView)findViewById(R.id.conf);

        List<String> recipeName =new ArrayList<String>();
        List<String> recipeId =new ArrayList<String>();
        List<String> recipeIg=new ArrayList<>();

//        conf.setText(""+getSharedPreferences("namaewa",MODE_PRIVATE).getString("hello",null));
        for(int i=1;i<getSharedPreferences("namaewa",MODE_PRIVATE).getString("RECIPE_NAME",null).split("\\^").length;i++){
            recipeName.add(getSharedPreferences("namaewa",MODE_PRIVATE).getString("RECIPE_NAME",null).split("\\^")[i]);
            recipeId.add(getSharedPreferences("namaewa",MODE_PRIVATE).getString("RECIPE_ID",null).split("\\^")[i]);
            recipeIg.add(getSharedPreferences("namaewa",MODE_PRIVATE).getString("RECIPE_INGREDIENTS",null).split("\\^")[i]);
        }

        rv.setAdapter(new RecyclerViewWidgetAdapter(recipeName,recipeId, recipeIg));
        rv.setLayoutManager(new LinearLayoutManager(this));

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

//        mAppWidgetText.setText(loadTitlePref(AppWidgetConfigureActivity.this, mAppWidgetId));
    }

    class RecyclerViewWidgetAdapter extends RecyclerView.Adapter<RecyclerViewWidgetAdapter.WidgetAdapter>{

        private Context context;
        List<String> recipes = new ArrayList<>();
        List<String> recipeIds = new ArrayList<>();
        List<String> recipeIg = new ArrayList<>();

        public RecyclerViewWidgetAdapter(List<String> recipes, List<String> recipeIds, List<String> recipeIg){
            this.recipes = recipes;
            this.recipeIds=recipeIds;
            this.recipeIg=recipeIg;
        }

        @Override
        public WidgetAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
            context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
            WidgetAdapter widgetAdapter = new WidgetAdapter(view);
            return widgetAdapter ;
        }

        @Override
        public void onBindViewHolder(WidgetAdapter holder, int position) {
            final String recipe = recipes.get(position);
            final String recipeId= recipeIds.get(position);
            final String recipeIgd= recipeIg.get(position);
            holder.tv.setText(recipe);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    execute(recipe);
                    Log.d("tug","hello dewangga");

                    final Context context = AppWidgetConfigureActivity.this;

                    // When the button is clicked, store the string locally
//                    String widgetText = mAppWidgetText.getText().toString();
                    saveTitlePref(context, mAppWidgetId, recipeIgd);

                    // It is the responsibility of the configuration activity to update the app widget
                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                    AppWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

                    // Make sure we pass back the original appWidgetId
                    Intent resultValue = new Intent();
                    resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                    setResult(RESULT_OK, resultValue);
                    finish();

                }
            });
        }

        @Override
        public int getItemCount() {
            if (recipes != null)
                return recipes.size();
            else return 0;
        }

        class WidgetAdapter extends RecyclerView.ViewHolder{
            @BindView(android.R.id.text1)
            TextView tv;
            public WidgetAdapter(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

    }




}

