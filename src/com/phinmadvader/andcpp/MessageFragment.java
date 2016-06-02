package com.phinmadvader.andcpp;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.phinvader.libjdcpp.DCUser;

import org.w3c.dom.Text;

public class MessageFragment extends Fragment implements TextView.OnEditorActionListener  {
	private MainActivity mainActivity;
	private TextView msg_board;
    private EditText textInput;
    private DCPPService service;

	public void setBuddy(DCUser buddy) {
		this.buddy = buddy;
	}

	private DCUser buddy = null;
	public boolean is_ready = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.mainActivity = (MainActivity) getActivity();
        View view = inflater.inflate(R.layout.message_board, container, false);
        this.textInput = (EditText)view.findViewById(R.id.textInput);
        this.msg_board = (TextView)view.findViewById(R.id.messageBoardField);
		this.textInput.setOnEditorActionListener(this);
        this.service = mainActivity.mService;
		if (buddy == null) {
			List<String> current_messages = this.service
					.get_board_messages();
			if (current_messages != null) {
				for (String msg : current_messages) {
					msg_board.append(msg + "\n");
				}
			}
		}
		is_ready = true;
		this.msg_board.scrollTo(0,this.msg_board.getHeight());
		return view;
	}


	public void add_msg(final String msg) {
		mainActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				msg_board.append(msg + "\n");
			}
		});
	}


    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
	    String message = textView.getText().toString();
	    if (message.equals("")) {
		    return true;
	    }
	    message = message.replace("\n", "").replace("\r", "");
	    Log.d("andcpp", message);
        if (buddy == null) {
	        this.service.sendMainBoardMessage(message);
        } else {
			this.service.sendPrivateMessage(buddy, message);

	        add_msg("<" + service.myuser.nick + "> " +  message);
        }

        textView.setText("");
        return true;
    }
}
