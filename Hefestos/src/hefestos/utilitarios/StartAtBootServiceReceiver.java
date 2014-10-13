package hefestos.utilitarios;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class StartAtBootServiceReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			
			// MENSAGEM DE CONFIRMACAO PARA ATIVACAO EM BOOT
			Log.i("BOOT","--------------------------- BOOT COMPLETED!! ---------------------------");
			
			Log.i("BOOT","--------------------------- BLUETOOTH!! ---------------------------");
			
			// CONECTA NO ARDUINO VIA BLUETOOTH
			Intent intBluetooth = new Intent("BluetoothService");			
			context.startService(intBluetooth);
			
			try {
				Thread.sleep(15000);
			} catch (InterruptedException e2) {
				e2.printStackTrace();
			}	
			
			/*Log.i("BOOT","--------------------------- GPS!! ---------------------------");
			Intent intGPS = new Intent("AndroidGPSService");
			context.startService(intGPS);*/
			
		}
	}
}
