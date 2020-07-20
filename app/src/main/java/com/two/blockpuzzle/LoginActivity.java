package com.two.blockpuzzle;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends Dialog {
    SharedPreferences pref, checkpref;
    EditText et_loginId;
    Button btn_join,btn_login;
    Dialog login_dialog;
    Context context;
    CheckBox saveid_check;
    Boolean check_type;

    public LoginActivity(final Context context) {
        super(context);
        this.context = context;

        login_dialog = new Dialog(context);
        login_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        login_dialog.setContentView(R.layout.login_dialog);
        login_dialog.show();

        btn_login = login_dialog.findViewById(R.id.btn_login);
        btn_join = login_dialog.findViewById(R.id.btn_join);
        et_loginId = login_dialog.findViewById(R.id.et_loginId);
        saveid_check = login_dialog.findViewById(R.id.saveid_check);

        //로그인 액티비티 LoginTask 클래스에 보내기
        //(버튼 이벤트에서 해당 액티비티 닫는 용도..로 만들었는데 오류 나서 주석 처리함)
        /*LoginTask class_loginTask = new LoginTask();
        class_loginTask.setActivity(this);*/

        pref = context.getSharedPreferences("SHARE", context.MODE_PRIVATE);
        pref.getString("id", "");//키값 빈값일 때 ""로 초기값 주기*/
        String successstr = pref.getString("id", "");
        et_loginId.setText(successstr);

        checkpref = context.getSharedPreferences("Check",context.MODE_PRIVATE);
        checkpref.getBoolean("check_box",false);
        check_type = checkpref.getBoolean("check_box",false);
        saveid_check.setChecked(check_type);

        btn_login.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String login_id = et_loginId.getText().toString().trim();
            //서버에 전달할 파라미터 id
            String result = "id="+login_id;
            new LoginTask().execute(result,"type_login");
            /*Toast.makeText(context, "login_id->"+login_id, Toast.LENGTH_SHORT).show();*/
            //0번방 result 1번방 type_login  이거로 구분해서 들어가게 했어요!
            }
        });

        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            login_dialog.dismiss();
            new JoinActivity(context);
            }
        });
    }

   //login용 Asyncclass
    class LoginTask extends AsyncTask<String, Void, String> {
        String ip = Util.IP;
        String sendMsg, receiveMsg;
        String serverIp = Util.SERVER_IP;//연결할 jsp의 주소
        String resultMsg = "";
        /*//로그인 액티비티 불러오기
        LoginActivity activity;

        public void setActivity(LoginActivity activity) {
            this.activity = activity;
        }*/

        @Override
        protected String doInBackground(String... strings) {
            String str = "";
            try {
                URL url = new URL(serverIp);

                //서버연결
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                //기록
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

                //서버로 전달 내용
                sendMsg = strings[0]+"&type="+strings[1];
                //서버로 값 전송
                osw.write(sendMsg);
                osw.flush();

                // 200이면 정상전송 404, 500이 나오면 비정상전송!!
                Log.i("LoginMY","" + conn.getResponseCode());
                if( conn.getResponseCode() == conn.HTTP_OK){
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(),"UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);

                    String buffer = "";
                    while((str = reader.readLine()) !=null){
                        buffer += str;

                    }
                    //web->돌려받은 JSON형식의 결과값
                    receiveMsg = buffer;

                    JSONArray jarray = new JSONObject(receiveMsg).getJSONArray("res");
                    JSONObject jObject = jarray.getJSONObject(0);
                    String result = jObject.getString("result");
                    String login_id = jObject.getString("id");

                    Log.i("MY", "id:"+login_id + "/ result : " + result);

                    //SharedPreference로 id저장하기
                    //액티비티가 아니라 다이얼로그로 다 바꿔서 해서
                    //modeprivate 이랑 sharedpreferences 다 context참조 시켜서 했어요!

                    if(result.equals("success")) {
                        if(saveid_check.isChecked() == true) {
                            pref = context.getSharedPreferences("SHARE", context.MODE_PRIVATE);
                            checkpref = context.getSharedPreferences("Check", context.MODE_PRIVATE);
                            SharedPreferences.Editor edit = pref.edit();
                            SharedPreferences.Editor edit2 = checkpref.edit();
                            edit.putString("id", login_id);
                            edit.commit();
                            edit2.putBoolean("check_box", saveid_check.isChecked());
                            edit2.commit();
                        }else{
                            pref = context.getSharedPreferences("SHARE", context.MODE_PRIVATE);
                            checkpref = context.getSharedPreferences("Check", context.MODE_PRIVATE);
                            SharedPreferences.Editor edit = pref.edit();
                            SharedPreferences.Editor edit2 = checkpref.edit();
                            edit.putString("id", null);
                            edit.commit();
                            edit2.putBoolean("check_box", false);
                            edit2.commit();
                        }
                        resultMsg = "success";
                    }else if(result.equals("fail2")){
                      resultMsg="blank";
                    } else {
                        resultMsg = "nonId";
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultMsg;
        }

      SweetAlertDialog sweetAlertDialog;
       @Override
        protected void onPostExecute(String s) {
           if(s.equals("success")){
               sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.SUCCESS_TYPE);
               sweetAlertDialog.setTitleText("Login Success");
               sweetAlertDialog.setContentText("Enjoy your Game\n It's Block Puzzle Time!! ");
               sweetAlertDialog.setConfirmText("OK");
               sweetAlertDialog.setCancelText("CLOSE");
               sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                   @Override
                   public void onClick(SweetAlertDialog sDialog) {
                       sweetAlertDialog.dismissWithAnimation();
                       Intent i = new Intent(context, SelectPageActivity.class);
                       login_dialog.dismiss();
                       context.startActivity(i);
                   }
               });
               sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                   @Override
                   public void onClick(SweetAlertDialog sweetAlertDialog) {
                       sweetAlertDialog.dismissWithAnimation();
                   }
               });
               sweetAlertDialog.show();
           }else if(s.equals("blank")){
               sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.WARNING_TYPE);
               sweetAlertDialog.setTitleText("Oops!");
               sweetAlertDialog.setContentText("Input your MemberID!*_*");
               sweetAlertDialog.setConfirmText("OK");
               sweetAlertDialog.show();
           }else{
               sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.ERROR_TYPE);
               sweetAlertDialog.setTitleText("Non-existent ID");
               sweetAlertDialog.setContentText("move to Joinform");
               sweetAlertDialog.setConfirmText("OK");
               sweetAlertDialog.show();
           }

        }
    }

}