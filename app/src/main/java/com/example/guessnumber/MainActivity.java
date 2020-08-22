package com.example.guessnumber;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private String ans;
    private EditText editText;
    private TextView textView;
    private int count;
    private int digit=3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText=findViewById(R.id.input);
        textView=findViewById(R.id.log);
        init();
    }

    public void guess(View view) {
        String guessnum=editText.getText().toString();
        String result=check(guessnum);
        editText.setText("");
        textView.append(count+" : "+guessnum+" : "+result+"\n");

        if(result.equals(digit+"A0B")){
            showdialog(true);
        }else if(count==5){
            showdialog(false);
        }
    }

    private void showdialog(boolean winornot){
        String mesg=winornot ? "獲勝":"懂不懂猜阿"+ans;
        AlertDialog alertDialog=null;
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Result");
        builder.setMessage(mesg);
        builder.setCancelable(false);
        builder.setPositiveButton("再來一次嘛", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                newround(null);
            }
        }).setNegativeButton("不玩了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.super.finish();
            }
        });
        alertDialog=builder.create();
        alertDialog.show();
    }
    private String check(String guessnum){
        int A=0,B=0;
        count++;
        for(int i=0;i<guessnum.length();i++){
            if(guessnum.charAt(i)==ans.charAt(i)){
                A++;
            }else if(guessnum.indexOf(ans.charAt(i))!=-1){
                B++;
            }
        }
        return String.format("%dA%dB",A,B);
    }

    public void exit(View view) {
        finish();
    }

    public void init(){
        ans=createans();
        editText.setText("");
        textView.setText("");
        count=0;
    }
    private long lastTime = 0;

    @Override
    public void finish() {
        if (System.currentTimeMillis() - lastTime < 3*1000){
            super.finish();
        }else{
            lastTime = System.currentTimeMillis();
            Toast.makeText(this, "One more time exit", Toast.LENGTH_SHORT).show();
        }
    }
    public String createans(){
        HashSet<Integer> set=new HashSet<>();
        String ret="";
        while (set.size()<digit){
            set.add((int)(Math.random()*10));
        }
        for(Integer i :set){
            ret+=i;
        }
        return ret;
    }

    public void newround(View view) {
        ans=createans();
        init();
        Log.d(TAG, "newround: "+ans);
    }
    private int tempdit=digit;
    public void settings(View view) {
        String[] items={"2","3","4","5"};
        new AlertDialog.Builder(this)
                .setTitle("想猜幾碼?")
                .setSingleChoiceItems(items, digit - 2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tempdit=i+2;

                    }
                })
                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        digit=tempdit;
                        newround(null);
                    }
                })
                .create()
                .show();
    }
}