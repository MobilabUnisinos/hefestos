package hefestos.interfaceusuario;

import hefestos.webservice.WebService;
import unisinos.br.R;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Cadastro extends Activity {
    /** Called when the activity is first created. */
    private LocationManager lm;
    private LocationListener locationListener;
    private double auxLatitude = 0;
	private double auxLongitude = 0;
	
    @Override
    public void onCreate(Bundle iBundle) {
        super.onCreate(iBundle);
        setContentView(R.layout.cadastro);
        
    	//button to save resource
        Button btSave = (Button) findViewById(R.id.btSave);
        btSave.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	EditText edNome = (EditText) findViewById(R.id.edNome);
            	EditText edTipoRecurso = (EditText) findViewById(R.id.edTipoRecurso);
            	EditText edTipoDeficiencia = (EditText) findViewById(R.id.edTipoDeficiencia);
            	EditText edRegiao = (EditText) findViewById(R.id.edRegiao);
            	EditText edContexto = (EditText) findViewById(R.id.edContexto);
            	EditText edLatitude = (EditText) findViewById(R.id.edLatitude);
            	EditText edLongitude = (EditText) findViewById(R.id.edLongitude);
            	
            	edLatitude.setText(String.valueOf(auxLatitude));
            	edLongitude.setText(String.valueOf(auxLongitude));
	
            	WebService wsResource = new WebService();
                String aux = wsResource.setNewResource(edNome.getText() + ";" + edTipoRecurso.getText() + ";" + edTipoDeficiencia.getText() + ";" + edRegiao.getText() + ";" + edContexto.getText() + ";" + edLatitude.getText() + ";" + edLongitude.getText());
                Toast.makeText(getBaseContext(),aux, Toast.LENGTH_SHORT).show();
            }
        });
        
        //button to clear screen
        // ************************************
        Button btClear = (Button) findViewById(R.id.btClear);
        btClear.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	EditText edNome = (EditText) findViewById(R.id.edNome);
            	EditText edTipoRecurso = (EditText) findViewById(R.id.edLatitude);
            	EditText edTipoDeficiencia = (EditText) findViewById(R.id.edTipoDeficiencia);
            	EditText edRegiao = (EditText) findViewById(R.id.edRegiao);
            	EditText edContexto = (EditText) findViewById(R.id.edContexto);
            	EditText edLatitude = (EditText) findViewById(R.id.edLatitude);
            	EditText edLongitude = (EditText) findViewById(R.id.edLongitude);

            	edNome.setText("");
            	edTipoRecurso.setText("");
            	edTipoDeficiencia.setText("");
            	edRegiao.setText("");
            	edContexto.setText("");
            	edLatitude.setText("");
            	edLongitude.setText("");
            }
        });
     // ************************************

     //---use the LocationManager class to obtain GPS locations---
     // ************************************
     lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);    
     locationListener = new MyLocationListener();
     lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
     // ************************************
    }
    
    
    // ************************************
    //CLASSE MyLocationListener QUE LIGA COORDENADAS A VARIAVEIS
    private class MyLocationListener implements LocationListener 
    {
        public void onLocationChanged(Location loc) {
        	if (loc != null) {
            	auxLatitude = loc.getLatitude();
            	auxLongitude = loc.getLongitude();
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
}