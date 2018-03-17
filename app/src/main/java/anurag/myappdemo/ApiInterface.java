package anurag.myappdemo;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by anurag on 17-03-2018.
 */

public interface ApiInterface {
    //yha change kiya han
    @POST("api/v1.5/tr.json/translate?lang=en-hi&key=trnsl.1.1.20180315T201927Z.0f89e876a1e23180.5ca9c6e550fc7c1baa3bb1f077c58d5007f680b5")
    Call<Data> getda(@Query("text") String text);
    @POST("api/v1.5/tr.json/translate?lang=hi-en&key=trnsl.1.1.20180315T201927Z.0f89e876a1e23180.5ca9c6e550fc7c1baa3bb1f077c58d5007f680b5")
    Call<Data> getde(@Query("text") String text);
   // @GET("api/v1.5/tr.json/translate")
    //Call<Data> getda(@QueryMap Map<String,String> options);
    //@GET("translate")
    //Call<Data> getT(@Query("key") String key,@Query("text") String text,@Query("lang") String lang);
//    @GET("translate?key={keys}&text={useText}&lang={langu}&[format={form}].json")
//    Call<Data> getText(@Path("keys") String keys,@Path("useText") String useText,@Path("langu") String langu,@Path("form") String form);
}
