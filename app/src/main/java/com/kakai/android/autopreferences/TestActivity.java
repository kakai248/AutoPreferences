package com.kakai.android.autopreferences;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kakai.android.autopreferences.annotations.Preference;
import com.kakai.android.autopreferences.backup.BackupHelper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = TestActivity.class.getSimpleName();

    private static final int PERMISSION_REQUEST = 1000;

    private TextView tvInt;
    private Button btnChangeInt;
    private Button btnResetInt;
    private Button btnRead;
    private Button btnWrite;
    private TextView tvBackupFile;
    private TextView tvRestoreFile;
    private Button btnBackup;
    private Button btnRestore;

    private PreferencesManager preferencesManager;

    private File backupFile = new File(Environment.getExternalStorageDirectory(), "backup.json");
    private File restoreFile = new File(Environment.getExternalStorageDirectory(), "restore.json");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        preferencesManager = new PreferencesManager(this);

        tvInt = (TextView) findViewById(R.id.tvInt);
        btnChangeInt = (Button) findViewById(R.id.btnChangeInt);
        btnResetInt = (Button) findViewById(R.id.btnResetInt);
        btnRead = (Button) findViewById(R.id.btnRead);
        btnWrite = (Button) findViewById(R.id.btnWrite);
        tvBackupFile = (TextView) findViewById(R.id.tvBackupFile);
        tvRestoreFile = (TextView) findViewById(R.id.tvRestoreFile);
        btnBackup = (Button) findViewById(R.id.btnBackup);
        btnRestore = (Button) findViewById(R.id.btnRestore);

        btnChangeInt.setOnClickListener(this);
        btnResetInt.setOnClickListener(this);
        btnRead.setOnClickListener(this);
        btnWrite.setOnClickListener(this);
        btnBackup.setOnClickListener(this);
        btnRestore.setOnClickListener(this);

        setIntText();
        setFileText();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST) {
            if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                finish();
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view == btnChangeInt) {
            preferencesManager.setTestInt((int) (Math.random() * 100));
            setIntText();
        } else if (view == btnResetInt) {
            preferencesManager.removeTestInt();
            setIntText();
        } else if (view == btnRead) {
            read();
        } else if (view == btnWrite) {
            write();
        } else if (view == btnBackup) {
            backup();
        } else if (view == btnRestore) {
            restore();
        }
    }

    private void setIntText() {
        tvInt.setText(getString(R.string.value, String.valueOf(preferencesManager.getTestInt())));
    }

    private void read() {
        Map<String, Object> values = BackupHelper.with(this).read(preferencesManager, Preference.ALL);

        Log.d(TAG, "------- READ -------");
        for (Map.Entry<String, Object> e : values.entrySet()) {
            System.out.println(e.getKey() + " - " + e.getValue());
        }
        Log.d(TAG, "----- END READ -----");
    }

    private void write() {
        Map<String, Object> values = new HashMap<>();
        values.put(getString(R.string.test_boolean), false);
        values.put(getString(R.string.test_long), 40);
        values.put(getString(R.string.test_int), 50);
        values.put(getString(R.string.test_enum), null);

        BackupHelper.with(this).write(preferencesManager, values);
    }

    private void setFileText() {
        tvBackupFile.setText(getString(R.string.backup_file_text, backupFile.getAbsolutePath()));
        tvRestoreFile.setText(getString(R.string.restore_file_text, restoreFile.getAbsolutePath()));
    }

    private void backup() {
        try {
            BackupHelper.with(this).backup(backupFile, preferencesManager, PreferencesManager.GROUP1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void restore() {
        try {
            BackupHelper.with(this).restore(restoreFile, preferencesManager);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
