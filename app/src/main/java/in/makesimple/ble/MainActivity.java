package in.makesimple.ble;

import android.bluetooth.BluetoothGatt;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "S";
    BleDevice newDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BleManager.getInstance().init(getApplication());

        BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(1, 5000)
                .setSplitWriteNum(20)
                .setConnectOverTime(10000)
                .setOperateTimeout(5000);


        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);

// This schedule a runnable task every 5 seconds to get
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                // Connect using mac adds
                BleManager.getInstance().connect("24:0A:C4:97:19:3E", new BleGattCallback() {
                    @Override
                    public void onStartConnect() {
                        Toast.makeText(getApplicationContext(), "Start:", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onConnectFail(BleDevice bleDevice, BleException exception) {

                    }

                    @Override
                    public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                        Toast.makeText(getApplicationContext(), "onConnectSuccess:", Toast.LENGTH_SHORT).show();

                        newDevice = bleDevice;
                    }

                    @Override
                    public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {

                    }
                });
            }
        }, 0, 5, TimeUnit.SECONDS);



        Button r = findViewById(R.id.read);
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BleManager.getInstance().notify(
                        newDevice,
                        "6E400001-B5A3-F393-E0A9-E50E24DCCA9E",  //UUID of device
                        "6E400003-B5A3-F393-E0A9-E50E24DCCA9E",  // NOtify channel
                        new BleNotifyCallback() {
                            @Override
                            public void onNotifySuccess() {
                            }

                            @Override
                            public void onNotifyFailure(BleException exception) {
                            }

                            @Override
                            public void onCharacteristicChanged(byte[] data) {
                                Toast.makeText(getApplicationContext(), "NOTI suss" + Character.toString((char) data[0]), Toast.LENGTH_SHORT).show();

                            }
                        });
            }
        });
    }
}

//24:0A:C4:97:19:3E

//6E400001-B5A3-F393-E0A9-E50E24DCCA9E
