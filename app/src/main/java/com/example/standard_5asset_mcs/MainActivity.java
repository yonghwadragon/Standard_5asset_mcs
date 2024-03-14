package com.example.standard_5asset_mcs;

import android.os.Bundle;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {
    Random random = new Random();
    Button Calculate_button;
    int len = 6;
    EditText[] Date_Edit = new EditText[len+1];
    EditText[] Coupon_Edit = new EditText[len];
    EditText[] Strike_Edit = new EditText[len];
    EditText Face_value_Edit;
    EditText Risk_free_rate_Edit;
    EditText Volatility1_Edit;
    EditText Volatility2_Edit;
    EditText Volatility3_Edit;
    EditText Volatility4_Edit;
    EditText Correlation12_Edit;
    EditText Correlation13_Edit;
    EditText Correlation14_Edit;
    EditText Correlation23_Edit;
    EditText Correlation24_Edit;
    EditText Correlation34_Edit;
    EditText Knock_in_Barrier_Edit;
    EditText Dummy_Edit;
    EditText NoSimulation_Edit;
    TextView ElapsedTime_Result1;
    TextView ElapsedTime_Result2;
    TextView ElapsedTime_Result3;
    TextView Price_Result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Four-asset step-down ELS Calculator");
        Date_Edit[0] = (EditText) findViewById(R.id.Basedate);
        Date_Edit[1] = (EditText) findViewById(R.id.Redemption1);
        Date_Edit[2] = (EditText) findViewById(R.id.Redemption2);
        Date_Edit[3] = (EditText) findViewById(R.id.Redemption3);
        Date_Edit[4] = (EditText) findViewById(R.id.Redemption4);
        Date_Edit[5] = (EditText) findViewById(R.id.Redemption5);
        Date_Edit[6] = (EditText) findViewById(R.id.Maturity);
        Coupon_Edit[0] = (EditText) findViewById(R.id.Coupon1);
        Coupon_Edit[1] = (EditText) findViewById(R.id.Coupon2);
        Coupon_Edit[2] = (EditText) findViewById(R.id.Coupon3);
        Coupon_Edit[3] = (EditText) findViewById(R.id.Coupon4);
        Coupon_Edit[4] = (EditText) findViewById(R.id.Coupon5);
        Coupon_Edit[5] = (EditText) findViewById(R.id.Coupon6);
        Strike_Edit[0] = (EditText) findViewById(R.id.Strike1);
        Strike_Edit[1] = (EditText) findViewById(R.id.Strike2);
        Strike_Edit[2] = (EditText) findViewById(R.id.Strike3);
        Strike_Edit[3] = (EditText) findViewById(R.id.Strike4);
        Strike_Edit[4] = (EditText) findViewById(R.id.Strike5);
        Strike_Edit[5] = (EditText) findViewById(R.id.Strike6);
        Face_value_Edit = findViewById(R.id.Face_value);
        Risk_free_rate_Edit = (EditText) findViewById(R.id.Risk_free_rate);
        Volatility1_Edit = findViewById(R.id.Volatility1);
        Volatility2_Edit = findViewById(R.id.Volatility2);
        Volatility3_Edit = findViewById(R.id.Volatility3);
        Volatility4_Edit = findViewById(R.id.Volatility4);
        Correlation12_Edit = findViewById(R.id.Correlation12);
        Correlation13_Edit = findViewById(R.id.Correlation13);
        Correlation14_Edit = findViewById(R.id.Correlation14);
        Correlation23_Edit = findViewById(R.id.Correlation23);
        Correlation24_Edit = findViewById(R.id.Correlation24);
        Correlation34_Edit = findViewById(R.id.Correlation34);
        Knock_in_Barrier_Edit = (EditText) findViewById(R.id.Knock_in_Barrier);
        Dummy_Edit = (EditText) findViewById(R.id.Dummy);
        NoSimulation_Edit = (EditText) findViewById(R.id.NoSimulation);
        //  ElapsedTime_Result1 = (TextView) findViewById(R.id.ElapsedTime1);
        //  ElapsedTime_Result2 = (TextView) findViewById(R.id.ElapsedTime2);
        ElapsedTime_Result3 = (TextView) findViewById(R.id.ElapsedTime3);
        Price_Result = (TextView) findViewById(R.id.Price);
        Calculate_button = (Button) findViewById(R.id.Calculate);
        Calculate_button.setOnClickListener(this::onClick);
    }
    public long diffOfDate(String begin, String end) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date beginDate = formatter.parse(begin);
        Date endDate = formatter.parse(end);
        long diff = endDate.getTime() - beginDate.getTime();
        long diffDays = diff / (24 * 60 * 60 * 1000);
        return diffDays;
    }
    private void onClick(View arg0) {
        long start1 = System.currentTimeMillis();
        long start3 = System.currentTimeMillis();
        int tot_date, M;
        int[] check_dayInt = new int[len];
        long[] check_day = new long[len];
        double Face_value, r, vol1, vol2, vol3, vol4, Knock_in_Barrier, dummy, ran1, ran2, ran3, ran4, corr12, corr13, corr14, corr23, corr24, corr34;
        double dt = 1.0 / 365.0;
        double sum = 0.0;
        double[] coupon_rate = new double[len];
        double[] strike_price = new double[len];
        double[] S1;
        double[] S2;
        double[] S3;
        double[] S4;
        double[] indexs = new double[len];
        double[] payoff;
        double[] tot_payoff = new double[len];
        double[] disc_payoff = new double[len];
        double[] payment = new double[len];
        String[] Date = new String[len + 1];
        String[] Coupon_String = new String[len];
        String[] Strike_String = new String[len];
        String Face_value_String, Risk_free_rate_String, Knock_in_Barrier_String, Volatility1_String, Volatility2_String, Volatility3_String, Volatility4_String;
        String Correlation12_String, Correlation13_String, Correlation14_String, Correlation23_String, Correlation24_String, Correlation34_String;
        String Dummy_String, NoSimulation_String;
        for (int i = 0; i < len + 1; i++) {
            Date[i] = Date_Edit[i].getText().toString();
        }
        for (int i = 0; i < len; i++) {
            Coupon_String[i] = Coupon_Edit[i].getText().toString();
            Strike_String[i] = Strike_Edit[i].getText().toString();
        }
        Face_value_String = Face_value_Edit.getText().toString();
        Risk_free_rate_String = Risk_free_rate_Edit.getText().toString();
        Volatility1_String = Volatility1_Edit.getText().toString();
        Volatility2_String = Volatility2_Edit.getText().toString();
        Volatility3_String = Volatility3_Edit.getText().toString();
        Volatility4_String = Volatility4_Edit.getText().toString();
        Correlation12_String = Correlation12_Edit.getText().toString();
        Correlation13_String = Correlation13_Edit.getText().toString();
        Correlation14_String = Correlation14_Edit.getText().toString();
        Correlation23_String = Correlation23_Edit.getText().toString();
        Correlation24_String = Correlation24_Edit.getText().toString();
        Correlation34_String = Correlation34_Edit.getText().toString();
        Knock_in_Barrier_String = Knock_in_Barrier_Edit.getText().toString();
        Dummy_String = Dummy_Edit.getText().toString();
        NoSimulation_String = NoSimulation_Edit.getText().toString();
        if (Date[0].trim().equals("")) {
            Date[0] = "20220714";
        }
        if (Date[1].trim().equals("")) {
            Date[1] = "20230110";
        }
        if (Date[2].trim().equals("")) {
            Date[2] = "20230711";
        }
        if (Date[3].trim().equals("")) {
            Date[3] = "20240110";
        }
        if (Date[4].trim().equals("")) {
            Date[4] = "20240710";
        }
        if (Date[5].trim().equals("")) {
            Date[5] = "20250110";
        }
        if (Date[6].trim().equals("")) {
            Date[6] = "20250710";
        }
        if (Coupon_String[0].trim().equals("")) {
            coupon_rate[0] = 0.05;
        } else {
            coupon_rate[0] = Double.parseDouble(Coupon_String[0]);
        }
        if (Coupon_String[1].trim().equals("")) {
            coupon_rate[1] = 0.1;
        } else {
            coupon_rate[1] = Double.parseDouble(Coupon_String[1]);
        }
        if (Coupon_String[2].trim().equals("")) {
            coupon_rate[2] = 0.15;
        } else {
            coupon_rate[2] = Double.parseDouble(Coupon_String[2]);
        }
        if (Coupon_String[3].trim().equals("")) {
            coupon_rate[3] = 0.2;
        } else {
            coupon_rate[3] = Double.parseDouble(Coupon_String[3]);
        }
        if (Coupon_String[4].trim().equals("")) {
            coupon_rate[4] = 0.25;
        } else {
            coupon_rate[4] = Double.parseDouble(Coupon_String[4]);
        }
        if (Coupon_String[5].trim().equals("")) {
            coupon_rate[5] = 0.3;
        } else {
            coupon_rate[5] = Double.parseDouble(Coupon_String[5]);
        }
        if (Face_value_String.trim().equals("")){
            Face_value = 10000.0;
        } else {
            Face_value = Double.parseDouble(Face_value_String);
        }
        if (Strike_String[0].trim().equals("")) {
            strike_price[0] = 85;
        } else {
            strike_price[0] = Double.parseDouble(Strike_String[5]);
        }
        if (Strike_String[1].trim().equals("")) {
            strike_price[1] = 80;
        } else {
            strike_price[1] = Double.parseDouble(Strike_String[1]);
        }
        if (Strike_String[2].trim().equals("")) {
            strike_price[2] = 75;
        } else {
            strike_price[2] = Double.parseDouble(Strike_String[2]);
        }
        if (Strike_String[3].trim().equals("")) {
            strike_price[3] = 70;
        } else {
            strike_price[3] = Double.parseDouble(Strike_String[3]);
        }
        if (Strike_String[4].trim().equals("")) {
            strike_price[4] = 65;
        } else {
            strike_price[4] = Double.parseDouble(Strike_String[4]);
        }
        if (Strike_String[5].trim().equals("")) {
            strike_price[5] = 60;
        } else {
            strike_price[5] = Double.parseDouble(Strike_String[5]);
        }
        if (Risk_free_rate_String.trim().equals("")) {
            r = 0.01;
        } else {
            r = Double.parseDouble(Risk_free_rate_String);
        }
        if (Volatility1_String.trim().equals("")) {
            vol1 = 0.2;
        } else {
            vol1 = Double.parseDouble(Volatility1_String);
        }
        if (Volatility2_String.trim().equals("")) {
            vol2 = 0.3;
        } else {
            vol2 = Double.parseDouble(Volatility2_String);
        }
        if (Volatility3_String.trim().equals("")) {
            vol3 = 0.25;
        } else {
            vol3 = Double.parseDouble(Volatility3_String);
        }
        if (Volatility4_String.trim().equals("")) {
            vol4 = 0.24;
        } else {
            vol4 = Double.parseDouble(Volatility4_String);
        }
        if (Correlation12_String.trim().equals("")) {
            corr12 = 0.7;
        } else {
            corr12 = Double.parseDouble(Correlation12_String);
        }
        if (Correlation13_String.trim().equals("")) {
            corr13 = 0.48;
        } else {
            corr13 = Double.parseDouble(Correlation13_String);
        }
        if (Correlation14_String.trim().equals("")) {
            corr14 = 0.27;
        } else {
            corr14 = Double.parseDouble(Correlation14_String);
        }
        if (Correlation23_String.trim().equals("")) {
            corr23 = 0.45;
        } else {
            corr23 = Double.parseDouble(Correlation23_String);
        }
        if (Correlation24_String.trim().equals("")) {
            corr24 = 0.3;
        } else {
            corr24 = Double.parseDouble(Correlation24_String);
        }
        if (Correlation34_String.trim().equals("")) {
            corr34 = 0.5;
        } else {
            corr34 = Double.parseDouble(Correlation34_String);
        }
        if (Knock_in_Barrier_String.trim().equals("")) {
            Knock_in_Barrier = 0.5 * 100.0;
        } else {
            Knock_in_Barrier = Double.parseDouble(Knock_in_Barrier_String) * 100.0;
        }
        if (Dummy_String.trim().equals("")) {
            dummy = 0.3;
        } else {
            dummy = Double.parseDouble(Dummy_String);
        }
        if (NoSimulation_String.trim().equals("")) {
            M = 1000;
        } else {
            M = Integer.parseInt(NoSimulation_String);
        }
        try {
            for (int i = 0; i < len; i++) {
                check_day[i] = diffOfDate(Date[0], Date[i + 1]);
                check_dayInt[i] = (int) check_day[i];
            }
        } catch (Exception z) {
            z.printStackTrace();
        }
        tot_date = check_dayInt[len - 1];
        S1 = new double[tot_date + 1];
        S2 = new double[tot_date + 1];
        S3 = new double[tot_date + 1];
        S4 = new double[tot_date + 1];
        S1[0] = 100.0;
        S2[0] = 100.0;
        S3[0] = 100.0;
        S4[0] = 100.0;
        double[] arr_ran1 = new double[tot_date];
        double[] arr_ran2 = new double[tot_date];
        double[] arr_ran3 = new double[tot_date];
        double[] arr_ran4 = new double[tot_date];
        double coef11 = (r - 0.5*Math.pow(vol1, 2)) * dt;
        double coef12 = vol1 * Math.sqrt(dt);
        double coef21 = (r - 0.5*Math.pow(vol2, 2)) * dt;
        double coef22 = vol2 * Math.sqrt(dt);
        double coef31 = (r - 0.5*Math.pow(vol3, 2)) * dt;
        double coef32 = vol3 * Math.sqrt(dt);
        double coef41 = (r - 0.5*Math.pow(vol4, 2)) * dt;
        double coef42 = vol4 * Math.sqrt(dt);
        for (int i = 0; i < len; i++) {
            payment[i] = Face_value * (1.0 + coupon_rate[i]);
        }
        long end1 = System.currentTimeMillis();
        long start2 = System.currentTimeMillis();
        payoff = new double[len];
        for (int j = 0; j < M; j++) {
            int repay_event = 0;
            double minn = 100.0;
            for (int i = 0; i < arr_ran1.length; i++) {
                ran1 = random.nextGaussian();
                ran2 = random.nextGaussian();
                ran3 = random.nextGaussian();
                ran4 = random.nextGaussian();
                arr_ran1[i] = ran1;
                arr_ran2[i] = corr12 * ran1 + Math.sqrt( 1 - Math.pow(corr12, 2) ) * ran2;
                arr_ran3[i] = corr13 * ran1 + ( (corr23 - corr12 * corr13) / Math.sqrt( 1 - Math.pow(corr12, 2) ) ) * ran2 + Math.sqrt( 1 - ( Math.pow(corr13, 2) + Math.pow(corr23 - corr12 * corr13, 2) / (1 - Math.pow(corr12, 2)) ) ) * ran3;
                arr_ran4[i] = corr14 * ran1 + ( (corr24 - corr12 * corr14) / Math.sqrt( 1 - Math.pow(corr12, 2) ) ) * ran2 + ( ( corr34 - corr13 * corr14 - (corr23 - corr12 * corr13) * (corr24 - corr12 * corr14) / (1 - Math.pow(corr12, 2)) ) / Math.sqrt(1 - Math.pow(corr13, 2) - Math.pow(corr23 - corr12 * corr13, 2) / (1 - Math.pow(corr12, 2))) ) * ran3 + Math.sqrt(1 - Math.pow(corr14,2) - Math.pow(corr24 - corr12 * corr14,2) / Math.sqrt(1 - Math.pow(corr12, 2)) - Math.pow(corr34 - corr13 * corr14 - (corr23 - corr12 * corr13) * (corr24 - corr12 * corr14) / (1 - Math.pow(corr12, 2)), 2) / (1 - Math.pow(corr13, 2) - Math.pow(corr23 - corr12 * corr13, 2) / (1 - Math.pow(corr12, 2))) ) * ran4;
                S1[i + 1] = S1[i] * Math.exp(coef11 + coef12 * arr_ran1[i]);
                S2[i + 1] = S2[i] * Math.exp(coef21 + coef22 * arr_ran2[i]);
                S3[i + 1] = S3[i] * Math.exp(coef31 + coef32 * arr_ran3[i]);
                S4[i + 1] = S4[i] * Math.exp(coef41 + coef42 * arr_ran4[i]);
                if (Math.min(Math.min(S3[i + 1], Math.min(S1[i + 1], S2[i + 1])), S4[i + 1]) < minn) {
                    minn = Math.min(Math.min(S3[i + 1], Math.min(S1[i + 1], S2[i + 1])), S4[i + 1]);
                }
            }
            for (int h = 0; h < len; h++) {
                indexs[h] = Math.min(Math.min(S3[check_dayInt[h]], Math.min(S1[check_dayInt[h]], S2[check_dayInt[h]])),S4[check_dayInt[h]]);
            }
            Arrays.fill(payoff,0.0);
            for (int n = 0; n < len; n++) {
                if (indexs[n] >= strike_price[n]) {
                    payoff[n] = payment[n];
                    repay_event = 1;
                    break;
                }
            }
            if (repay_event == 0) {
                if (minn > Knock_in_Barrier) {
                    payoff[payoff.length - 1] = Face_value * (1.0 + dummy);
                } else {
                    payoff[payoff.length - 1] = Face_value * indexs[len - 1] / 100.0;
                }
            }
            for (int k = 0; k < len; k++) {
                tot_payoff[k] = tot_payoff[k] + payoff[k];
            }
        }
        for (int i = 0; i < len; i++) {
            tot_payoff[i] = tot_payoff[i] / M;
            disc_payoff[i] = tot_payoff[i] * Math.exp(-r * check_day[i] / 365.0);
            sum += disc_payoff[i];
        }
        long end2 = System.currentTimeMillis();
        long end3 = System.currentTimeMillis();
        //     ElapsedTime_Result1.setText("Elapsed time1 : \n" + (end1 - start1) / 10000.0 + " sec");
        //     ElapsedTime_Result2.setText("Elapsed time2 : \n" + (end2 - start2) / 10000.0 + " sec");
        ElapsedTime_Result3.setText("Elapsed time : \n" + (end3 - start3) / 1000.0 + " sec");
        Price_Result.setText("ELS Price : \n" + Math.round(sum * 100.0) / 100.0);
        String state = Environment.getExternalStorageState();
        String pic_dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        String filename = "4Asset_Android_Standard_M_" + M + ".csv";
        try {
            Log.d("TEST", "CSV1");
            BufferedWriter buf = new BufferedWriter(new FileWriter(pic_dir + "/" + filename, true));
            buf.append(String.valueOf((end1 - start1) / 100.0));
            buf.append(',');
            buf.append(String.valueOf((end2 - start2) / 100.0));
            buf.append(',');
            buf.append(String.valueOf((end3 - start3) / 100.0));
            buf.append(',');
            buf.append(String.valueOf(Math.round(sum * 100.0) / 100.0));
            buf.append('\n');
            buf.close();
        } catch (FileNotFoundException e) {
            Log.d("TEST", "CSV2");
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("TEST", "CSV3");
            e.printStackTrace();
        }
    }
}