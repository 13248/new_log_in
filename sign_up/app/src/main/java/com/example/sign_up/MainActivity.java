package com.example.sign_up;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class MainActivity extends AppCompatActivity {        //要连接的数据库url,注意：此处连接的应该是服务器上的MySQl的地址

    private String url = "jdbc:mysql://120.24.191.82:3306/timeapp?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8";
    //连接数据库使用的用户名
    private String userName = "timeapp";
    //连接的数据库时使用的密码
    private String password = "user12345";
    private Connection connection=null;
    private EditText id;
    private EditText password2;
    private Button new_sign;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.denglu);
        id=findViewById(R.id.user_log_id);

        password2 = (EditText) findViewById(R.id.user_log_password);
        Button esc=findViewById(R.id.esc);
        new_sign=findViewById(R.id.new_sign_up);
        new_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,sign_up_page.class);
                startActivity(intent);
            }
        });
        esc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        try {
                            Class.forName("com.mysql.jdbc.Driver").newInstance();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        System.out.println("驱动加载成功！！！");
                        try {
                            connection = DriverManager.getConnection(url, userName, password);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        System.out.println("连接数据库成功！！！");
                        //获取输入框的数据
                        try {
                            login();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        finally {
                            if(connection!=null){
                                try {
                                    connection.close();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }Looper.loop();
                    }
                }).start();
            }
            private void login() throws SQLException{
                //获取输入框的数据
                String edit_id = id.getText().toString();

                String edit_password = password2.getText().toString();
                String user_table = "SELECT * FROM infos where user_id=?";


                PreparedStatement pre_id = connection.prepareStatement(user_table);
                pre_id.setString(1, edit_id);

                // 创建用来执行sql语句的对象

                // 执行sql查询语句并获取查询信息
                ResultSet U_id = pre_id.executeQuery();



                if (edit_id.equals("") || edit_password.equals("")) {

                    Toast.makeText(MainActivity.this, "账号或密码不能为空", Toast.LENGTH_SHORT).show();

                }
                U_id.last();
                int id_count=U_id.getRow();

                U_id.first();
                        if(id_count == 0){
                            Toast.makeText(getApplicationContext(), "账号不存在", Toast.LENGTH_SHORT).show();

                        }

                   else if (!edit_id.equals(U_id.getString("user_id"))) {
                        if (edit_id.equals("")) {

                            Toast.makeText(getApplicationContext(), "账号不能为空", Toast.LENGTH_SHORT).show();

                        } else if (edit_password.equals("")) {

                            Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();

                        }

                    } else if (edit_id.equals(U_id.getString("user_id"))) {
                        if (!edit_password.equals(U_id.getString("user_password"))) {

                            Toast.makeText(getApplicationContext(), "密码不正确，请重试", Toast.LENGTH_SHORT).show();

                        } else {

                            Toast.makeText(getApplicationContext(), "登陆成功", Toast.LENGTH_SHORT).show();
                            String edit_name=U_id.getString("user_id");
                            if(User_IO.saveinfos(edit_id,edit_password,edit_name,MainActivity.this)){
                                System.out.println("写入成功");
                                List<String> userList=User_IO.get_userinfos(MainActivity.this);   ////////////////读入文档的
                                System.out.println(userList.get(0)+":"+userList.get(1)+":"+userList.get(2));       ///////读入文档的
                            }
                            else {
                                System.out.println("写入失败");
                            }

                        }

                    }connection.close();


                }





        });


    }}