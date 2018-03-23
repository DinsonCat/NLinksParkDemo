package com.nlinks.parkdemo.module.login.view;


/**
 *
 */
public interface ILoginView {

      void onLoginResult(Boolean result, String msg);

      void onSetProgressBarVisibility(boolean visibility);

      void countDown();

      void junp2addPlateNum();
}
