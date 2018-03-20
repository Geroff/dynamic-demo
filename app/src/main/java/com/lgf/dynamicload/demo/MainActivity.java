package com.lgf.dynamicload.demo;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.lgf.plugin.IDynamic;

import java.io.File;

import dalvik.system.DexClassLoader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // layout相应的View中使用onClick属性
    public void showMessage(View view) {
        // 此路径为插件存放路径
        File dexPathFile = new File(Environment.getExternalStorageDirectory() + File.separator + "plugin.jar");
        String dexPath = dexPathFile.getAbsolutePath();
        String dexDecompressPath = getDir("dex", MODE_PRIVATE).getAbsolutePath(); // dex解压后的路径
//        String dexDecompressPath = Environment.getExternalStorageDirectory().getAbsolutePath(); // 不能放在SD卡下，否则会报错
        /**
         * DexClassLoader参数说明
         * 参数1 dexPath：待加载的dex文件路径，如果是外存路径，一定要加上读外存文件的权限（<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/> ）
         * 参数2 optimizedDirectory：解压后的dex存放位置，此位置一定要是可读写且仅该应用可读写（安全性考虑），所以只能放在data/data下。本文getDir("dex", MODE_PRIVATE)会在/data/data/**package/下创建一个名叫”app_dex1“的文件夹，其内存放的文件是自动生成output.dex；如果不满足条件，Android会报错误
         * 参数3 libraryPath：指向包含本地库(so)的文件夹路径，可以设为null
         * 参数4 parent：父级类加载器，一般可以通过Context.getClassLoader获取到，也可以通过ClassLoader.getSystemClassLoader()获取到。
         */
        DexClassLoader dexClassLoader = new DexClassLoader(dexPath, dexDecompressPath, null, getClassLoader());
        Class libClazz = null;
        try {
            libClazz = dexClassLoader.loadClass("com.lgf.base.DynamicTest");
            IDynamic lib = (IDynamic) libClazz.newInstance();
            Toast.makeText(this, lib.show(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
