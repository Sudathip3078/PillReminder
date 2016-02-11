package org.usablelabs.pillreminder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;

public class FormActivity extends BaseActivity {

    private static final int MenuItem_SaveID = 1;

    private Task task = null;
    private EditText nameEdit, surnameEdit, dateEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        setDrawer(true);

        nameEdit = (EditText) findViewById(R.id.nameEdit);
        surnameEdit = (EditText) findViewById(R.id.surnameEdit);
        dateEdit = (EditText) findViewById(R.id.dateEdit);

        long id = getIntent().getLongExtra("id", 0);
        if (id == 0) {
            setTitle(R.string.new_task);
        } else {
            setTitle(R.string.edit_task);
            task = Task.load(Task.class, id);
            if (task != null) {
                nameEdit.setText(task.title);
                surnameEdit.setText(task.content);
                dateEdit.setText(task.dueAt);
            } else {
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        addMenuItem(menu, MenuItem_SaveID, R.string.save, buildDrawable(MaterialDesignIconic.Icon.gmi_save));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (isEdited()) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setTitle(android.R.string.dialog_alert_title);
                    alert.setMessage(R.string.unsaved_exit_alert);
                    alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onBackPressed();
                        }
                    });
                    alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alert.show();
                    return true;
                }
                break;
            case MenuItem_SaveID:
                save();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onStart(){
        super.onStart();
        EditText dateEdit=(EditText)findViewById(R.id.dateEdit);
        dateEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DateDialog dialog = new DateDialog(v);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    dialog.show(ft, "TimePicker");
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /* surpass all keys in activity; force the user to use form controls */
        return true;
    }

    private boolean isEdited() {
        if (task == null)
            return nameEdit.getText().length() > 0 || surnameEdit.getText().length() > 0 || dateEdit.getText().length() > 0;
        else
            return !task.title.equals(nameEdit.getText().toString()) || !task.content.equals(surnameEdit.getText().toString()) || !task.dueAt.equals(dateEdit.getText().toString());
    }

    private void save() {
        if (nameEdit.getText().length() > 0 || surnameEdit.getText().length() > 0 || dateEdit.getText().length() > 0) {
            if (task == null)
                task = new Task();
            task.title = nameEdit.getText().toString();
            task.content = surnameEdit.getText().toString();
            task.dueAt = dateEdit.getText().toString();
            task.saveWithTimestamp();
            setResult(Activity.RESULT_OK, new Intent().putExtra("id", task.getId()));
            this.finish();
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(android.R.string.dialog_alert_title);
            alert.setMessage(R.string.name_is_required);
            alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alert.show();
        }
    }
}
