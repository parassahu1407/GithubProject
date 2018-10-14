package com.example.parassahu.parastaskapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.view.KeyEvent.KEYCODE_ENTER;

public class MainActivity extends AppCompatActivity {

    EditText etSearch;
    String number = "";
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 38;
    List<String> nm = new ArrayList();
    List<String> mobnum = new ArrayList();
    List<Person> personList = new ArrayList<>();

    String option = "";
    boolean flag;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etSearch = (EditText)findViewById(R.id.editText);

        etSearch.setOnEditorActionListener( new EditText.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i== EditorInfo.IME_ACTION_SEARCH || i == EditorInfo.IME_ACTION_DONE || (keyEvent!= null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                {

                    if(keyEvent == null || !keyEvent.isShiftPressed())
                    {
                        search();
                        //return true;
                    }
                }
                return false;
            }
        });
        readContacts();
    }

    public void search()
    {
        option = etSearch.getText().toString();
        //Toast.makeText(getApplicationContext(),option,Toast.LENGTH_LONG).show();

        if(personList.contains(option))
        {
            personList.clear();

            ContentResolver resolver = getContentResolver();
            Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);

            try
            {
                    //String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    //String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    nm.add(option);
                    //Toast.makeText(getApplicationContext(),"Name is :"+name,Toast.LENGTH_LONG).show();

                    Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " = ?", new String[]{option}, null);

                    while (phoneCursor.moveToNext())
                    {
                        mobnum.add(phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                    }
            }
            catch (Exception e)
            {
                Toast.makeText(getApplicationContext(),"Some problem occurred"+e,Toast.LENGTH_LONG).show();
            }

            RecyclerView recyclerView = (RecyclerView)findViewById(R.id.contacts);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            //String[] name = (String[]) nm.toArray();
            String[] name = new String[nm.size()];

            for(int i=0;i<nm.size();i++)
            {
                name[i] = (String)nm.get(i);
            }

            //String[] mobNumber = (String[])mobnum.toArray();
            String[] mobNumber = new String[mobnum.size()];

            for(int i=0;i<mobnum.size();i++)
            {
                mobNumber[i] = (String)mobnum.get(i);
            }
            for (int i = 0 ; i <nm.size();i++)
            {
                String nameCustom = (String) nm.get(i);
                String phonnoCustom = (String) mobnum.get(i);
                Person person  = new Person(nameCustom,phonnoCustom);
                personList.add(person);
            }
            recyclerView.setAdapter(new ContactsAdapter(personList));
        }
        else
        {
            readContacts();
        }
    }

    private void readContacts()
    {
        personList.clear();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        }
        else
        {
            //Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();

            ContentResolver resolver = getContentResolver();
            Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);

            try {
                while (cursor.moveToNext())
                {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    nm.add(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                    //Toast.makeText(getApplicationContext(),"Name is :"+name,Toast.LENGTH_LONG).show();

                    Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);

                   while (phoneCursor.moveToNext())
                   {
                        mobnum.add(phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                    }
                }
            }
            catch (Exception e)
            {
                Toast.makeText(getApplicationContext(),"Some problem occurred"+e,Toast.LENGTH_LONG).show();
            }

            RecyclerView recyclerView = (RecyclerView)findViewById(R.id.contacts);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            //String[] name = (String[]) nm.toArray();
            String[] name = new String[nm.size()];

            for(int i=0;i<nm.size();i++)
            {
                name[i] = (String)nm.get(i);
            }

            //String[] mobNumber = (String[])mobnum.toArray();
            String[] mobNumber = new String[mobnum.size()];

            for(int i=0;i<mobnum.size();i++)
            {
                mobNumber[i] = (String)mobnum.get(i);
            }
            for (int i = 0 ; i <nm.size();i++){
                String nameCustom = (String) nm.get(i);
                String phonnoCustom = (String) mobnum.get(i);
                Person person  = new Person(nameCustom,phonnoCustom);
                personList.add(person);
;            }
            recyclerView.setAdapter(new ContactsAdapter(personList));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                readContacts();
            }
            else
            {
                Toast.makeText(this, "Please grant the permission to display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void call(View v)
    {
        //int id = v.getId();
        //if(id==2131492946)
        /*if(tv1 == v)
        {
            number = "8959868514";
        }
        else if(tv2 == v)
        {
            number = "8889125719";
        }
        else if(tv3==v)
        {
            number = "9009950451";
        }
        else if(tv4==v)
        {
            number = "9617344393";
        }
        else if(tv5==v)
        {
            number = "7879503330";
        }
        else if(tv6==v)
        {
            number = "9669401444";
        }
        else if(tv7==v)
        {
            number = "9691304160";
        }
        //Toast.makeText(getApplicationContext(),""+id,Toast.LENGTH_SHORT).show();
        Intent cl = new Intent(Intent.ACTION_CALL);
        cl.setData(Uri.parse("tel:"+ number));
        startActivity(cl);*/
    }
}
