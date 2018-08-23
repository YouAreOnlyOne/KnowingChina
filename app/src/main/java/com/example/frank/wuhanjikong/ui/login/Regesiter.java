package com.example.frank.wuhanjikong.ui.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.frank.wuhanjikong.R;
import com.example.frank.wuhanjikong.config.local.SharePreferenceSave;
import com.example.frank.wuhanjikong.log.L;
import com.example.frank.wuhanjikong.service.ApiService;
import com.example.frank.wuhanjikong.ui.home.ApplicationLoad;
import com.example.frank.wuhanjikong.ui.home.HomeActivity;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;


public class Regesiter extends AppCompatActivity {

    String nickname,password,yonghuid,yanzhengma;
    Button zhuce,getyanzhengma,chakanyouxiang;
    EditText nick,pass,comfirm,youxiang,yanzheng;
    int rnumber;
    String yzm=null;
    static int flag=0;
    TextView shuoming;
    ProgressBar zhucejiazai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regesiter);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().
                detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().
                detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());

        getyanzhengma=(Button)findViewById(R.id.button2r);
        chakanyouxiang=(Button)findViewById(R.id.button3r);
        zhuce=(Button)findViewById(R.id.button7r);

        nick=(EditText)findViewById(R.id.editText5r);
        pass=(EditText)findViewById(R.id.editText6r);
        comfirm=(EditText)findViewById(R.id.editText7r);
        youxiang=(EditText)findViewById(R.id.editText9r);
        yanzheng=(EditText)findViewById(R.id.editText10r);
        shuoming=(TextView)findViewById(R.id.textView8r);
        zhucejiazai=(ProgressBar)this.findViewById(R.id.progressBar2);


        final AlertDialog.Builder alertDialog  =new AlertDialog.Builder(this);
        getyanzhengma.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (youxiang.getText().toString().trim()!=null&&youxiang.getText().toString().trim().length()>4) {
                    rnumber = (int) (Math.random() * 100000);
                    //yzm=String.valueOf(rnumber) ;
                    yzm = Integer.toString(rnumber);
                    String content="您本次注册的验证码为： " + yzm + " 。十分钟内有效，请十分钟内在注册界面输入此验证码！ 如您没有进行注册操作，无需理会此邮件!";

                    try {
                        sendEmail(youxiang.getText().toString().trim(),"“KnowingChina”帐号注册邮件!", content);
                        flag=1;
                    } catch (Exception e) {
                        flag=0;
                        e.printStackTrace();
                    }
                    if (flag==1) {
                        alertDialog.setTitle("系统提示").setMessage("验证码获取成功，请注册邮箱查看！").setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                flag=0;

                            }
                        }).show();
                    } else {
                        alertDialog.setTitle("系统提示").setMessage("验证码获取失败，请检查网络并重新获取！").setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        }).show();
                    }
                }else {
                    alertDialog.setTitle("系统提示").setMessage("请输入完整的且合法的邮箱！").setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    }).show();
                }


            }
        });


        chakanyouxiang.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Regesiter.this, ApplicationLoad.class);
                intent.putExtra("url", "http://www.benpig.com/mail.htm");
                startActivity(intent);

            }
        });


        zhuce.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {


                if (panduan()){

                    Map<String, String> values = new HashMap<String, String>();
                    values.put("userEmail", youxiang.getText().toString().trim());
                    values.put("userPass",pass.getText().toString().trim());
                    values.put("userNick", nick.getText().toString().trim());



                    Map<String, Object> paremetes = new HashMap<>();
                    paremetes.put("data", JSON.toJSONString(values));
                    ApiService.GetString(Regesiter.this, "registerAccount", paremetes, new RxStringCallback() {
                        @Override
                        public void onNext(Object tag, String response) {

                            if (response.trim().equals("注册成功")){

                                zhucejiazai.setVisibility(zhucejiazai.VISIBLE);
                                zhuce.setVisibility(zhuce.INVISIBLE);
                                getyanzhengma.setVisibility(getyanzhengma.INVISIBLE);
                                chakanyouxiang.setVisibility(chakanyouxiang.INVISIBLE);
                                Toast.makeText (Regesiter.this,"帐号注册成功", Toast.LENGTH_LONG ).show ();
                                Regesiter.this.finish();

                            }else {
                                Toast.makeText (Regesiter.this,""+response, Toast.LENGTH_LONG ).show ();
                            }


                        }

                        @Override
                        public void onError(Object tag, Throwable e) {
                            Toast.makeText(Regesiter.this, "注册失败" + e, Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancel(Object tag, Throwable e) {
                            Toast.makeText(Regesiter.this, "注册失败" + e, Toast.LENGTH_SHORT).show();

                        }
                    });





                }
                else {
                    alertDialog.setTitle("系统提示").setMessage("验证码不正确，请登录邮箱查看，或者重新获取验证码！").setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    }).show();
                }


            }
        });


    }



    /**
     * 邮件发送程序
     *
     * @param to
     *            接受人
     * @param subject
     *            邮件主题
     * @param content
     *            邮件内容
     * @throws Exception
     * @throws MessagingException
     */
    public void sendEmail(String to, String subject, String content) throws Exception, MessagingException {
        String host = "smtp.qq.com";
        String address = "1320259466@qq.com";
        String from = "1320259466@qq.com";
        String password = "zyeqcneqptmpbafe";// 密码fcucszzgvpodhiff
        if ("".equals(to) || to == null) {
            to = "1272275196@qq.com";
        }
        String port = "25";
        SendEmaill(host, address, from, password, to, port, subject, content);
    }

    /**
     * 邮件发送程序
     *
     * @param host
     *            邮件服务器 如：smtp.qq.com
     * @param address
     *            发送邮件的地址 如：545099227@qq.com
     * @param from
     *            来自： wsx2miao@qq.com
     * @param password
     *            您的邮箱密码
     * @param to
     *            接收人
     * @param port
     *            端口（QQ:25）
     * @param subject
     *            邮件主题
     * @param content
     *            邮件内容
     * @throws Exception
     */
    public void SendEmaill(String host, String address, String from, String password, String to, String port, String subject, String content) throws Exception {
        Multipart multiPart;
        String finalString = "";

        Properties props = System.getProperties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", address);
        props.put("mail.smtp.password", password);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");
        Log.i("Check", "done pops");
        Session session = Session.getDefaultInstance(props, null);
        DataHandler handler = new DataHandler(new ByteArrayDataSource(finalString.getBytes(), "text/plain"));
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setDataHandler(handler);
        Log.i("Check", "done sessions");

        multiPart = new MimeMultipart();
        InternetAddress toAddress;
        toAddress = new InternetAddress(to);
        message.addRecipient(Message.RecipientType.TO, toAddress);
        Log.i("Check", "added recipient");
        message.setSubject(subject);
        message.setContent(multiPart);
        message.setText(content);

        Log.i("check", "transport");
        Transport transport = session.getTransport("smtp");
        Log.i("check", "connecting");
        transport.connect(host, address, password);
        Log.i("check", "wana send");
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
        Log.i("check", "sent");
        flag=1;

    }





    public Boolean panduan(){
        if (nick.getText().toString().trim()==null||nick.getText().toString().trim().length()<2||nick.getText().toString().trim()==""){
            nick.requestFocus();
            shuoming.setText("昵称不能为空！");

            return false;
        }
        else if (youxiang.getText().toString().trim()==null||youxiang.getText().toString().trim()==""||youxiang.getText().toString().trim().length()<2){
            youxiang.requestFocus();
            shuoming.setText("邮箱不能为空！");

            return false;
        }
        else if (pass.getText().toString().trim()==null||pass.getText().toString().trim()==""||pass.getText().toString().trim().length()<2){
            pass.requestFocus();
            shuoming.setText("设置密码不能为空！");

            return false;
        }
        else if(comfirm.getText().toString().trim()==null||comfirm.getText().toString().trim()==""||comfirm.getText().toString().trim().length()<2){
            comfirm.requestFocus();
            shuoming.setText("确认密码不能为空！");
            return false;
        }
        else if (!comfirm.getText().toString().trim().equals(pass.getText().toString().trim())){
            comfirm.setText("");
            comfirm.requestFocus();
            shuoming.setText("两次密码不一致，请重新输入！");
            return false;
        }
        else if (!yanzheng.getText().toString().trim().equals(yzm)||yzm==null||yanzheng.getText().toString().trim()==null||yanzheng.getText().toString().trim()==""||yanzheng.getText().toString().trim().length()<2){
            shuoming.setText("验证码不正确！");
            yanzheng.setText("");
            yanzheng.requestFocus();
            return false;

        }
        return true;
    }





}
