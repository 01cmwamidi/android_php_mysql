package com.clinton.android_php_myql;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class FetchActivity extends AppCompatActivity {

    @BindView(R.id.input_id)
    EditText input_id;

    @BindView(R.id.textViewTitle)
    TextView txtTitle;

    @BindView(R.id.textViewAuthor)
    TextView txtAuthor;

    @BindView(R.id.textViewYear)
    TextView txtYear;

    @BindView(R.id.textViewCost)
    TextView txtCost;

    @BindView(R.id.button_search)
    Button btnsearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button_search)
    public void fetch(){
        //check if internet is available
        if (! new Network().isInternetAvailable())
        {
            Toast.makeText(this, "Check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }

        String id = input_id.getText().toString().trim();

        if (id.isEmpty())
            return;

        RequestParams params = new RequestParams();
        params.put("id",id);

        String url = "http://3adf41e6.ngrok.io/apis/fetch.php";

        AsyncHttpClient client = new AsyncHttpClient();

        client.post(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
                Toast.makeText(FetchActivity.this, "Could not communicate to the server. Please try again", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int i, Header[] headers, String s) {

                //create a JSON object
                try {
                    JSONObject mainObject = new JSONObject(s);
                    String status = mainObject.getString("status");

                    if (status.equalsIgnoreCase("success"))
                    {
                        JSONObject data = mainObject.getJSONObject("data");
                        String title = data.getString("title");
                        String author = data.getString("author");
                        String year = data.getString("year");
                        int cost = data.getInt("cost");

                        txtTitle.setText(title);
                        txtAuthor.setText(author);
                        txtYear.setText(year);
                        txtCost.setText("Ksh "+cost);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
