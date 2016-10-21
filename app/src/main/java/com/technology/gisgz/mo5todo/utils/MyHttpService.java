package com.technology.gisgz.mo5todo.utils;

import android.os.Build;

import com.technology.gisgz.mo5todo.BuildConfig;
import com.technology.gisgz.mo5todo.model.ApprovalActionPOJO;
import com.technology.gisgz.mo5todo.model.BasePOJO;
import com.technology.gisgz.mo5todo.model.FormCountsPOJO;
import com.technology.gisgz.mo5todo.model.FormDetailDataGirdPOJO;
import com.technology.gisgz.mo5todo.model.FormDetailPOJO;
import com.technology.gisgz.mo5todo.model.FormItemsListPOJO;
import com.technology.gisgz.mo5todo.model.FormRoutingPOJO;
import com.technology.gisgz.mo5todo.model.NewVersionPOJO;
import com.technology.gisgz.mo5todo.model.PostParameterPOJO;
import com.technology.gisgz.mo5todo.model.UserLoginPOJO;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Jim.Lee on 2016/3/22.
 */
public class MyHttpService {
    // singleton retrofitInstance
    private static boolean single = true;
    private static Retrofit retrofitInstance = _retrofit();
    private static Retrofit _retrofit(){
        // Add the interceptor to OkHttpClient
        OkHttpClient client = new OkHttpClient().newBuilder()
                .readTimeout(40, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request newRequest = chain.request().newBuilder()
                                .addHeader("PlatForm", "Android")
                                .addHeader("Build.MODEL", Build.MODEL)
                                .addHeader("Build.BRAND", Build.BRAND)
                                .addHeader("Build.VERSION.SDK_INT", Build.VERSION.SDK_INT+"")
                                .addHeader("BuildConfig.APPLICATION_ID", BuildConfig.APPLICATION_ID)
                                .addHeader("BuildConfig.VERSION_NAME", BuildConfig.VERSION_NAME)
                                .addHeader("BuildConfig.VERSION_CODE", BuildConfig.VERSION_CODE+"")
                                .addHeader("Build.ID", Build.ID)
                                .addHeader("Build.SERIAL", Build.SERIAL)
                                .build();
                        return chain.proceed(newRequest);
                    }
                }).build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit;
    }

    public static Retrofit instanceRetrofit(){
        if(single){
            return retrofitInstance;
        }else{
            MyHttpService.single = true;
            retrofitInstance = _retrofit();
            return retrofitInstance;
        }
    }

    public static void setSingle(boolean isSingle){
        single = isSingle;
    }
    // User login
    public interface UserLoginInterface {
        ///api/v1/userslogins/{type}/{company}/{username}/{password}
        //http://10.152.86.186/MyOffice_WebAPI/api/v1/userslogins/11/a/2/a
        @GET("userslogins/"+MyConfig.APP_KEY+"/getuserlogin/{type}/{company}/{username}/{password}")
        Call<UserLoginPOJO> getUserLogin(@Path("type") int loginType,@Path("company") String company,@Path("username") String userName,@Path("password") String password);

    }
    public interface FormsInterface {
        ///api/v1/userslogins/{type}/{company}/{username}/{password}
        //http://10.152.86.186/MyOffice_WebAPI/api/v1/userslogins/11/a/2/a
        @GET("forms/"+MyConfig.APP_KEY+"/{userid}/{myaction}")
        Call<FormCountsPOJO> getFormCounts(@Path("userid") int userId, @Path("myaction") String myActionFlag);
        @GET("forms/"+MyConfig.APP_KEY+"/{userid}/{page}/{rows}/{myaction}")
        Call<FormItemsListPOJO> getFormList(@Path("userid") int userId,@Path("page") int page,@Path("rows") int rows, @Path("myaction") String myActionFlag);
        //GET /api/v1/forms/{appkey}/{userId}/{page}/{rows}/ActionHistory
        @GET("forms/"+MyConfig.APP_KEY+"/{userid}/{page}/{rows}/ActionHistory")
        Call<FormItemsListPOJO> getActionHistoryFormList(@Path("userid") int userId,@Path("page") int page,@Path("rows") int rows);
        @GET("forms/"+MyConfig.APP_KEY+"/{userid}/{formid}/view/detail")
        Call<FormDetailPOJO> getForm(@Path("userid") int userId, @Path("formid") int formId);
        ///api/v1/forms/{appkey}/{userId}/{formId}/approval
        @POST("forms/"+MyConfig.APP_KEY+"/{userid}/{formid}/action/approve")
        Call<ApprovalActionPOJO> approvalForm(@Path("userid") int userId, @Path("formid") int formId, @Body String comment);
        @POST("forms/"+MyConfig.APP_KEY+"/{userid}/{formid}/action/reject")
        Call<ApprovalActionPOJO> rejectForm(@Path("userid") int userId, @Path("formid") int formId,@Body String comment);
        @POST("forms/"+MyConfig.APP_KEY+"/{userid}/{formid}/action/undoapprove")
        Call<ApprovalActionPOJO> undoForm(@Path("userid") int userId, @Path("formid") int formId);
        @POST("forms/"+MyConfig.APP_KEY+"/{userid}/{formid}/action/execute")
        Call<ApprovalActionPOJO> executeForm(@Path("userid") int userId, @Path("formid") int formId);
        @GET("forms/"+MyConfig.APP_KEY+"/{userid}/{formid}/action/RoutingStatus")
        Call<ApprovalActionPOJO> getActionStatus(@Path("userid") int userId, @Path("formid") int formId);
        //GET /api/v1/forms/{appkey}/{formid}/action/RoutingPath
        @GET("forms/"+MyConfig.APP_KEY+"/{userid}/{formid}/action/RoutingPath")
        Call<FormRoutingPOJO> getRouting(@Path("userid") int userId,@Path("formid") int formId);
    }
    public interface FormsInterfaceNew {

        @POST("forms/"+MyConfig.APP_KEY+"/getformlistcount/{userid}/{myaction}")
        Call<FormCountsPOJO> getFormCounts(@Path("userid") int userId, @Path("myaction") String myActionFlag,@Body PostParameterPOJO ppm);

        @POST("forms/"+MyConfig.APP_KEY+"/getformlist/{userid}/{page}/{rows}/{myaction}")
        Call<FormItemsListPOJO> getFormList(@Path("userid") int userId,@Path("page") int page,@Path("rows") int rows, @Path("myaction") String myActionFlag,@Body PostParameterPOJO ppm);

//        @POST("forms/"+MyConfig.APP_KEY+"/getformlist/{userid}/{page}/{rows}/ActionHistory")
//        Call<FormItemsListPOJO> getActionHistoryFormList(@Path("userid") int userId,@Path("page") int page,@Path("rows") int rows, @Path("myaction") String myActionFlag,@Body PostParameterPOJO ppm);

        @GET("forms/"+MyConfig.APP_KEY+"/getformdetail/{userid}/{formid}/{formType}")
        Call<FormDetailPOJO> getForm(@Path("userid") int userId, @Path("formid") int formId,@Path("formType") String formType);

        @POST("forms/"+MyConfig.APP_KEY+"/action/{userid}/{formid}/{formType}/{actionType}")
        Call<ApprovalActionPOJO> actionToForm(@Path("userid") int userId, @Path("formid") int formId, @Path("formType") String formType, @Path("actionType") String actionType, @Body PostParameterPOJO ppm);

        @GET("forms/"+MyConfig.APP_KEY+"/getactionstatus/{userid}/{formid}/{formType}")
        Call<ApprovalActionPOJO> getActionStatus(@Path("userid") int userId, @Path("formid") int formId,@Path("formType") String formType);

        @GET("forms/"+MyConfig.APP_KEY+"/getformrouting/{userid}/{formid}/{formType}")
        Call<FormRoutingPOJO> getRouting(@Path("userid") int userId,@Path("formid") int formId,@Path("formType") String formType);

        @GET("forms/"+MyConfig.APP_KEY+"/getformdetaildatagrid/{userid}/{formid}/{formTemplateDetialId}")
        Call<FormDetailDataGirdPOJO> getFormDetailDataGird(@Path("userid") int userId, @Path("formid") int formId, @Path("formTemplateDetialId") int formTemplateDetialId);
    }

    public interface CommonInterface {

        @GET("common/"+MyConfig.APP_KEY+"/checkversion/{versionCode}")
        Call<NewVersionPOJO> checkVersion(@Path("versionCode") int versionCode);
        //GET /api/v1/forms/{appkey}/getfile/{fileId}/{typeId}
        String getFileURL = "common/"+MyConfig.APP_KEY+"/getfile/%d/%d/%d";
        @GET("common/"+MyConfig.APP_KEY+"/checkfileifexists/{userid}/{fileId}/{fileName}")
        Call<BasePOJO> checkFileIfExists(@Path("userid") int userId,@Path("fileId") int fileId, @Path("fileName") String fileName);
    }
}
