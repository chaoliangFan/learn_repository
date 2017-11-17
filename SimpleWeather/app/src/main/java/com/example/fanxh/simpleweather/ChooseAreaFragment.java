package com.example.fanxh.simpleweather;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.example.fanxh.simpleweather.db.DbUtil;
import com.example.fanxh.simpleweather.util.HttpUtil;
import com.example.fanxh.simpleweather.util.Utility;

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
    public final List<String> dataList = new ArrayList<>();
    public static SQLiteDatabase db;
    private String selectedProvinceF;
    /**
     * 选中的城市
     */
    private String selectedCityF;
    private String selectedCountyF;

    private City selectedCity;
    private County selectedCounty;
    /**
     * 当前选中的级别
     */
    public static int currentLevel;
    public Cursor cursor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area_fragment, container, false);
        db = DbUtil.getDb(getActivity());
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
                    selectedProvinceF = dataList.get(position);
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    selectedCityF = dataList.get(position);
                    queryCounties();
                } else if (currentLevel == LEVEL_COUNTY) {
                    selectedCountyF = dataList.get(position);
                    try {
                        cursor = db.query("County", null, "county_name = ?", new String[]{selectedCountyF}, null, null, null);
                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                String weatherId = cursor.getString(cursor.getColumnIndex("weatherId"));
                                Intent intent = new Intent(getActivity(), SearchAreaActivity.class);
                                intent.putExtra("weather_id", weatherId);
                                intent.putExtra("countyName", selectedCountyF);
                                getActivity().startActivity(intent);
                            }
                        }
                        getActivity().finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            cursor.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        /**
         * 获取查看的天气的地区
         */

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
    public void queryProvinces() {
        mTitleText.setText("中国");
        mBackButton.setVisibility(View.GONE);
        try {
            cursor = db.query("Province", null, null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                dataList.clear();
                do {
                    String provinceName = cursor.getString(cursor.getColumnIndex("province_name"));
                    dataList.add(provinceName);
                } while (cursor.moveToNext());

                mShowAreaAdapter.notifyDataSetChanged();
                mShowArea.setSelection(0);
                currentLevel = LEVEL_PROVINCE;
            } else {
                String address = "http://guolin.tech/api/china";
                queryFromServe(address, "province");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 查询选中的所有的市，优先从数据库中查询，如果没有查到再去服务器上查询
     */
    public void queryCities() {
        mTitleText.setText(selectedProvinceF);
        mBackButton.setVisibility(View.VISIBLE);
        try {
            cursor = db.query("Province", null, "province_name = ?", new String[]{selectedProvinceF}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int provinceCode = cursor.getInt(cursor.getColumnIndex("province_code"));
                try {
                    cursor = db.query("City", null, "province_id = ?", new String[]{"" + provinceCode}, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        dataList.clear();
                        do {
                            String cityName = cursor.getString(cursor.getColumnIndex("city_name"));
                            dataList.add(cityName);
                        } while (cursor.moveToNext());
                        if (dataList.size() != 1) {
                            mShowAreaAdapter.notifyDataSetChanged();
                            mShowArea.setSelection(0);
                            currentLevel = LEVEL_CITY;
                        } else {
                            if (cursor.moveToFirst()) {
                                String cityName = cursor.getString(cursor.getColumnIndex("city_name"));
                                selectedCityF = cityName;
                                queryCounties();
                            }
                        }
                    } else {
                        String address = "http://guolin.tech/api/china/" + provinceCode;
                        queryFromServe(address, "city");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 查询选中的市内的所有县，优先从数据库查询，如果没有查询到再去服务器上查询
     */
    private void queryCounties() {
        mTitleText.setText(selectedCityF);
        mBackButton.setVisibility(View.VISIBLE);
        try {
            cursor = db.query("City", null, "city_name = ?", new String[]{selectedCityF}, null, null, null);
            if (cursor.moveToFirst()) {
                int cityCode = cursor.getInt(cursor.getColumnIndex("city_code"));
                try {
                    cursor = db.query("County", null, "city_id = ?", new String[]{"" + cityCode}, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        dataList.clear();
                        do {
                            String countyName = cursor.getString(cursor.getColumnIndex("county_name"));
                            dataList.add(countyName);
                        } while (cursor.moveToNext());
                        mShowAreaAdapter.notifyDataSetChanged();
                        mShowArea.setSelection(0);
                        currentLevel = LEVEL_COUNTY;
                    } else {
                        try {
                            cursor = db.query("City", null, "city_name = ?", new String[]{selectedCityF}, null, null, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                int provinceCode = cursor.getInt(cursor.getColumnIndex("province_id"));
                                String address = "http://guolin.tech/api/china/" + provinceCode + "/" + cityCode;
                                queryFromServe(address, "county");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
                    result = Utility.handleProvincesResponse(responseText, db);
                } else if ("city".equals(type)) {
                    try {
                        cursor = db.query("Province", null, "province_name = ?", new String[]{selectedProvinceF}, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            int provinceCode = cursor.getInt(cursor.getColumnIndex("province_code"));
                            result = Utility.handleCitiesResponse(responseText, provinceCode, db);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            cursor.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else if ("county".equals(type)) {
                    try {
                        cursor = db.query("City", null, "city_name = ?", new String[]{selectedCityF}, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            int cityCode = cursor.getInt(cursor.getColumnIndex("city_code"));
                            result = Utility.handleCountiesResponse(responseText, cityCode, db);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            cursor.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
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
