package hefestos.interfaceusuario;

import hefestos.webservice.WebService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import unisinos.br.R;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RecursosKeyword extends Activity {

	WebService wsResource;
	static int NUM_OF_DISPLAYED_RESOURCES = 5;
	static String CARACTER_DIVISOR = "+";
	ListView listview;
	private List<Map<String, Object>> list_recurso;
	AdapterListView adapterListView;
	String string_Coordenada;
	private ArrayList<ItemListView> itens;
	Map<String, Object> item_recurso;
	private AlertDialog alerta;
	String number = "5191691958", sms = "Mensagem de socorro Android";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recursos_keyword);

		Intent intent = getIntent();
		String data = intent.getStringExtra("idTag");
		String geo = intent.getStringExtra("geo");

		WebService wsResource = new WebService();
		String retorno_WS = wsResource.consultaKey(data,geo,NUM_OF_DISPLAYED_RESOURCES);

		listview = (ListView) findViewById(R.id.listView1);
		createListView(retorno_WS);
		
		// Definicao da imagem
				ImageButton btSos = (ImageButton) findViewById(R.id.ibSos);
				
				  btSos.setOnClickListener(new OnClickListener() { 
				  public void onClick(View v) { 
				  AlertDialog.Builder bd = new AlertDialog.Builder(getBaseContext());
						bd.setTitle("Voce realmente deseja enviar um SMS de socorro?");
						bd.setPositiveButton("Sim",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface arg0, int arg1) {
										
										SmsManager smsManager = SmsManager.getDefault();
										smsManager.sendTextMessage(number, null, sms,null, null);
										Toast.makeText(getBaseContext(), "Mensagem enviada",Toast.LENGTH_SHORT).show();
									}
								});

						bd.setNegativeButton("Não",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface arg0, int arg1) {
										Toast.makeText(getBaseContext(), "Mensagem nao enviada",Toast.LENGTH_SHORT).show();
									}
								});
						alerta = bd.create();
						alerta.show();
					}
				});
	}

	private void createListView(String retorno) {
		// Criamos nossa lista que preenchera o ListView
		itens = new ArrayList<ItemListView>();

		String[] recursos = new String[NUM_OF_DISPLAYED_RESOURCES];
		recursos = retorno.split(";");
		int aux = recursos.length;

		for (int i = 0; i < aux; i++) {
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

			ItemListView item1 = new ItemListView(iconID, nome, descricao, metros);
			itens.add(item1);			
		}

		// Cria o adapter
		adapterListView = new AdapterListView(RecursosKeyword.this, itens);

		// Define o Adapter
		listview.setAdapter(adapterListView);

	}

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.recursos_keyword, menu);
		return true;
	}

}
