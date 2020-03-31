package com.kentrasoft.entrance;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.kentrasoft.entrance.Test.TestActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Integer[] array1 = {1, 2, 3, 4}, array2 = {5, 6, 7, 8};
        Observable.just(array1, array2).flatMap(new Function<Integer[], ObservableSource<Integer>>() {
            @Override
            public ObservableSource<Integer> apply(Integer[] integers) throws Exception {
                return Observable.fromArray(integers);
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integers) {
                Log.i("123", String.valueOf(integers));
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @OnClick(R.id.tv)
    public void onViewClicked() {
        startActivity(new Intent(this, TestActivity.class));
    }
}
