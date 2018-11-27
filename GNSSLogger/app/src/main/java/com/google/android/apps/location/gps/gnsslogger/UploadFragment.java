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
        import android.support.annotation.Nullable;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.content.ContextCompat;
        import android.support.v7.app.AppCompatActivity;
        import android.text.Editable;
        import android.text.SpannableStringBuilder;
        import android.text.style.ForegroundColorSpan;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.ScrollView;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.io.File;
        import java.io.FileNotFoundException;
        import java.io.FileOutputStream;
        import java.io.IOException;


public class UploadFragment extends Fragment {



    private TextView mLogView;
    private ScrollView mScrollView;
    final String ON = "1";
    final String OFF = "0";
    final private String TAG="Hi";
    //final private Context context = this;

    BluetoothSPP bluetooth;
    final private Fragment mfragment= this;
    private int var=0;
    public FileOutputStream oldstream;
    String filename = "/mfile.txt";

    // File directory = context.getFilesDir();
   /*public String path= Environment.getExternalStorageDirectory().getAbsolutePath();
    private File file = new File(path + filename);
    final private String pathfile = file.getAbsolutePath();

*/
    private UIFragmentUploadComponent mUiFragmentuploadComponent;

    Button connect;
    Button upload;
    //Button on;
    //Button off;

    public synchronized UIFragmentUploadComponent getUiFragmentComponent() {
        return mUiFragmentuploadComponent;
    }

    public synchronized void setmUiFragmentuploadComponent(UIFragmentUploadComponent value) {
        mUiFragmentuploadComponent = value;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View upView = inflater.inflate(R.layout.fragment_upload, container, false /* attachToRoot */);
        //super.onCreateView(inflater, container, savedInstanceState);


        bluetooth = new BluetoothSPP(this.getContext());

        connect = (Button) upView.findViewById(R.id.connect);
        upload= (Button) upView.findViewById(R.id.upload_btn);
        // off = (Button) findViewById(R.id.off);



        //setmUiFragmentuploadComponent;


        if (!bluetooth.isBluetoothAvailable()) {
            Toast.makeText(mfragment.getContext(), "Bluetooth is not available", Toast.LENGTH_SHORT).show();
            mfragment.getActivity().finish();
            // Toast.makeText(mfragment.getContext(), "Bluetooth is not available", pathfile.toString()).show();
            //Log.d("hi",pathfile);
        }

        bluetooth.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            public void onDeviceConnected(String name, String address) {
                connect.setText("Connected to " + name);
            }

            public void onDeviceDisconnected() {
                connect.setText("Connection lost");
            }

            public void onDeviceConnectionFailed() {
                connect.setText("Unable to connect");
            }
        });
        bluetooth.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener()
        {
            public void onDataReceived(byte[] data, String message)
            {
                /// msg_box.setText(message);
                // myFile.append(message);
                //check for permission
                if (ContextCompat.checkSelfPermission(mfragment.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // Do the file write
                    writeToFile(data);
                } else {
                    // Request permission from the user
                    ActivityCompat.requestPermissions(mfragment.getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                    //  ...
                    writeToFile(data);
                }

                     /* String msg_strg = new String(data,0);
                    msg_box.setText(msg_strg);
                    Log.d(TAG,"Data received");
*/
                Log.d(TAG,message);





            }
        });

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetooth.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    bluetooth.disconnect();
                } else {
                    Intent intent = new Intent(getActivity(), DeviceList.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
            }
    });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluetooth.send("s");
            }
        });





        return upView;
    }

    public void onStart() {
        super.onStart();
        if (!bluetooth.isBluetoothEnabled()) {
            bluetooth.enable();
        } else {
            if (!bluetooth.isServiceAvailable()) {
                bluetooth.setupService();
                bluetooth.startService(BluetoothState.DEVICE_OTHER);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 0:
                // Re-attempt file write
                Log.d(TAG,"permission granted");
        }
    }


    public void onDestroy() {
        super.onDestroy();
        bluetooth.stopService();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                bluetooth.connect(data);
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bluetooth.setupService();
            } else {
                Toast.makeText(getContext()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        }
    }

    public void writeToFile(byte[] array)
    {


        try {


            if(var==0)
            {//  String path = "/data/data/.txt";
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
                File file = new File(baseDirectory,"extnlfile.txt");
                FileOutputStream stream = /*context.openFileOutput(filename, Context.MODE_PRIVATE);*/new FileOutputStream(file);
                oldstream=stream;
                Toast.makeText(this.getContext(), "File created ...reception started", Toast.LENGTH_SHORT).show();
                var++;
                try {
                    stream.write(array);
                    stream.write('\n');
                    stream.flush();
                    //stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                try {
                    oldstream.write(array);
                    oldstream.write('\n');
                    //stream.close();
                    oldstream.flush();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (FileNotFoundException e1)
        {
            e1.printStackTrace();
        }
        Log.d(TAG,"inside write to file. abt to exit");
    }



    public class UIFragmentUploadComponent {

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
                            mLogView.append(builder);
                            SharedPreferences sharedPreferences = PreferenceManager.
                                    getDefaultSharedPreferences(getActivity());
                            Editable editable = mLogView.getEditableText();
                            int length = editable.length();
                            if (length > MAX_LENGTH) {
                                editable.delete(0, length - LOWER_THRESHOLD);
                            }
                            if (sharedPreferences.getBoolean(SettingsFragment.PREFERENCE_KEY_AUTO_SCROLL,
                                    false /*default return value*/)){
                                mScrollView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mScrollView.fullScroll(View.FOCUS_DOWN);
                                    }
                                });
                            }
                        }
                    });
        }

        public void startActivity(Intent intent) {
            mfragment.getActivity().startActivity(intent);
        }
    }

}


