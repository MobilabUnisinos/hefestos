package unisinos.br;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MeuProjetoActivity extends Activity {

    private LocationManager lm;
    private LocationListener locationListener;
    private double auxLatitude = 0;
	private double auxLongitude = 0;

    public Handler mHandler = new Handler();

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		startService(new Intent(this, BluetoothService.class));

        mHandler.removeCallbacks(mUpdateTimeTask);
        mHandler.postDelayed(mUpdateTimeTask, 100);

        //button to search resource
        ImageButton btConsultar = (ImageButton) findViewById(R.id.ibConsultar);
        btConsultar.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"Consultar!", Toast.LENGTH_SHORT).show();
            }
        });

    	//button to view map
        ImageButton btMapa = (ImageButton) findViewById(R.id.ibMapa);
        btMapa.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"Mapa!", Toast.LENGTH_SHORT).show();
            }
        });

    	//button to preferences
        ImageButton btPreferencia = (ImageButton) findViewById(R.id.ibPreferencia);
        btPreferencia.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"Preferencias!", Toast.LENGTH_SHORT).show();
            }
        });
        
    	//button to SOS
        ImageButton btSos = (ImageButton) findViewById(R.id.ibSos);
        btSos.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
    			Toast.makeText(getBaseContext(),"SOS!", Toast.LENGTH_SHORT).show();
            }
        });

        //---use the LocationManager class to obtain GPS locations---
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);    
        locationListener = new MyLocationListener();
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        
    }
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu){
    	super.onCreateOptionsMenu(menu);
    	menu.add(0,0,0, "Cadastrar recurso");
    	
		return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()) {
		case 0:
			Intent i = new Intent(this, CadastroActivity.class);
			startActivity(i);
			break;
			}
		return true;
		}

	public void onDestroy(){
    	super.onDestroy();
        mHandler.removeCallbacks(mUpdateTimeTask);
    }
    	
    private int getTipoRecurso(String tipoRecurso){
    	if (tipoRecurso.equals("Estacionamento")){
    		return R.drawable.parking; 
    	}
    	else if (tipoRecurso.equals("Alimentacao")){
    		return R.drawable.food; 
    	}
    	else if (tipoRecurso.equals("Banco")){
    		return R.drawable.bank; 
    	}
    	else if (tipoRecurso.equals("Toalete")){
    		return R.drawable.toilet; 
    	}
    	else if (tipoRecurso.equals("Rampa")){
    		return R.drawable.ramp; 
    	}
    	else if (tipoRecurso.equals("Servico")){
    		return R.drawable.service; 
    	}
    	else if (tipoRecurso.equals("Pessoa")){
    		return R.drawable.general; 
    	}

    	return R.drawable.general;
    }

	private class MyLocationListener implements LocationListener 
    {
        public void onLocationChanged(Location loc) {
        	if (loc != null) {
            	auxLatitude = loc.getLatitude();
            	auxLongitude = loc.getLongitude();
            }            
        }

        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        public void onStatusChanged(String provider, int status, 
            Bundle extras) {
            // TODO Auto-generated method stub
        }
    }
    
    private Runnable mUpdateTimeTask = new Runnable() {
 	   public void run() {
 	       //Toast.makeText(getBaseContext(),"Teste", Toast.LENGTH_SHORT).show();
 		   TextView tvNome1 = (TextView) findViewById(R.id.tvRecurso1);
 		   ImageView ivRecurso1 = (ImageView) findViewById(R.id.ivRecurso1);

 		   TextView tvNome2 = (TextView) findViewById(R.id.tvRecurso2);
 		   ImageView ivRecurso2 = (ImageView) findViewById(R.id.ivRecurso2);

 		   TextView tvNome3 = (TextView) findViewById(R.id.tvRecurso3);
 		   ImageView ivRecurso3 = (ImageView) findViewById(R.id.ivRecurso3);

 		   TextView tvNome4 = (TextView) findViewById(R.id.tvRecurso4);
 		   ImageView ivRecurso4 = (ImageView) findViewById(R.id.ivRecurso4);
       	
 		   TextView tvNome5 = (TextView) findViewById(R.id.tvRecurso5);
 		   ImageView ivRecurso5 = (ImageView) findViewById(R.id.ivRecurso5);

 		   WebService wsResource = new WebService();
           String aux = wsResource.getTopResources(String.valueOf(auxLatitude) + ";" + String.valueOf(auxLongitude), 5);
           
       	   //String aux = "Caixa Eletronico;banco;1#Estacionamento com vagas para PCDs;estacionamento;5#Cantina Happy Station;alimentacao;10#Banheiro adaptado;toalete;18#Banheiro adaptado;toalete;18#";
           
           String [] listaRecursos = aux.split("#");
           
           String [] recurso = listaRecursos[0].split(";");
           tvNome1.setText(recurso[1] + "\n Aprox. " + recurso[0] + " metros");
           ivRecurso1.setImageResource(getTipoRecurso(recurso[2]));

           recurso = listaRecursos[1].split(";");
           tvNome2.setText(recurso[1] + "\n Aprox. " + recurso[0] + " metros");
           ivRecurso2.setImageResource(getTipoRecurso(recurso[2]));

           recurso = listaRecursos[2].split(";");
           tvNome3.setText(recurso[1] + "\n Aprox. " + recurso[0] + " metros");
           ivRecurso3.setImageResource(getTipoRecurso(recurso[2]));

           recurso = listaRecursos[3].split(";");
           tvNome4.setText(recurso[1] + "\n Aprox. " + recurso[0] + " metros");
           ivRecurso4.setImageResource(getTipoRecurso(recurso[2]));
           
           recurso = listaRecursos[4].split(";");
           tvNome5.setText(recurso[1] + "\n Aprox. " + recurso[0] + " metros");
           ivRecurso5.setImageResource(getTipoRecurso(recurso[2]));

 	       //mHandler.postAtTime(this,start + (((minutes * 60) + seconds + 1) * 1000));
 	       mHandler.postDelayed(mUpdateTimeTask, 5000);
 	   }
 	};	
}