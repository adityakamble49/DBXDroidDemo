package adityakamble49.dbxdroiddemo;

import adityakamble49.dbxdroid.DBXColumn;
import adityakamble49.dbxdroid.DBXColumnList;
import adityakamble49.dbxdroid.DBXDatabase;
import adityakamble49.dbxdroid.DBXFieldType;
import adityakamble49.dbxdroid.DBXFieldValuePair;
import adityakamble49.dbxdroid.DBXFieldValuePairList;
import adityakamble49.dbxdroid.DBXResult;
import adityakamble49.dbxdroid.DBXTable;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	DBXDatabase studentDatabase;

	/* View Components */
	EditText etStudentID, etStudentName, etStudentDept;
	Button btAddStudent, btResetFields, btViewAll;
	TextView tvResults;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setupDatabase();
		setupUI();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onPause() {
		super.onPause();

		try {
			studentDatabase.closeDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		try {
			studentDatabase.openDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.V_BT_AddStudent:

			String studentID = etStudentID.getText().toString();
			String studentName = etStudentName.getText().toString();
			String studentDept = etStudentDept.getText().toString();

			if (studentID.isEmpty() || studentName.isEmpty()
					|| studentDept.isEmpty()) {

				Toast.makeText(this, "Fill All Fields", Toast.LENGTH_SHORT)
						.show();
				break;
			}

			DBXFieldValuePairList studentFieldsList = new DBXFieldValuePairList();
			studentFieldsList.addFieldValuePair(new DBXFieldValuePair(
					"student_id", Integer.parseInt(studentID)));
			studentFieldsList.addFieldValuePair(new DBXFieldValuePair(
					"student_name", studentName));
			studentFieldsList.addFieldValuePair(new DBXFieldValuePair(
					"student_dept", studentDept));

			try {
				if (studentDatabase.insertEntry("students", studentFieldsList) != -1) {

					Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			break;

		case R.id.V_BT_ResetFields:

			etStudentID.setText("");
			etStudentName.setText("");
			etStudentDept.setText("");

			break;

		case R.id.V_BT_ViewAll:

			DBXResult dbxResult = studentDatabase.getEntries("students");
			String totalResult = "";
			String[][] result = dbxResult.getResults();

			for (int i = 0; i < dbxResult.getRowSize(); i++) {

				for (int j = 0; j < dbxResult.getColumnSize(); j++) {

					totalResult += result[i][j]+" ";
				}

				totalResult +="\n";
			}

			tvResults.setText(totalResult);
			
			break;
		}
	}

	private void setupDatabase() {

		studentDatabase = new DBXDatabase("college.db", this);

		DBXColumnList studentColumns = new DBXColumnList();
		studentColumns.addColumn(new DBXColumn("student_id",
				DBXFieldType.INTEGER));
		studentColumns.addColumn(new DBXColumn("student_name",
				DBXFieldType.TEXT));
		studentColumns.addColumn(new DBXColumn("student_dept",
				DBXFieldType.VARCHAR));

		studentDatabase.addTable(new DBXTable("students", studentColumns));

		try {
			studentDatabase.createDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setupUI() {

		etStudentID = (EditText) findViewById(R.id.V_ET_StudentID);
		etStudentName = (EditText) findViewById(R.id.V_ET_StudentName);
		etStudentDept = (EditText) findViewById(R.id.V_ET_StudentDept);
		tvResults = (TextView) findViewById(R.id.V_TV_Results);
		btAddStudent = (Button) findViewById(R.id.V_BT_AddStudent);
		btResetFields = (Button) findViewById(R.id.V_BT_ResetFields);
		btViewAll = (Button) findViewById(R.id.V_BT_ViewAll);
		btAddStudent.setOnClickListener(this);
		btResetFields.setOnClickListener(this);
		btViewAll.setOnClickListener(this);

	}

}
