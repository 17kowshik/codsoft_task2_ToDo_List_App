package com.example.todo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends Activity {
    private ArrayList<String> taskList;
    private TaskAdapter taskAdapter;
    private static final int REQUEST_TASK_INPUT = 1;
    private static final int REQUEST_TASK_EDIT = 2;
    private static final String EDIT_OPTION = "Edit";
    private static final String DELETE_OPTION = "Delete";
    private static final String MARK_COMPLETED_OPTION = "Mark as Completed";
    private static final String MARK_NOT_COMPLETED_OPTION = "Mark as Not Completed";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton addButton = findViewById(R.id.addButton);
        ListView taskListView = findViewById(R.id.taskListView);

        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(this, taskList);
        taskListView.setAdapter(taskAdapter);
        registerForContextMenu(taskListView);

        loadTasksFromSharedPreferences(); // Load tasks from SharedPreferences

        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TaskInputActivity.class);
            startActivityForResult(intent, REQUEST_TASK_INPUT);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TASK_INPUT && resultCode == RESULT_OK && data != null) {
            handleTaskInput(data);
        } else if (requestCode == REQUEST_TASK_EDIT && resultCode == RESULT_OK && data != null) {
            handleTaskEdit(data);
        }
    }

    private void handleTaskInput(Intent data) {
        String taskName = data.getStringExtra("taskName");
        String dueDate = data.getStringExtra("dueDate");
        String description = data.getStringExtra("description");

        String taskDetails = "<br/>" + " ";
        taskDetails += "<b>" + taskName + "</b>";
        if (!dueDate.isEmpty() && !dueDate.equals("Due Date (optional)")) {
            taskDetails += "<br/>Due: " + dueDate;
        }
        if (!description.isEmpty()) {
            taskDetails += "<br/>" + description;
        }
        taskDetails += "<br/>" + " ";
        taskList.add(taskDetails);
        taskAdapter.notifyDataSetChanged();
        saveTasksToSharedPreferences(); // Save tasks to SharedPreferences
    }

    private void handleTaskEdit(Intent data) {
        String editedTaskDetails = data.getStringExtra("editedTaskDetails");
        int position = data.getIntExtra("position", -1);

        if (position >= 0) {
            taskList.set(position, editedTaskDetails);
            taskAdapter.notifyDataSetChanged();
            saveTasksToSharedPreferences(); // Save tasks to SharedPreferences
        }
    }

    private class TaskAdapter extends ArrayAdapter<String> {
        public TaskAdapter(Context context, ArrayList<String> taskList) {
            super(context, android.R.layout.simple_list_item_1, taskList);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            String taskDetails = getItem(position);

            TextView textView = (TextView) view;
            textView.setText(Html.fromHtml(taskDetails, Html.FROM_HTML_MODE_COMPACT));

            if (isTaskCompleted(taskDetails)) {
                textView.setTextColor(Color.parseColor("#00ff00")); // Green
            } else {
                textView.setTextColor(Color.parseColor("#01cecf")); // Blue color
            }
            return view;
        }
    }

    private void markTaskAsCompleted(int position) {
        String taskDetails = taskList.get(position);
        if (taskDetails != null) {
            if (!isTaskCompleted(taskDetails)) {
                taskDetails = "<font color='#00ff00'>" + taskDetails + "</font>";
                taskList.remove(position);
                taskList.add(taskDetails);
                taskAdapter.notifyDataSetChanged();
                saveTasksToSharedPreferences(); // Save tasks to SharedPreferences
            }
        }
    }

    private boolean isTaskCompleted(String taskDetails) {
        return taskDetails != null && taskDetails.contains("<font color='#00ff00'>");
    }

    private void unmarkTaskAsCompleted(int position) {
        String taskDetails = taskList.get(position);
        if (taskDetails != null) {
            if (isTaskCompleted(taskDetails)) {
                // Set text color back to #01cecf
                taskDetails = taskDetails.replace("<font color='#00ff00'>", "<font color='#01cecf'>");
                taskList.set(position, taskDetails);
                taskAdapter.notifyDataSetChanged();
                saveTasksToSharedPreferences(); // Save tasks to SharedPreferences
            }
        }
    }

    private void saveTasksToSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("TaskList", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Convert the ArrayList to a Set of Strings for storage
        Set<String> taskSet = new HashSet<>(taskList);
        editor.putStringSet("taskList", taskSet);

        editor.apply();
    }

    private void loadTasksFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("TaskList", Context.MODE_PRIVATE);
        Set<String> taskSet = sharedPreferences.getStringSet("taskList", new HashSet<>());

        // Convert the Set back to an ArrayList
        taskList.clear();
        taskList.addAll(taskSet);
        taskAdapter.notifyDataSetChanged();
    }

    private void openTaskEditActivity(int position) {
        if (position >= 0 && position < taskList.size()) {
            Intent intent = new Intent(MainActivity.this, TaskEditActivity.class);
            intent.putExtra("taskDetails", taskList.get(position));
            intent.putExtra("position", position);
            startActivityForResult(intent, REQUEST_TASK_EDIT);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;

        String selectedItem = item.getTitle().toString(); // Get the selected item label

        switch (selectedItem) {
            case EDIT_OPTION:
                // Handle edit action
                openTaskEditActivity(position);
                return true;

            case DELETE_OPTION:
                // Handle delete action
                taskList.remove(position);
                taskAdapter.notifyDataSetChanged();
                saveTasksToSharedPreferences();
                return true;

            case MARK_COMPLETED_OPTION:
                // Handle mark as completed action
                markTaskAsCompleted(position);
                return true;

            case MARK_NOT_COMPLETED_OPTION:
                // Handle mark as not completed action
                unmarkTaskAsCompleted(position);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        int position = info.position;

        // Get the task details
        String taskDetails = taskList.get(position);

        // Create context menu items
        menu.setHeaderTitle("Task Options");

        menu.add(0, 0, 0, EDIT_OPTION);
        menu.add(0, 1, 1, DELETE_OPTION);

        // Check if the task is completed and add appropriate options
        if (isTaskCompleted(taskDetails)) {
            menu.add(0, 2, 2, MARK_NOT_COMPLETED_OPTION);
        } else {
            menu.add(0, 3, 2, MARK_COMPLETED_OPTION);
        }
    }
}