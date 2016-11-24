package ge.bog.firebasetutorial;

import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ge.bog.firebasetutorial.bean.Message;

/**
 * Created by ana on 24.11.2016.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private ArrayList<Message> mMessages;
    private RecyclerView mRecyclerView;

    public ChatAdapter(RecyclerView recyclerView){
        mRecyclerView = recyclerView;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getMessageType();

    }


    public Message getItem(int pos)
    {
        return mMessages.get(pos);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == Message.TYPE_IN){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_in_message, parent, false);
            return new InMessageViewHolder(v);
        }else {

        }

        return null;
    }

    public void addMessage(Message message){
//        notifyItemInserted();
//

        mRecyclerView.getLayoutManager().scrollToPosition(getItemCount()-1);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    class InMessageViewHolder extends ViewHolder{

        public InMessageViewHolder(View itemView) {
            super(itemView);
        }
    }


    class OutMessageViewHolder extends ViewHolder{
        public OutMessageViewHolder(View itemView) {
            super(itemView);
        }
    }
}
