package gachon.termproject.joker.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import gachon.termproject.joker.R;
import gachon.termproject.joker.container.PortfolioMyInfoContent;
import gachon.termproject.joker.fragment.MyInfoPortfolioAdapter;

public class PortfolioMyInfo extends AppCompatActivity {
    private View view;
    private RecyclerView contents;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<PortfolioMyInfoContent> dataSet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.portfolio_myinfo);

        contents = (RecyclerView)findViewById(R.id.content_portfolio_myinfo);
        contents.setHasFixedSize(true);
        contents.setLayoutManager(new GridLayoutManager(getApplication(), 1));

        dataSet = new ArrayList<>();
        mAdapter = new MyInfoPortfolioAdapter(dataSet);
        contents.setAdapter(mAdapter);


        dataSet.add(new PortfolioMyInfoContent(R.mipmap.ic_launcher, "제목1"));
        dataSet.add(new PortfolioMyInfoContent(R.mipmap.ic_launcher, "제목2"));
        dataSet.add(new PortfolioMyInfoContent(R.mipmap.ic_launcher, "제목3"));
        dataSet.add(new PortfolioMyInfoContent(R.mipmap.ic_launcher, "제목4"));
        dataSet.add(new PortfolioMyInfoContent(R.mipmap.ic_launcher, "제목5"));
        dataSet.add(new PortfolioMyInfoContent(R.mipmap.ic_launcher, "제목6"));
        dataSet.add(new PortfolioMyInfoContent(R.mipmap.ic_launcher, "제목7"));
        dataSet.add(new PortfolioMyInfoContent(R.mipmap.ic_launcher, "제목8"));
        dataSet.add(new PortfolioMyInfoContent(R.mipmap.ic_launcher, "제목9"));
    }
}