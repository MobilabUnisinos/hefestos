package hefestos.interfaceusuario;

import hefestos.utilitarios.BluetoothService;
import hefestos.webservice.WebService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import unisinos.br.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RecursosLista extends Activity {
	final Context context = this;
	private String keyword;
	private AlertDialog alerta;
	private LocationManager lm;
	private LocationListener locationListener;
	private double auxLatitude = -29.792972000000002;
	private double auxLongitude = -51.15281066666667;
	public Handler mHandler = new Handler();
	Geocoder geocoder;	

	// Definicao de variaveis ListView
	static int NUM_OF_DISPLAYED_RESOURCES = 5;
	static String CARACTER_DIVISOR = "+";
	private List<Map<String, Object>> list_recurso;
	Map<String, Object> item_recurso;
	String[] recursos_1 = { "icon", "recurso", "desc", "dist" };
	int[] recursos_2 = { R.id.recursoIcon, R.id.recursoNome, R.id.recursoDesc,
			R.id.recursoDist };
	ListView listview;
	AdapterListView adapterListView;
	WebService wsResource;
	String string_Coordenada;
	private ArrayList<ItemListView> itens;

	public void updateIndoor(String data) {
		// mHandler.removeCallbacks(mUpdateTimeTask);

		wsResource = new WebService();
		String retorno_WS = wsResource.consultaOut("-29.0+-29.0",
				NUM_OF_DISPLAYED_RESOURCES);
		/* Toast.makeText(getBaseContext(),"aaaa",Toast.LENGTH_SHORT).show(); */

		listview = (ListView) findViewById(R.id.listView1);
		createListView(retorno_WS);
	}

	public void updateKeyword(String data) {
		// mHandler.removeCallbacks(mUpdateTimeTask);

		wsResource = new WebService();
		String retorno_WS = wsResource.consultaOut("-29.0+-29.0",
				NUM_OF_DISPLAYED_RESOURCES);
		/* Toast.makeText(getBaseContext(),"aaaa",Toast.LENGTH_SHORT).show(); */

		listview = (ListView) findViewById(R.id.listView1);
		createListView(retorno_WS);
	}

	public void onCreate(Bundle savedInstanceState) {
		// INICIALIZACAO TELA PRINCIPAL
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// SINCRONIZACAO BLUETOOTH
		startService(new Intent(this, BluetoothService.class));	

		// ************************************
		// USA CLASSE LocationManager PARA OBTER LOCALIZACAO GPS
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationListener = new MyLocationListener();
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0,
				locationListener);
		// ************************************

		mHandler.removeCallbacks(mUpdateTimeTask);
		mHandler.postDelayed(mUpdateTimeTask, 10000);

		// ************************************
		// BOTAO DE PESQUISA
		// Definicao da imagem
		ImageButton btConsultar = (ImageButton) findViewById(R.id.ibConsultar);

		btConsultar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AlertDialog.Builder alert = new AlertDialog.Builder(context);
				alert.setTitle("Insira o recurso que você deseja procurar:");
				final EditText input = new EditText(context);

				alert.setView(input);

				alert.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								keyword = input.getEditableText().toString();
								Toast.makeText(context, keyword,
										Toast.LENGTH_LONG).show();
								Intent abc = new Intent(
										getApplicationContext(),
										RecursosKeyword.class);
								abc.putExtra("idTag", keyword);
								string_Coordenada = String.valueOf(auxLatitude)
										+ "+" + String.valueOf(auxLongitude);
								abc.putExtra("geo", string_Coordenada);
								abc.addFlags(abc.FLAG_ACTIVITY_NEW_TASK);
								startActivity(abc);
							}
						});

				alert.setNegativeButton("CANCEL",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.cancel();
							}
						});

				AlertDialog alertDialog = alert.create();
				alertDialog.show();
			}
		});
		// ************************************

		// ************************************
		// BOTAO DE MAPA
		// Definicao da imagem
		ImageButton btMapa = (ImageButton) findViewById(R.id.ibMapa);
		
		btMapa.setOnClickListener(new OnClickListener() { public void
		onClick(View v) {
			//Toast.makeText(getBaseContext(),"Mapa!",Toast.LENGTH_SHORT).show(); 
			Intent intentIniciarMapa = new Intent(getApplicationContext(),Mapa.class);
			intentIniciarMapa.putExtra("idMapToLoad", "outdoor1");
			intentIniciarMapa.putExtra("lat", auxLatitude);
			intentIniciarMapa.putExtra("lon", auxLongitude);
			intentIniciarMapa.addFlags(intentIniciarMapa.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intentIniciarMapa);
		}});

		// ************************************

		// ************************************
		// BOTÃO DE PREFERENCIAS
		// Definicao da imagem
		ImageButton btPreferencia = (ImageButton) findViewById(R.id.ibPreferencia);
		/*
		 * btPreferencia.setOnClickListener(new OnClickListener() { public void
		 * onClick(View v) { Toast.makeText(getBaseContext(),"Preferencias!",
		 * Toast.LENGTH_SHORT).show(); } });
		 */
		// ************************************

		// ************************************
		// BOTÃO DE SOS
		// Definicao da imagem
		ImageButton btSos = (ImageButton) findViewById(R.id.ibSos);

		btSos.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
 				AlertDialog.Builder bd = new AlertDialog.Builder(context);
				bd.setTitle("Voce realmente deseja enviar um SMS de socorro?");
				bd.setPositiveButton("Sim",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {

								wsResource = new WebService();
								String retorno_WS = wsResource.consultaSOS("1");

								int nm = retorno_WS.indexOf(";");
								String nome = retorno_WS.substring(0, nm);
								String telefone = retorno_WS.substring(nm + 1);
								
								List<Address> addresses = null;
								geocoder = new Geocoder(getBaseContext());
								
								try {
									addresses = geocoder.getFromLocation(auxLatitude, auxLongitude, 1);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								String address = "null";
								address = addresses.get(0).getAddressLine(0);
								String city = "null";
								city = addresses.get(0).getAddressLine(1);
								String country = "null";
								country = addresses.get(0).getAddressLine(2);
								
								String sms = "Chamada " + nome + " em: " + address;
								if(sms.length()>70){
									Toast.makeText(context, "Mensagem muito longa " + sms.length(),Toast.LENGTH_SHORT).show();
								}
								else{
									Toast.makeText(context, "Mensagem SOS enviada, localizacao atual: " + address,Toast.LENGTH_LONG).show();
								}
								SmsManager smsManager = SmsManager.getDefault();
								smsManager.sendTextMessage(telefone, null, sms, null, null);								
								
								
							}
						});

				bd.setNegativeButton("Não",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
								Toast.makeText(context, "Mensagem nao enviada",
										Toast.LENGTH_SHORT).show();
							}
						});
				alerta = bd.create();
				alerta.show();
			}
		});

	}

	// Metodo que adiciona itens no array do ListView
	// ************************************
	private List<? extends Map<String, ?>> carregarLista(String retorno){

		list_recurso = new ArrayList<Map<String, Object>>();

		String[] recursos = new String[NUM_OF_DISPLAYED_RESOURCES];
		recursos = retorno.split(";");
		String[] recursos_splited = new String[10];
		String aux = "";

		for (int i = 0; i < NUM_OF_DISPLAYED_RESOURCES; i++) {
			int id = recursos[i].indexOf(CARACTER_DIVISOR);
			int nm = recursos[i].indexOf(CARACTER_DIVISOR, id + 1);
			int ds = recursos[i].indexOf(CARACTER_DIVISOR, nm + 1);
			int lt = recursos[i].indexOf(CARACTER_DIVISOR, ds + 1);
			int lg = recursos[i].indexOf(CARACTER_DIVISOR, lt + 1);
			int mt = recursos[i].indexOf(CARACTER_DIVISOR, lg + 1);

			String Id_TipoRecurso = recursos[i].substring(0, id);
			String nome = recursos[i].substring(id + 1, nm);
			String descricao = recursos[i].substring(nm + 1, ds);
			String latitude = recursos[i].substring(ds + 1, lt);
			String longitude = recursos[i].substring(lt + 1, lg);
			String metros = recursos[i].substring(lg + 1);

			item_recurso = new HashMap<String, Object>();

			switch (Integer.parseInt(Id_TipoRecurso)) {
			case 1:
				item_recurso.put("icon", R.drawable.parking);
				break;
			case 2:
				item_recurso.put("icon", R.drawable.ramp);
				break;
			case 3:
				item_recurso.put("icon", R.drawable.elevator);
				break;
			case 4:
				item_recurso.put("icon", R.drawable.toilet);
				break;
			case 5:
				item_recurso.put("icon", R.drawable.general);
				break;
			case 6:
				item_recurso.put("icon", R.drawable.general);
				break;
			case 7:
				item_recurso.put("icon", R.drawable.food);
				break;
			case 8:
				item_recurso.put("icon", R.drawable.telephone);
				break;
			case 9:
				item_recurso.put("icon", R.drawable.bank);
				break;
			case 10:
				item_recurso.put("icon", R.drawable.general);
				break;
			case 11:
				item_recurso.put("icon", R.drawable.general);
				break;
			case 12:
				item_recurso.put("icon", R.drawable.general);
				break;
			default:
				item_recurso.put("icon", R.drawable.general);
				break;
			}

			item_recurso.put("recurso", nome);
			item_recurso.put("desc", descricao);
			item_recurso.put("dist", metros + " metros");
			list_recurso.add(item_recurso);
		}
		;

		return list_recurso;
	}

	// ************************************

	// ************************************
	// CLASSE MyLocationListener QUE LIGA COORDENADAS A VARIAVEIS
	private class MyLocationListener implements LocationListener {
		public void onLocationChanged(Location loc) {
			if (loc != null) {
				auxLatitude = loc.getLatitude();
				auxLongitude = loc.getLongitude();
				/* Log.i("BOOT",String.valueOf(auxLatitude)); */
			}
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}

	// ************************************

	// ************************************
	// CRIACAO MENU NO BOTAO MENU
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, "Cadastrar recurso");
		return true;
	}

	// SELECAO DE ITEM DO MENU
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			// CHAMA TELA DE CADASTRO
			Intent i = new Intent(this, Cadastro.class);
			startActivity(i);
			break;
		}
		return true;
	}

	// ************************************

	// ************************************
	// FECHAR TELA
	public void onDestroy() {
		super.onDestroy();
		mHandler.removeCallbacks(mUpdateTimeTask);
	}

	// ************************************

	private void createListView(String retorno) {
		// Criamos nossa lista que preenchera o ListView
		itens = new ArrayList<ItemListView>();

		String[] recursos = new String[NUM_OF_DISPLAYED_RESOURCES];
		recursos = retorno.split(";");
		String[] recursos_splited = new String[10];
		String aux = "";

		for (int i = 0; i < NUM_OF_DISPLAYED_RESOURCES; i++) {
			int id = recursos[i].indexOf(CARACTER_DIVISOR);
			int nm = recursos[i].indexOf(CARACTER_DIVISOR, id + 1);
			int ds = recursos[i].indexOf(CARACTER_DIVISOR, nm + 1);
			int lt = recursos[i].indexOf(CARACTER_DIVISOR, ds + 1);
			int lg = recursos[i].indexOf(CARACTER_DIVISOR, lt + 1);
			int mt = recursos[i].indexOf(CARACTER_DIVISOR, lg + 1);

			String Id_TipoRecurso = recursos[i].substring(0, id);
			String nome = recursos[i].substring(id + 1, nm);
			String descricao = recursos[i].substring(nm + 1, ds);
			String latitude = recursos[i].substring(ds + 1, lt);
			String longitude = recursos[i].substring(lt + 1, lg);
			String metros = recursos[i].substring(lg + 1);

			item_recurso = new HashMap<String, Object>();
			int iconID;

			switch (Integer.parseInt(Id_TipoRecurso)) {
			case 1:
				iconID = R.drawable.parking;
				break;
			case 2:
				iconID = R.drawable.ramp;
				break;
			case 3:
				iconID = R.drawable.elevator;
				break;
			case 4:
				iconID = R.drawable.toilet;
				break;
			case 5:
				iconID = R.drawable.general;
				break;
			case 6:
				iconID = R.drawable.general;
				break;
			case 7:
				iconID = R.drawable.food;
				break;
			case 8:
				iconID = R.drawable.telephone;
				break;
			case 9:
				iconID = R.drawable.bank;
				break;
			case 10:
				iconID = R.drawable.general;
				break;
			case 11:
				iconID = R.drawable.general;
				break;
			case 12:
				iconID = R.drawable.general;
				break;
			default:
				iconID = R.drawable.general;
				break;
			}

			ItemListView item1 = new ItemListView(iconID, nome, descricao,
					metros + " metros");
			itens.add(item1);
		}

		// Cria o adapter
		adapterListView = new AdapterListView(RecursosLista.this, itens);

		// Define o Adapter
		listview.setAdapter(adapterListView);

	}

	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {

			/*
			 * Toast.makeText(getBaseContext(),"ATIVA THREAD!",Toast.LENGTH_SHORT
			 * ).show();
			 */

			wsResource = new WebService();
			string_Coordenada = String.valueOf(auxLatitude) + "+"
					+ String.valueOf(auxLongitude);
			String retorno_WS = wsResource.consultaOut(string_Coordenada,
					NUM_OF_DISPLAYED_RESOURCES);

			/*
			 * Toast.makeText(getBaseContext(),retorno_WS,Toast.LENGTH_SHORT).show
			 * ();
			 */

			listview = (ListView) findViewById(R.id.listView1);

			createListView(retorno_WS);

			// mHandler.postAtTime(this,start + (((minutes * 60) + seconds + 1)
			// * 1000));
			mHandler.postDelayed(mUpdateTimeTask, 10000);
		}

	};

	public class ItemListView {
		private int icon;
		private String nome;
		private String desc;
		private String dist;

		public ItemListView() {
		}

		public ItemListView(int icon, String nome, String desc, String dist) {
			this.icon = icon;
			this.nome = nome;
			this.desc = desc;
			this.dist = dist;
		}

		public int getIcon() {
			return icon;
		}

		public String getNome() {
			return nome;
		}

		public String getDesc() {
			return desc;
		}

		public String getDist() {
			return dist;
		}

	}

	public String getCoordenadas() {
		string_Coordenada = String.valueOf(auxLatitude) + "+"
				+ String.valueOf(auxLongitude);
		return string_Coordenada;
	}

	public class AdapterListView extends BaseAdapter {
		private LayoutInflater mInflater;
		private ArrayList<ItemListView> itens;

		public AdapterListView(Context context, ArrayList<ItemListView> itens) {
			// Itens que preencheram o listview
			this.itens = itens;
			// responsavel por pegar o Layout do item.
			mInflater = LayoutInflater.from(context);
		}

		/* Retorna a quantidade de itens */
		public int getCount() {
			return itens.size();
		}

		/* Retorna o item de acordo com a posicao dele na tela. */
		public ItemListView getItem(int position) {
			return itens.get(position);
		}

		/* Sem implementação */
		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View view, ViewGroup parent) {
			// Pega o item de acordo com a posção.
			ItemListView item = itens.get(position);
			// infla o layout para podermos preencher os dados
			view = mInflater.inflate(R.layout.list_row, null);

			// atravez do layout pego pelo LayoutInflater, pegamos cada id
			// relacionado
			// ao item e definimos as informações.
			((ImageView) view.findViewById(R.id.recursoIcon))
					.setImageResource(item.getIcon());
			((TextView) view.findViewById(R.id.recursoNome)).setText(item
					.getNome());
			((TextView) view.findViewById(R.id.recursoDesc)).setText(item
					.getDesc());
			((TextView) view.findViewById(R.id.recursoDist)).setText(item
					.getDist());

			return view;
		}

	}
}
