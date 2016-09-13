package a4336.a0.lab4.james.lab8;

import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;

/**
 * Created by james on 13/09/2016.
 * Class implements PeerListListener for Wifi2P2 manager. Enables easier access to list of discovered devices.
 * Although won't be fully implemented until testing finished on current peer list code in MainActivity.
 */
public class PeerListListener implements WifiP2pManager.PeerListListener {

    @Override
    public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {


    }
}
