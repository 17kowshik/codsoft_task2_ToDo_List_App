# To-Do List App

A simple Android to-do list application for managing your tasks and to-do items.

## Overview

This Android app allows you to create, edit, and manage your to-do list tasks. You can add task names, due dates, and descriptions for each task. Tasks can be marked as completed, and you can also delete them as needed.

## Features

- Add new tasks to your to-do list.
- Include optional due dates for tasks.
- Add descriptions to provide more context for your tasks.
- Mark tasks as completed or not completed.
- Edit existing tasks to update their details.
- Delete tasks you no longer need.

## Usage

1. *Add a New Task*:
   - Tap the floating action button with a plus icon to add a new task.
   - Enter a task name, due date (optional), and a description (optional).
   - Tap the "Save" button to add the task to your list.

2. *Edit an Existing Task*:
   - Long-press on a task in the list to open the context menu.
   - Select "Edit" from the menu to edit the task's details.
   - Make your changes and tap "Save" to update the task.

3. *Mark Task as Completed*:
   - Long-press on a task and select "Mark as Completed" to change its status.
   - The task's text color will change to green when marked as completed.

4. *Unmark Task as Completed*:
   - Long-press on a completed task and select "Mark as Not Completed" to change its status.
   - The task's text color will change back to blue.

5. *Delete a Task*:
   - Long-press on a task and select "Delete" to remove it from your list.

## Screenshots
<img src="https://github.com/17kowshik/codsoft_task2_ToDo_List_App/assets/114803937/12718755-46e5-4609-959a-c142ec038fab" width="400" height="920">

## Technical Details

This Android application is built using Java and includes the following key components:

- MainActivity is the main activity where tasks are displayed and managed.
- TaskAdapter is a custom adapter for rendering task items with HTML formatting.
- TaskInputActivity is used for adding new tasks to the list.
- TaskEditActivity is used for editing existing tasks.
