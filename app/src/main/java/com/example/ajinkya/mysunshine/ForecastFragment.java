package com.example.ajinkya.mysunshine;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;


/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {

    public ForecastFragment() {
    }

    ArrayAdapter<String> forecast_adap;
/*    private void makeConnection()
    {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String forecastJsonStr = null;

        try
        {
            URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=corvallis,US&mode=json&units=metric&cnt=7&APPID=");

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            StringBuffer buffer = new StringBuffer();

            InputStream inputStream = urlConnection.getInputStream();

            if (inputStream == null)
            {
                forecastJsonStr = null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            while ((line = reader.readLine()) != null)
            {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0)
            {
                forecastJsonStr = null;
            }
            forecastJsonStr = buffer.toString();
            Log.v("INFO:","The forecast GODS predict - " + forecastJsonStr);

        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            Log.e("Placeholder fragment","Error",ex);
            forecastJsonStr = null;
        }
        finally
        {
            if (urlConnection != null)
            {
                urlConnection.disconnect();
            }
            if (reader != null )
            {
                try
                {
                    reader.close();
                }
                catch (final IOException e)
                {
                    Log.e("Fragment","Error closing stream",e);
                }
            }
        } // finally close
    } // close for make Connection
*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_main, container, false);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        String[] list = {
                "Today - Sunny - 88/63",
                "Tomorrow - Cloudy - 78/50",
                "Wednesday - Sunshine - 50/50",
                "Thurs - Chance of meatballs - 60/50",
                "Fri - YAY! - 100/100"
        };

        ArrayList<String> weekForecast = new ArrayList<String>(Arrays.asList(list));

        forecast_adap = new ArrayAdapter<String>(
                            getActivity(),
                            R.layout.list_item, // xml layout file for single items of the list, is a textview
                            R.id.list_item_forecast_textview,
                            weekForecast);

        final ListView l_view = (ListView) rootView.findViewById(R.id.listview_forecast);
        l_view.setAdapter(forecast_adap); // makes connection between Fragment Layout fragment_main.xml placed inside of main activity layout
                                          // and the list view items list_item.xml i.e. text items to be displayed inside of the fragment

        // makeConnection(); moving to asynctask

        // First time initialization
        //new fetchWeatherData().execute("97330");

        l_view.setOnItemClickListener(new AdapterView.OnItemClickListener() // setting up Item Click in-place
            {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
                {
                    String forecast = forecast_adap.getItem(position);
                    Intent intent = new Intent(getActivity(), DetailActivity.class)
                            .putExtra(Intent.EXTRA_TEXT, forecast);
                    startActivity(intent);
                    //Toast.makeText(getActivity(),forecast_adap.getItem(position), Toast.LENGTH_SHORT).show();
                }
            }
        );

        return rootView;
    }

    public class fetchWeatherData extends AsyncTask<String,Void,String[]>
    {
        @Override
        protected String[] doInBackground(String... params)
        {
            if (params.length == 0)
            {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String forecastJsonStr = null;

            String format = "json";
            String units = "metric";
            int numDays = 7;

            String forecastFinal[] = new String[numDays];

            try
            {
                final String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
                final String QUERY_PARAM = "q";
                final String FORMAT_PARAM = "mode";
                final String UNITS_PARAM = "units";
                final String DAYS_PARAM = "cnt";
                final String APPID_PARAM = "APPID";

                final String APPID = "";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, params[0]) // to get the 97330 zip passed from onCreate
                        .appendQueryParameter(FORMAT_PARAM, format)
                        .appendQueryParameter(UNITS_PARAM, units)
                        .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                        .appendQueryParameter(APPID_PARAM, APPID)
                        .build();

                URL url = new URL(builtUri.toString());

                //Log.v("BUILT URI==",builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                StringBuffer buffer = new StringBuffer();

                InputStream inputStream = urlConnection.getInputStream();

                if (inputStream == null)
                {
                    forecastJsonStr = null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                while ((line = reader.readLine()) != null)
                {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0)
                {
                    forecastJsonStr = null;
                }
                forecastJsonStr = buffer.toString();
                Log.v("INFO:","The forecast GODS predict - " + forecastJsonStr);

                // Parse JSON goes here

                JSONObject jsonObject = new JSONObject(forecastJsonStr);
                JSONObject jsonObjectTemp;

                String min_temp[] = new String[numDays];
                String max_temp[] = new String[numDays];
                String weather_condition[] = new String[numDays];
                JSONArray jsonArray = jsonObject.getJSONArray("list");

                int count = 0;
                while (count < numDays)
                {
                    jsonObjectTemp = jsonArray.getJSONObject(count);
                    min_temp[count] = jsonObjectTemp.getJSONObject("temp").getString("min");
                    max_temp[count] = jsonObjectTemp.getJSONObject("temp").getString("max");
                    weather_condition[count] = jsonObjectTemp.getJSONArray("weather").getJSONObject(0).getString("main");

                    forecastFinal[count] = "Thisday "+weather_condition[count]+" - "+min_temp[count]+"/"+max_temp[count];
                    //Log.i("Min Temp ["+count+"] ==",min_temp[count]);
                   // Log.i("Max Temp ["+count+"] ==",max_temp[count]);
                    //Log.i("Weather ["+count+"] ==",weather_condition[count]);
                    count++;
                }

                ///////////////////
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
                Log.e("Placeholder fragment","IOError",ex);
                forecastJsonStr = null;
            }
            catch (JSONException jsex)
            {
                jsex.printStackTrace();
                Log.e("Placeholder fragment","JSONError",jsex);
                forecastJsonStr = null;
            }
            finally
            {
                if (urlConnection != null)
                {
                    urlConnection.disconnect();
                }
                if (reader != null )
                {
                    try
                    {
                        reader.close();
                    }
                    catch (final IOException e)
                    {
                        Log.e("Fragment","Error closing stream",e);
                    }
                }
            } // finally close

            /*Log.i("ASYCTASK:","Make the connection about to be hit");
            makeConnection(params[0]);
            */
            // return the parsed and final String [] containing the forecast. This passes to onPostExecute below
            return forecastFinal;
        } // doing It in background

        @Override
        protected void onPostExecute(String[] result)
        {
            if (result != null)
            {
                forecast_adap.clear();
                for (String daydata : result)
                {
                    forecast_adap.add(daydata); // Triggers view to update -_- how are you supposed to know this!?!
                    // adapter also supposedly has methods to notify VIew of changes to data
                }
            }
            // Code below works just as well, above one is from tutorial
           /* forecast_adap = new ArrayAdapter<String>(
                    getActivity(),
                    R.layout.list_item, // xml layout file for single items of the list, is a textview
                    R.id.list_item_forecast_textview,
                    strings);

            View root = getView();
            ListView l_view = (ListView) root.findViewById(R.id.listview_forecast);
            l_view.setAdapter(forecast_adap);*/
            //super.onPostExecute(strings);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_main, menu); this is getting called in mainactivity I do not need this in fragment

        //menu.add("Refresh");
       // super.onCreateOptionsMenu(menu, inflater); // so just do this here

        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        //Log.e("INFO:",""+item.getTitle());
        if (item.getTitle().equals("Refresh"))
        {
            Log.e("INFO:","Refresh hit in fragment");
            //new fetchWeatherData().execute("97330");
            new fetchWeatherData().execute("corvallis,US");
        }

        return super.onOptionsItemSelected(item);
    }
}
