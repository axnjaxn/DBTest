package com.axnjaxn.dbtest;

import java.util.ArrayList;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.marxent.sdk.CategoryListAdapter;
import com.marxent.sdk.CategoryListAdapter.Category;
import com.marxent.sdk.CategoryListAdapter.PopulateListener;

public class MainActivity extends Activity {
	private ArrayList<ContentClass> displayList;
	private CategoryListAdapter adapter;
	private MyDBHelper dbhelper;
	
	private Random random = new Random();
	private static final String[] names = {
		"Adam",
		"Bruce",
		"Chris",
		"Dave",
		"Edward",
		"Frank",
		"George",
		"Hilbert",
		"Ian",
		"John",
		"Karl",
		"Mark",
		"Neil",
		"Orville",
		"Pubert",
		"Quincy",
		"Robert",
		"Steve",
		"Terrence",
		"Ulysses",
		"Vincent",
		"William",
		"Xavier",
		"Young",
		"Zachary"
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		dbhelper = new MyDBHelper(getApplicationContext());
		
		displayList = dbhelper.getList();
		
		adapter = new CategoryListAdapter(this);
		Category cat = new Category();
		cat.setItemLayout(R.layout.listitem);
		cat.setData(displayList);
		cat.setEnabled(true);
		cat.setPopulate(new PopulateListener() {
			@Override
			public void setFields(View view, Object obj, CategoryListAdapter adapter) {
				ContentClass item = (ContentClass)obj;
				TextView title = (TextView)view.findViewById(R.id.title);
				TextView content = (TextView)view.findViewById(R.id.content);
				TextView age = (TextView)view.findViewById(R.id.age);
				
				title.setText(item.name);
				content.setText(item.content);
				age.setText(Integer.toString(item.age));
			}
		});
		adapter.addCategory(cat);
		
		ListView listView = (ListView)findViewById(R.id.listView);
		listView.setAdapter(adapter);
	}
	
	@SuppressLint("DefaultLocale")
	public ContentClass createRandomItem() {
		String name = names[random.nextInt(names.length)];
		
		String content = String.format("I.Q. %3d", (int)(100 + 10 * random.nextGaussian()));
		
		int age = 20 + random.nextInt(40);
		
		return dbhelper.insert(name, content, age);
	}
	
	public void add(View v) {
		displayList.add(createRandomItem());

		refreshDisplay();
	}

	public void drop(View v) {
		if (!displayList.isEmpty()) {
			dbhelper.delete(displayList.get(0).id);
			displayList.remove(0);
		}
		refreshDisplay();
	}
	
	public void resort(View v) {
		displayList = dbhelper.getList();
		adapter.getCategory(0).setData(displayList);
		adapter.notifyDataSetChanged();
	}
	

	private void refreshDisplay() {
		adapter.notifyDataSetChanged();
	}
}
