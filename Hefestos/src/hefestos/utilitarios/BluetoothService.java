package hefestos.utilitarios;

import hefestos.interfaceusuario.*;
import hefestos.webservice.WebService;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import android.app.Notification;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

/*
 Created by Remor
 */
public class BluetoothService extends Service {
	


	Message msg;		
	boolean envi_indoor = false;
	
	// private static int REQUEST_ENABLE_BT = 1;
	public BluetoothAdapter adapter;
	// private final Handler mHandler = new Handler();
	private Thread t;
	// Handler handler;
	private BluetoothSocket socket;
	RecursosLista objRecList = new RecursosLista();	
	private static final UUID MY_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");
	private InputStream mmInStream = null;

	RecursosLista objRL;

	
	//MOSTRA TAG LIDA COM TOAST
	//***************************************
	private final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.arg1 == 1) {
				String aux = Integer.toString(msg.arg2);
				Toast.makeText(getApplicationContext(), aux, Toast.LENGTH_SHORT).show();
					
			}
		}
	};
	//***************************************

	/*
	 * new Thread(new Runnable() { public void run() { AlertDialog.Builder alert
	 * = new AlertDialog.Builder(getApplicationContext());
	 * alert.setTitle("Status");
	 * alert.setMessage("Upload finalizado com sucesso");
	 * alert.setNeutralButton("Talvez", null); alert.setNegativeButton("Nao",
	 * null); alert.setPositiveButton("OKay", null); alert.show();
	 * 
	 * //Intent i = new Intent(this, CadastroActivity.class);
	 * //startActivity(i); } } });
	 */

	
	//CONSTRUTOR DA CLASSE
	@Override
	public void onCreate() {
		//ATIVACAO BLUETOOTH DO DISPOSITIVO MOVEL
		//***************************************
		Log.i("BT", "ONCREATE BLUETOOTH SERVICE");
		super.onCreate();
		adapter = BluetoothAdapter.getDefaultAdapter();
		if (adapter == null) {
			Toast.makeText(this, "Bluetooth is not available",Toast.LENGTH_LONG).show();
			this.stopSelf();
			return;
		}

		if (!adapter.isEnabled()) {
			adapter.enable();
			// Intent enableIntent = new
			// Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			// startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
		}
		adapter.cancelDiscovery();
		t = new Thread();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//***************************************
	}

	@Override
	public void onStart(Intent intent, int startId) {
		//CONEXAO BLUETOOTH COM MODULO INDOOR	
		//***************************************
		Log.i("BT", "ONSTART BLUETOOTH SERVICE");
		super.onStart(intent, startId);
		// serve para impedir que o android mate o serviço
		this.startForeground(startId, new Notification());

		// BluetoothDevice device =
		// adapter.getRemoteDevice("00:19:A4:03:CE:8E");
		BluetoothDevice device = adapter.getRemoteDevice("00:11:11:13:40:83");
		BluetoothSocket socket = null;		
		//***************************************
		
		
		try {
			//MENSAGEM CONEXAO SUCESSO
			Log.e("BT", "CONECTANDO");
			socket = device.createRfcommSocketToServiceRecord(MY_UUID);
			socket.connect();
			Log.e("BT", "CONECTOU");

			InputStream tmpIn = null;
			try {
				tmpIn = socket.getInputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
			mmInStream = tmpIn;

			//THREAD PARA A LEITURA DE TAGS
			//***************************************
			t = new Thread(new Runnable() {
				public void run() {
					int bytes;
					String data;
					String tag_pai = "";
					while (true) {
						try {
							data = "";
							while (data.length() != 8) {
								bytes = mmInStream.read();
								data += (char) bytes;

								if (data.length() == 8) {
									Log.i("DATA", data);									
									
									msg = handler.obtainMessage();
									msg.arg1=1;
									msg.arg2=Integer.parseInt(data);
							        handler.sendMessage(msg);
									
									WebService wsResource = new WebService();		
									String retorno_WS = wsResource.consultaAmb(data);
									

									if(!envi_indoor){	
										
										envi_indoor = true;
										tag_pai = retorno_WS;								

										Intent abc = new Intent(getApplicationContext(), RecursosListaIn.class);
								        abc.putExtra("idTag", retorno_WS);
								        abc.putExtra("geo", objRecList.getCoordenadas());
								        abc.addFlags(abc.FLAG_ACTIVITY_NEW_TASK);
								        startActivity(abc);	
									}
									else{
										if(tag_pai.equals(retorno_WS))
										{
											envi_indoor = false;
											Intent abc = new Intent(getApplicationContext(), RecursosLista.class);
									        abc.addFlags(abc.FLAG_ACTIVITY_NEW_TASK);
									        startActivity(abc);									        
										}
										else{
											Intent abc = new Intent(getApplicationContext(), RecursosListaIn.class);
									        abc.putExtra("idTag", retorno_WS);
									        abc.putExtra("geo", objRecList.getCoordenadas());
									        abc.addFlags(abc.FLAG_ACTIVITY_NEW_TASK);
									        startActivity(abc);
										}
										
									}
							        
							        
								}								
							}
							Thread.sleep(100);
						} catch (IOException e) {
							break;
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			});
			//***************************************
			t.start();
			
	    //CONEXAO FALHA
		} catch (IOException e) {
			e.printStackTrace();
			this.onDestroy();
		}

	}

	// DESTRUTOR DA CLASSE
	//***************************************
	@Override
	public void onDestroy() {
		try {
			if (socket != null) {
				socket.close();
			}
			if (t != null) {
				// t.stop();
				t = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}
	//***************************************

	//METODO BASE PARA SERVICES
	//***************************************
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	//***************************************
}
