package fr.insarouen.asi.notesync.sync;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.AlertDialog;
import android.app.Activity;

import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Bundle;

import android.content.DialogInterface;

import java.util.List;

import fr.insarouen.asi.notesync.R;

public class PeerListDialog extends DialogFragment {
	private List<WifiP2pDevice> peers;
	private OnPeerSelected callback;
	private PeerListAdapter adapter;
	
	public PeerListDialog(List<WifiP2pDevice> peers, OnPeerSelected callback) {
		this.peers = peers;
		this.callback = callback;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		adapter = new PeerListAdapter(getActivity(), peers);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.choosepeer);
		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface Dialog, int which) {
				PeerListDialog.this.callback.onPeerSelected(
					(WifiP2pDevice)PeerListDialog.this.adapter.getItem(which)
					);
				PeerListDialog.this.dismiss();
			}
		});

		return builder.create();
	}

	public void setPeerList(List<WifiP2pDevice> peers) {
		this.peers = peers;
	}

	public OnPeerSelected getPeerSelection() {
		return this.callback;
	}

	public interface OnPeerSelected {
		public void onPeerSelected(WifiP2pDevice device);
		public void setConnected();
	}

}
