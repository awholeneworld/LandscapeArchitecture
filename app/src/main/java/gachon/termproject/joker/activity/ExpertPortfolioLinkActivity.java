package gachon.termproject.joker.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import gachon.termproject.joker.R;

public class ExpertPortfolioLinkActivity extends AppCompatActivity {

    private EditText original_link_text;
    private Button original_link_button;
    private EditText new_link_text;
    private Button new_link_button;
    private Button change_link_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_portfolio_link);

        original_link_text = (EditText)findViewById(R.id.original_link_text);

        original_link_button = (Button)findViewById(R.id.original_link_button);
        original_link_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(original_link_text.getText().toString())));
            }
        });

        new_link_text = (EditText)findViewById(R.id.new_link_text);

        new_link_button = (Button)findViewById(R.id.new_link_button);
        new_link_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(new_link_text.getText().toString())));
            }
        });

        change_link_button = (Button)findViewById(R.id.change_link_button);
        change_link_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newLink = new_link_text.getText().toString();
                // 링크 변경 로직
            }
        });
    }
}