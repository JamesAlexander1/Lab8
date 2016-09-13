package a4336.a0.lab4.james.lab8;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private WifiP2pManager WD_Manager;
    WifiP2pManager.Channel channel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WD_Manager = (WifiP2pManager)getSystemService(Context.WIFI_P2P_SERVICE);
        channel = WD_Manager.initialize(this, getMainLooper(), null);


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
    @Override
    public void onPause(){
        super.onPause();


    }

    public void runDiscovery(){

        final List<WifiP2pDevice> list = new ArrayList<WifiP2pDevice>();
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
                    //list.add(device.deviceName + '\n' + device.deviceAddress + '\n' + device.primaryDeviceType + '\n' + device.secondaryDeviceType + '\n' + device.wpsDisplaySupported());
                    list.add(device);

                }else if(WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION.equals(action)){

                    //check if discovery has completed.
                    int state = intent.getIntExtra(WifiP2pManager.EXTRA_DISCOVERY_STATE, 1);

                    if(state == WifiP2pManager.WIFI_P2P_DISCOVERY_STOPPED){
                        //this method displays peer list to listView.
                        unregisterReceiver(this);
                        endDiscovery(list);
                    }
                }
            }
        };

        //register Receiver

        registerReceiver(broadcastReceiver, intentFilter);

        //initiate peer discovery

        WD_Manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                // code for peer discovery goes in onRecieve method
            }

            @Override
            public void onFailure(int i) {
                //alert user that something went wrong
            }
        });
    }

    public void endDiscovery(List<WifiP2pDevice> list){

        //end discovery and display results to ListView.
        List<String> stringList = new ArrayList<String>();
        Iterator<WifiP2pDevice> iterator = list.iterator();

        while(iterator.hasNext()){
            WifiP2pDevice device = iterator.next();
            stringList.add(device.deviceName + '\n' + device.deviceAddress + '\n' + device.primaryDeviceType + '\n' + device.secondaryDeviceType + '\n' + device.wpsDisplaySupported());

        }
       final Iterator<WifiP2pDevice> iterator2 = list.iterator();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.activity_main, stringList);
        ListView deviceList = (ListView) findViewById(R.id.DiscoverList);

        deviceList.setAdapter(adapter);

        //initalise functionality for connecting to devices specified in ListView.

        deviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startConnect(adapterView, iterator2, i);
            }
        });
    }

    //code for starting connection init. process when clicking on item in ListView.
    public void startConnect(AdapterView<?> adapterView, Iterator<WifiP2pDevice> iterator, int pos){

            int i = 0;
            WifiP2pDevice device = iterator.next();

            while (iterator.hasNext()){
                if(i == pos){
                    connectToPeer(device);
                    break;
                }
                device = iterator.next();
                i++;
            }

            //continued.
    }


    //code for testing initial connection to peer
    public void connectToPeer(WifiP2pDevice device){

        final TextView statusText = (TextView) findViewById(R.id.statusText);

        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        WD_Manager.connect(channel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                //on success
                String temp = "connection successful";
                statusText.setText(temp);
            }

            @Override
            public void onFailure(int i) {
                //on failure
                String temp = "connection DENIED";
                statusText.setText(temp);
            }
        });
    }
}
