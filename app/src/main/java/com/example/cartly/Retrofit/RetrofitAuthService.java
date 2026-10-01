package com.example.cartly.Retrofit;

public class RetrofitAuthService {
//    private final ApiAuthService apiAuthService;
//    private String jwtToken = null;

//    public RetrofitAuthService(String jwtToken){
//        apiAuthService = new ApiAuthService(jwtToken);
//        apiAuthService = RetrofitService.getRetrofitInstance(null).create(ApiAuthService.class);
//    }

//    public void getLogin(LoginRequestDao request, Observer<ApiResponseDao<LoginResponseDao>> callback) {
//        apiAuthService.authLogin(request)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<ApiResponseDao<LoginResponseDao>>() {
//                    @Override
//                    public void onSubscribe(@NonNull Disposable d) {
//                        callback.onSubscribe(d);
//                    }
//
//                    @Override
//                    public void onNext(ApiResponseDao<LoginResponseDao> response) {
//                        callback.onNext(response);
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//                        callback.onError(e);
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        callback.onComplete();
//                    }
//                });
//    }
}
