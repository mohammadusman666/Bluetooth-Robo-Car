package com.fcc.myapplication;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class CarControl extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {


    String address = null;
    Button f;
    Button b;
    Button l;
    Button r;

    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_control);

        f=findViewById(R.id.fr);
        b=findViewById(R.id.back);
        l=findViewById(R.id.left);
        r=findViewById(R.id.rite);
        f.setOnTouchListener(this);
        b.setOnTouchListener(this);
        r.setOnTouchListener(this);
        l.setOnTouchListener(this);
        JoystickView joystick = (JoystickView) findViewById(R.id.joystickView);
        joystick.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                // do whatever you want

                if(angle>=45 && angle <136 && strength>10)
                {
                    btsend("F");
                }
                else if (angle>=225 && angle <316 && strength>10)
                {
                    btsend("B");
                }
                else if (angle>=135 && angle <226 && strength>10)
                {
                    btsend("L");
                }
                else if (strength<11)
                {
                    btsend("S");
                }
                else
                {
                    btsend("R");
                }



            }
        },17);
        Intent newint = getIntent();
        address = newint.getStringExtra(connect.EXTRA_ADDRESS); //receive the address of the bluetooth device



        new ConnectBT().execute(); //Call the class to connect




    }

    private void Disconnect()
    {
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.close(); //close connection
            }
            catch (IOException e)
            { msg("Bluetooth Communication Error");
                finish();}
        }
        finish(); //return to the first layout

    }

    void btsend(String str)
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write(str.toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Bluetooth Communication Error");
                finish();
            }
        }
    }

    // fast way to call Toast
    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN)
        {
            if(v==f)
            {
                btsend("F");
            }
            else if(v==b)
            {
                btsend("B");
            }
            else if(v==l)
            {
                btsend("L");
            }
            else if(v==r)
            {
                btsend("R");
            }
        }
        else if(event.getAction() == MotionEvent.ACTION_UP)
        {
            btsend("S");
        }
        return false;
    }


    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(CarControl.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            }
            else
            {
                msg("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }


}
