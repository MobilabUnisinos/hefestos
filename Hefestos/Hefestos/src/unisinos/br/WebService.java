package unisinos.br;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class WebService {
	//settings to connect kSoap web service
    private Object resultRequestSOAP = null;
    private String auxResult = "";

    private static final String URL = "http://200.188.161.248:8080/axis/HefestosResources.jws";
    private static final String SOAP_ACTION = "HefestosResources";
    private static final String NAMESPACE = "urn:HefestosResources";

    private static final String METHOD_NAME_SAVE = "saveResource";
    private static final String METHOD_NAME_SEARCH = "returnTopResources";

    public String setNewResource(String resource){

    	SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_SAVE);
    	
        //SoapObject 
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);

    	request.addProperty("resource",resource);
    	
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try 
        {
           androidHttpTransport.call(SOAP_ACTION, envelope);
           resultRequestSOAP =  envelope.getResponse();
           auxResult = resultRequestSOAP.toString();
        }
        catch (Exception aE)
        {
        	aE.printStackTrace ();;
        }
  	
        return auxResult;
    } 

    public String getTopResources(String place, int qtd){

    	SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_SEARCH);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
    	request.addProperty("place", place);
    	request.addProperty("qtd", qtd);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try 
        {
           androidHttpTransport.call(SOAP_ACTION, envelope);
           resultRequestSOAP =  envelope.getResponse();
           auxResult = resultRequestSOAP.toString();
        }
        catch (Exception aE)
        {
        	aE.printStackTrace ();;
        }
  	
    	return auxResult;
    }
}
