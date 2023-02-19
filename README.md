Device: Raspberry Pi 4 8GB
Emteria.OS: 13.3.0
BLE device: CC2650 Sensor Tag

Scanning works, but then after I call:

bluetoothLeScanner.stopScan(leScanCallback)
scanResult.device.connectGatt(application, true, gattCallback)
I've also run the Apps on Samsung Tab3 Active Pro running Android 11 without issue.

I see the following in the logs (I've attached files for the full copy of the log error from both test apps)

packages/modules/Bluetooth/system/stack/btm/btm_ble.cc:1720 btm_ble_connected: Updating device record timestamp for existing ble connection
2023-02-19 18:04:47.576  3124-3168  bluetooth               pid-3124                             I  packages/modules/Bluetooth/system/gd/hci/le_address_manager.cc:514 OnCommandComplete: Received command complete with op_code LE_REMOVE_DEVICE_FROM_FILTER_ACCEPT_LIST
2023-02-19 18:04:47.586  3124-3168  bluetooth               pid-3124                             A  assertion 'op_code == OpCode::NONE' failed - Received COMMAND_COMPLETE event with OpCode 0x2016 (LE_READ_REMOTE_FEATURES) without a waiting command(is the HAL sending commands, but not handling the events?)
2023-02-19 18:04:47.587  3124-3168  libc                    pid-3124                             A  Fatal signal 6 (SIGABRT), code -1 (SI_QUEUE) in tid 3168 (bt_stack_manage), pid 3124 (droid.bluetooth)
2023-02-19 18:04:47.687  3298-3298  crash_dump64            pid-3298                             I  obtaining output fd from tombstoned, type: kDebuggerdTombstoneProto
2023-02-19 18:04:47.690   244-244   tombstoned              pid-244                              I  received crash request for pid 3168
2023-02-19 18:04:47.691  3298-3298  crash_dump64            pid-3298                             I  performing dump of process 3124 (target tid = 3168)
2023-02-19 18:04:47.711  3298-3298  DEBUG                   pid-3298                             E  failed to read process info: failed to open /proc/3124
2023-02-19 18:04:48.183  3298-3298  crash_dump64            pid-3298                             I  type=1400 audit(0.0:1337): avc: denied { read } for name="bluetooth_db-shm" dev="mmcblk0p16" ino=311770 scontext=u:r:crash_dump:s0 tcontext=u:object_r:bluetooth_data_file:s0 tclass=file permissive=1
2023-02-19 18:04:48.183  3298-3298  crash_dump64            pid-3298                             I  type=1400 audit(0.0:1338): avc: denied { open } for path="/data/user_de/0/com.android.bluetooth/databases/bluetooth_db-shm" dev="mmcblk0p16" ino=311770 scontext=u:r:crash_dump:s0 tcontext=u:object_r:bluetooth_data_file:s0 tclass=file permissive=1
2023-02-19 18:04:48.183  3298-3298  crash_dump64            pid-3298                             I  type=1400 audit(0.0:1339): avc: denied { getattr } for path="/data/user_de/0/com.android.bluetooth/databases/bluetooth_db-shm" dev="mmcblk0p16" ino=311770 scontext=u:r:crash_dump:s0 tcontext=u:object_r:bluetooth_data_file:s0 tclass=file permissive=1
2023-02-19 18:04:48.183  3298-3298  crash_dump64            pid-3298                             I  type=1400 audit(0.0:1340): avc: denied { map } for path="/data/user_de/0/com.android.bluetooth/databases/bluetooth_db-shm" dev="mmcblk0p16" ino=311770 scontext=u:r:crash_dump:s0 tcontext=u:object_r:bluetooth_data_file:s0 tclass=file permissive=1
2023-02-19 18:04:48.214  3298-3298  crash_dump64            pid-3298                             I  type=1400 audit(0.0:1341): avc: denied { read } for name="data@resource-cache@com.android.systemui-accent-o8xL.frro@idmap" dev="mmcblk0p16" ino=139281 scontext=u:r:crash_dump:s0 tcontext=u:object_r:resourcecache_data_file:s0 tclass=file permissive=1
2023-02-19 18:04:48.214  3298-3298  crash_dump64            pid-3298                             I  type=1400 audit(0.0:1342): avc: denied { open } for path="/data/resource-cache/data@resource-cache@com.android.systemui-accent-o8xL.frro@idmap" dev="mmcblk0p16" ino=139281 scontext=u:r:crash_dump:s0 tcontext=u:object_r:resourcecache_data_file:s0 tclass=file permissive=1
2023-02-19 18:04:48.309  3298-3298  DEBUG                   pid-3298                             A  *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***
2023-02-19 18:04:48.309  3298-3298  DEBUG                   pid-3298                             A  Build fingerprint: 'emteria/rpi4/rpi4:13/TP1A.220624.021.A1/eng.root.20230209.120150:user/release-keys'
2023-02-19 18:04:48.309  3298-3298  DEBUG                   pid-3298                             A  Revision: '0'
2023-02-19 18:04:48.309  3298-3298  DEBUG                   pid-3298                             A  ABI: 'arm64'
2023-02-19 18:04:48.309  3298-3298  DEBUG                   pid-3298                             A  Timestamp: 2023-02-19 18:04:47.710649441-0500
2023-02-19 18:04:48.309  3298-3298  DEBUG                   pid-3298                             A  Process uptime: 0s
2023-02-19 18:04:48.309  3298-3298  DEBUG                   pid-3298                             A  Cmdline: com.android.bluetooth
2023-02-19 18:04:48.309  3298-3298  DEBUG                   pid-3298                             A  pid: 3124, tid: 3168, name: bt_stack_manage  >>> com.android.bluetooth <<<
2023-02-19 18:04:48.309  3298-3298  DEBUG                   pid-3298                             A  uid: 1002
2023-02-19 18:04:48.309  3298-3298  DEBUG                   pid-3298                             A  tagged_addr_ctrl: 0000000000000001 (PR_TAGGED_ADDR_ENABLE)
2023-02-19 18:04:48.309  3298-3298  DEBUG                   pid-3298                             A  signal 6 (SIGABRT), code -1 (SI_QUEUE), fault addr --------
2023-02-19 18:04:48.309  3298-3298  DEBUG                   pid-3298                             A  Abort message: 'assertion 'op_code == OpCode::NONE' failed - Received COMMAND_COMPLETE event with OpCode 0x2016 (LE_READ_REMOTE_FEATURES) without a waiting command(is the HAL sending commands, but not handling the events?)'
2023-02-19 18:04:48.309  3298-3298  DEBUG                   pid-3298                             A      x0  0000000000000000  x1  0000000000000c60  x2  0000000000000006  x3  0000006da5e84da0
2023-02-19 18:04:48.309  3298-3298  DEBUG                   pid-3298                             A      x4  0000000000000000  x5  0000000000000000  x6  0000000000000000  x7  000000001284bda1
2023-02-19 18:04:48.309  3298-3298  DEBUG                   pid-3298                             A      x8  00000000000000f0  x9  00000070d0575a00  x10 0000000000000001  x11 00000070d05b6950
2023-02-19 18:04:48.309  3298-3298  DEBUG                   pid-3298                             A      x12 0000006da5e83c70  x13 00000000000000cf  x14 0000006da5e85000  x15 0000000000000000
2023-02-19 18:04:48.309  3298-3298  DEBUG                   pid-3298                             A      x16 00000070d0624d60  x17 00000070d05ffa20  x18 0000006da4e8a038  x19 00000000000000ac
2023-02-19 18:04:48.309  3298-3298  DEBUG                   pid-3298                             A      x20 00000000000000b2  x21 0000000000000c34  x22 0000000000000c60  x23 00000000ffffffff
2023-02-19 18:04:48.310  3298-3298  DEBUG                   pid-3298                             A      x24 0000006da5e86000  x25 0000006da5e85500  x26 0000006dbed4a618  x27 0000006e34416000
2023-02-19 18:04:48.310  3298-3298  DEBUG                   pid-3298                             A      x28 0000000000000000  x29 0000006da5e84e20
2023-02-19 18:04:48.310  3298-3298  DEBUG                   pid-3298                             A      lr  00000070d05a72f4  sp  0000006da5e84d80  pc  00000070d05a7324  pst 0000000000000000
2023-02-19 18:04:48.310  3298-3298  DEBUG                   pid-3298                             A  backtrace:
2023-02-19 18:04:48.310  3298-3298  DEBUG                   pid-3298                             A        #00 pc 0000000000053324  /apex/com.android.runtime/lib64/bionic/libc.so (abort+180) (BuildId: 3bb3bed8953efcf72e0c16e90a83355e)
2023-02-19 18:04:48.310  3298-3298  DEBUG                   pid-3298                             A        #01 pc 000000000062b704  /apex/com.android.art/lib64/libart.so (art::Runtime::Abort(char const*)+116) (BuildId: 02d928b697320e3df5fc9038332319ff)
2023-02-19 18:04:48.310  3298-3298  DEBUG                   pid-3298                             A        #02 pc 0000000000017560  /system/lib64/libbase.so (android::base::SetAborter(std::__1::function<void (char const*)>&&)::$_3::__invoke(char const*)+80) (BuildId: 167481ebf6d9d15c317feb684fe496e7)
2023-02-19 18:04:48.310  3298-3298  DEBUG                   pid-3298                             A        #03 pc 0000000000006f24  /system/lib64/liblog.so (__android_log_assert+308) (BuildId: 7d98d43a559683c93b06c31a1cd05780)
2023-02-19 18:04:48.310  3298-3298  DEBUG                   pid-3298                             A        #04 pc 00000000008b4268  /apex/com.android.btservices/lib64/libbluetooth_jni.so (bluetooth::hci::HciLayer::impl::on_hci_event(bluetooth::hci::EventView)+4024) (BuildId: 50d4f30faa8acf1f25a67a7ab9e7f638)
2023-02-19 18:04:48.310  3298-3298  DEBUG                   pid-3298                             A        #05 pc 00000000008b4408  /apex/com.android.btservices/lib64/libbluetooth_jni.so (void base::internal::FunctorTraits<void (bluetooth::hci::HciLayer::impl::*)(bluetooth::hci::EventView), void>::Invoke<void (bluetooth::hci::HciLayer::impl::*)(bluetooth::hci::EventView), bluetooth::hci::HciLayer::impl*, bluetooth::hci::EventView>(void (bluetooth::hci::HciLayer::impl::*)(bluetooth::hci::EventView), bluetooth::hci::HciLayer::impl*&&, bluetooth::hci::EventView&&)+296) (BuildId: 50d4f30faa8acf1f25a67a7ab9e7f638)
2023-02-19 18:04:48.310  3298-3298  DEBUG                   pid-3298                             A        #06 pc 00000000008b42a8  /apex/com.android.btservices/lib64/libbluetooth_jni.so (base::internal::Invoker<base::internal::BindState<void (bluetooth::hci::HciLayer::impl::*)(bluetooth::hci::EventView), base::internal::UnretainedWrapper<bluetooth::hci::HciLayer::impl>, bluetooth::hci::EventView>, void ()>::RunOnce(base::internal::BindStateBase*)+56) (BuildId: 50d4f30faa8acf1f25a67a7ab9e7f638)
2023-02-19 18:04:48.310  3298-3298  DEBUG                   pid-3298                             A        #07 pc 00000000009a3cbc  /apex/com.android.btservices/lib64/libbluetooth_jni.so (bluetooth::os::Handler::handle_next_event()+236) (BuildId: 50d4f30faa8acf1f25a67a7ab9e7f638)
2023-02-19 18:04:48.310  3298-3298  DEBUG                   pid-3298                             A        #08 pc 00000000009fa498  /apex/com.android.btservices/lib64/libbluetooth_jni.so (bluetooth::os::Reactor::Run()+584) (BuildId: 50d4f30faa8acf1f25a67a7ab9e7f638)
2023-02-19 18:04:48.310  3298-3298  DEBUG                   pid-3298                             A        #09 pc 00000000009fb220  /apex/com.android.btservices/lib64/libbluetooth_jni.so (bluetooth::os::Thread::run(bluetooth::os::Thread::Priority)+176) (BuildId: 50d4f30faa8acf1f25a67a7ab9e7f638)
2023-02-19 18:04:48.310  3298-3298  DEBUG                   pid-3298                             A        #10 pc 00000000009fb3c4  /apex/com.android.btservices/lib64/libbluetooth_jni.so (void* std::__1::__thread_proxy<std::__1::tuple<std::__1::unique_ptr<std::__1::__thread_struct, std::__1::default_delete<std::__1::__thread_struct> >, void (bluetooth::os::Thread::*)(bluetooth::os::Thread::Priority), bluetooth::os::Thread*, bluetooth::os::Thread::Priority> >(void*)+68) (BuildId: 50d4f30faa8acf1f25a67a7ab9e7f638)
2023-02-19 18:04:48.310  3298-3298  DEBUG                   pid-3298                             A        #11 pc 00000000000c1e50  /apex/com.android.runtime/lib64/bionic/libc.so (__pthread_start(void*)+208) (BuildId: 3bb3bed8953efcf72e0c16e90a83355e)
2023-02-19 18:04:48.310  3298-3298  DEBUG                   pid-3298                             A        #12 pc 0000000000054b50  /apex/com.android.runtime/lib64/bionic/libc.so (__start_thread+64) (BuildId: 3bb3bed8953efcf72e0c16e90a83355e)
2023-02-19 18:04:48.408   244-244   tombstoned              pid-244                              E  Tombstone written to: tombstone_07
2023-02-19 18:04:48.430   479-543   NativeTombstoneManager  pid-479                              E  Tombstone's UID (1002) not an app, ignoring
2023-02-19 18:04:48.445   479-543   BootReceiver            pid-479                              I  Copying /data/tombstones/tombstone_07 to DropBox (SYSTEM_TOMBSTONE)
2023-02-19 18:04:48.447   479-543   DropBoxManagerService   pid-479                              I  add tag=SYSTEM_TOMBSTONE isTagEnabled=true flags=0x6
2023-02-19 18:04:48.454   479-3302  DropBoxManagerService   pid-479                              I  add tag=system_app_native_crash isTagEnabled=true flags=0x2
2023-02-19 18:04:48.458   279-279   android.ha....1-btlinux pid-279                              E  BluetoothDeathRecipient::serviceDied - Bluetooth service died