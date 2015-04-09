package mobileenterprise.brandonward.myrecipies.presentation;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import mobileenterprise.brandonward.myrecipies.R;
import mobileenterprise.brandonward.myrecipies.domain.Recipe;
import mobileenterprise.brandonward.myrecipies.services.IRestSvc;
import mobileenterprise.brandonward.myrecipies.services.MyRestServiceImpl;

public class MyRestService extends ActionBarActivity {

    private ArrayAdapter adapter;
    private Context self = null;
    private ListView listView = null;
    private static final String TAG = "MyRestService";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_rest_service);
        self = this;
        listView = (ListView) findViewById(R.id.myRestData);
        new MyRestServiceAsyncTask().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_regis, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class MyRestServiceAsyncTask extends AsyncTask<String, String, String> {//(params, progress-information, result) for arguments

        @Override//the web-service
        protected String doInBackground(String... params) {//String... is equivalent to saying String[] args
            IRestSvc impl = new MyRestServiceImpl();
            return impl.retrieve();
            //return impl.create(new Recipe());
        }

        @Override//updates the UI
        protected void onPostExecute(String result){

            Log.i(TAG, "OnPostExecute");
            //Toast.makeText(getApplicationContext(),"Hello "+result, Toast.LENGTH_SHORT).show();
            try{
                JSONArray jsonArray = new JSONArray(result);
                final int length = jsonArray.length();
                Log.i(TAG, "Number of entries " + length);
                String[] regisPrograms = new String[length];
                for (int i = 0; i < length; i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Log.i(TAG, jsonObject.getString("id")+ " "+ jsonObject.getString("recipeName"));
                    regisPrograms[i] = jsonObject.getString("recipeName");
                }
                adapter = new ArrayAdapter<String>(self,android.R.layout.simple_list_item_1, regisPrograms);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } catch(Exception e){
                e.printStackTrace();
            }
            Log.i(TAG, "Exiting onPostExecute");
        }
    }
}
