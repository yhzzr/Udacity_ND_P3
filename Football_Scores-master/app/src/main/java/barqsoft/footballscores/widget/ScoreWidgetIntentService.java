package barqsoft.footballscores.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;

/**
 * Created by Heng on 3/3/16.
 */
public class ScoreWidgetIntentService extends IntentService {

    private static final String[] SCORE_COLUMNS = {
            DatabaseContract.scores_table.HOME_COL,
            DatabaseContract.scores_table.AWAY_COL,
            DatabaseContract.scores_table.HOME_GOALS_COL,
            DatabaseContract.scores_table.AWAY_GOALS_COL,
            DatabaseContract.scores_table.TIME_COL,
            DatabaseContract.scores_table.DATE_COL
    };

    private static final int INDEX_HOME = 0;
    private static final int INDEX_AWAY = 1;
    private static final int INDEX_HOME_GOALS = 2;
    private static final int INDEX_AWAY_GOALS = 3;
    private static final int INDEX_TIME = 5;
    private static final int INDEX_DATE = 6;

    public ScoreWidgetIntentService() {
        super("ScoreWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent){
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                ScoreWidgetProvider.class));

        Uri scoreForDateUri = DatabaseContract.BASE_CONTENT_URI;
        Cursor data = getContentResolver().query(scoreForDateUri, SCORE_COLUMNS, null, null, null);

        if(data == null) {
            return;
        }
        if(!data.moveToFirst()){
            data.close();
            return;
        }

        String homeName = data.getString(INDEX_HOME);
        String awayName = data.getString(INDEX_AWAY);
        String time = data.getString(INDEX_TIME);
        String score = Utilies.getScores(data.getInt(INDEX_HOME_GOALS), data.getInt(INDEX_AWAY_GOALS));
        data.close();

        for(int appWidgetId : appWidgetIds){
            int layoutId = R.layout.widget_score_small;
            RemoteViews views = new RemoteViews(getPackageName(), layoutId);
            views.setTextViewText(R.id.home_name, homeName);
            views.setTextViewText(R.id.away_name, awayName);
            views.setTextViewText(R.id.score_textview, score);
            views.setTextViewText(R.id.data_textview, time);

            Intent launchIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
