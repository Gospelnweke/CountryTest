package com.example.countriesapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.countriesapp.R;
import com.example.countriesapp.viewModel.ListViewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.countriesList)
    RecyclerView countriList;

    @BindView(R.id.list_error)
    TextView listError;

    @BindView(R.id.loadingBar)
    ProgressBar loadingBar;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout refreshLayout;

    private ListViewModel viewModel;
    private CountryListAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);


        adapter=new CountryListAdapter(new ArrayList<>());

        viewModel= ViewModelProviders.of(this).get(ListViewModel.class);

        countriList.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        countriList.setAdapter(adapter);
        countriList.setHasFixedSize(true);

        refreshLayout.setOnRefreshListener(() -> {
            viewModel.refresh();
            refreshLayout.setRefreshing(false);
        });

        viewModel.refresh();

        observerViewmodel();



    }





    private void observerViewmodel() {

        viewModel.countries.observe(this, countryModels -> {
            if(countryModels != null){
                //countriesList.setVisibility(View.VISIBLE);
                adapter.updateCountries(countryModels);
            }

        });

        viewModel.countryLoadError.observe(this,isError -> {
           if(isError!=null){

               listError.setVisibility(isError ? View.VISIBLE : View.GONE);

           }
        });

        viewModel.loading.observe(this,isLoading -> {
            if(isLoading != null){
                loadingBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
            if(isLoading){
                listError.setVisibility(View.GONE);
                countriList.setVisibility(View.GONE);
            }
        });
    }


}