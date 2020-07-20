package com.two.blockpuzzle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class JoinActivity extends Dialog {

    EditText et_joinId;
    Button btn_check, btn_movelogin;
    Dialog join_dialog, login_dialog;
    Context context;

    public JoinActivity(final Context context) {
        super(context);
        this.context = context;
        join_dialog = new Dialog(context);
        join_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        join_dialog.setContentView(R.layout.join_dialog);

        login_dialog = new Dialog(context);
        login_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        login_dialog.setContentView(R.layout.login_dialog);

        et_joinId=join_dialog.findViewById(R.id.et_joinId);
        btn_check=join_dialog.findViewById(R.id.btn_check);
        btn_movelogin = join_dialog.findViewById(R.id.btn_movelogin);

        et_joinId.getText().toString().trim();
        join_dialog.show();

        btn_movelogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                join_dialog.dismiss();
                new LoginActivity(context);
            }//btn_movelogin onclick
        });

        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String join_id = et_joinId.getText().toString();

                String result = "id=" + join_id; //서버로 전달할 파라미터

                new JoinTask().execute(result, "type_join");
                    //0번방 result 1번방 type_join
            }
        });
    }

    //회원가입용 Async클래스
    class JoinTask extends AsyncTask<String, Void, String> {

        String ip = Util.IP;
        String sendMsg, receiveMsg;
        String serverIp =Util.SERVER_IP;//연결할 jsp의 주소

        @Override
        protected String doInBackground(String... strings) {

            String str = "";
            try {
                URL url = new URL(serverIp);
                //Toast.makeText(getApplicationContext(), "url : " + url, Toast.LENGTH_SHORT).show();
                //서버연결
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");//전송방식

                //기록
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

                //서버로 전달할 내용
                //tetrislist.jsp?id=___ &type=type_join
                sendMsg = strings[0]+"&type=" + strings[1];

                //서버로 값 전송
                osw.write(sendMsg);
                osw.flush();

                //전송완료 후 서버에서 처리 결과
                //   MY-> 200이면 정상전송 MY->404나 500이 나오면 비정상전송
                //   200떠요!!!
                Log.i("JoinMY","" + conn.getResponseCode());
                if( conn.getResponseCode() == conn.HTTP_OK){
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(),"UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);

                    String buffer = "";
                    while((str = reader.readLine()) !=null){
                        buffer += str;

                    }

                    //최종적으로 돌려받은 JSON형식의 결과값
                    receiveMsg = buffer;
                   /* Toast.makeText(context, "receiveMsg : "+receiveMsg, Toast.LENGTH_SHORT).show();*/
                    JSONArray jarray = new JSONObject(receiveMsg).getJSONArray("res");
                    JSONObject jObject = jarray.getJSONObject(0);
                    String result = jObject.getString("result");

                    if(result.equals("success")){
                        receiveMsg = "success";
                    }else if(result.equals("fail2")){
                        receiveMsg="blank";
                    }else{
                        receiveMsg="using_id";
                    }
                    Log.i("MY",result);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return receiveMsg;
        }

        SweetAlertDialog sweetAlertDialog;

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("success")){
                sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.SUCCESS_TYPE);
                sweetAlertDialog.setTitleText("Welcome!!");
                sweetAlertDialog.setContentText("회원가입 성공\n Move to login:)");
                sweetAlertDialog.setConfirmText("OK");
                sweetAlertDialog.show();
            }else if(s.equals("blank")){
                sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.WARNING_TYPE);
                sweetAlertDialog.setTitleText("앗!");
                sweetAlertDialog.setContentText("한 글자 이상은\n 입력해 주셔야 하지 않을까요오~?0_0");
                sweetAlertDialog.setConfirmText("OK");
                sweetAlertDialog.show();
            }else{
                sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText("Already Using ID!");
                sweetAlertDialog.setContentText("이미 사용중인 아이디에요 ㅜ_ㅜ;");
                sweetAlertDialog.setConfirmText("OK");
                sweetAlertDialog.show();
            }

        }
    }

}