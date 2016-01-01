package in.ngrail.NGERail;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class WebViewActivity extends AppCompatActivity {

    public String dateval = null;
    public String jsonvalue = "";
    public String jsonval = "";
    private WebView web;
    private ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.irctcwebview_main);
        Bundle bundle = getIntent().getExtras();
        jsonvalue = bundle.getString("jsonvalue");
        dateval = bundle.getString("dateval");
        jsonval = bundle.getString("jsonval");
        try {

            Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
            myToolbar.setTitle("NGRail IRCTC WEB View");
            myToolbar.setNavigationIcon(R.drawable.leftarrow);
            myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i;
                    WebViewActivity.this.finish();
                    i = new Intent(WebViewActivity.this, AdvSeatAvail.class);
                    i.putExtra("jsonvalue", jsonvalue);
                    i.putExtra("dateval", dateval);
                    i.putExtra("jsonval", jsonval);
                    WebViewActivity.this.startActivity(i);
                    overridePendingTransition(R.anim.slide_in_b, R.anim.slide_out_b);
                }
            });

            //myToolbar.setSubtitle("One Stop Train Enquiry Hub");
            myToolbar.setLogo(R.mipmap.ngrailsmlogo);
            pb = (ProgressBar)findViewById(R.id.imageView111);
            pb.setVisibility(View.GONE);

            web = (WebView)findViewById(R.id.irctcweb);
            web.getSettings().setJavaScriptEnabled(true);
            // Allow Zoom in/out controls
            web.getSettings().setBuiltInZoomControls(true);

            // Zoom out the best fit your screen
            web.getSettings().setLoadWithOverviewMode(true);
            web.getSettings().setUseWideViewPort(true);
            web.loadUrl("http://www.irctc.co.in");

            // Show the progress bar
            web.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    setProgress(progress * 100);
                }
            });

            // Call private class InsideWebViewClient
            web.setWebViewClient(new InsideWebViewClient());

        }catch (Exception e)
        {

        }
    }

    private class InsideWebViewClient extends WebViewClient {
        @Override
        // Force links to be opened inside WebView and not in Default Browser
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;

        }
        @Override
        public void onPageFinished(WebView view, String url) {
            pb.setVisibility(View.GONE);
            WebViewActivity.this.pb.setProgress(100);
            super.onPageFinished(view, url);
        }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            pb.setVisibility(View.VISIBLE);
            WebViewActivity.this.pb.setProgress(0);
            super.onPageStarted(view, url, favicon);
        }

    }

    @Override
    public void onBackPressed() {
        // your code.
        Intent i;
        WebViewActivity.this.finish();
        i = new Intent(WebViewActivity.this, AdvSeatAvail.class);
        i.putExtra("jsonvalue", jsonvalue);
        i.putExtra("dateval", dateval);
        i.putExtra("jsonval", jsonval);
        WebViewActivity.this.startActivity(i);
        // This makes the new screen slide up as it fades in
        // while the current screen slides up as it fades out.
        overridePendingTransition(R.anim.slide_in_b, R.anim.slide_out_b);
    }

}
