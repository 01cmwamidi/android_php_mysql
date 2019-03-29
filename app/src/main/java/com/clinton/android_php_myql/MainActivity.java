package com.clinton.android_php_myql;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity
{

    @BindView(R.id.input_title)
    EditText input_title;

    @BindView(R.id.input_author)
    EditText input_author;

    @BindView(R.id.input_year)
    EditText input_year;

    @BindView(R.id.input_cost)
    EditText input_cost;

    @BindView(R.id.button_save)
    Button btnsave;

    @BindView(R.id.button_fetch)
    Button btnfetch;

    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        progress = new ProgressDialog(this);
        progress.setMessage("Saving...");
    }

    //save function
    @OnClick(R.id.button_save)
    public void save()
    {
        //check if internet is available
        if (! new Network().isInternetAvailable())
        {
            Toast.makeText(this, "Check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        String title = input_title.getText().toString().trim();
        String author = input_author.getText().toString().trim();
        String year = input_year.getText().toString().trim();
        String cost = input_cost.getText().toString().trim();

        //check if all inputs are filled
        if (title.isEmpty() || author.isEmpty() || year.isEmpty() || cost.isEmpty())
        {
            Toast.makeText(this, "Fill in all values", Toast.LENGTH_SHORT).show();
            return;
        }

        //parameters
        RequestParams params=new RequestParams();
        params.put("title",title);
        params.put("author",author);
        params.put("year",year);
        params.put("cost",cost);

        //data will be sent to the below url
        String url = "http://3adf41e6.ngrok.io/apis/save.php";

        AsyncHttpClient client = new AsyncHttpClient();

        progress.show();
        client.post(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
                progress.dismiss();
                Toast.makeText(MainActivity.this, "Failed to send Please Check Your Connection", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int i, Header[] headers, String s) {
                progress.dismiss();
                Toast.makeText(MainActivity.this, "Book Saved Successfully", Toast.LENGTH_SHORT).show();
                input_title.setText("");
                input_author.setText("");
                input_year.setText("");
                input_cost.setText("");
            }
        });
    }

    //fetch function
    @OnClick(R.id.button_fetch)
    public void fetch()
    {
        Intent x = new Intent(this, FetchActivity.class);
        startActivity(x);
    }

}
