package hu.tvarga.bor.borkostolas;

import android.test.ActivityUnitTestCase;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.sql.SQLException;

import hu.tvarga.bor.borkostolas.hu.tvarga.bor.borkostolas.model.RemoteDAO;
import hu.tvarga.bor.borkostolas.model.bean.Wine;

/**
 * Created by Calebzor on 3/18/2015.
 */
public class RemoteDAOTest{

       public static void main(String args[]){
//        Wine wine = new Wine(321, "asaaaaaaaaaaaaaasddf", "asdsdadf", "asdf, ", 1, "asdf", 3);
//           HttpPost httppost;
//           HttpResponse response;
//           HttpClient httpclient;
//
//           try{
//
//               httpclient=new DefaultHttpClient();
//               httppost= new HttpPost("http://bor.tvarga.hu/getWineData.php"); // make sure the url is correct.
//               //add your data
//
//               //Execute HTTP Post Request
//               httpclient.execute(httppost);
//               ResponseHandler<String> responseHandler = new BasicResponseHandler();
//               String result = httpclient.execute(httppost, responseHandler);
//               JSONObject obj = new JSONObject(result);
//               System.out.println("Response : " + result);
//
//
//           }catch(Exception e){
//               System.out.println("Exception : " + e);
//           }

           RemoteDAO dao = new RemoteDAO();
           dao.getWines();
//        System.out.println(wine);
    }
}
