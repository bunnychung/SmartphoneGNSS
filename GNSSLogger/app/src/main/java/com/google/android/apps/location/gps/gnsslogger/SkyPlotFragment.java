package com.google.android.apps.location.gps.gnsslogger;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;





public class SkyPlotFragment extends Fragment{

   SkyPlotView spView;
    SkyPlotView spView2;
    private ScrollView mScrollView;
    //TextView msg;
    final private Fragment spFragment=this;
    EditText textPRN;
    //GraphView graph_snr;
    String txt="12,14,15";
    // BarGraphSeries<DataPoint> series;

    //    Button btn_capture;
    LinearLayout linear_parent;

    //TextView text_nmea;
    private SkyPlotFragment.UIFragmentSkyPlotComponent mUiFragmentSkyPlotComponent;
   final double SVid=1;
    Button connect;
    //Button on;
    //Button off;

    public synchronized SkyPlotFragment.UIFragmentSkyPlotComponent getUiFragmentComponent() {
        return  mUiFragmentSkyPlotComponent;
    }

    public synchronized void setmUiFragmentuploadComponent(SkyPlotFragment.UIFragmentSkyPlotComponent value) {
        mUiFragmentSkyPlotComponent = value;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View skyView = inflater.inflate(R.layout.fragment_skyplot, container, false /* attachToRoot */);

        linear_parent = (LinearLayout) skyView.findViewById(R.id.linear_parent);
        // graph_snr = (GraphView) findViewById(R.id.graph_snr);
        spView =  skyView.findViewById(R.id.spview);
        spView2 =  skyView.findViewById(R.id.spview2);
//        spView.showDegree(false);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if (spFragment.getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d("permission", "permission denied to SEND_SMS - requesting it");
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};

                requestPermissions(permissions, 1);

            }
        }


        Button btn= (Button) skyView.findViewById(R.id.btn_capture);
      textPRN= (EditText) skyView.findViewById(R.id.prn_code);
       // msg =(TextView) skyView.findViewById(R.id.msg) ;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    FileOutputStream f = spFragment.getContext().openFileOutput("files",0);
                    f.write(txt.getBytes());
                    f.close();

                   // openfile();
                    spView.removeAllSatellites();
                   openfile(/*Double.parseDouble(textPRN.getText().toString())*/);



                }catch (IOException e){
                    e.printStackTrace();
                }




                // plotdata(55.0,200.75);
              /*plotdata(60.0,0);
              plotdata(60.0,90.0);
             plotdata(60.0,270.0);*/

            }
        });
      return skyView;

    }



    public void plotdata(double elevation,double azimuth)
    {
        double ele= elevation;
        double az=azimuth;
        // int a = 0;

        for (int a=0;a<200;a++)
        {

            // Draw SkyPlot
            //spView.removeAllSatellites();

            // for (int i = 0; i < data.size(); i++)
            //   {
          //  this.spView.addSatellite(
                   // ele+a,az-a," ",true);
            //   }
            this.spView.refreshCanvas();

        }


    }
    //openfile
    public void openfile(/*double svid*/)  {

        Log.d("STATE","inside openfile");

        File baseDirectory;
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            baseDirectory = new File(Environment.getExternalStorageDirectory(), "gnss_log");
            baseDirectory.mkdirs();
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            Log.d("STATE","can't write");
            return;
        } else {
            Log.d("STATE","can't read");
            return;
        }

//Get the text file
        File file = new File(baseDirectory,"gnss_log.txt");
        //File csv

        //csv file
  /*  try {
        CSVReader reader = new CSVReader(new FileReader(file));
        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            // nextLine[] is an array of values from the line
            msg.setText(nextLine[1]);
          //  spView.addSatellite(Double.parseDouble(nextLine[3]), Double.parseDouble(nextLine[2]), nextLine[0],true);
           // spView.refreshCanvas();
        }
    }catch (Exception e){
            e.printStackTrace();
    }*/




        //end csv file
        String[] result={};
//Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;


            while ((line = br.readLine()) != null) {

             //   Log.d("STATE","inside reading file");

               //msg.setText(result.toString());
                //text.append(line);
                //text.append('\n');
              //  if(line.contains(",")) {
                   /* String[] parts = line.split("\\.");
                    String part1 = parts[0];
                    String part2 = parts[1];*/
                    result= line.split(",");
                   /* try{
                        Double az=Double.parseDouble(result[3]);
                    }catch (NumberFormatException e)
                    {
                        Log.e("State",result[3]);
                    }*/
                    if(result.length==4  ) {

                        try
                        {

                            double svid = Double.parseDouble(textPRN.getText().toString());
                            if (Double.parseDouble(result[0]) == svid) {


                                if(Double.parseDouble(result[3])>90) {
                                    this.spView2.addSatellite(Double.parseDouble(result[3]), Double.parseDouble(result[2]), Double.parseDouble(result[1]), true);
                                    this.spView2.refreshCanvas();
                                }
                                else
                                {
                                    this.spView.addSatellite(Double.parseDouble(result[3]), Double.parseDouble(result[2]), Double.parseDouble(result[1]), true);
                                    this.spView.refreshCanvas();

                                }
                            }

                        }catch (NumberFormatException e)
                        {
                            if(Double.parseDouble(result[3])>90) {
                                this.spView2.addSatellite(Double.parseDouble(result[3]), Double.parseDouble(result[2]), Double.parseDouble(result[1]), true);
                                this.spView2.refreshCanvas();
                            }
                            else
                            {
                                this.spView.addSatellite(Double.parseDouble(result[3]), Double.parseDouble(result[2]), Double.parseDouble(result[1]), true);
                                this.spView.refreshCanvas();

                            }
                        }


                    }
                   else {
                        Toast.makeText(this.getContext(), "File does not have the correct data", Toast.LENGTH_SHORT).show();
                       // Log.e("State", "the line doen't have 4 elements");
                        //Log.e("here is the line", line);
                    }
                   // line= result.length;
              //  Log.e("State", result[0]);
                //int[] ln={result.length};
               // Log.e("here result at 0 index",result[2]);

               // } else {
                    //Log.e("State","Corrupted data as: " );
               // }
            }
            br.close();
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
        }
        //TextView tv = (TextView)findViewById(R.id.text_view);
        // tv.setText(text.toString());
        Log.d("MSG",text.toString());



    }



    @Override
    public void onStart() {
        super.onStart();
        //MainActivity.registerOnSkyPlotAddedListener(mListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        //  MainActivity.unregisterOnSkyPlotAddedListener(mListener);

    }
    public class UIFragmentSkyPlotComponent {

        private static final int MAX_LENGTH = 42000;
        private static final int LOWER_THRESHOLD = (int) (MAX_LENGTH * 0.5);

        public synchronized void logTextFragment(final String tag, final String text, int color) {
            final SpannableStringBuilder builder = new SpannableStringBuilder();
            builder.append(tag).append(" | ").append(text).append("\n");
            builder.setSpan(
                    new ForegroundColorSpan(color),
                    0 /* start */,
                    builder.length(),
                    SpannableStringBuilder.SPAN_INCLUSIVE_EXCLUSIVE);

            Activity activity = getActivity();
            if (activity == null) {
                return;
            }
            activity.runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run() {
                           // mLogView.append(builder);
                            SharedPreferences sharedPreferences = PreferenceManager.
                                    getDefaultSharedPreferences(getActivity());
                           // Editable editable = mLogView.getEditableText();
                           // int length = editable.length();
                          /*  if (length > MAX_LENGTH) {
                                editable.delete(0, length - LOWER_THRESHOLD);
                            }
                            if (sharedPreferences.getBoolean(SettingsFragment.PREFERENCE_KEY_AUTO_SCROLL,*
                                    false /*default return value)){
                                mScrollView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mScrollView.fullScroll(View.FOCUS_DOWN);
                                    }
                                });
                            }*/
                        }
                    });
        }

        public void startActivity(Intent intent) {
            spFragment.getActivity().startActivity(intent);
        }
    }


}

