package fr.insarouen.asi.notesync;

import fr.insarouen.asi.notesync.tasks.*;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.app.ActionBar;

import android.os.Bundle;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;

import android.widget.ListView;
import android.widget.ListAdapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemLongClickListener;

import java.util.ArrayList;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import android.util.Log;

public class TaskListFragment extends ListFragment implements OnItemLongClickListener,
							      ProjectFilterDialog.ProjectFilterListener,
							      ActionBar.OnNavigationListener
{

	public interface Callbacks {
		public TaskList getTasks();
		public TaskListAdapter getTasksAdapter();
		public void showDetails(int pos);
		public void removeTask(int pos);
		public void onSyncClick();
		public void onAddClick();
	}

	private TaskListAdapter.ProjectsAdapter adapter;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		this.setListAdapter(((Callbacks)getActivity()).getTasksAdapter());
		getListView().setOnItemLongClickListener(this);
		setHasOptionsMenu(true);
		ActionBar ab = getActivity().getActionBar();
		ab.setDisplayShowTitleEnabled(false);
		ab.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		adapter = ((Callbacks)getActivity()).getTasksAdapter().getProjectsAdapter();
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		ab.setListNavigationCallbacks(adapter, this);
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		if(itemPosition == 0) {
			((TaskListAdapter)getListAdapter()).resetData();
		} else {
			filterByProject(adapter.getItem(itemPosition));
		}
		return true;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		((Callbacks)getActivity()).showDetails(position);
	}
	
	private class OnListItemLongClickListener implements OnClickListener {
		private int position;

		public OnListItemLongClickListener(int position) {
			this.position = position;
		}

		public void onClick(DialogInterface dialog, int id) {
			((Callbacks)getActivity()).removeTask(position);
		}
	}

	public boolean onItemLongClick(AdapterView av, View v, int position, long id) {
		AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
		adb.setMessage(R.string.done);
		adb.setPositiveButton(R.string.yes, new OnListItemLongClickListener(position));
		adb.setNegativeButton(R.string.cancel,
				new OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		adb.show();
		return true;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.ab, menu);
		//getActivity().onCreateOptionsMenu(menu);
	}

	@Override
	public void filterByProject(String project) {
		((TaskListAdapter)getListAdapter()).getFilter().filter(project);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
			case R.id.sync:
				((Callbacks)getActivity()).onSyncClick();
				return true;
			case R.id.add:
				((Callbacks)getActivity()).onAddClick();
				return true;
			case R.id.filterByProject:
				(new ProjectFilterDialog(this)).show(getFragmentManager(), "ProjectFilter");
				return true;
			case R.id.clearFilter:
				((TaskListAdapter)getListAdapter()).resetData();
				return true;
			default:
				return getActivity().onOptionsItemSelected(item);
		}
	}

	
}
