package hefestos.interfaceusuario;

import unisinos.br.R;
import unisinos.br.R.drawable;
import unisinos.br.R.id;
import unisinos.br.R.layout;
import unisinos.br.R.menu;

import com.google.android.gms.plus.model.people.Person.Image;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.widget.ImageView;

public class Mapa extends Activity {
	
	ImageView mapa1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapa);		
		
		Intent intent = getIntent();
		String idMap = intent.getStringExtra("idMapToLoad");
		String idTag = intent.getStringExtra("tag");
		double lat = intent.getDoubleExtra("lat", 0.0);
		double lon = intent.getDoubleExtra("lon", 0.0);
		int dist = distanceBetweenPoints(lat,-29.7944745,lon,-51.155055499999996);
		
		
		
		if(idMap.equalsIgnoreCase("outdoor1")){
			if (dist<=200){
				mapa1 = (ImageView) findViewById(R.id.mapaEstatico);
				Drawable res = getResources().getDrawable(R.drawable.mapa1);
				mapa1.setImageDrawable(res);
			}
			else{
				mapa1 = (ImageView) findViewById(R.id.mapaEstatico);
				Drawable res = getResources().getDrawable(R.drawable.mapa2);
				mapa1.setImageDrawable(res);
			}
		}
		else if(idMap.equalsIgnoreCase("indoor")){
			
			if (idTag.equalsIgnoreCase("8")){				
				mapa1 = (ImageView) findViewById(R.id.mapaEstatico);
				Drawable res = getResources().getDrawable(R.drawable.mapa3);
				mapa1.setImageDrawable(res);}
			else{
				mapa1 = (ImageView) findViewById(R.id.mapaEstatico);
				Drawable res = getResources().getDrawable(R.drawable.mapa4);
				mapa1.setImageDrawable(res);
			}
			
		}
	}
	
	private int distanceBetweenPoints(double lat1, double lat2, double long1, double long2) {
        Double dlon, dlat, a, distancia;

        dlon = Math.toRadians(long2 - long1);
        dlat = Math.toRadians(lat2 - lat1);
        a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
        distancia = (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))) * 6378140;
        //return 6378140 * distancia; /* 6378140 is the radius of the Earth in meters*/
        return distancia.intValue();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mapa, menu);
		return true;
	}

}
