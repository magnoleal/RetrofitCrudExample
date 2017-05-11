package com.magnoleal.retrofitcrudexample.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.magnoleal.retrofitcrudexample.util.Constantes;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by magno on 06/12/15.
 */
public abstract class AbstractService {

    protected Retrofit retrofit;

    protected void init(){

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.readTimeout(60, TimeUnit.SECONDS);
        httpClient.connectTimeout(60, TimeUnit.SECONDS);

        if(Constantes.API_LOG_ENABLE){
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.interceptors().add(logging);
        }

        OkHttpClient buildClient = httpClient.build();

        Gson gson =  new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();

        this.retrofit = new Retrofit.Builder()
                .baseUrl(getUrlApi())
                .client(buildClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

    }

    protected String getUrlApi(){
        return Constantes.API_URL;
    }

    public <T> T responseDefault(Response response) throws Exception {

        if(response.isSuccessful()){
            Object res = response.body();
            if(res != null)
                return (T) res;
        }

        //404 or the response cannot be converted to User.
        ResponseBody responseBody = response.errorBody();
        String msg = "Erro ao receber resposta do servidor!";
        if (responseBody != null) {

            String rs = responseBody.string();
            if(rs != null && !rs.isEmpty()){
                JsonObject jobj = new Gson().fromJson(rs, JsonObject.class);
                msg = jobj.get("message").getAsString();
            }

            throw new Exception(msg);

        } else {
            throw new Exception(msg);
        }


    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
