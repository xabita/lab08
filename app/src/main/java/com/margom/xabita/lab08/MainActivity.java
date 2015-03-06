package com.margom.xabita.lab08;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.margom.xabita.lab08.data.NamesArray;

public class MainActivity extends Activity {

    public static NamesArray mNamesArray;
    public listado mNameListAdapter;
    public ListView listView;
    public Button btnLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLoad = (Button) findViewById(R.id.btn_load);
        listView = (ListView) findViewById(R.id.list_view);


        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebServices.getNames(mNamesHandler);
                btnLoad.setBackgroundColor(getResources().getColor(R.color.gray));
            }
        });

    }


    private Handler mNamesHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            //get response
            String response = bundle != null ? bundle.getString("response") : "";

            //Check response
            if ((response.equals("")) || response.equals("no connection")) {
                //No internet access
                Toast.makeText(getBaseContext(),
                        getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
            }

            else {

                try {
                    //get json response into object
                    mNamesArray = new Gson().fromJson(response, NamesArray.class);
                    mNameListAdapter = new listado(getBaseContext(),mNamesArray.getNames());
                    listView.setAdapter(mNameListAdapter);
                    btnLoad.setBackgroundColor(getResources().getColor(R.color.green));

                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    //invalid json response from the server
                    Toast.makeText(getBaseContext(), getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                }
            }
        }

    };


}
