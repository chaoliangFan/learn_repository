package com.example.fanxh.simpleweather;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fanxh.simpleweather.db.City;
import com.example.fanxh.simpleweather.db.County;
import com.example.fanxh.simpleweather.db.Province;
import com.example.fanxh.simpleweather.util.HttpUtil;
import com.example.fanxh.simpleweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by fanxh on 2017/10/23.
 */

public class ChooseAreaFragment extends Fragment {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;
    private ProgressDialog progressDidog;
    private TextView mTitleText;
    private Button mBackButton;
    private ListView mShowArea;
    private ArrayAdapter<String> mShowAreaAdapter;
    private List<String> dataList = new ArrayList<>();
    /**
     * 省列表
     */
    private List<Province> provinceList;
    /**
     * 市列表
     */
    private List<City> cityList;
    /**
     * 区或县列表
     */
    private List<County> countyList;
    /**
     * 选中的省份
     */
    private Province selectedProvince;
    /**
     * 选中的城市
     */
    private City selectedCity;
    private County selectedCounty;
    /**
     * 当前选中的级别
     */
    private int currentLevel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area_fragment, container, false);
        mTitleText = (TextView) view.findViewById(R.id.title_text);
        mBackButton = (Button) view.findViewById(R.id.back_button);
        mShowArea = (ListView) view.findViewById(R.id.list_view);
        mShowAreaAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, dataList);
        mShowArea.setAdapter(mShowAreaAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mShowArea.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectedProvince = provinceList.get(position);
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    selectedCity = cityList.get(position);
                    queryCounties();
                } else if (currentLevel == LEVEL_COUNTY) {
                    selectedCounty = countyList.get(position);
                    String weatherId = selectedCounty.getWeatherId();
                    Intent intent = new Intent(getActivity(), WeatherActivity.class);
                    intent.putExtra("weather_id", weatherId);
                    getActivity().startActivity(intent);
                    getActivity().finish();
                }


                /**
                 * 获取查看的天气的地区
                 */


            }
        });
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLevel == LEVEL_COUNTY) {
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    queryProvinces();
                }
            }
        });
        queryProvinces();
    }

    /**
     * 查询全国所有的省，优先从数据库里查询，如果没有查询到再去服务器上查询
     */
    private void queryProvinces() {
        mTitleText.setText("中国");
        mBackButton.setVisibility(View.GONE);
        provinceList = DataSupport.findAll(Province.class);
        if (provinceList.size() > 0) {
            dataList.clear();
            for (Province province : provinceList) {
                dataList.add(province.getProvinceName());
            }
            mShowAreaAdapter.notifyDataSetChanged();
            mShowArea.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        } else {
            String address = "http://guolin.tech/api/china";
            queryFromServe(address, "province");
        }
    }

    /**
     * 查询选中的所有的市，优先从数据库中查询，如果没有查到再去服务器上查询
     */
    private void queryCities() {
        mTitleText.setText(selectedProvince.getProvinceName());
        mBackButton.setVisibility(View.VISIBLE);
        cityList = DataSupport.where("provinceid = ?", String.valueOf(selectedProvince.getId())).find(City.class);
        if (cityList.size() > 0) {
            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getCityName());
            }
            mShowAreaAdapter.notifyDataSetChanged();
            mShowArea.setSelection(0);
            currentLevel = LEVEL_CITY;
        } else {
            int provinceCode = selectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/" + provinceCode;
            queryFromServe(address, "city");
        }
    }

    /**
     * 查询选中的市内的所有县，优先从数据库查询，如果没有查询到再去服务器上查询
     */
    private void queryCounties() {
        mTitleText.setText(selectedCity.getCityName());
        mBackButton.setVisibility(View.VISIBLE);
        countyList = DataSupport.where("cityid = ?", String.valueOf(selectedCity.getId())).find(County.class);
        if (countyList.size() > 0) {
            dataList.clear();
            for (County county : countyList) {
                dataList.add(county.getCountyName());
            }
            mShowAreaAdapter.notifyDataSetChanged();
            mShowArea.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        } else {
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/" + provinceCode + "/" + cityCode;
            queryFromServe(address, "county");
        }
    }

    /**
     * 根据传入的地址和类型从服务器上查询省市县数据
     */
    private void queryFromServe(String address, final String type) {
        showProgressDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getActivity(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                if ("province".equals(type)) {
                    result = Utility.handleProvincesResponse(responseText);
                } else if ("city".equals(type)) {
                    result = Utility.handleCitiesResponse(responseText, selectedProvince.getId());
                } else if ("county".equals(type)) {
                    result = Utility.handleCountiesResponse(responseText, selectedCity.getId());
                }
                if (result) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)) {
                                queryProvinces();
                            } else if ("city".equals(type)) {
                                queryCities();
                            } else if ("county".equals(type)) {
                                queryCounties();
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {
        if (progressDidog == null) {
            progressDidog = new ProgressDialog(getActivity());
            progressDidog.setMessage("正在加载...");
            progressDidog.setCanceledOnTouchOutside(false);
        }
        progressDidog.show();
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog() {
        if (progressDidog != null) {
            progressDidog.dismiss();
        }
    }
}
