package com.bjzc.greendao;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.bjzc.greendao.entity.PersonInfor;
import com.bjzc.greendao.entity.User;
import com.bjzc.greendao.utils.DBManager;
import com.bjzc.greendao.utils.DbController;

import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private Button Add,Delete,Update,Search;
    private DbController mDbController;
    private PersonInfor personInfor1,personInfor2,personInfor3,personInfor4,personInfor5;
    private long insertTipId;
    private TextView dataArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDbController = DbController.getInstance(MainActivity.this);
        initView();
        Envent();
        similateData();
    }
    private void similateData() {
        personInfor1 = new PersonInfor(null,UUID.randomUUID().toString(),"王大宝","男");
        personInfor2 = new PersonInfor(null,UUID.randomUUID().toString(),"李晓丽","女");
        personInfor3 = new PersonInfor(null,UUID.randomUUID().toString(),"王麻麻","男");
        personInfor4 = new PersonInfor(null,UUID.randomUUID().toString(),"王大锤","女");
        personInfor5 = new PersonInfor(null,UUID.randomUUID().toString(),"王大天","女");
    }

    private void Envent() {

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<100;i++){
                    personInfor1 = new PersonInfor(null,UUID.randomUUID().toString(),"王大宝","男");
                    personInfor2 = new PersonInfor(null,UUID.randomUUID().toString(),"李晓丽","女");
                    personInfor3 = new PersonInfor(null,UUID.randomUUID().toString(),"王麻麻","男");
                    personInfor4 = new PersonInfor(null,UUID.randomUUID().toString(),"王大锤","女");
                    personInfor5 = new PersonInfor(null,UUID.randomUUID().toString(),"王大天","女");
                    //Add
                    mDbController.insertOrReplace(personInfor1);

                    mDbController.insertOrReplace(personInfor2);

                    mDbController.insertOrReplace(personInfor3);

                    mDbController.insertOrReplace(personInfor4);
                    mDbController.insertOrReplace(personInfor5);
                }
                showDataList();



//                DBManager dbManager = DBManager.getInstance(MainActivity.this);
//                for (int i = 0; i < 5; i++) {
//                    User user = new User();
//                    user.setId((long) i);
//                    user.setAge(i * 3);
//                    user.setName("第" + i + "人");
//                    dbManager.insertUser(user);
//                }
//                List<User> userList = dbManager.queryUserList();
//                for (User user : userList) {
//                    Log.e("TAG", "queryUserList--before-->" + user.getId() + "--" + user.getName() +"--"+user.getAge());
//                    if (user.getId() == 0) {
//                        dbManager.deleteUser(user);
//                    }
//                    if (user.getId() == 3) {
//                        user.setAge(10);
//                        dbManager.updateUser(user);
//                    }
//                }
//                userList = dbManager.queryUserList();
//                for (User user : userList) {
//                    Log.e("TAG", "queryUserList--after--->" + user.getId() + "---" + user.getName()+"--"+user.getAge());
//                }

            }
        });
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<PersonInfor>personInfors = mDbController.searchAll();
                for(PersonInfor personInfor:personInfors){
                    //Delete
                    mDbController.delete(personInfor.getName());
                }
                showDataList();
            }
        });

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Update
                mDbController.update(personInfor1);

                showDataList();
            }
        });

        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Search
                showDataList();
            }
        });
    }

    private void showDataList() {
        StringBuilder sb = new StringBuilder();
        List<PersonInfor>personInfors = mDbController.searchAll();
        for(PersonInfor personInfor:personInfors){
            // dataArea.setText("id:"+p);
            sb.append("id:").append(personInfor.getId())
                    .append("perNo:").append(personInfor.getPerNo())
                    .append("name:").append(personInfor.getName())
                    .append("sex:").append(personInfor.getSex())
                    .append("\n");
        }
        dataArea.setText(sb.toString());
    }

    private void initView() {

        Add = findViewById(R.id.button);

        Delete = findViewById(R.id.button2);

        Update = findViewById(R.id.button3);

        Search = findViewById(R.id.button4);

        dataArea= findViewById(R.id.textView);

    }
}