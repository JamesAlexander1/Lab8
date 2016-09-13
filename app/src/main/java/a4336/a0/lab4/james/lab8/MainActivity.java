package a4336.a0.lab4.james.lab8;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private WifiP2pManager WD_Manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WD_Manager = (WifiP2pManager)getSystemService(Context.WIFI_P2P_SERVICE);
        final Button statusButton = (Button) findViewById(R.id.StatusButton);

        statusButton.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View view) {

                TextView statusText = (TextView) findViewById(R.id.statusText);
                if(WD_Manager.WIFI_P2P_STATE_ENABLED != 2){

                    String temp = "WifiP2P not enabled";
                    statusText.setText(temp);


                }else{
                    String temp = "WifiP2P enabled";
                    statusText.setText(temp);
                }
            }
        });

        final Button discoverButton = (Button) findViewById(R.id.DiscoverPeers);

        discoverButton.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View view) {
                runDiscovery();
            }
        });
    }

    public void runDiscovery(){

        final List<String> list = new ArrayList<String>();
        //first, have application listen for intents.

        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        // now create new broadcastReceiver to listen for changes to the System's Wifi P2P state.
        //adding conditions to handle each P2P state change above.

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String action = intent.getAction();

                //add code for other changes to Wifi P2P state.

                if(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)){

                    WifiP2pDevice device = (WifiP2pDevice) intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
                    list.add(device.deviceName + '\n' + device.deviceAddress + '\n' + device.primaryDeviceType + '\n' + device.secondaryDeviceType + '\n' + device.wpsDisplaySupported());
                }
            }
        };
    }
}
