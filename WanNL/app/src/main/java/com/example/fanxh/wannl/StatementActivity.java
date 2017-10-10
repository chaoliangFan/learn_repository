package com.example.fanxh.wannl;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class StatementActivity extends AppCompatActivity{
   private Button button;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statement);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView statement = (TextView)findViewById(R.id.web);
        statement.setText(Html.fromHtml("<font size=40><p>\n" +
                "&nbsp;&nbsp;&nbsp;&nbsp;万年历提醒您：在使用万年历各项服务前，请您务必仔细阅读并透彻理解本声明。您可以选择不使用万年历的在线服务，但如果您使用万年历的在线服务，您的使用行为将被视为对本声明以下全部内容的认可。\n" +
                "</p>\n" +
                "<br />\n" +
                "<b>一、关于测试结果</b>\n" +
                "<p>\n" +
                "&nbsp;&nbsp;&nbsp;&nbsp;1．本站所有的测试结果不作为您真正的人生指导策划，仅作娱乐参考！某些民俗占卜，仅作研究之用，请勿迷信。按此操作一切后果自负。\n" +
                "<br /><br />\n" +
                "&nbsp;&nbsp;&nbsp;&nbsp;2．不得将本算命系统用于任何非法用途，且必须自行承担因使用本系统带来的任何后果和责任。\n" +
                "</p>\n" +
                "<br />\n" +
                "<b>二、不可抗力及其它免责事由</b>\n" +
                "<p>\n" +
                "&nbsp;&nbsp;&nbsp;&nbsp;1．您理解并同意，本服务同大多数互联网服务一样，可能会受多种因素影响，包括但不限于用户原因、网络服务质量、社会环境等；也可能会受各种安全问题的侵扰，包括但不限于他人非法利用用户资料，进行现实中的骚扰；用户下载安装的其他软件或访问的其他网站中可能含有病毒、木马程序或其他恶意程序，威胁您的终端设备信息和数据安全，继而影响本服务的正常使用等，因此导致的任何损失，万年历不承担任何责任。\n" +
                "<br /><br />\n" +
                "&nbsp;&nbsp;&nbsp;&nbsp;2．您理解并确认，万年历需要定期或不定期地对网站平台或相关的设备进行检修或者维护，如因此类情况而造成服务在合理时间内的中断，万年历无需为此承担任何责任。\n" +
                "<br /><br />\n" +
                "&nbsp;&nbsp;&nbsp;&nbsp;3. 八字测算、姻缘分析等是万年历与第三方公司合作接入的一款产品，意在为您提供命理类测算服务。您在万年历平台上点击“八字测算”等入口，将直接进入该产品供应商的网页。天奇文化服务展示的内容、介绍及链接网页中之所有内容，均系第三方公司制作和提供，该服务也由第三方公司自行运营和管理。您如需购买此项服务，将会在第三方公司的网页上直接与第三方公司进行交易。对于天奇文化产品的测试结果准确度和数据来源的真实性、准确性和合法性等均由第三方公司负责，与万年历无关，万年历作为互联网信息平台对外不提供任何保证，对链接网页信息的真实性、准确性、完整性不作任何承诺和保证，对信息的延误、失误或遗漏不承担任何责任。若天奇文化产品侵犯了任何单位或个人的知识产权或其他权利，责任由第三方公司承担，万年历对此不承担责任。您使用天奇文化产品所产生或存在的任何风险及一切后果将完全由您本人及/或第三方公司承担，万年历对此不承担任何责任。\n" +
                "<br />\n" +
                "</p>\n</font>"));
    }
}