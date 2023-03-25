package com.karan.bunchtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karan.bunchtest.adapter.EmployeeAdapter;
import com.karan.bunchtest.database.EmployeeDbHelper;
import com.karan.bunchtest.model.EmployeeModel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private EmployeeDbHelper employeeDbHelper;
    private RecyclerView employeeRecyclerView;
    private EmployeeAdapter employeeAdapter;
    private ArrayList<EmployeeModel> employeeDetails = new ArrayList<>();
    private FloatingActionButton sendMail;
    private final String pathName = "employeesdetails.xls";

    private final File filePath = new File(Environment.getExternalStorageDirectory() + "/"+pathName);
    public static File dir = new File(new File(Environment.getExternalStorageDirectory(), "Employee"), "Excle");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        employeeRecyclerView = findViewById(R.id.emp_details);
        sendMail = findViewById(R.id.send_mail);

        employeeDbHelper = new EmployeeDbHelper(MainActivity.this);
        if(doesDatabaseExist(MainActivity.this, "EmployeeDB")){
            addData();
        }

        readData();
        setAdapter();

        sendMail.setOnClickListener(view -> sendData());


    }

    private static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

    private void addData(){
        SQLiteDatabase db = employeeDbHelper.getWritableDatabase();
        String count = "SELECT count(*) FROM EmployeeDetails";
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if(icount>0){

        }
        else{
            employeeDbHelper.addNewCourse("Karan");
            employeeDbHelper.addNewCourse("Kush");
            employeeDbHelper.addNewCourse("Kathan");
            employeeDbHelper.addNewCourse("Man");
            employeeDbHelper.addNewCourse("Purva");
            employeeDbHelper.addNewCourse("Ket");
            employeeDbHelper.addNewCourse("Vijay");
            employeeDbHelper.addNewCourse("Bhatt");
            employeeDbHelper.addNewCourse("Patel");
            employeeDbHelper.addNewCourse("Raj");
            employeeDbHelper.addNewCourse("Rajesh");
            employeeDbHelper.addNewCourse("Shivang");
            employeeDbHelper.addNewCourse("Nilesh");
            employeeDbHelper.addNewCourse("Ganesh");
            employeeDbHelper.addNewCourse("Nishant");
            employeeDbHelper.addNewCourse("Dev");
            employeeDbHelper.addNewCourse("Khush");
            employeeDbHelper.addNewCourse("Divya");
            employeeDbHelper.addNewCourse("Khushi");
        }
    }

    private void setAdapter() {
        employeeAdapter = new EmployeeAdapter(employeeDetails);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        employeeRecyclerView.setLayoutManager(layoutManager);
        employeeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        employeeRecyclerView.setAdapter(employeeAdapter);
    }

    private void readData() {

        SQLiteDatabase db = employeeDbHelper.getReadableDatabase();


        Cursor cursorCourses
                = db.rawQuery("SELECT * FROM EmployeeDetails" , null);

        employeeDetails = new ArrayList<>();

        if (cursorCourses.moveToFirst()) {
            do {

                employeeDetails.add(new EmployeeModel(
                        cursorCourses.getInt(0),
                        cursorCourses.getString(1)));
            } while (cursorCourses.moveToNext());
        }
        cursorCourses.close();
    }

    private void sendData(){
        createExcleFile();

    }

    private void createExcleFile(){
        if (Environment.isExternalStorageManager()) {
            if (!dir.exists()){
                dir.mkdirs();
            }
        }
        if (!Environment.isExternalStorageManager()) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
            startActivity(intent);
            if (!dir.exists()){
                dir.mkdirs();
            }
        }

        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = hssfWorkbook.createSheet("Employee Details");


        int rowNum = 0;
        int cellNum = 0;

        HSSFRow row = hssfSheet.createRow(rowNum);
        HSSFCell cell = row.createCell(0);

        cell.setCellValue("EmpId");
        cell = row.createCell(1);
        cell.setCellValue("EmpName");

        for(EmployeeModel emp : employeeDetails){
            rowNum++;
            row = hssfSheet.createRow(rowNum);
            cellNum = 0;
            cell = row.createCell(cellNum);
            cell.setCellValue(emp.getEmpId());
            cellNum++;
            cell = row.createCell(cellNum);
            cell.setCellValue(emp.getEmpName());
        }


        try {
            if (!filePath.exists()){
                filePath.createNewFile();
            }

            FileOutputStream fileOutputStream= new FileOutputStream(filePath);
            hssfWorkbook.write(fileOutputStream);

            if (fileOutputStream!=null){
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        sendEmailWithAttachment(getApplicationContext(),
                "PatelKaran6699@gmail.com",
                "List of employees Details", "List of employees in Excl formate",
                hssfSheet, filePath);

    }

    public void sendEmailWithAttachment(Context ctx, String to, String subject, String message, HSSFSheet sheet, File fileAndLocation) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        emailIntent.setType("application/excel");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {to});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT,message);

        if (fileAndLocation.exists())
        {
            Log.v("Error", "Email file_exists!" );

            MimeTypeMap mime = MimeTypeMap.getSingleton();
            String mimeTypeForXLSFile = mime.getMimeTypeFromExtension(".xls");

            Uri apkURI = FileProvider.getUriForFile(
                    ctx, ctx.getPackageName() + ".provider", fileAndLocation);

            emailIntent.setDataAndType(apkURI, mimeTypeForXLSFile);

            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        }
        else
        {
            Log.v("Error", "Email file does not exist!" );
        }

    }
}
