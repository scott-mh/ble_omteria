package com.sdc.bleomteria

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.* // ktlint-disable no-wildcard-imports
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sdc.bleomteria.ui.theme.BleOmteriaTheme

class MainActivity : ComponentActivity() {

    private val messages = mutableStateListOf<String>()
    private var isConnecting = false

    private lateinit var bluetoothManager: BluetoothManager

    private lateinit var bluetoothAdapter: BluetoothAdapter

    private lateinit var bluetoothLeScanner: BluetoothLeScanner

    private val scanSettings: ScanSettings by lazy {
        ScanSettings.Builder()
            .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
            .setLegacy(false)
            .setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE)
            .setNumOfMatches(ScanSettings.MATCH_NUM_MAX_ADVERTISEMENT)
            .setPhy(ScanSettings.PHY_LE_ALL_SUPPORTED)
            .setReportDelay(0)
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()
    }

    private val scanFilters: MutableList<ScanFilter> by lazy {
        val scanFilters = mutableListOf<ScanFilter>(
            ScanFilter.Builder()
                .setDeviceName("CC2650 SensorTag")
                .build()
        )
        scanFilters
    }

    private val leScanCallback = object : ScanCallback() {
        @SuppressLint("MissingPermission")
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            updateMessages("onScanResult")
            if (isConnecting) {
                updateMessages("onScanResult: Already connected")
                return
            }
            result?.let { scanResult ->
                isConnecting = true
                updateMessages("onScanResult device: ${scanResult.device.name}")
                stopScan()
                scanResult.device.connectGatt(application, false, gattCallback)
            }
        }
        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            updateMessages("onScanFailed errorCode: $errorCode")
        }
    }

    private val gattCallback = object : BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(
            gatt: BluetoothGatt?,
            status: Int,
            newState: Int
        ) {
            super.onConnectionStateChange(gatt, status, newState)
            updateMessages("onConnectionStateChange")

            if (gatt == null) {
                updateMessages("onConnectionStateChange Gatt null")
                return
            }
            if (status != BluetoothGatt.GATT_SUCCESS) {
                updateMessages("onConnectionStateChange status issue: $status")
                gatt.disconnect()
                gatt.close()
            }

            when (newState) {
                BluetoothProfile.STATE_CONNECTING -> {
                    updateMessages("onConnectionStateChange: STATE_CONNECTING")
                }
                BluetoothProfile.STATE_CONNECTED -> {
                    updateMessages("onConnectionStateChange: STATE_CONNECTED")
                    gatt.discoverServices()
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    updateMessages("onConnectionStateChange: DISCONNECTED")
                    gatt.disconnect()
                    gatt.close()
                }
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
            updateMessages("onServicesDiscovered")
            if (status != BluetoothGatt.GATT_SUCCESS || gatt == null) {
                updateMessages("onServicesDiscovered status: $status")
                return
            }

            val services = gatt.services ?: return

            for (srv in services) {
                updateMessages("onServicesDiscovered service: ${srv.uuid}")
                for (char in srv.characteristics) {
                    updateMessages("onServicesDiscovered char: ${char.uuid}")
                    for (desc in char.descriptors) {
                        updateMessages("onServicesDiscovered descriptor: ${desc.uuid}")
                    }
                }
            }
        }
    }

    private fun updateMessages(message: String) {
        messages.add(message)
    }

    @SuppressLint("MissingPermission")
    private fun stopScan() {
        updateMessages("Scan Stopping")
        bluetoothLeScanner.stopScan(leScanCallback)
    }

    @SuppressLint("MissingPermission")
    private fun startScan() {
        updateMessages("Scan Starting")
        bluetoothLeScanner.startScan(
            scanFilters,
            scanSettings,
            leScanCallback
        )
    }

    override fun onStart() {
        super.onStart()
        bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE)
            as BluetoothManager

        bluetoothAdapter = bluetoothManager.adapter

        bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BleOmteriaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column() {
                        Row() {
                            Spacer(modifier = Modifier.weight(1.0f))
                            Button(onClick = { startScan() }) {
                                Text("Start")
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Button(onClick = { stopScan() }) {
                                Text("Stop")
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                        }
                        LazyColumn() {
                            items(messages) { message ->
                                Text(message)
                            }
                        }
                    }
                }
            }
        }
    }
}

