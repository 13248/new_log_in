package com.example.sign_up;


import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class sign_up_page extends AppCompatActivity {
    //要连接的数据库url,注意：此处连接的应该是服务器上的MySQl的地址
     String url = "jdbc:mysql://120.24.191.82:3306/timeapp?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8";
    //连接数据库使用的用户名
     String userName = "timeapp";
    //连接的数据库时使用的密码
     String password = "user12345";
     Connection connection=null;
    private EditText id,name;
    private EditText password2;
    private Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);
        Button button= (Button) findViewById(R.id.zhuce);
        id= (EditText) findViewById(R.id.user_id);
        name=findViewById(R.id.user_log_name);
        password2= (EditText) findViewById(R.id.user_password);
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(sign_up_page.this,MainActivity.class);
                startActivity(intent);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        //获取输入框的数据
                        String edit_id=id.getText().toString();
                        String edit_name=name.getText().toString();
                        String edit_password=password2.getText().toString();
                        try {
                            //1、加载驱动
                            Class.forName("com.mysql.jdbc.Driver").newInstance();
                            System.out.println("驱动加载成功！！！");
                            //2、获取与数据库的连接
                            connection = DriverManager.getConnection(url, userName, password);
                            System.out.println("连接数据库成功！！！");

                            String table_id = "SELECT user_id FROM infos where user_id=?";
                            String table_name = "SELECT user_name FROM infos where user_name=?";
                            String table_psd = "SELECT user_password FROM infos where user_password=?";
                            PreparedStatement pre_id = connection.prepareStatement(table_id);
                            pre_id.setString(1, edit_id);
                            PreparedStatement pre_name = connection.prepareStatement(table_name);
                            pre_name.setString(1, edit_name);
                            PreparedStatement pre_psd = connection.prepareStatement(table_psd);
                            pre_psd.setString(1, edit_password);
                            ResultSet U_id = pre_id.executeQuery();
                            ResultSet U_name=pre_name.executeQuery();
                            ResultSet U_password = pre_psd.executeQuery();
                            U_id.last();
                            int id_count=U_id.getRow();
                            U_name.last();
                            int name_count=U_name.getRow();
                            //3.sql添加数据语句

                            if (!edit_id.equals("")&&!edit_name.equals("")&&!edit_password.equals("")){//判断输入框是否有数据
                                if(id_count !=0){
                                    Toast.makeText(sign_up_page.this,"学号已被注册",Toast.LENGTH_SHORT).show();
                                }
                                else if (name_count != 0) {
                                    Toast.makeText(sign_up_page.this,"昵称已被注册",Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    String sql = "INSERT INTO infos (user_id,user_name, user_password) VALUES (?, ?, ?)";
                                //4.获取用于向数据库发送sql语句的ps
                                PreparedStatement put_in_table=connection.prepareStatement(sql);
                                //获取输入框的数据 添加到mysql数据库
                                put_in_table.setString(1,edit_id);
                                put_in_table.setString(2,edit_name);
                                put_in_table.setString(3,edit_password);
                                put_in_table.executeUpdate();//更新数据库
                                Intent intent=new Intent(sign_up_page.this,MainActivity.class);
                                startActivity(intent);
                                Toast.makeText(sign_up_page.this,"注册成功",Toast.LENGTH_SHORT).show();
                            }}
                            else {
                                Toast.makeText(sign_up_page.this,"账号、昵称和密码不能为空",Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                        finally {
                            if(connection!=null){
                                try {
                                    connection.close();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }
                        Looper.loop();
                    }

                }).start();


            }
        });


    }




}