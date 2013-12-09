package org.instorm.example.contactpicker;

import org.instorm.example.R;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.ListView;

public class ContactPickerActivity extends Activity {
	
	private ListView lvContact;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contactpicker);
		
		lvContact = (ListView) findViewById(R.id.lv_contact);
		
		final Cursor c = getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI, 
				null, 
				null, 
				null, 
				null);
		
		String from[] = new String[]{Contacts.DISPLAY_NAME_PRIMARY};
		int[] to = new int[]{R.id.tv_item};
		
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, 
				R.layout.list_item_contact, 
				c, 
				from, 
				to);
		lvContact.setAdapter(adapter);
	}
}
