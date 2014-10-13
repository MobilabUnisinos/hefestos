package hefestos.webservice;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class WebService {
	// settings to connect kSoap web service
	private Object resultRequestSOAP = null;
	private String auxResult = "";

	// DEFINICOES WEB SERVICE
	private static final String URL = "http://200.188.161.248:8080/axis/WheelChairService.jws";
	private static final String SOAP_ACTION = "WheelChairService";
	private static final String NAMESPACE = "urn:WheelChairService";
	
	// DEFINICOES WEB SERVICE
	private static final String URL2 = "http://200.188.161.248:8080/axis/HefestosResources.jws";
	private static final String SOAP_ACTION2 = "HefestosResources";
	private static final String NAMESPACE2 = "urn:HefestosResources";

	// DEFINICAO CHAMADA DE RECURSOS
	private static final String METHOD_NAME_SAVE = "saveResource";
	
	private static final String METHOD_ADD = "adicionarRecurso";
	private static final String METHOD_OUT = "consultarRecursosOutdoor";
	private static final String METHOD_IN = "consultarRecursosIndoor";
	private static final String METHOD_KEYWORD = "consultarRecursosKeyword";
	private static final String METHOD_AMB = "consultarRecursosAmb";
	private static final String METHOD_SOS = "consultarNumeroSOS";

	// METODO CONSULTA OUTDOOR
	// *********************************************
	public String consultaOut(String geo, int quant) {
		SoapObject request = new SoapObject(NAMESPACE, METHOD_OUT);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		request.addProperty("geo", geo);
		request.addProperty("qtd", quant);
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

		try {
			androidHttpTransport.call(SOAP_ACTION, envelope);
			resultRequestSOAP = envelope.getResponse();
			auxResult = resultRequestSOAP.toString();
		} catch (Exception aE) {
			aE.printStackTrace();
			;
		}
		return auxResult;
	}

	// *********************************************

	// METODO CONSULTA KEYWORD
	// *********************************************
	public String consultaKey(String key, String geo, int quant) {
		SoapObject request = new SoapObject(NAMESPACE, METHOD_KEYWORD);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		request.addProperty("keyword", key);
		request.addProperty("geo", geo);
		request.addProperty("qtd", quant);
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

		try {
			androidHttpTransport.call(SOAP_ACTION, envelope);
			resultRequestSOAP = envelope.getResponse();
			auxResult = resultRequestSOAP.toString();
		} catch (Exception aE) {
			aE.printStackTrace();
			;
		}
		return auxResult;
	}
	// *********************************************
	
	// METODO CONSULTA TELEFONESOS
		// *********************************************

		public String consultaSOS(String id_pessoa) {
			SoapObject request = new SoapObject(NAMESPACE, METHOD_SOS);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);
			request.addProperty("id_pessoa", id_pessoa);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

			try {
				androidHttpTransport.call(SOAP_ACTION, envelope);
				resultRequestSOAP = envelope.getResponse();
				auxResult = resultRequestSOAP.toString();
			} catch (Exception aE) {
				aE.printStackTrace();
				;
			}
			return auxResult;
		}

	// METODO CONSULTA INDOOR
	// *********************************************
	public String consultaAmb(String codigoTag) {
		SoapObject request = new SoapObject(NAMESPACE, METHOD_AMB);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		request.addProperty("codigoTag", codigoTag);

		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

		try {
			androidHttpTransport.call(SOAP_ACTION, envelope);
			resultRequestSOAP = envelope.getResponse();
			auxResult = resultRequestSOAP.toString();
		} catch (Exception aE) {
			aE.printStackTrace();
			;
		}

		return auxResult;
	}

	// *********************************************
	
	
	// METODO CONSULTA INDOOR
		// *********************************************
		public String consultaIn(String codigoTag) {
			SoapObject request = new SoapObject(NAMESPACE, METHOD_IN);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);
			request.addProperty("codigoTag", codigoTag);

			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

			try {
				androidHttpTransport.call(SOAP_ACTION, envelope);
				resultRequestSOAP = envelope.getResponse();
				auxResult = resultRequestSOAP.toString();
			} catch (Exception aE) {
				aE.printStackTrace();
				;
			}

			return auxResult;
		}

		// *********************************************

	// METODO PARA CADASTRO DE NOVOS RECURSOS
	// *********************************************
		public String setNewResource(String resource){

	    	SoapObject request = new SoapObject(NAMESPACE2, METHOD_NAME_SAVE);
	    	
	        //SoapObject 
	        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	        envelope.setOutputSoapObject(request);

	    	request.addProperty("resource",resource);
	    	
	        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL2);

	        try 
	        {
	           androidHttpTransport.call(SOAP_ACTION2, envelope);
	           resultRequestSOAP =  envelope.getResponse();
	           auxResult = resultRequestSOAP.toString();
	        }
	        catch (Exception aE)
	        {
	        	aE.printStackTrace ();;
	        }
	  	
	        return auxResult;
	    }

	// *********************************************

	// METODO PARA RETORNO DE RECURSOS
	// *********************************************
	public String getTopResources(String place, int qtd) {

		SoapObject request = new SoapObject(NAMESPACE, METHOD_ADD);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		request.addProperty("place", place);
		request.addProperty("qtd", qtd);
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

		try {
			androidHttpTransport.call(SOAP_ACTION, envelope);
			resultRequestSOAP = envelope.getResponse();
			auxResult = resultRequestSOAP.toString();
		} catch (Exception aE) {
			aE.printStackTrace();
			;
		}

		return auxResult;
	}
	// *********************************************

}
