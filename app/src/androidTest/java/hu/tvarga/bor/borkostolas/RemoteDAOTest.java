package hu.tvarga.bor.borkostolas;

import hu.tvarga.bor.borkostolas.model.RemoteDAO;

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
