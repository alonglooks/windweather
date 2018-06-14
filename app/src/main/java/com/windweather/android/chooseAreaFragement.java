package com.windweather.android;

;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.windweather.android.db.City;
import com.windweather.android.db.County;
import com.windweather.android.db.Province;
import com.windweather.android.util.OkHttpUtil;
import com.windweather.android.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by jiepeng on 2018/6/13.
 */

public class chooseAreaFragement extends Fragment {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;
   public ProgressDialog progressDialog;
   private boolean result;
    private TextView titleText;
    private Button backButton;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> datalist = new ArrayList<>();
    //省列表
    private  List<Province> provinceList;
    //市列表
    private List<City> cityList;
    //县列表
    private List<County> countyList;
    //选中的省份
    private Province selectedProvince;
    //选中的市
    private City selectedCity;
    //当前选中的级别
    private int currentLevel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area, container, false);
        titleText = view.findViewById(R.id.title_text);
        backButton =  view.findViewById(R.id.back_button);
        listView = view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(Myapplication.getMyapplication(), android.R.layout.simple_list_item_1, datalist);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (currentLevel == LEVEL_PROVINCE){
                    selectedProvince = provinceList.get(i);
                    queryCities();
                }else if (currentLevel == LEVEL_CITY){
                    selectedCity = cityList.get(i);
                    queryCounties();
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentLevel == LEVEL_COUNTY){
                    queryCities();
                }else if (currentLevel == LEVEL_CITY){
                    queryCounties();
                }
            }
        });
        queryProvinces();

    }

    private void queryCities() {
        titleText.setText(selectedProvince.getmProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList = DataSupport.where("mprovinceid = ?",String.valueOf(selectedProvince.getId())).find(City.class);
        if (cityList.size() > 0){
            datalist.clear();
            for (City city : cityList){
                datalist.add(city.getmCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        }else {
            int provinceCode = selectedProvince.getmProivinceCode();
            String address = "http://guolin.tech/api/china/" + provinceCode;
            queryFromServer(address,"city");
        }
    }


    private void queryProvinces() {
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        provinceList = DataSupport.findAll(Province.class);
        if (provinceList.size() > 0){
            datalist.clear();
            for (Province province:provinceList){
                datalist.add(province.getmProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        }else {
            String address = "http://guolin.tech/api/china";
            queryFromServer(address,"province");
        }


    }

    private void queryFromServer(String address, final String type) {
        OkHttpUtil.sendOkHttpRequest(address, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(Myapplication.getMyapplication(),"数据失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                showProgressDialog();
                String responseText = response.body().string();
                if ("province".equals(type)){
                   result =  Utility.handleProvinceResponse(responseText);
                }else if ("city".equals(type)){
                     result = Utility.handleCityResponse(responseText, selectedProvince.getId());
                }else if ("county".equals(type)){
                    result = Utility.handCountyResponse(responseText,selectedCity.getId());
                }
                if (result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgessDialog();
                            if ("province".equals(type)){
                                queryProvinces();

                            }else if ("city".equals(type)){
                                queryCities();
                            }else if ("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }

            }
        });
    }

    private void queryCounties() {
        titleText.setText(selectedCity.getmCityName());
        countyList = DataSupport.where(" mcityid = ?",String.valueOf(selectedCity.getId())).find(County.class);
        if (countyList.size() > 0){
            datalist.clear();
            for (County county:countyList){
                datalist.add(county.getmCountyName());
                adapter.notifyDataSetChanged();
                listView.setSelection(0);
                currentLevel = LEVEL_COUNTY;
            }
        }else {
            int provinceCode = selectedProvince.getmProivinceCode();
            int cityCode = selectedCity.getmCityCode();
            String address = "http://guolin.tech/api/china/" + provinceCode +"/" + cityCode;
            queryFromServer(address,"county");
        }


    }

    private void closeProgessDialog() {
        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }

    private void showProgressDialog() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog == null){
                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("正在加载");
                    progressDialog.setCanceledOnTouchOutside(false);
                }
                progressDialog.show();
            }
        });

    }
}
