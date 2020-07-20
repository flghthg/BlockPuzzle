package com.two.blockpuzzle;

import android.content.Context;

import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.TemplateParams;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;

public class KakaoShare {

    private Context context;

    public KakaoShare(Context context) {
        this.context = context;
    }

    public void share_kakao(int score) {

        TemplateParams params = FeedTemplate
                .newBuilder(ContentObject.newBuilder(
                        "BLOCK PUZZLE",
                        "https://i.ibb.co/gVF3pXp/capture4.jpg",
                        LinkObject.newBuilder()
                                .setWebUrl("https://developers.kakao.com")
                                .setMobileWebUrl("https://developers.kakao.com")
                                .build())
                        .setDescrption(score + "점, 내 기록을 한번 깨 보시지?")
                        .build())

                .addButton(new ButtonObject(
                        "기록 깨러 가기!",
                        LinkObject.newBuilder()
                                .setAndroidExecutionParams("key1=value1")
                                .setIosExecutionParams("key1=value1")
                                .build()))
                .build();


        KakaoLinkService.getInstance().sendDefault(context, params, new ResponseCallback<KakaoLinkResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {
            }

            @Override
            public void onSuccess(KakaoLinkResponse result) {
            }
        });
    }
}


